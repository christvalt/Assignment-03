import Coordinator.{AnalyzerDone, LoaderDone, PAGES_EACH_ANALYZER, REGEX, StartAnalyzer}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, DispatcherSelector}
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import java.io.{File, IOException}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object DocLoader {

  final case class Load(docId: Int,cordRef: ActorRef[Msg]) extends Msg

  final case class GetWords(doc: PDDocument, initialNbPage: Int, nPages: Int, stripper: PDFTextStripper, cordRef: ActorRef[Msg], docId: Int) extends Msg

  final case class CloseDoc(doc: PDDocument) extends Msg

  def apply(doc: File): Behavior[Msg] =
    Behaviors.setup(context => new DocLoader(context,doc))


class  DocLoader(context:ActorContext[Msg],doc: File) extends AbstractBehavior[Msg](context){
  //import DocLoader._


  override def onMessage(msg: Msg): Behavior[Msg] = msg match{
    case Load(docId,cordRef) =>
      implicit val executionContext: ExecutionContext = context.system.dispatchers.lookup(DispatcherSelector.fromConfig("blocking-dispatcher"))
      Future {
        print(s"D: Loading ${doc.getName}\n")
        val document = PDDocument.load(doc)
        val ap = document.getCurrentAccessPermission
        if (!ap.canExtractContent) throw new IOException("You do not have permission to extract text")
        print(s"D: DONE Loading ${doc.getName}\n")
        document
      }.onComplete {
        case Success(doc) => context.self ! GetWords(doc, 0, doc.getNumberOfPages, new PDFTextStripper(),cordRef,docId)
        case Failure(exception) => print(s"Exception in Loading ($exception) in ${context.self.path.name}")
      }
      Behaviors.same
    case GetWords(doc, p, totP, stripper,replyTo,docId) =>
      implicit val executionContext: ExecutionContext = context.system.dispatchers.lookup(DispatcherSelector.fromConfig("blocking-dispatcher"))
      Future {
        stripper.setStartPage(p)
        stripper.setEndPage(Math.min(p + PAGES_EACH_ANALYZER - 1, totP))
        val chunk: Array[String] = stripper.getText(doc).split(REGEX)
        (p < totP, chunk)
      }.onComplete {
        case Success((true, w)) =>
          context.self ! GetWords(doc, p + PAGES_EACH_ANALYZER, totP, stripper,replyTo,docId)
          if (w.length > 0) {
            replyTo ! StartAnalyzer(p, docId, w)
          } else {
            replyTo ! AnalyzerDone()
          }
        case Success((false, _)) =>
          context.self ! DocLoader.CloseDoc(doc)
          replyTo ! LoaderDone();
        case Failure(exception) => print(s"Exception in Stripper  at page $p, totPages $totP ($exception). \n")
      }
      Behaviors.same
  }
}
}

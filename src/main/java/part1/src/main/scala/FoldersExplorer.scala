import Coordinator.StartLoader
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, Behavior}

import java.io.File

object FoldersExplorer {

  def apply(StartDir:File, nDocsFound: Int): Behavior[Msg] = Behaviors.setup(context => new FoldersExplorer(context,StartDir))

  final case class Explore(dir: File, replyTo: ActorRef[Msg]) extends Msg
  final case class RespondDocExplored(nDocsFound:Int) extends Msg

}


class FoldersExplorer (context:ActorContext[Msg],StartDir:File) extends AbstractBehavior[Msg](context){
  import FoldersExplorer._

  override def onMessage(msg: Msg): Behavior[Msg] = msg match {
    case Explore(StartDir, replyTo) =>
      var nDocsFound :Int = 0
      context.log.info("started",replyTo)
      for (f:File <- StartDir.listFiles()){
        if(f.isDirectory()){
          context.self ! Explore(f, replyTo)
        } else if (f.getName().endsWith(".pdf")) {
          context.log.info("find a new doc: " + f.getName())
          nDocsFound +=1
          replyTo ! StartLoader(f,nDocsFound)
        }

      };this
      case _ => Behaviors.stopped
  }
}

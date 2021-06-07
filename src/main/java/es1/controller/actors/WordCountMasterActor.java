package controller.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import controller.messages.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCountMasterActor extends AbstractActor  {

    List<String> files;
    Map<String, Integer> wordCount = new HashMap<>();
    ActorRef requester;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartCountingMsg.class,
                message -> { requester = sender();
                    List<ActorRef> workers = createWorkers(message.numActors);
                    files = getFilesList(message.docRoot);
                    beginSorting(files, workers);
                }).
                match(WordCountMsg.class,
                        message -> { wordCount.put(message.fileName, message.count);
                            if (wordCount.size() == files.size()) {
                                requester.tell(new WordCountSuccessMsg(wordCount), self());
                            }
                        }).
                build();
    }

    private List<ActorRef> createWorkers(int numActors) {
        List<ActorRef> actorRefs = new ArrayList<>();
        for (int i = 0; i < numActors; i++) {
            actorRefs.add(context().actorOf(Props.create(WordCountWorker.class), "wc-worker-" + i));
        }

        return actorRefs;
    }

    private List<String> getFilesList(String rootPath) {
        List<String> results = new ArrayList<>();
        File[] files = new File(rootPath).listFiles();

        for (File file : files != null ? files : new File[0]) {
            if (file.isFile()) {
                results.add(file.getPath());
            }
        }
        return results;
    }

    private void beginSorting(List<String> fileNames, List<ActorRef> workers) {
        for (int i = 0; i < fileNames.size(); i++) {
            workers.get(i % workers.size()).tell(new FileToCountMsg(fileNames.get(i)), self());
        }
    }
}

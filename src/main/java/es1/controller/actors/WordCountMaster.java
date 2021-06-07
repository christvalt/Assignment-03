package controller.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/*public class WordCountActor {

    public  class WordCountMaster extends AbstractActor {
        List<String> files;
        Map<String, Integer> wordCount = new HashMap<>();
        ActorRef requester;

        @Override
        public Receive createReceive() {
            return null;
        }

        @Override public PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder.
                    match(StartCounting.class,
                            message -> { requester = sender();
                                List<ActorRef> workers = createWorkers(message.numActors);
                                files = getFilesList(message.docRoot);
                                beginSorting(files, workers);
                            }).
                    match(WordCount.class,
                            message -> { wordCount.put(message.fileName, message.count);
                                if (wordCount.size() == files.size()) {
                                    requester.tell(new WordCountSuccess(wordCount), self());
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
                workers.get(i % workers.size()).tell(new FileToCount(fileNames.get(i)), self());
            }
        }
    }

    public class WordCountWorker extends AbstractActor {

        @Override
        public Receive createReceive() {
            return null;
        }

        @Override public PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder.
                    match(FileToCount.class,
                            message -> {
                                int count = getWordCount(message.fileName);
                                sender().tell(new WordCount(message.fileName, count), self());
                            }
                    ).
                    build();
        }

        private Integer getWordCount(String fileName) {
            Path filePath = Paths.get(fileName);
            int count = 0;
            try (BufferedReader reader = Files.newBufferedReader(filePath, UTF_8)) {
                count = reader.lines().filter(l -> !l.trim().isEmpty()).mapToInt(l -> l.split(" ").length).sum();
            } catch (IOException | UncheckedIOException e) {
                System.out.println(e.getMessage());
            }
            return count;
        }
    }

    //Message Classes
    public static class FileToCount implements Serializable {
        public final String fileName;
        public FileToCount(String fileName) {
            this.fileName = fileName;
        }
    }

    public static class WordCount implements Serializable {
        public final String fileName;
        public final int count;
        public WordCount(String fileName, int count) {
            this.fileName = fileName;
            this.count = count;
        }
    }

    public static class StartCounting implements Serializable {
        public final String docRoot;
        public final int numActors;
        public StartCounting(String docRoot, int numActors) {
            this.docRoot = docRoot;
            this.numActors = numActors;
        }
    }

    public static class WordCountSuccess implements Serializable {
        public final Map<String, Integer> result;
        public WordCountSuccess(Map<String, Integer> result) {
            this.result = result;
        }
    }
}*/
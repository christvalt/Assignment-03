package controller.actors;

import akka.actor.AbstractActor;
import controller.messages.FileToCountMsg;
import controller.messages.TurnMsg;
import controller.messages.WordCountMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WordCountWorker  extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(FileToCountMsg.class,
                message -> {
                    int count = getWordCount(message.fileName);
                    sender().tell(new WordCountMsg(message.fileName, count), self());
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

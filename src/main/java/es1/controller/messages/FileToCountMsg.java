package controller.messages;

import java.io.Serializable;

public class FileToCountMsg implements Serializable {

    public final String fileName;
    public FileToCountMsg(String fileName) {
        this.fileName = fileName;
    }
}

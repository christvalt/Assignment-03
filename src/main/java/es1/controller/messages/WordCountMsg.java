package controller.messages;

import java.io.Serializable;

public class WordCountMsg implements Serializable {

    public final String fileName;
    public final int count;
    public WordCountMsg(String fileName, int count) {
        this.fileName = fileName;
        this.count = count;
    }

}

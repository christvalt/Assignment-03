package controller.messages;

import java.io.Serializable;

public class StartCountingMsg implements Serializable {

    public final String docRoot;
    public final int numActors;
    public StartCountingMsg(String docRoot, int numActors) {
        this.docRoot = docRoot;
        this.numActors = numActors;
    }
}

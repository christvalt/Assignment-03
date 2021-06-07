package controller.messages;

import java.io.Serializable;
import java.util.Map;

public class WordCountSuccessMsg implements Serializable {
    public final Map<String, Integer> result;
    public WordCountSuccessMsg(Map<String, Integer> result) {
        this.result = result;
    }
}

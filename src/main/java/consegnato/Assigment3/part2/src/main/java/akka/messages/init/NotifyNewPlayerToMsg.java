package akka.messages.init;

import java.io.Serializable;

public class NotifyNewPlayerToMsg implements Serializable {
    private String host;
    private String port;


    public NotifyNewPlayerToMsg(String host, String port) {
        this.host = host;
        this.port = port;
    }


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}

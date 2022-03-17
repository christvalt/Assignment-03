package akka.messages.update;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.util.List;

public class UpdateListPlayersMsg implements Serializable {

    public List<ActorRef> players;


    public UpdateListPlayersMsg(List<ActorRef> players) {
        this.players = players;
    }


    public List<ActorRef> getPlayers() {
        return players;
    }
}

package akka.messages.init;


import java.io.Serializable;
import java.util.List;

public class PlayerAcceptedMsg implements Serializable {

    private final List<Integer> currentPositions;
   // private ActorRef player;

    public PlayerAcceptedMsg(final List<Integer> currentPositions) {
        this.currentPositions = currentPositions;
    }

    public List<Integer> getCurrentPositions() {
        return currentPositions;
    }
}

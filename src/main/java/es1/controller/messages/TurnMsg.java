package controller.messages;

public class TurnMsg {

    private final int actorId;
    private final long guessStartTime;

    public TurnMsg(int playerId, long guessStartTime) {
        this.actorId = playerId;
        this.guessStartTime = guessStartTime;
    }

    public int getActorId() {
        return actorId;
    }


    public long getGuessStartTime() {
        return guessStartTime;
    }

}


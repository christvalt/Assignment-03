package akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.messages.init.NewPlayerMsg;
import akka.messages.init.NotifyNewPlayerToMsg;
import akka.messages.terminate.PlayerExitMsg;
import akka.messages.init.PlayerAcceptedMsg;
import akka.messages.update.UpdateListPlayersMsg;
import akka.messages.update.UpdateNextMsg;
import akka.messages.update.UpdateTilesMsg;
import akka.puzzle.PuzzleBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Player extends AbstractActor {
    private PuzzleBoard board;
    private List<ActorRef> playerList;
    private int myIndex;




    @Override
    public void preStart() {
        this.playerList = new ArrayList<>();
        playerList.add(getSelf());
        this.initializeBoard();
        System.out.println(getSelf() + " initialized");
    }
    @Override
    public void postRestart(Throwable reason){}

    @Override
    public Receive createReceive() {
        String simplePath= "akka://DistributedSystem@";
        return receiveBuilder()
                .match(NotifyNewPlayerToMsg.class,msg->getContext()
                        .actorSelection(simplePath+msg.getHost()+":"+msg.getPort()+"/user/*")
                        .tell(new NewPlayerMsg(),getSelf()))
                .match(NewPlayerMsg.class,msg->{
                    this.addToPlayersList(getSender());
                    this.tellNextPlayer(new UpdateListPlayersMsg(this.playerList));
                    getSender().tell(new PlayerAcceptedMsg(this.board.getCurrentPositions()),getSelf());
                })
                .match(PlayerAcceptedMsg.class,msg->{board.setCurrentPositions(msg.getCurrentPositions());})
                .match(UpdateTilesMsg.class, msg->{
                    System.out.println("update tile from"+getSender());
                    if (!board.getCurrentPositions().equals(msg.getCurrentPositions())){
                        System.out.println("update : different position");
                        board.setCurrentPositions(msg.getCurrentPositions());
                        this.tellNextPlayer(new UpdateTilesMsg(msg.getCurrentPositions()));
                    }
                })
                .match(UpdateNextMsg.class, msg -> this.tellNextPlayer(new UpdateTilesMsg(this.board.getCurrentPositions())))
                .match(UpdateListPlayersMsg.class, msg -> {
                    System.out.println("update players from " + getSender());
                    if (!playerList.equals(msg.getPlayers())){
                        System.out.println("update: different players: " + msg.getPlayers());
                        this.playerList = msg.getPlayers();
                        this.myIndex = playerList.indexOf(getSelf());
                        this.tellNextPlayer(new UpdateListPlayersMsg(msg.getPlayers()));
                    }
                })
                .match(PlayerExitMsg.class, msg->{
                    System.out.println(getSender()+"to");
                    this.playerList.remove(getSender());
                    this.tellNextPlayer(new UpdateListPlayersMsg(this.playerList));
                })
                .match(Terminated.class,msg->{
                    System.out.println(msg.actor()+"terminated");
                })

                .build();
    }



    private void initializeBoard(){
        final int n = 3;
        final int m = 5;
        final String imagePath = "src/main/resources/bletchley-park-mansion.jpg";
        this.board = new PuzzleBoard(n, m, imagePath, getSelf());
        this.board.setVisible(true);
    }

    private void addToPlayersList(ActorRef newPlayerActorRef){
       // getContext().watch(newPlayerActorRef);
        //System.out.println("watching " + newPlayerActorRef + "from addToPlayers");
        final int playerIndex =(myIndex +1) % playerList.size();
        if (playerIndex==0){
            playerList.add(newPlayerActorRef);
        }else{
            this.playerList.add(playerIndex,newPlayerActorRef);
        }

    }
    private  void tellNextPlayer(final Serializable msg){

        ActorRef nextPLayer= getNextPLayer();
        if (nextPLayer !=getSelf()){

            nextPLayer.tell(msg,self());
        }

    }


    public ActorRef getNextPLayer(){
        return playerList.get((myIndex +1) % playerList.size());
    }
    @Override
    public void postStop() throws Exception {
        System.out.println("post stop");
        this.tellNextPlayer(new PlayerExitMsg());

        super.postStop();
    }
}
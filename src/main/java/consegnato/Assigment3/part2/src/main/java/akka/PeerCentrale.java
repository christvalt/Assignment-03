package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.messages.init.NotifyNewPlayerToMsg;

import java.io.File;

public class PeerCentrale {
    public final  ActorRef player;
    public final  ActorSystem system;


    public PeerCentrale (int port) {
        Config standardConfig = ConfigFactory.parseFile(new File("src/main/java/part2/player.conf"));
        Config config = ConfigFactory.parseString("akka.remote.artery.canonical.port=" + port).withFallback(standardConfig);
        //create main actor system
        system = ActorSystem.create("DistributedSystem",config);
        player = system.actorOf(Props.create(Player.class),"player");
        System.out.println("You can connect to the player on port " + port);
    }


    public void notifyPlayer(String host,String port){
        this.player.tell(new NotifyNewPlayerToMsg(host, port),ActorRef.noSender());
    }
}

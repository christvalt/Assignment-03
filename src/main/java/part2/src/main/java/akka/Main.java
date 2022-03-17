package akka;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws Exception {
        PeerCentrale peer;
        final Optional<Integer> initialPort = findFreePort();
        if (initialPort.isPresent()){
            peer = new PeerCentrale(initialPort.get());

        } else {
            throw new Exception("not available port");
        }
        if (args.length != 0){
            peer.notifyPlayer("127.0.0.1", args[0]);
        }


    }
    private static Optional<Integer> findFreePort(){
        final InetSocketAddress randomSocketAddress = new InetSocketAddress(0);
        Optional<Integer> port = Optional.empty();
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(randomSocketAddress);
            port = Optional.of(ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }



}


import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roshan
 */
public class Networking {

    int tenThousand = 10000;
    int threeThousand = 3000;
    int sixThousand = 6000;
    String ThisAddress;
    Boolean RINGEXSISTS = false;

    int SERVER_PORT_NUMBER = 63336;
    KUSOCKET Socket = new KUSOCKET();
    ENDPOINT ServerAddress = new ENDPOINT("0.0.0.0", SERVER_PORT_NUMBER);
    ENDPOINT SourceAddress = new ENDPOINT();
    MESSAGE Message = new MESSAGE();
    String Comment = new String();
    String state;
    String[] adressIP;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Networking intial = new Networking();

            intial.Socket.CreateUDPSocket(intial.ServerAddress);
            Started(intial);

            if (intial.state == "TOKENED") {

            } else if (intial.state == "RINGDOESNTEXSIST") {
                System.out.println("Creating new ring");
            }

        } catch (SocketException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Started(Networking intial) throws SocketException, IOException {
        long timeout = System.currentTimeMillis() + intial.tenThousand;

        while (System.currentTimeMillis() <= timeout) {
            boolean hasMessageArrived;

            hasMessageArrived = intial.Socket.RetrieveQueuedMessage(50, intial.Message, intial.SourceAddress);

            while (hasMessageArrived == true) {
                System.out.println("something recived");
                if ("RING EXSISTS".equals(intial.Message.Describe(intial.Comment, 256))) {
                    intial.RINGEXSISTS = true;
                    NODEnoToken(intial);
                    break;
                }

            }

        }

        if (intial.RINGEXSISTS == false) {
            NodeTokened(intial);
            intial.state = "RINGDOESNTEXSIST";
        }

    }

    public static void NODEnoToken(Networking intial) {

    }

    public static void NodeTokened(Networking intial) {
        System.out.println("entered TOKENED STATE");
        intial.Message.BuildMessage(0, 0, "RING EXSISTS", ("RING EXSISTS").length());

    }

}

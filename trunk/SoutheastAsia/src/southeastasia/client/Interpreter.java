/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.client;
import java.util.ArrayList;
import java.util.Arrays;
import southeastasia.SoutheastAsiaClientApp;

/**
 *
 * @author Leland
 */
public class Interpreter {
    public static void interpret(SoutheastAsiaClientApp receiver, String message) {
        String netcode[] = message.split("#");
        ArrayList<String> temp = new ArrayList<String> (Arrays.asList(netcode));
        temp.remove(0);
        String args[] = (String[]) temp.toArray();
        if (netcode[0].equals("chat")) {
            // chat signature: chat(identifier (who said it, likely string so we can modify
            // say for world chat, it would be Country, for private chat it would be Country (Private)),
            // chatstuff);
            receiver.receiveChat(args[0],args[1]);
        }
        else if (netcode[0].equals("stats")) {
            //here is the format: "stats"#playerid#cultural#economic#military#political#player2#etc
            if(args.length%5!=0)
            {
                System.err.println("Netcode format error! in stats");
            }
            else
            {
                //add method to clientapp that updates stats
            }
        }
        else if (netcode[0].equals("stat1")) {
            // format: "stat1"#player...wait how do we know which? baka same lang. :)) gawin na lang na same ung dun sa stats.
            
        }
        else if (netcode[0].equals("terr1")) {

        }
        else if (netcode[0].equals("terrs")) {
            //here is the format: terrs#burma#brunei#etc
            //literally just paste the entire array
        }
        else if(netcode[0].equals("verified")) {
            receiver.receiveVerified(Integer.parseInt(args[0]));
        }
        else if (netcode[0].equals("warn")) {
            receiver.receiveWarn(args[0]);
        }
        else if (netcode[0].equals("startgame")) {
            receiver.receiveStart();
        }
    }

}

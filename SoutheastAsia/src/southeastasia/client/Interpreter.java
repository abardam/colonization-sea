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
            for(int i=0;i<args.length;i++)
            {
                //add method to clientapp that adds args[i] to chat
            }
        }
        else if (netcode[0].equals("stats")) {
            //here is the format: "stats"#playerid#cultural#economic#military#political#player2#etc
            if(args.length%5!=0)
            {
                System.err.println("Netcode error! in stats");
            }
            else
            {
                //add method to clientapp that updates stats
            }
        }
        else if (netcode[0].equals("stat1")) {
            
        }
        else if (netcode[0].equals("terr1")) {

        }
        else if (netcode[0].equals("terrs")) {
            //here is the format: terrs#burma#brunei#etc
            //literally just paste the entire array
        }
        else if(netcode[0].equals("verified")) {
            
        }
        else if (netcode[0].equals("warn")) {

        }
        else if (netcode[0].equals("startgame")) {

        }
    }

}

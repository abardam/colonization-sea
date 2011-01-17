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

        }
        else if (netcode[0].equals("stats")) {

        }
        else if (netcode[0].equals("stat1")) {

        }
        else if (netcode[0].equals("terr1")) {

        }
        else if (netcode[0].equals("terrs")) {

        }
        else if(netcode[0].equals("verified")) {

        }
        else if (netcode[0].equals("warn")) {

        }
        else if (netcode[0].equals("startgame")) {

        }
    }

}

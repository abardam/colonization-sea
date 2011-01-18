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
        //String args[] = (String[]) temp.toArray();
        String args[]=new String[temp.size()];
        for(int i=0;i<args.length;i++)
        {
            args[i]=temp.get(i);
        }

        if (netcode[0].equals("chat")) {
            // chat format: args[0] is source (String), args[1] is message (String).
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
                for(int i=0;i<args.length;i+=5)
                {
                    receiver.updateStats(Integer.parseInt(args[i]), Integer.parseInt(args[i+1]), Integer.parseInt(args[i+2]), Integer.parseInt(args[i+3]), Integer.parseInt(args[i+4]));
                    
                }
            }
        }
        else if (netcode[0].equals("terrs")) {
            if(args.length!=southeastasia.SoutheastAsiaServerStats.NUM_TERRITORIES)
            {
                System.err.println("Netcode error! in territories");
            }
            else
            {
                int[] intargs=new int[args.length];
                for(int i=0;i<intargs.length;i++)
                {
                    intargs[i]=Integer.parseInt(args[i]);
                }
                receiver.updateTerritories(intargs);
            }
        }
        else if(netcode[0].equals("verified")) {
            receiver.receiveVerify(Integer.parseInt(args[0]));
        }
        else if (netcode[0].equals("warn")) {
                receiver.receiveWarn(args[0]);

        }
        else if (netcode[0].equals("startgame")) {
            receiver.receiveStart();
        }
        else if(netcode[0].equals("privmsg"))
        {
            //got a private message!
            //privmsg#sender#message
            
        }
    }

}

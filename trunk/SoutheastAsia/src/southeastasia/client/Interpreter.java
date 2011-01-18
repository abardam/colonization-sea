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
            for(int i=0;i<args.length;i++)
            {
                //add method to clientapp that adds args[i] to chat
                receiver.addChat(args[i]);
            }
        }
        else if (netcode[0].equals("stats")) {
            //here is the format: "stats"#playerid#cultural#economic#military#political#player2#etc
            if(args.length%5!=0)
            {
                System.err.println("Netcode format error! in stats");
            }
            else
            {
                //receiver.receiveStats(args);
                for(int i=0;i<args.length;i+=5)
                {
                    receiver.updateStats(Integer.parseInt(args[i]), Integer.parseInt(args[i+1]), Integer.parseInt(args[i+2]), Integer.parseInt(args[i+3]), Integer.parseInt(args[i+4]));
                    
                }
            }
        }/*
        else if (netcode[0].equals("stat1")) {
            // format: "stat1"#player...wait how do we know which? baka same lang. :)) gawin na lang na same ung dun sa stats.
            
        }
        else if (netcode[0].equals("terr1")) {

        }*/
        else if (netcode[0].equals("terrs")) {
            //here is the format: terrs#burma#brunei#etc
            //literally just paste the entire array

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
            }
        }
        else if(netcode[0].equals("verified")) {
            receiver.recieveVerify(Integer.parseInt(args[0]));
        }
        else if (netcode[0].equals("warn")) {
            String warning="";
            for(String s:args)
            {
                warning+=s+"\n";
            }
                receiver.recieveWarn(warning);

        }
        else if (netcode[0].equals("startgame")) {
            receiver.startGameScreen();
        }
    }

}

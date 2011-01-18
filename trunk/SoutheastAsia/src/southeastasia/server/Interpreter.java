/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.server;

import southeastasia.SoutheastAsiaView;

/**
 *
 * @author Leland
 */
public class Interpreter {
    public static void interpret (SoutheastAsiaView receiver, int source, String input) {
        String temp[] = input.split("#");
        if (temp[0].equals("chat")) {
            receiver.sendChat(source, temp[1]);
        } else if (temp[0].equals("privmsg")) {
            int rec = Integer.parseInt(temp[1]);
            if (rec < southeastasia.SoutheastAsiaApp.MAX_PLAYERS) {
                receiver.sendPrivateMessage(rec, source, temp[2]);
            }
        }
        else if (temp[0].equals("action")) {
        } else {
            System.out.println("Received invalid order. " + input);
        }
    }

}

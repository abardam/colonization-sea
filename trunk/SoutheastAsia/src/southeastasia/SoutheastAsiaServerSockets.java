/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package southeastasia;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Coming soon: THREADS
 * 
 * @author Albert
 */
public class SoutheastAsiaServerSockets {

    private Socket[] players;
    private int port;
    private Accepter accepter;
    //private ServerSocket[]server;
    private ServerSocket server;

    public SoutheastAsiaServerSockets() throws IOException {
        players = new Socket[SoutheastAsiaApp.MAX_PLAYERS];
        //server = new ServerSocket[SoutheastAsiaApp.MAX_PLAYERS];
        port = 7777;
        server = new ServerSocket(port);
        accepter = new Accepter();
        
    }

    /**
     * this accepts clients and inputs them into the array
     * 
     * to do: add parameters
     * 
     */
    public void acceptSockets() {
        accepter.start();
    }

    public int getPlayer() {
        return 1;
    }

    class Accepter extends Thread
    {
        int i;
        int max;

        public Accepter() {
         i = 0;
         max = SoutheastAsiaApp.MAX_PLAYERS;
         //max = 1;
        }

        @Override
        public void run()
        {
            while (i < max)
            {
                    System.out.println("Waiting for player " + (i + 1) + "...");

                    /**
                     * TODO: Make new threads for the sockets?
                     * See if it's necessary.
                     */

                try {
                    players[i] = server.accept();
                } catch (IOException ex) {
                    System.out.println("Jumping Javabeans, Batman, something happened!");
                    Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                }


                i++;
            }
            System.out.println("Slots full. Ceasing to accept.");
        }
        
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package southeastasia;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Coming soon: THREADS
 * 
 * @author Albert
 */
public class SoutheastAsiaServerSockets {

    private Socket[] players;
    private final int port;
    private final int MAX_PLAYERS;
    private Accepter accepter;
    //private ServerSocket[]server;
    private ServerSocket server;
    private PrintWriter[] sender;
    

    public SoutheastAsiaServerSockets() throws IOException {
        MAX_PLAYERS = SoutheastAsiaApp.MAX_PLAYERS;
        players = new Socket[MAX_PLAYERS];
        //server = new ServerSocket[SoutheastAsiaApp.MAX_PLAYERS];
        port = 7777;
        server = new ServerSocket(port);
        accepter = new Accepter();
        sender = new PrintWriter[MAX_PLAYERS];
        
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

    public void giveOrder(String order, int player)
    {
        //insert string, sender[player] printstream shizz here
    }

    public void sendToAll(String message)
    {
        for (int i = 0; i < MAX_PLAYERS; i++)
        {
            sender[i].print(message);
        }
    }

    public void interpret(String order, int player)
    {
        //same as above
    }

    public void interpret(String order)
    {
        //lookie the top
    }

    public Socket getPlayer(int i) {
        return players[i - 1];
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

                    
                try
                {
                    players[i] = server.accept();
                    sender[i] = new PrintWriter(players[i].getOutputStream(), true);
                    
                    PlayRunner g = new PlayRunner(players[i]);
                    g.start();
                }
                
                catch (IOException ex)
                {
                    Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Jumping Javabeans, Batman, something happened!");
                }



                i++;
            }
            System.out.println("Slots full. Ceasing to accept.");
        }
        
    }

        class PlayRunner extends Thread //doot doot doot. Should I stick this into another class?
    {
            Socket socket;

            public PlayRunner(Socket socket)
            {
                this.socket = socket;
            }

        @Override
            public void run()
            {
                try
                {
                    
                    Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));

                    while(true)
                    {
                        String msg = in.nextLine();
                        if (!(msg.equals("")||msg==null))
                        {
                            //interpret(msg); ? Is this how it should be done?
                        }
                        
                    }
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Yikes! Something happened.");
                }


            }

    }
    
}

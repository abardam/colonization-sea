/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package southeastasia.networking;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import southeastasia.ChatWindow;
import southeastasia.SoutheastAsiaApp;

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
    private ChatWindow chat;    //Might be unnecessary. - A., 110109
    private SoutheastAsiaApp seaApp;

    public SoutheastAsiaServerSockets() throws IOException {
        seaApp = SoutheastAsiaApp.getApplication();
        MAX_PLAYERS = SoutheastAsiaApp.MAX_PLAYERS;
        players = new Socket[MAX_PLAYERS];
        //server = new ServerSocket[SoutheastAsiaApp.MAX_PLAYERS];
        port = 7777;
        server = new ServerSocket(port);
        accepter = new Accepter(this);
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

    public void sendToOne(String message, int player) {
        sender[player].println(message);
        //insert string, sender[player] printstream shizz here
    }

    public void setChat(ChatWindow c) {
        chat = c;
    }

    public void sendToAll(String message) {

        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (sender[i] != null) {
                sender[i].println(message);
            }
        }
    }

    public void interpret(String order, int player) {
        seaApp.receive(player, order); // ... nirefactor ko na lang. pwede pala na nasa asia view.
        System.out.println("Yo - ServerSockets has received input from Player " + player);
    }

    /*public void interpret(String order) {
    seaApp.receive(order);
    System.out.println("FroYo - ServerSockets has received input!");
    //chat.tempAddMessage(order);     // Expand tempAddMessage in ChatWindow and this afterwards
    }*/ // hindi yata kailangan, since all commands come from one person.
    public Socket getPlayer(int i) {
        return players[i - 1];
    }

    public int getNumPlayers()
    {
        int np=0;
        for(int i=0;i<MAX_PLAYERS;i++)
        {
            if(players[i]!=null)
            {
                np++;
            }
        }

        return np;
    }

    class Accepter extends Thread {

        int i;
        int max;
        SoutheastAsiaServerSockets ss;

        public Accepter(SoutheastAsiaServerSockets ss) {
            i = 0;
            max = SoutheastAsiaApp.MAX_PLAYERS;
            //max = 1;
            this.ss = ss;
        }

        @Override
        public void run() {
            while (i < max) {

                System.out.println("Waiting for player " + (i + 1) + "...");

                try {
                    players[i] = server.accept();
                    sender[i] = new PrintWriter(players[i].getOutputStream(), true);
                    PlayRunner g = new PlayRunner(players[i], ss, i);
                    g.start();
                    sendToOne("verified#" + i, i);
                } catch (IOException ex) {
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
        SoutheastAsiaServerSockets ss;
        int i;

        public PlayRunner(Socket socket, SoutheastAsiaServerSockets ss, int i) {
            this.socket = socket;
            this.ss = ss;
            this.i = i;
        }

        @Override
        public void run() {
            try {

                Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    String msg = in.nextLine();
                    if (!(msg.equals("") || msg == null)) {
                        //interpret(msg); ? Is this how it should be done?
                        ss.interpret(msg, i);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Yikes! Something happened.");
            }


        }
    }
}

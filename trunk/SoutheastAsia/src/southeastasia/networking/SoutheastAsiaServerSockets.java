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
//        System.out.println("Yo - ServerSockets has received input from Player " + player);
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
        private PlayRunner[] playRunner;

        public Accepter(SoutheastAsiaServerSockets ss) {
            i = 0;
            max = SoutheastAsiaApp.MAX_PLAYERS;
            //max = 1;
            this.ss = ss;
            playRunner=new PlayRunner[max];
        }

        @Override
        public void run() {
            while (i < max) {

                System.out.println("Waiting for player " + (i + 1) + "...");

                try {
                    Socket s=server.accept();
                    PlayRunner clone=findSocketClone(s);
                    if(clone==null)
                    {
                        players[i] = s;
                        sender[i] = new PrintWriter(players[i].getOutputStream(), true);
                        playRunner[i] = new PlayRunner(players[i], ss, i);
                        playRunner[i].start();
                        sendToOne("verified#" + i, i);
                        i++;
                    }
                    else
                    {
                        int oldNum=clone.setSocket(s);
                        players[oldNum]=s;
                        sender[oldNum]=new PrintWriter(s.getOutputStream(), true);
                        sendToOne("verified#"+oldNum, oldNum);
                        seaApp.sendBigUpdate(oldNum);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Jumping Javabeans, Batman, something happened!");
                }
            }
            System.out.println("Slots full. Ceasing to accept.");
        }

        private PlayRunner findSocketClone(Socket s) {
            for(PlayRunner pr:playRunner)
            {
                if(pr!=null&&pr.socketEquals(s))
                {
//                    return pr;
                    return null;
                }
            }
            return null;
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

        private boolean socketEquals(Socket s) {
            return(socket.getInetAddress().equals(s.getInetAddress()));
        }

        private int setSocket(Socket s) {
            socket=s;
            return i;
        }
    }
}

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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enzo
 */
public class SoutheastAsiaClient {
    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    private SoutheastAsiaClientApp app;
    private Socket socket;
    private PlayRunner runner;
    private PrintWriter sender;

    public SoutheastAsiaClient(FakeSockets fs)
    {
        //call this constructor when using fakesockets
        useFakeSockets=true;
        fakesockets=fs;
    }

    public SoutheastAsiaClient()
    {
        useFakeSockets=false;
    }

    public void setApp(SoutheastAsiaClientApp seapp)
    {
        app=seapp;
    }

    public void setSocket(Socket s)
    {
        socket = s;
        runner = new PlayRunner(socket);
        runner.start();
    }

    // to add: interpret messages, and send them out

    public void sendMessage(String message)
    {
        if(useFakeSockets)
        {
            fakesockets.serverRecieveTransmission(message);
        }
        else
        {
            /* put not fake sockets here
             * para fun, gonna need to use outputstreamwriter
             * must learn to flush.
             */
        }
    }

    public int getClientCode()
    {
        if(useFakeSockets)
            return fakesockets.getClientCode(this);
        else
            System.out.println("EDIT SoutheastAsiaClient.getClientCode to use not-fake-sockets!");
        return -1;
    }

    public void recieveMessage(String message)
    {
        if(message.equals("startgame"))
        {
            //switch screen
            app.startGameScreen();

        }
        else
        {
            app.tempMessage(message);
        }
    }



            class PlayRunner extends Thread //Copy-pasted from ServerSockets
    {
            Socket playSocket;

            public PlayRunner(Socket socket)
            {
                this.playSocket = socket;
            }

                @Override
                public void run() {
                    try {

                        sender = new PrintWriter(socket.getOutputStream(), true);
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

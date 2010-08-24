/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

import java.net.Socket;

/**
 *
 * @author Enzo
 */
public class SoutheastAsiaClient {
    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    private SoutheastAsiaClientApp app;
    private Socket socket;
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
    }

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

    /*
     * todo: make a thread for input!
     */
}

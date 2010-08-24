/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

/**
 *
 * @author Enzo
 */
public class SoutheastAsiaClient {
    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    private SoutheastAsiaClientApp app;

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

    public void sendMessage(String message)
    {
        if(useFakeSockets)
        {
            fakesockets.serverRecieveTransmission(message);
        }
        else
        {
            //put not fake sockets here
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
}

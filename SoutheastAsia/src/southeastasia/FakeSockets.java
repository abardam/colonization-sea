/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;
import java.util.ArrayList;
/**
 *
 * for simulating sockets
 * @author Enzo
 */
public class FakeSockets {
    private SoutheastAsiaApp server;
    private ArrayList<SoutheastAsiaClientApp> clients;
    public FakeSockets(SoutheastAsiaApp server)
    {
        this.server=server;
        clients=new ArrayList<SoutheastAsiaClientApp>();

    }

    public void addClient(SoutheastAsiaClientApp sac)
    {
        clients.add(sac);
    }

    public void serverRecieveTransmission(String message)
    {
        //parse it
        //call some methods

        //temporary parsing
        server.tempParse(message);
    }

    public void clientRecieveTransmission(String message, int clientCode)
    {
        if(clientCode>=0)
        {
            int i=0;
            for(SoutheastAsiaClientApp sac:clients)
            {
                if(i==clientCode)
                {
                    //parse it
                    //call methods of sac
                    sac.recieveMessage(message);

                }
                i++;
            }
        }
    }

    public int getClientCode(SoutheastAsiaClientApp sac2)
    {
        int i=0;
        for(SoutheastAsiaClientApp sac:clients)
        {
            if(sac==sac2)
            {
                return i;

            }
            i++;
        }

        return -1;
    }
    

}

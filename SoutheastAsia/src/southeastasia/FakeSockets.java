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
    private ArrayList<SoutheastAsiaClient> clients;
    public FakeSockets(SoutheastAsiaApp server)
    {
        this.server=server;
        clients=new ArrayList<SoutheastAsiaClient>();

    }

    public void addClient(SoutheastAsiaClient sac)
    {
        clients.add(sac);
    }

    public void recieveTransmission(String message)
    {
        //parse it
        //call some methods

        //temporary parsing
        server.tempParse(message);
    }

}

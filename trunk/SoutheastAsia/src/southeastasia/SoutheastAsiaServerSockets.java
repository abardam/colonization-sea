/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package southeastasia;

/**
 * Coming soon: THREADS
 * 
 * @author Albert
 */
public class SoutheastAsiaServerSockets {

    int[] players;
    Accepter accepter;

    public SoutheastAsiaServerSockets() {
        players = new int[SoutheastAsiaApp.MAX_PLAYERS];
        accepter = new Accepter();

    }

    /**
     * this accepts clients and inputs them into the array
     * 
     * to do: add parameters
     * 
     */
    public void acceptSockets() {
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
        }

        public void run()
        {
            while (i < max)
            {
                //wait for socket
                //accept socket
                //assign socket as i
                i++;
            }
        }

        public void setPlayers(int max)
        {
            this.max = max;
        }
        
    }
    
}

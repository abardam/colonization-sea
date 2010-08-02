/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

/**
 *
 * @author Enzo
 *
 * "actual" class for the server
 */
public class SoutheastAsiaServer {
    
    private SoutheastAsiaServerStats stats;
    private boolean gameStarted;

    public SoutheastAsiaServer()
    {
        stats=new SoutheastAsiaServerStats();
        gameStarted=false;
    }

    /**
     * call this class whenever a new turn starts
     * (possibly the teacher presses a button)
     * wipes all actions, problems, and sets stats
     *
     * if a country has not submitted an action, override must
     * be "true" in order to continue
     *
     * returns 1 if all countries submitted, 2 if not all submitted
     * but was overridden, 0 if not all submitted and was not overridden
     *
     * @param override true if next turn starts with not all countries ready with an action
     */
    public int startNewTurn(boolean override)
    {
        if(gameStarted)
        {
            
        }
        return 0;
    }

    /**
     *
     * call this function whenever a chat message is recieved
     * from the players or the sent by the teacher
     *
     * adds the chat to the log in serverstats
     *
     * @param chat the chat message in some format (handle plz)
     * @return
     */
    public int recieveChatMessage(String chat)
    {
        stats.updateChatlog(chat);

        return 1;
    }

    /**
     *
     * this function interprets the strings sent by the
     * sockets and plugs it into the stats as Actions
     *
     * @param action the action in the code transmitted
     * @return 1 if valid, 0 if not
     */
    public int recieveAction(String action)
    {
        //do the parsing here

        return 0;
    }

    /**
     * this method returns the action corresponding to the
     * country
     *
     * i guess kung wala pang action it returns something else
     *
     * @param playerCode the player's number
     * @return the action in String format
     */
    public String getAction(int playerCode)
    {
        return stats.getAction(playerCode).toString();
    }

    /**
     *
     * call this method during country select
     * 
     * @param playerCode a number from 0-5 representing a player
     * @param countryCode a number from 0-5 representing the country
     * @return 1 if successful 0 if country already chosen -1 if playercode invalid -2 if countrycode invalid
     */
    public int chooseCountry(int playerCode, int countryCode)
    {
        return 0;
    }
    
    /**
     * call this method when done with country select
     * 
     * will not start if not all slots are filled and override is false
     * will not start if 2+ players have the same country (probably shouldnt happen but whatever)
     *
     * sets gameStarted to true
     * prepares ServerStats
     * 
     * @param override set to true if starting with incomplete players
     * @return 0 if not all slots filled 1 if all slots filled 2 if country overlap
     */
    public int startGame(boolean override)
    {
        //put some code in here
        //gameStarted=true;
        return 0;
    }
}

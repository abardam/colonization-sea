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

    public SoutheastAsiaServer()
    {
        stats=new SoutheastAsiaServerStats();
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
     * @param countryCode the country's number
     * @return the action in String format
     */
    public String getAction(int countryCode)
    {
        return stats.getAction(countryCode).toString();
    }
}

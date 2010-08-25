/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;
import java.io.IOException;
/**
 *
 * @author Enzo
 *
 * "actual" class for the server
 */
public class SoutheastAsiaServer {
    
    private SoutheastAsiaServerStats stats;
    private boolean gameStarted;
    private SoutheastAsiaServerSockets ss;
    public SoutheastAsiaView window;

    public SoutheastAsiaServer()
    {
        try
        {
            ss = new SoutheastAsiaServerSockets();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
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
            if(stats.newTurn())
            {
                //game won! now check to see who won and in what category
            }
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

    public String getActionName(int playerCode)
    {
        return stats.getActionName(playerCode);
    }

    public boolean getActionApproved(int playerCode)
    {
        return stats.getActionApproved(playerCode);
    }

    public String getProblem(int playerCode)
    {
        return stats.getProblem(playerCode);
    }

    public String getProblemName(int playerCode)
    {
        return stats.getProblemName(playerCode);
    }

    /**
     *
     * call this method during country select
     *
     * once picked, countries cannot be picked again unless overridden
     * 
     * @param playerCode a number from 0-5 representing a player
     * @param countryCode a number from 0-5 representing the country
     * @param override if true, can replace country already picked
     * @return 1 if successful 0 if country already chosen -1 if playercode invalid -2 if countrycode invalid
     */
    public int chooseCountry(int playerCode, int countryCode, boolean override)
    {
        if(override)
        {
            stats.replaceAllCountryChoices(countryCode); //all players who have picked the country will be reset
            return stats.setCountry(playerCode, countryCode);
        }
        else
        {
            if(stats.countryIsAlreadySelected(countryCode))
            {
                return 0;
            }
            else
            {
                return stats.setCountry(playerCode, countryCode);
            }

        }
    }

    public int getCountry(int playerCode)
    {
        return stats.getCountry(playerCode);
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
     * @return 0 if not all slots filled 1 if all slots filled OR overridden 2 if country overlap
     */
    public int startGame(boolean override)
    {
        //put some code in here
        //gameStarted=true;
        return 1;
    }

    /**
     *
     * generates the text log for this turn
     *
     * includes: stats at the start, problems generated, actions, whether the
     * problem was solved, stats at the end
     *
     * @return the log for this turn
     */
    public String generateTurnLog()
    {
        //stuff here
        return "";
    }

    /**
     * solves the problem belonging to a player
     * (called before the end of the turn)
     * @param playerCode
     * @return 1 if problem exists, 0 otherwise
     */
    public int solveProblem(int playerCode)
    {
        return stats.solveProblem(playerCode);
        
    }

    /**
     * gets the stats of a player
     * @param playerCode the player to query
     * @return the stats
     */
    public CountryVariables getStats(int playerCode)
    {
        return stats.getStats(playerCode);
        
    }

    public static SoutheastAsiaAction parseAction(String actionCode)
    {
        //change the delimiter here
        String[] newaction= actionCode.split("#");
        return new SoutheastAsiaAction(newaction[1],newaction[2],Integer.parseInt(newaction[3]),Integer.parseInt(newaction[4]),Integer.parseInt(newaction[5]),Integer.parseInt(newaction[6]));
        //cultural, economic, military, political
    }

    /**
     * call this method to send in actions
     * @param playerCode the player sending in an action
     * @param actionCode the action in encoded string format
     * @param override if set to false, will not change existing action
     * @return 1 if action set, 0 if there is an existing action, 2 if there is an existing action but was overridden
     */
    public int setAction(String actionCode, boolean override)
    {
        int playerCode; //do something to get playerCode from actionCode

        String parsedCode[]=actionCode.split("#");
        playerCode=Integer.parseInt(parsedCode[0]);

        if(stats.hasAction(playerCode))
        {
            if(override)
            {
                //parse actioncode, turn it into an action
                stats.setAction(parseAction(actionCode), playerCode);

                
                //pass to serverstats
                 stats.setApproval(playerCode, false);

                 window.updateActionTables();

                return 2;
            }

            return 0;
        }
        //parse actioncode, turn it into an action
        //pass it to serverstats

        stats.setAction(parseAction(actionCode), playerCode);
        //set action approval to false
        stats.setApproval(playerCode, false);

        window.updateActionTables();

        return 1;
    }

    public static Problem generateProblem()
    {
        return Problem.noProblem();
    }

    public void accept()
    {
        ss.acceptSockets();
    }

    public int countPlayers()
    {
        return stats.countSelectedCountries();
    }

    public void setAction(SoutheastAsiaAction seact, int playerCode)
    {
        stats.setAction(seact, playerCode);
    }

    public SoutheastAsiaAction getActionData(int playerCode)
    {
        return stats.getActionData(playerCode);
    }

    public int getPlayerCode(int countryCode)
    {
        return stats.getPlayerCode(countryCode);
    }

    public int getPlayerCode(String countryCode)
    {
        return stats.getPlayerCode(Integer.parseInt(countryCode));
    }

    public void setApproval(boolean approval, int playerCode)
    {
        stats.setApproval(playerCode, approval);
    }
}

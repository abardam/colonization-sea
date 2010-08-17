/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package southeastasia;

import java.util.ArrayList;

/**
 *
 * @author Enzo
 *
 * HELOW
 * this class is for stuff like country variables and chatlogs
 */
public class SoutheastAsiaServerStats {

    //an array for storing each country's stats
    private CountryVariables[] variables;

    private ArrayList<String> chatlog;

    //array for storing each country's current actions
    private Action[] actions;

    private Problem[] problems;

    //is the action approved?
    private boolean[] approval;

    //in this array the index is the player and the number is the country
    //so if countries[0]=1 that means player 0 has country 1 (whatever that is)
    private int[] countries;

    //each player corresponds to a number (a slot in the arrays)

    public SoutheastAsiaServerStats()
    {
        //initialize arrays
        variables=new CountryVariables[SoutheastAsiaApp.MAX_PLAYERS];
        chatlog=new ArrayList<String>();
        actions=new Action[SoutheastAsiaApp.MAX_PLAYERS];
        problems=new Problem[SoutheastAsiaApp.MAX_PLAYERS];
        countries=new int[SoutheastAsiaApp.MAX_PLAYERS];
        approval=new boolean[SoutheastAsiaApp.MAX_PLAYERS];

        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            variables[i]=new CountryVariables("");
            actions[i]=Action.noAction();
            problems[i]=Problem.noProblem();
            countries[i]=-1;
            approval[i]=false;
        }
    }

    //to do: update chatlog method
    //to do: get chatlog method

    public int updateChatlog(String chat)
    {
        chatlog.add(chat);
        return 1;
    }

    //to do: update actions method
    //to do: get actions method

    public Action getAction(int playerCode)
    {
        return actions[playerCode];
    }

    /**
     * checks if the specified country is already selected
     *
     * @param countryCode the country to be checked
     * @return true if selected, false otherwise
     */
    public boolean countryIsAlreadySelected(int countryCode)
    {
        if(countryCode<0||countryCode>=SoutheastAsiaApp.MAX_PLAYERS)
        {
            return false;
        }

        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(countries[i]==countryCode)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * replaces all players who have picked a country with country -1
     * @param countryCode the country code to be replaced with -1
     * @return the number of countries replaced
     */
    public int replaceAllCountryChoices(int countryCode)
    {
        int number=0;

        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(countries[i]==countryCode)
            {
                number++;
                countries[i]=-1;
            }
        }

        return number;
    }

    /**
     * sets the country of a player
     * @param playerCode the code of the player
     * @param countryCode the code of the country
     * @return 1 if successful, -1 if playercode invalid -2 if countrycode invalid
     */
    public int setCountry(int playerCode, int countryCode)
    {
        if(playerCode>=0&&playerCode<SoutheastAsiaApp.MAX_PLAYERS)
        {
            //to do: check if countrycode is valid
            countries[playerCode]=countryCode;
            return 1;
        }

        return -1;
    }

    public int getCountry(int playerCode)
    {
        return countries[playerCode];
    }

    /**
     * call this method every turn
     *
     * updates all stats
     * wipes all actions
     *
     * @return true if there is a winner false otherwise
     */
    public boolean newTurn()
    {
        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(approval[i])
            {
                //apply effects of the action
                actions[i].applyEffect(variables[i]);
            }

            approval[i]=false;

            actions[i]=Action.noAction();
        }

        //the reason that this is in a separate for loop is so that
        //maapply yung stats ng lahat ng countries first
        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(variables[i].cultural>=100||variables[i].economic>=100||variables[i].military>=100||variables[i].political>=100)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * returns the stats of the corresponding player
     * @param playerCode the player to query
     * @return the stats
     */
    public CountryVariables getStats(int playerCode)
    {
        return variables[playerCode];
    }

    /**
     * solves the problem belonging to a player
     * (called before the end of the turn)
     * @param playerCode
     * @return 1 if problem exists, 0 otherwise
     */
    public int solveProblem(int playerCode)
    {
        if(problems[playerCode].isNull)
        {
            return 0;
        }
        else
        {
            problems[playerCode].solveProblem(variables[playerCode]);
            return 1;
        }
    }

    /**
     * sets action approval
     * @param playerCode the player to set approval
     * @param approval true if action is approved
     */
    public void setApproval(int playerCode, boolean approval)
    {
        this.approval[playerCode]=approval;
    }

    /**
     * this method checks if a player has sent in an action this turn
     * @param playerCode the player
     * @return true if the player has an action
     */
    public boolean hasAction(int playerCode)
    {
        return !(actions[playerCode].isNull);

    }

    /**
     * counts how many players have countryCodes of not -1
     * @return the number of players who have selected countries
     */
    public int countSelectedCountries()
    {
        int count=0;
        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(countries[i]>=0&&countries[i]<6)
            {
                count++;
            }
        }
        return count;
    }
}

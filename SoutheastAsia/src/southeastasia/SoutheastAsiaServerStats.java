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

    //to do: put an array for storing each country's stats (use CountryVariables)
    private CountryVariables[] variables;

    //to do: chatlog (array siguro of strings)
    private ArrayList<String> chatlog;

    //to do: put an array for storing each country's current actions (use Action)
    private Action[] actions;

    //to do: an array for storing each player's country
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
        countries=new int[SoutheastAsiaApp.MAX_PLAYERS];
    }

    //to do: update stats method
    //to do: get stats method

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


}

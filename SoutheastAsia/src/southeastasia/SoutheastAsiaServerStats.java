/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

package southeastasia;

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
    }

    //to do: update stats method
    //to do: get stats method

    //to do: update chatlog method
    //to do: get chatlog method

    public int updateChatlog(String chat)
    {
        return 1;
    }

    //to do: update actions method
    //to do: get actions method

    public Action getAction(int playerCode)
    {
        return actions[playerCode];
    }


}

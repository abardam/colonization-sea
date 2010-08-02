/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    //to do: chatlog (array siguro of strings)

    //to do: put an array for storing each country's current actions (use Action)
    private Action[] actions;

    //each country corresponds to a number (a slot in the arrays)

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

    public Action getAction(int countryCode)
    {
        return actions[countryCode];
    }

}

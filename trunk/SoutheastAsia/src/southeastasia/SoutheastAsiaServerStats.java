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
    private SoutheastAsiaAction[] actions;

    private Problem[] problems;

    //is the problem solved?
    private boolean[] solved;

    //is the action approved?
    private boolean[] approval;

    //in this array the index is the player and the number is the country
    //so if countries[0]=1 that means player 0 has country 1 (whatever that is)
    private int[] countries;


    //each player corresponds to a number (a slot in the arrays)

    private int[] territories;
    //each number here corresponds to a territory:
    public static final int BURMA=0;
    public static final int BRUNEI=1;
    public static final int CAMBODIA=2;
    public static final int JAVA=3;
    public static final int KALIMANTAN=4;
    public static final int LAOS=5;
    public static final int MALAYA=6;
    public static final int PAPUA=7;
    public static final int PHILIPPINES=8;
    public static final int SABAH=9;
    public static final int SARAWAK=10;
    public static final int SULAWESI=11;
    public static final int SUMATRA=12;
    public static final int THAILAND=13;
    public static final int TIMOR=14;
    public static final int VIETNAM=15;
    public static final int NUM_TERRITORIES=16;

    public static String[] TERRITORY_NAME;

    public SoutheastAsiaServerStats()
    {
        //initialize arrays
        variables=new CountryVariables[SoutheastAsiaApp.MAX_PLAYERS];
        chatlog=new ArrayList<String>();
        actions=new SoutheastAsiaAction[SoutheastAsiaApp.MAX_PLAYERS];
        problems=new Problem[SoutheastAsiaApp.MAX_PLAYERS];
        countries=new int[SoutheastAsiaApp.MAX_PLAYERS];
        approval=new boolean[SoutheastAsiaApp.MAX_PLAYERS];
        solved=new boolean[SoutheastAsiaApp.MAX_PLAYERS];

        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            variables[i]=new CountryVariables("");
            actions[i]=SoutheastAsiaAction.noAction();
            problems[i]=Problem.noProblem();
            countries[i]=-1;
            approval[i]=false;
            solved[i]=false;
        }

        territories=new int[NUM_TERRITORIES];

        for(int i=0;i<NUM_TERRITORIES;i++)
        {
            territories[i]=-1;
        }

        TERRITORY_NAME=new String[NUM_TERRITORIES];
        TERRITORY_NAME[0] = "Burma";
        TERRITORY_NAME[1] = "Brunei";
        TERRITORY_NAME[2] = "Cambodia";
        TERRITORY_NAME[3] = "Java";
        TERRITORY_NAME[4] = "Kalimantan";
        TERRITORY_NAME[5] = "Laos";
        TERRITORY_NAME[6] = "Malaya & Singapore";
        TERRITORY_NAME[7] = "Papua New Guinea";
        TERRITORY_NAME[8] = "The Philippines";
        TERRITORY_NAME[9] = "Sabah";
        TERRITORY_NAME[10] = "Sarawak";
        TERRITORY_NAME[11] = "Sulawesi & Moluccas";
        TERRITORY_NAME[12] = "Sumatra";
        TERRITORY_NAME[13] = "Thailand";
        TERRITORY_NAME[14] = "Timor";
        TERRITORY_NAME[15] = "Vietnam";
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

    public SoutheastAsiaAction getAction(int playerCode)
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
        //System.out.println("plcold"+playerCode);
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

                if(actions[i].landing!=-1)
                {
                    territories[actions[i].landing]=i;
                }
            }

            approval[i]=false;

            actions[i]=SoutheastAsiaAction.noAction();

            if(!problems[i].isNull)
            {
                problems[i].applyEffect(this.variables[i]);
                
                if(solved[i])
                {
                    solveProblem(i);
                }


                solved[i]=false;
                problems[i]=Problem.noProblem();
            }

            //TO DO: landings!

            
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
     * (called at the end of the turn)
     * @param playerCode
     * @return 1 if problem exists, 0 otherwise
     */
    public int solveProblem(int playerCode)
    {
        if(problems[playerCode].isNull)
        {
            return 0;
        }
        else if(solved[playerCode])
        {
            problems[playerCode].solveProblem(variables[playerCode]);
            return 1;
        }
        return 0;
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


    public String[] getCountryNames()
    {
        int count=SoutheastAsiaApp.MAX_PLAYERS;
        String[] a=new String[count];
        for(int i=0;i<count;i++)
        {
            a[i]=variables[i].name;
        }
        return a;
    }

    public int setAction(SoutheastAsiaAction seact, int playerCode)
    {
        actions[playerCode]=seact;
        approval[playerCode]=false;
        return 1;
    }

    public int setProblem(Problem prob, int playerCode)
    {
        problems[playerCode]=prob;
        solved[playerCode]=false;
        return 1;
    }

    public boolean getActionApproved(int playerCode)
    {
        return approval[playerCode];
    }

    public String getProblem(int playerCode)
    {
        return problems[playerCode].toString();
    }

    public boolean getProblemSolved(int playerCode)
    {
        return solved[playerCode];
    }

    public void setSolved(int playerCode, boolean psolved)
    {
        solved[playerCode]=psolved;
    }

    public String getProblemName(int playerCode)
    {
        return problems[playerCode].name;
    }

    public String getActionName(int playerCode)
    {
        return actions[playerCode].name;
    }

    public SoutheastAsiaAction getActionData(int playerCode)
    {
        return actions[playerCode];
    }

    //dumb way to get player code by reversing country name
    public int getPlayerCode(String countryName)
    {
        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(variables[i].name.equalsIgnoreCase(countryName))
            {
                return i;
            }
        }
        return -1;
    }

    //dumb way to get player code by reversing country code but whatever
    public int getPlayerCode(int countryCode)
    {
        if(countryCode==-1)
        {
            return -1;
        }
        else
        {
            for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
            {
                if(countries[i]==countryCode)
                {
                    return i;
                }
            }

        }

        return -1;
    }

    public Problem getProblemData(int playerCode)
    {
        return problems[playerCode];
    }

    public void setTerritory(int territoryCode, int countryCode)
    {
        territories[territoryCode]=countryCode;
    }

    public boolean landingConflicts()
    {
        //just checks if any two+ countries have the same landing

        boolean[] territorycheck=new boolean[NUM_TERRITORIES];
        for(int i=0;i<NUM_TERRITORIES;i++)
        {
            territorycheck[i]=false;
        }

        for(int i=0;i<countSelectedCountries();i++)
        {
            if(actions[i].landing!=-1)
            {
                if(territorycheck[actions[i].landing])
                {
                    return true;
                }
                else
                {
                    territorycheck[actions[i].landing]=true;
                }
            }

        }

        return false;


    }


    public int[] getTerritories() {
        return territories;
    }
}

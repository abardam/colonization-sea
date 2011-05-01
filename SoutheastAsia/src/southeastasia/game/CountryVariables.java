/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

/**
 *
 * @author Enzo
 * stores variables per country
 * walang struct sa java :<
 */
public class CountryVariables {
    public String name;
    public String playerName;
    public int political;
    public int cultural;
    public int military;
    public int economic;
    public String inventory;
    public String agreements;

    public CountryVariables(String name)
    {
        political=0;
        cultural=0;
        military=0;
        economic=0;
        playerName = "DefaultName";
        this.name=name;
        inventory="";
        agreements="";
    }

    public CountryVariables(String name, int c, int e, int m, int p)
    {
        political=p;
        cultural=c;
        military=m;
        economic=e;
        this.name=name;
        inventory="";
        agreements="";
        playerName = "DefaultName";
    }

    public CountryVariables(String name, int c, int e, int m, int p, String playerName)
    {
        political=p;
        cultural=c;
        military=m;
        economic=e;
        this.name=name;
        inventory="";
        agreements="";
        this.playerName = playerName;
    }

    /**
     * modifies a country's stats (use with Action and Problem)
     * @param stats the stats modifying the country
     */
    public void modifyStats(CountryVariables stats)
    {
        political+=stats.political;
        cultural+=stats.cultural;
        military+=stats.military;
        economic+=stats.economic;
    }

}

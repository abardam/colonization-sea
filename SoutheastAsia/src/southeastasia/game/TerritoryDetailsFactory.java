/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

import java.util.ArrayList;
import southeastasia.SoutheastAsiaServerStats;

/**
 * singleton
 * @author Enzo
 */
public class TerritoryDetailsFactory {
    /* dumb
    private static TerritoryDetails burma;
    private static TerritoryDetails borneo;
    private static TerritoryDetails brunei;
    private static TerritoryDetails cambodia;
    private static TerritoryDetails etimor;
    private static TerritoryDetails java;
    private static TerritoryDetails laos;
    private static TerritoryDetails malaya;
    private static TerritoryDetails papua;
    private static TerritoryDetails philippines;
    private static TerritoryDetails sabah;
    private static TerritoryDetails sarawak;
    private static TerritoryDetails sulawesi;
    private static TerritoryDetails sumatra;
    private static TerritoryDetails thailand;
    private static TerritoryDetails vietnam;
    private static TerritoryDetails wtimor;
    */

    private static TerritoryDetailsFactory factory;
    private ArrayList<TerritoryDetails> territoryDetails;

    private TerritoryDetailsFactory()
    {
        territoryDetails=new ArrayList<TerritoryDetails>();
        //load from xml in the future
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.BURMA,"Burma", "Coconut", "Rice", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.MALAYA,"Malaya", "Timber", "Spices", "Fish", "Tea", "Coconut", "Rice", "Pepper", "Ginger", "Copper", "Tin", "Gold"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.SABAH,"Sabah","Timber", "Fish", "Rice", "Palm Oil", "Nutmeg"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.SARAWAK,"Sarawak","Timber", "Rice", "Nutmeg", "Palm Oil", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.BRUNEI,"Brunei","Timber", "Oil", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.SUMATRA,"Sumatra","Timber", "Rice", "Palm Oil", "Nutmeg", "Fish", "Pepper", "Ginger"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.JAVA,"Java","Timber", "Fish", "Nutmeg", "Coconut", "Pepper", "Ginger", "Copper", "Tin", "Silver"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.SULAWESI,"Sulawesi","Timber", "Nutmeg", "Pepper", "Nutmeg", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.PAPUA,"Papua New Guinea","Timber", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.PHILIPPINES,"Philippines","Sugar Cane", "Rice", "Timber", "Coconut", "Ginger", "Fish", "Gold"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.THAILAND,"Thailand","Rice", "Fish", "Pepper", "Ginger", "Fish", "Gold"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.LAOS,"Laos","Timber", "Rice", "Copper", "Gold", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.CAMBODIA,"Cambodia","Timber", "Rice", "Fish", "Gold"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.WTIMOR,"West Timor","Sandalwood", "Ginger", "Timber", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.ETIMOR,"East Timor","Sandalwood", "Ginger", "Timber", "Fish"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.BORNEO,"Borneo","Timber", "Rice", "Fish", "Gold"));
        territoryDetails.add(new TerritoryDetails(SoutheastAsiaServerStats.VIETNAM,"Vietnam","Coconut", "Rice", "Timber", "Fish", "Gold", "Tin"));

    }

    public static TerritoryDetails getTerritoryDetails(int code)
    {
        if(factory==null)
            factory=new TerritoryDetailsFactory();

        for(TerritoryDetails td:factory.territoryDetails)
        {
            if(td.getTerritoryCode()==code)
            {
                return td;
            }
        }

        return null;
    }
    public static TerritoryDetails getTerritoryDetails(String name)
    {
        if(factory==null)
            factory=new TerritoryDetailsFactory();

        for(TerritoryDetails td:factory.territoryDetails)
        {
            if(td.getName().equalsIgnoreCase(name))
            {
                return td;
            }
        }
        return null;
        /* this is dumb
        switch(territoryCode)
        {
            case SoutheastAsiaServerStats.BURMA:
                if(burma==null)
                {
                    burma=new TerritoryDetails();
                }
                break;
            case SoutheastAsiaServerStats.BORNEO:
                if(borneo==null)
                {
                    borneo=new TerritoryDetails();
                }
                break;
            case SoutheastAsiaServerStats.BRUNEI:
                if(brunei==null)
                {
                    brunei=new TerritoryDetails();
                }
                break;
            case SoutheastAsiaServerStats.CAMBODIA:
                if(cambodia==null)
                {
                    cambodia=new TerritoryDetails();
                }
                break;
            case SoutheastAsiaServerStats.ETIMOR:
                if(etimor==null)
                {
                    etimor=new TerritoryDetails();
                }
                break;
            case SoutheastAsiaServerStats.JAVA:
                break;
            case SoutheastAsiaServerStats.LAOS:
                break;
            case SoutheastAsiaServerStats.MALAYA:
                break;
            case SoutheastAsiaServerStats.PAPUA:
                break;
            case SoutheastAsiaServerStats.PHILIPPINES:
                break;
            case SoutheastAsiaServerStats.SABAH:
                break;
            case SoutheastAsiaServerStats.SARAWAK:
                break;
            case SoutheastAsiaServerStats.SULAWESI:
                break;
            case SoutheastAsiaServerStats.SUMATRA:
                break;
            case SoutheastAsiaServerStats.THAILAND:
                break;
            case SoutheastAsiaServerStats.VIETNAM:
                break;
            case SoutheastAsiaServerStats.WTIMOR:
                break;
        }*/
    }
}

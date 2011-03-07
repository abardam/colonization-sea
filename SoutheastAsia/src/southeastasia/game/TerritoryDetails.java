/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

import java.util.ArrayList;

/**
 * 
 * @author Enzo
 */
public class TerritoryDetails {
    private String name;
    private ArrayList<ItemDetails> items;
    private int territoryCode;

    public TerritoryDetails(int territoryCode, String name, ItemDetails... items)
    {
        this.territoryCode=territoryCode;
        this.name=name;
        this.items=new ArrayList<ItemDetails>();
        for(ItemDetails id:items)
        {
            this.items.add(id);
        }
    }

    public TerritoryDetails(int territoryCode, String name, String... items)
    {
        this.territoryCode=territoryCode;
        this.name=name;
        this.items=new ArrayList<ItemDetails>();
        for(String id:items)
        {
            this.items.add(ItemDetailsFactory.getItem(id));
        }
    }

    public ArrayList<ItemDetails> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public int getTerritoryCode(){
        return territoryCode;
    }

}

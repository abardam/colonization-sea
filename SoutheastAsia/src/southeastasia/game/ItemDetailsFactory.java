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
public class ItemDetailsFactory {
    private ArrayList<ItemDetails> items;
    private static ItemDetailsFactory factory;
    private ItemDetailsFactory()
    {
        items=new ArrayList<ItemDetails>();
        items.add(new ItemDetails("Nutmeg"));
        items.add(new ItemDetails("Timber"));
        items.add(new ItemDetails("Rice"));
        items.add(new ItemDetails("Palm oil"));
        items.add(new ItemDetails("Fish"));
        items.add(new ItemDetails("Sugar cane"));
        items.add(new ItemDetails("Tea"));
        items.add(new ItemDetails("Coconut"));
        items.add(new ItemDetails("Pepper"));
        items.add(new ItemDetails("Sandalwood"));
        items.add(new ItemDetails("Copper"));
        items.add(new ItemDetails("Gold"));
        items.add(new ItemDetails("Tin"));
        items.add(new ItemDetails("Silver"));
        items.add(new ItemDetails("Oil"));
        items.add(new ItemDetails("Spices"));
        items.add(new ItemDetails("Ginger"));

    }

    public static ItemDetails getItem(String name)
    {
        if(factory==null)
        {
            factory=new ItemDetailsFactory();
        }

        for(ItemDetails id:factory.items)
        {
            if(id.getName().equalsIgnoreCase(name))
            {
                return id;
            }
        }

        return null;
    }
}

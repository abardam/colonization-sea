/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

/**
 *
 * @author Enzo
 *
 * this class is for representing actions
 * idk kung ano laman
 *
 * maybe a string for description
 * tapos idk; stat modifiers?
 */
public class SoutheastAsiaAction {
    //the variables that will modify the country doing the action
    public CountryVariables statModifiers;

    public String name;
    public String description;

    public int landing;
    //is the action null
    public boolean isNull;

    public int item;
    public static final int ITEM_GAIN=1;
    public static final int ITEM_TRADE=2;
    public String targetItem;

    public int war;
    public static final int WAR_ATTACK=1;
    public static final int WAR_GIVEUP=2;

    public int targetCountry; //for trade or war

    public SoutheastAsiaAction()
    {
        landing=-1;
        isNull=false;
        item=0;
        war=0;
        targetItem="";
        targetCountry=-1;
    }

    public SoutheastAsiaAction(boolean isNull)
    {
        landing=-1;
        this.isNull=isNull;
        item=0;
        war=0;
        targetItem="";
        targetCountry=-1;
    }

    public SoutheastAsiaAction(String name, String desc, int c, int e, int m, int p)
    {
        isNull=false;
        this.name=name;
        description=desc;
        statModifiers=new CountryVariables("", c, e, m ,p);
        landing=-1;
        item=0;
        war=0;
        targetItem="";
        targetCountry=-1;
    }
    /**
     * this method converts the action to a string
     *
     * for displaying to the teacher/student
     *
     * @return String representation of the action
     */
    public String toString()
    {
        return name;
    }

    /**
     * this method returns a null action for new turns, etc
     * @return a blank action
     */
    public static SoutheastAsiaAction noAction()
    {
        return new SoutheastAsiaAction(true);
    }

    /**
     * applies the effects of the action/problem
     * @param target the country that is doing the action/receiving the problem
     */
    public void applyEffect(CountryVariables target)
    {
        target.modifyStats(statModifiers);
    }


}

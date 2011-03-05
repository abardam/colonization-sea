/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

import southeastasia.game.CountryVariables;

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

    public SoutheastAsiaAction()
    {
        isNull=false;
    }

    public SoutheastAsiaAction(boolean isNull)
    {
        this.isNull=isNull;
    }

    public SoutheastAsiaAction(String name, String desc, int c, int e, int m, int p)
    {
        isNull=false;
        this.name=name;
        description=desc;
        statModifiers=new CountryVariables("", c, e, m ,p);
        landing=-1;
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

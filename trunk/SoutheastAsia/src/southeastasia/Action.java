/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

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
public class Action {
    //the variables that will modify the country doing the action
    public CountryVariables statModifiers;

    //is the action null
    public boolean isNull;

    public Action()
    {
        isNull=false;
    }

    public Action(boolean isNull)
    {
        this.isNull=isNull;
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
        return "";
    }

    /**
     * this method returns a null action for new turns, etc
     * @return a blank action
     */
    public static Action noAction()
    {
        return new Action(true);
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

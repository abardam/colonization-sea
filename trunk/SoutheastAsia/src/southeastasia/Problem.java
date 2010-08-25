/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

/**
 *
 * @author Enzo
 */
public class Problem extends SoutheastAsiaAction{
    //the solve bonuses
    public CountryVariables solveModifiers;


    public Problem()
    {
        super();
    }

    public Problem(boolean isNull)
    {
        super(isNull);
    }

    public Problem(String name, String desc, int c, int e, int m, int p)
    {
        super(name, desc, c, e, m, p);
    }

    /**
     * similar to the method in SoutheastAsiaAction
     * @return the problem in string form
     *
     */
    @Override
    public String toString()
    {
        return "";
    }
    

    /**
     * applies the solve bonuses
     * @param target the country that is solving the problem
     */
    public void solveProblem(CountryVariables target)
    {
        target.modifyStats(solveModifiers);
    }

    /**
     * returns a blank problem
     * @return a blank problem
     */
    public static Problem noProblem()
    {
        return new Problem(true);
    }

}

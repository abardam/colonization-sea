/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

/**
 *
 * @author Enzo
 * stores variables per country
 * walang struct sa java :<
 */
public class CountryVariables {
    public String name;
    public int political;
    public int cultural;
    public int military;
    public int economic;

    public CountryVariables(String name)
    {
        political=0;
        cultural=0;
        military=0;
        economic=0;
        this.name=name;
    }

}

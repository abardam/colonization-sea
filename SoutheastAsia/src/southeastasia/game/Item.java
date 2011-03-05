/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

/**
 *
 * @author Enzo
 */
public class Item {
    private String name;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public int incrementCount(int add) {
        count+=add;
        return count;
    }

}

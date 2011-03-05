/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.game;

import java.awt.Point;
import java.awt.Polygon;

/**
 *
 * @author Enzo
 */
public class ClickablePolygon {
    private Polygon target;
    private int targetCountry;

    public ClickablePolygon(Polygon target, int targetCountry) {
        this.target = target;
        this.targetCountry = targetCountry;
    }

    public boolean contains(int x, int y) {
        return target.contains(x, y);
    }

    public boolean contains(Point p) {
        return target.contains(p);
    }

    public int getTargetCountry() {
        return targetCountry;
    }
    

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import southeastasia.game.ClickablePolygon;

/**
 *
 * @author Enzo
 */
public class MapMouseListener implements MouseListener{

    private ClickablePolygon[] clickableArea;
    public MapMouseListener(ClickablePolygon[] targets)
    {
        clickableArea=targets;
    }
    public void mouseClicked(MouseEvent e) {
        //System.out.println(e.getX()+" "+e.getY());
        for(int i=0;i<clickableArea.length;i++)
        {
            if(clickableArea[i].contains(e.getPoint()))
            {
                System.out.println("clicked on "+clickableArea[i].getTargetCountry());
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

}

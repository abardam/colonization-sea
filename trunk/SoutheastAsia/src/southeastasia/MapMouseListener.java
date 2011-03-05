/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import southeastasia.game.ClickablePolygon;

/**
 *
 * @author Enzo
 */
public class MapMouseListener implements MouseListener{

    private ClickablePolygon[] clickableArea;
    private BufferedWriter debugWriter;
    public MapMouseListener(ClickablePolygon[] targets)
    {
        clickableArea=targets;
    }
    public void mouseClicked(MouseEvent e) {
        //System.out.println(e.getX()+" "+e.getY());

        if(debugWriter==null)
        {
            for(int i=0;i<clickableArea.length;i++)
            {
                if(clickableArea[i].contains(e.getPoint()))
                {
                    System.out.println("clicked on "+clickableArea[i].getTargetCountry());
                }
            }
        }
        else
        {
            try
            {
                debugWriter.write("<Point>"+e.getX()+","+e.getY()+"</Point>\n");
            }
            catch(IOException ioe)
            {
                System.out.println(ioe.getMessage());
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        if(debugWriter!=null)
        {
            try
            {
                String s = JOptionPane.showInputDialog("Type something, or DONE to save");
                if(s!=null&&!s.isEmpty())
                {
                    debugWriter.write(s+"\n");

                    if(s.equals("DONE"))
                    {
                        doneWriting();
                    }
                }


            }
            catch(IOException ioe)
            {
                System.out.println(ioe.getMessage());
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void setOutfile(String outfile)
    {
        try
        {
            debugWriter=new BufferedWriter(new FileWriter(outfile));
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void doneWriting()
    {
        try
        {
            if(debugWriter!=null)
            {
                debugWriter.close();
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

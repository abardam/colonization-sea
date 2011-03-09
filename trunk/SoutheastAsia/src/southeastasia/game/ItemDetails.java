/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package southeastasia.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 
 * @author Enzo
 */
public class ItemDetails {

    private String name;
    private ImageIcon icon;

    public String getName() {
        return name;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public ItemDetails(String name) {
        this.name = name;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src\\southeastasia\\icons\\"+name+".png"));
            this.icon=new ImageIcon(img);

        } catch (IOException e) {
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.loader;
import southeastasia.game.ClickablePolygon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.Polygon;
import java.util.*;
import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import southeastasia.game.ClickablePolygon;

/**
 *
 * @author Leland
 * Call ClickablePolygonLoader.load(filename) to get stuff from xml file.
 * Input XML format:
 * <poly>
 *  <id>COUNTRY_ID</id>
 *  <points>X1,Y1;X2,Y2;...;XN,YN</points>
 * </poly>
 */
public class ClickablePolygonLoader {
    public static ArrayList<ClickablePolygon> load (String filename) {
        ArrayList <ClickablePolygon> r = new ArrayList <ClickablePolygon> ();
        try {
            File file = new File(""+filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("poly");

            for (int s = 0; s < list.getLength(); s++) {
                Node node = list.item(s);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element first = (Element) node;
                    NodeList nameList = first.getElementsByTagName("id");
                    Element nameElement = (Element) nameList.item(0);
                    NodeList name = nameElement.getChildNodes();
                    NodeList pointList = first.getElementsByTagName("points");
                    Element pointElement = (Element) pointList.item(0);
                    NodeList point = pointElement.getChildNodes();
                    String[] points = point.item(0).getNodeValue().split(";");
                    if (points.length > 3) {
                    	ArrayList <Integer> x = new ArrayList <Integer> ();
                    	ArrayList <Integer> y = new ArrayList <Integer> ();
                    	for (String temp : points) {
                    		x.add (Integer.parseInt(temp.split(",")[0]));
                    		y.add (Integer.parseInt(temp.split(",")[1]));
                    	}
                    	int [] xpoints = new int [x.size()];
                    	int [] ypoints = new int [y.size()];
                    	for (int i = 0; i < x.size(); i++) {
                    		xpoints[i] = x.get(i);
                    		ypoints[i] = y.get(i);
                    	}
                    	r.add(new ClickablePolygon(new Polygon(xpoints,ypoints,points.length),Integer.parseInt(name.item(0).getNodeValue())));
                    }
                }
            }
        }
        catch (Exception x) {
            System.out.println("Clickable polygon loader error. " + x.toString());
        }
        return r;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.loader;
import java.util.ArrayList;
import southeastasia.SoutheastAsiaAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Leland
 * Call ActionsLoader.loadactions(filename) to get stuff from xml file.
 */
public class ActionsLoader {
    public static ArrayList<SoutheastAsiaAction> loadActions (String filename) {
        ArrayList <SoutheastAsiaAction> r = new ArrayList <SoutheastAsiaAction> ();
        try {
            File file = new File(""+filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("action");

            for (int s = 0; s < list.getLength(); s++) {
                Node node = list.item(s);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element first = (Element) node;
                    NodeList nameList = first.getElementsByTagName("name");
                    Element nameElement = (Element) nameList.item(0);
                    NodeList name = nameElement.getChildNodes();
                    NodeList descList = first.getElementsByTagName("desc");
                    Element descElement = (Element) descList.item(0);
                    NodeList desc = descElement.getChildNodes();
                    NodeList statList = first.getElementsByTagName("stat");
                    Element statElement = (Element) statList.item(0);
                    NodeList stat = statElement.getChildNodes();
                    String[] stats = stat.item(0).getNodeValue().split(",");
                    if (stats.length > 3) r.add(new SoutheastAsiaAction(name.item(0).getNodeValue(),desc.item(0).getNodeValue(),
                                                                        Integer.parseInt(stats[0].trim()), Integer.parseInt(stats[1].trim()), Integer.parseInt(stats[2].trim()), Integer.parseInt(stats[3].trim()) ));
                }
            }
        }
        catch (Exception x) {
            System.out.println("Action loader error. " + x.toString());
            SoutheastAsiaAction m = r.get(r.size()-1);
            System.out.println(m.name);
        }
        return r;
    }
}

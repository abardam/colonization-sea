/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.loader;
import java.util.ArrayList;
import southeastasia.game.Problem;
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
 * ... Problems loader. Call ProblemsLoader.loadProblems(filename).
 */
public class ProblemsLoader {
    public static ArrayList<Problem> loadProblems (String filename) {
        ArrayList <Problem> r = new ArrayList <Problem> ();
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
                    if (stats.length > 7) r.add(new Problem(name.item(0).getNodeValue(),desc.item(0).getNodeValue(),
                                                                        Integer.parseInt(stats[0].trim()), Integer.parseInt(stats[1].trim()), Integer.parseInt(stats[2].trim()), Integer.parseInt(stats[3].trim()),
                                                                        Integer.parseInt(stats[4].trim()), Integer.parseInt(stats[5].trim()), Integer.parseInt(stats[6].trim()), Integer.parseInt(stats[7].trim()) ));
                }
            }
        }
        catch (Exception x) {
            System.out.println("Problem loader error. " + x.toString());
            Problem m = r.get(r.size()-1);
            if (m!=null) System.out.println("Last problem loaded: "+m.name);
        }
        return r;
    }
}

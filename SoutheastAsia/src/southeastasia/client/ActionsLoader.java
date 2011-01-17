/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package southeastasia.client;
import java.util.ArrayList;
import southeastasia.SoutheastAsiaAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
            // System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList list = doc.getElementsByTagName("action");
            // System.out.println("Information of all employees");

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
        catch (FileNotFoundException fnfe) {
            System.out.println("Resulted to hardcoding. " + fnfe.toString());
            r = hardcode(r);
        }
        catch (Exception x) {
            System.out.println("Action loader error. " + x.toString());
            SoutheastAsiaAction m = r.get(r.size()-1);
            System.out.println(m.name);
        }
        return r;
    }

    private static ArrayList<SoutheastAsiaAction> hardcode(ArrayList<SoutheastAsiaAction> actions) {
        actions.add(new SoutheastAsiaAction("Military Landing", "After sailing in the sea for weeks, you decide to land on this land with military to secure your landing. Your military presence will warn all exisiting rulers of your offensive capability. This step is a must to prepare your military for war. Your military have to be in close proximity of your enemy before you can launch an invasion.", 0, -2, 5, 0));
        actions.add(new SoutheastAsiaAction("Military Exercise", "This exercise demonstrates your military�s offensive and defensive capabilities for the world to see, while providing training for your forces at the same time. Your people are jubilant to be protected by a strong military force, while the other world leaders are aware of your military might. The only downside is the cost involved.", 3, -5, 5, 0));
        actions.add(new SoutheastAsiaAction("Invasion", "You must have at least 5E to execute an invasion. Look at the Battle Chart or your teacher for the result of the battle.", 0, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Military Surrender", "", 2, 0, -2, 0));
        actions.add(new SoutheastAsiaAction("Purchase of Military Equipment, Weapons, Vehicles, Planes and Ships", "Gain +xM, -xE, x can be 1 to 10", 0, -10, 10, 0));
        actions.add(new SoutheastAsiaAction("Trade Sabotage", "Sinking your competitors� trading ships is one easy way to dissuade merchants from trading with your competitor, as well as hurt their economy.", 0, 0, 5, 0));
        actions.add(new SoutheastAsiaAction("Economic Landing", "After sailing in the sea for weeks, you decide to land on this land in peace and seek out the existing rulers and negotiate for trade, allow your people to settle a part of this land or bribe them into submission so that you are the true governor of this land.", 0,3,0,0));
        actions.add(new SoutheastAsiaAction("Financial Assistance", "You offer financial help to the affected country who is in economic dire straits. This country must have an Economic Strength of less than 10. Global leaders around the world look up to your altruistic behavior. -xE, +xP (+xE to affected party), x refers to any whole number from 5 to 30", 0, -30, 0, 30));
        actions.add(new SoutheastAsiaAction("Slavery", "You legalise the concept that men and women in that country can be owned by richer citizens from your country. They work for long hours for very low pay, and hence, your profit soars sky high. Naturally, the slaves are resentful towards you.", -5, 15, 0, 0));
        actions.add(new SoutheastAsiaAction("Gather a resource", "Look at your resource chart for the available resources on your land or island. For instance, you can start an oil palm plantation or a fishery or a rice field farm. Planting cash crops such as rubber, tea or palm oil over a large piece of land in Southeast Asia is the best choice because the climate here is very suitable for these cash crops.", 0, 10, 0, 0));
        actions.add(new SoutheastAsiaAction("Trade", "You must have a resource to trade!You must also have a trade agreement with that country. The trade commodity must be different and lacking in both empires. Establishing trading routes via sea during this period is the way to trade excess goods and resources that you have with goods and resources that you do not have but want badly. Through trade, you get the resource card from your teacher that your trading partner decides to trade with you. ", 0, 15, 0, 0));
        actions.add(new SoutheastAsiaAction("Taxation", "You decide to raise the tax your people have to pay for living on your colonies. This move is very unpopular with the people as they are giving you more of the money that they earn.  +xE, -xC (where x can take on a value of 1 to 10)", -10, 10, 0, 0));
        actions.add(new SoutheastAsiaAction("Missionary Landing", "After sailing in the sea for weeks, you decide to land on this land to promote your faith as the one true and only faith in the world.", 5, -2, 0, 0));
        actions.add(new SoutheastAsiaAction("Building religious institutions", "By providing a physical space for your home country's missionaries to gather believers, your chances of spreading your faith will be higher.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Building artistic institutions","A place for the Arts is a place where all dances, music and other art forms congregate for all the masses to enjoy and relax.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Building historical institutions", "Reminding the people of their roots, their ancestors and preseving a common memory of a place will certainly win you many hearts and minds.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Building recreational facilities", "When the people get to relax and have fun, they are less likely to be out there rioting and creating trouble for your government.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Building educational institutions", "Through a Western education, you are getting the population to speak your language and think the way you think.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Building public institutions", "Law and order, as well as public safety, are essential for any effective rule in any forms of government.", 5, -5, 0, 0));
        actions.add(new SoutheastAsiaAction("Sign a ceasefire treaty", "Your people are glad that war is over and some global leaders are impressed with your political move. +5P to affected parties (Violation: -10P)", 2, 0, 0, 5));
        actions.add(new SoutheastAsiaAction("Sign a military alliance treaty", "Your people feel safer to be your citizen and some global leaders are impressed with your political move. +5P to affected parties (Violation: -10P)", 2, 0, 3, 5));
        actions.add(new SoutheastAsiaAction("Sign a trade agreement", "Your people will get to enjoy more goods and services from this agreement, and their standard of living will increase. Some global leaders decide to learn from you too. Your next turn must be a Trade or you will face a penalty. +3P to affected parties (If the affected party did not trade next turn, -5P to the party that violates this agreement)", 5, 0, 0, 3));
        actions.add(new SoutheastAsiaAction("Mediate between two parties", "Your intervention between two countries in a military, economic, cultural or political dispute has resulted in great admiration for your leadership by the global community. +5P to affected parties if they reconcile (Failure: -5P)", 0,0,0,10));
        actions.add(new SoutheastAsiaAction("Form a union", "Numbers and size speak for themselves in the political arena. With more countries as your friends, your enemies and potential friends from all over the world cannot help but take notice of your next action. +10P to all participating countries, with +3P to all countries for any additional country who joins this union. (Violation: -10P to violating party only)", 0, 0, 0, 10));
        return actions;
    }
}

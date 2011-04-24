/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SoutheastAsiaClientApp.java
 *
 * Created on 08 8, 10, 12:40:53 AM
 */
package southeastasia;

import southeastasia.game.SoutheastAsiaAction;
import southeastasia.game.CountryVariables;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import southeastasia.game.ClickablePolygon;
import southeastasia.game.ItemDetails;
import southeastasia.game.ItemDetailsFactory;
import southeastasia.game.Problem;
import southeastasia.loader.ActionsLoader;
import southeastasia.loader.ClickablePolygonLoader;
import southeastasia.networking.SoutheastAsiaServerSockets;
import southeastasia.placards.CountryPlacard;

/**
 *
 * @author Enzo
 */
public class SoutheastAsiaClientApp extends javax.swing.JFrame {

    public static final String ACTIONSPATH = "src\\southeastasia\\resources\\actions.xml";
    public static final String MAP_AREAS = "src\\southeastasia\\resources\\poly.xml";
    private DefaultTableModel countriesTableModel;
    private ChatWindow chat;
    private SoutheastAsiaServerStats stats;
    //for actions
    private int territoryTargetted; //should be set to -1 every turn, and if the action is changed
    //secretly sent when sending actions
    private String productTargetted; //should be set to -1 every turn
    private boolean tradeOrGain; //true: trade, false: gain
    private boolean actionLock;
    private boolean invasionDeclared;
    public String targetIP;
    private MapMouseListener mapListener;
    private ItemPanel itemPanel;

    /** Creates new form SoutheastAsiaClientApp */
    public SoutheastAsiaClientApp(String ip) {
        targetIP = ip;
        port = 7777;
        territoryTargetted = -1;
        productTargetted = "";
        actionLock = false;
        invasionDeclared = false;

        countriesTableModel = new DefaultTableModel();
        countriesTableModel.addColumn("Country");
        countriesTableModel.addColumn("Cultural");
        countriesTableModel.addColumn("Economic");
        countriesTableModel.addColumn("Military");
        countriesTableModel.addColumn("Political");


        initComponents();
        setSize(842, 630);
        itemPanel=new ItemPanel();
        //jPanel6.add(itemPanel); replaced for now by a text box
        cl = (CardLayout) jPanel4.getLayout();
        isConnected = false;
        useFakeSockets = false;
        loadActions();
        stats = new SoutheastAsiaServerStats();

        /*
        int testx[]={333,197,101,31, 160,140,269,301,392,377,439,366,401,322,531,396,337,401};
        int testy[]={42, 151, 301, 459, 678, 748, 736, 688, 975, 1088, 989, 811, 742, 623, 445, 290, 264,184};
        double mapx=map.getWidth();
        double origx=3508; //what is the width of the map
        double mapy=map.getHeight();
        double origy=2480; //what is the height of the map
        for(int i=0;i<testx.length;i++)
        {
        testx[i]*=mapx;
        testx[i]/=origx;
        testy[i]*=mapy;
        testy[i]/=origy;
        }

        Polygon testpolygon=new Polygon(testx, testy, testx.length);
        ClickablePolygon[] p = {new ClickablePolygon(testpolygon, 0)};*/

        ArrayList<ClickablePolygon> p = ClickablePolygonLoader.load(MAP_AREAS);
        ClickablePolygon[] c = new ClickablePolygon[SoutheastAsiaServerStats.NUM_TERRITORIES];
        p.toArray(c);
        mapListener = new MapMouseListener(c, this);
        map.addMouseListener(mapListener);

        chat = new ChatWindow(this);

        class PlacardListener implements ActionListener {

            private String countryName;

            public PlacardListener(String countryName) {
                this.countryName = countryName;
            }

            public void actionPerformed(ActionEvent e) {
                new CountryPlacard(countryName).setVisible(true);
            }
        }
        usbtn.addActionListener(new PlacardListener("Americans"));
        britainbtn.addActionListener(new PlacardListener("British"));
        dutchbtn.addActionListener(new PlacardListener("Dutch"));
        frenchbtn.addActionListener(new PlacardListener("French"));
        portugalbtn.addActionListener(new PlacardListener("Portugal"));
        spainbtn.addActionListener(new PlacardListener("Spain"));

        chat.setVisible(true);
    }
    javax.swing.JComboBox[] territoryCBs;

    private void setTerritories() //for now, just sets all territory combo boxes to disabled.
    {

        burmaCB.setEnabled(false);
        bruneiCB.setEnabled(false);
        cambodiaCB.setEnabled(false);
        javaCB.setEnabled(false);
        kalimantanCB.setEnabled(false);
        laosCB.setEnabled(false);
        malayaCB.setEnabled(false);
        papuaCB.setEnabled(false);
        philippinesCB.setEnabled(false);
        sabahCB.setEnabled(false);
        sarawakCB.setEnabled(false);
        sulawesiCB.setEnabled(false);
        sumatraCB.setEnabled(false);
        thailandCB.setEnabled(false);
        etimorCB.setEnabled(false);
        wtimorCB.setEnabled(false);
        vietnamCB.setEnabled(false);

        territoryCBs = new javax.swing.JComboBox[SoutheastAsiaServerStats.NUM_TERRITORIES];
        territoryCBs[0] = burmaCB;
        territoryCBs[1] = bruneiCB;
        territoryCBs[2] = cambodiaCB;
        territoryCBs[3] = (javaCB);
        territoryCBs[4] = (kalimantanCB);
        territoryCBs[5] = (laosCB);
        territoryCBs[6] = (malayaCB);
        territoryCBs[7] = (papuaCB);
        territoryCBs[8] = (philippinesCB);
        territoryCBs[9] = (sabahCB);
        territoryCBs[10] = (sarawakCB);
        territoryCBs[11] = (sulawesiCB);
        territoryCBs[12] = (sumatraCB);
        territoryCBs[13] = (thailandCB);
        territoryCBs[14] = (etimorCB);
        territoryCBs[15] = (wtimorCB);
        territoryCBs[16] = vietnamCB;

        for (javax.swing.JComboBox jcb : territoryCBs) {
            jcb.removeAllItems();

            jcb.addItem("---");
            for (String s : stats.getCountryNames()) {
                jcb.addItem(s);
            }
        }

        class MapListener implements ActionListener {

            private int territoryCode;
            private String name;
            private SoutheastAsiaClientApp app;

            public MapListener(int territoryCode, String name, SoutheastAsiaClientApp app) {
                this.territoryCode = territoryCode;
                this.name = name;
                this.app = app;
            }

            public void actionPerformed(ActionEvent ae) {
                new TerritoryViewerFrame(name, territoryCode, app).setVisible(true);
            }
        }
    }
    private ArrayList<SoutheastAsiaAction> actions;

    private void loadActions() //loads the actions in; hardcoding ftw
    // hohoho hindi na siya hardcoded
    {
        actions = ActionsLoader.loadActions(ACTIONSPATH);
        jComboBox1.removeAllItems();
        for (SoutheastAsiaAction a : actions) {
            jComboBox1.addItem(a);
        }


    }
    //private FakeSockets sockets;
    private Socket socket;
    private InetAddress host;
    private int port;
    private boolean isConnected;
    private int clientCode;
    //private SoutheastAsiaClient client;
    private CardLayout cl;

    /**
     * after the game starts, the server sends a message and the
     * client switches to the game screen.
     */
    public void startGameScreen() {
        cl.show(jPanel4, "game_play");

        setSize(700, 760);
        setTerritories();
    }

    public void setOutfile(String outfile) {
        mapListener.setOutfile(outfile);
    }

    /*
     * this is for the FAKESOCKETS

    public SoutheastAsiaClientApp(FakeSockets sock, SoutheastAsiaClient client)
    {
    port = 7777;
    initComponents();
    sockets=sock;
    this.client=client;
    }*/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        connect = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        portugalbtn = new javax.swing.JButton();
        spainbtn = new javax.swing.JButton();
        britainbtn = new javax.swing.JButton();
        dutchbtn = new javax.swing.JButton();
        frenchbtn = new javax.swing.JButton();
        usbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        culturalField = new javax.swing.JTextField();
        economicField = new javax.swing.JTextField();
        militaryField = new javax.swing.JTextField();
        politicalField = new javax.swing.JTextField();
        countryField = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        problemField = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        culturalPenalty = new javax.swing.JTextField();
        economicPenalty = new javax.swing.JTextField();
        militaryPenalty = new javax.swing.JTextField();
        politicalPenalty = new javax.swing.JTextField();
        culturalSolve = new javax.swing.JTextField();
        economicSolve = new javax.swing.JTextField();
        militarySolve = new javax.swing.JTextField();
        politicalSolve = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemArea = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        wtimorCB = new javax.swing.JComboBox();
        thailandCB = new javax.swing.JComboBox();
        etimorCB = new javax.swing.JComboBox();
        sulawesiCB = new javax.swing.JComboBox();
        sumatraCB = new javax.swing.JComboBox();
        sarawakCB = new javax.swing.JComboBox();
        sabahCB = new javax.swing.JComboBox();
        philippinesCB = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        papuaCB = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        malayaCB = new javax.swing.JComboBox();
        laosCB = new javax.swing.JComboBox();
        kalimantanCB = new javax.swing.JComboBox();
        javaCB = new javax.swing.JComboBox();
        cambodiaCB = new javax.swing.JComboBox();
        bruneiCB = new javax.swing.JComboBox();
        burmaCB = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        vietnamCB = new javax.swing.JComboBox();
        map = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(660, 280));
        setName("Form"); // NOI18N

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new java.awt.CardLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getResourceMap(SoutheastAsiaClientApp.class);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(660, 280));
        jPanel1.setLayout(new java.awt.BorderLayout());

        connect.setText(resourceMap.getString("connect.text")); // NOI18N
        connect.setName("connect"); // NOI18N
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });
        jPanel1.add(connect, java.awt.BorderLayout.SOUTH);

        jLabel28.setBackground(resourceMap.getColor("jLabel28.background")); // NOI18N
        jLabel28.setIcon(resourceMap.getIcon("jLabel28.icon")); // NOI18N
        jLabel28.setText(resourceMap.getString("jLabel28.text")); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N
        jPanel1.add(jLabel28, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel1, "country_select");

        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setPreferredSize(new java.awt.Dimension(660, 280));
        jPanel9.setLayout(new java.awt.GridLayout(2, 3));

        portugalbtn.setIcon(resourceMap.getIcon("portugalbtn.icon")); // NOI18N
        portugalbtn.setText(resourceMap.getString("portugalbtn.text")); // NOI18N
        portugalbtn.setName("portugalbtn"); // NOI18N
        jPanel9.add(portugalbtn);

        spainbtn.setIcon(resourceMap.getIcon("spainbtn.icon")); // NOI18N
        spainbtn.setText(resourceMap.getString("spainbtn.text")); // NOI18N
        spainbtn.setName("spainbtn"); // NOI18N
        jPanel9.add(spainbtn);

        britainbtn.setIcon(resourceMap.getIcon("britainbtn.icon")); // NOI18N
        britainbtn.setText(resourceMap.getString("britainbtn.text")); // NOI18N
        britainbtn.setName("britainbtn"); // NOI18N
        jPanel9.add(britainbtn);

        dutchbtn.setIcon(resourceMap.getIcon("dutchbtn.icon")); // NOI18N
        dutchbtn.setText(resourceMap.getString("dutchbtn.text")); // NOI18N
        dutchbtn.setName("dutchbtn"); // NOI18N
        jPanel9.add(dutchbtn);

        frenchbtn.setIcon(resourceMap.getIcon("frenchbtn.icon")); // NOI18N
        frenchbtn.setText(resourceMap.getString("frenchbtn.text")); // NOI18N
        frenchbtn.setName("frenchbtn"); // NOI18N
        jPanel9.add(frenchbtn);

        usbtn.setIcon(resourceMap.getIcon("usbtn.icon")); // NOI18N
        usbtn.setText(resourceMap.getString("usbtn.text")); // NOI18N
        usbtn.setName("usbtn"); // NOI18N
        jPanel9.add(usbtn);

        jPanel4.add(jPanel9, "countryselect");

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(660, 280));

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        culturalField.setEditable(false);
        culturalField.setText(resourceMap.getString("culturalField.text")); // NOI18N
        culturalField.setName("culturalField"); // NOI18N

        economicField.setEditable(false);
        economicField.setText(resourceMap.getString("economicField.text")); // NOI18N
        economicField.setName("economicField"); // NOI18N

        militaryField.setEditable(false);
        militaryField.setText(resourceMap.getString("militaryField.text")); // NOI18N
        militaryField.setName("militaryField"); // NOI18N

        politicalField.setEditable(false);
        politicalField.setText(resourceMap.getString("politicalField.text")); // NOI18N
        politicalField.setName("politicalField"); // NOI18N

        countryField.setEditable(false);
        countryField.setText(resourceMap.getString("countryField.text")); // NOI18N
        countryField.setName("countryField"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTable1.setModel(countriesTableModel);
        jTable1.setEnabled(false);
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane4.setViewportView(jTable1);

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jTextArea4.setColumns(20);
        jTextArea4.setEditable(false);
        jTextArea4.setRows(5);
        jTextArea4.setName("jTextArea4"); // NOI18N
        jScrollPane5.setViewportView(jTextArea4);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jTextArea5.setColumns(20);
        jTextArea5.setEditable(false);
        jTextArea5.setRows(5);
        jTextArea5.setName("jTextArea5"); // NOI18N
        jScrollPane6.setViewportView(jTextArea5);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(countryField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(culturalField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(economicField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(militaryField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(politicalField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(countryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(culturalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(economicField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(militaryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(politicalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setName("jLabel4"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setName("jTextField4"); // NOI18N

        jTextField5.setName("jTextField5"); // NOI18N

        jTextField6.setName("jTextField6"); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField7.setEditable(false);
        jTextField7.setName("jTextField7"); // NOI18N

        jComboBox1.setEditable(true);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("jComboBox1"); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 530, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING))))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))))
                        .addGap(34, 34, 34)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        problemField.setColumns(20);
        problemField.setRows(5);
        problemField.setName("problemField"); // NOI18N
        jScrollPane2.setViewportView(problemField);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        culturalPenalty.setText(resourceMap.getString("culturalPenalty.text")); // NOI18N
        culturalPenalty.setName("culturalPenalty"); // NOI18N

        economicPenalty.setText(resourceMap.getString("economicPenalty.text")); // NOI18N
        economicPenalty.setName("economicPenalty"); // NOI18N

        militaryPenalty.setText(resourceMap.getString("militaryPenalty.text")); // NOI18N
        militaryPenalty.setName("militaryPenalty"); // NOI18N

        politicalPenalty.setText(resourceMap.getString("politicalPenalty.text")); // NOI18N
        politicalPenalty.setName("politicalPenalty"); // NOI18N

        culturalSolve.setText(resourceMap.getString("culturalSolve.text")); // NOI18N
        culturalSolve.setName("culturalSolve"); // NOI18N

        economicSolve.setText(resourceMap.getString("economicSolve.text")); // NOI18N
        economicSolve.setName("economicSolve"); // NOI18N

        militarySolve.setText(resourceMap.getString("militarySolve.text")); // NOI18N
        militarySolve.setName("militarySolve"); // NOI18N

        politicalSolve.setText(resourceMap.getString("politicalSolve.text")); // NOI18N
        politicalSolve.setName("politicalSolve"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(culturalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(economicPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(militaryPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(politicalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(culturalSolve, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(economicSolve, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(militarySolve, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(politicalSolve, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(culturalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(economicPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(militaryPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(politicalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(culturalSolve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(economicSolve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(militarySolve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(politicalSolve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        itemArea.setColumns(20);
        itemArea.setEditable(false);
        itemArea.setRows(5);
        itemArea.setName("itemArea"); // NOI18N
        jScrollPane3.setViewportView(itemArea);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jPanel7.setBackground(resourceMap.getColor("jPanel7.background")); // NOI18N
        jPanel7.setName("jPanel7"); // NOI18N

        wtimorCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        wtimorCB.setName("wtimorCB"); // NOI18N

        thailandCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        thailandCB.setName("thailandCB"); // NOI18N

        etimorCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        etimorCB.setName("etimorCB"); // NOI18N

        sulawesiCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sulawesiCB.setName("sulawesiCB"); // NOI18N

        sumatraCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sumatraCB.setName("sumatraCB"); // NOI18N

        sarawakCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sarawakCB.setName("sarawakCB"); // NOI18N

        sabahCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sabahCB.setName("sabahCB"); // NOI18N

        philippinesCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        philippinesCB.setName("philippinesCB"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        papuaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        papuaCB.setName("papuaCB"); // NOI18N

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        malayaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        malayaCB.setName("malayaCB"); // NOI18N

        laosCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        laosCB.setName("laosCB"); // NOI18N

        kalimantanCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        kalimantanCB.setName("kalimantanCB"); // NOI18N

        javaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javaCB.setName("javaCB"); // NOI18N

        cambodiaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cambodiaCB.setName("cambodiaCB"); // NOI18N

        bruneiCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        bruneiCB.setName("bruneiCB"); // NOI18N

        burmaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        burmaCB.setName("burmaCB"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        vietnamCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        vietnamCB.setName("vietnamCB"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(malayaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(laosCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kalimantanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(javaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cambodiaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bruneiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(86, 86, 86)
                        .addComponent(burmaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(papuaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vietnamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(philippinesCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sabahCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sarawakCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sulawesiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sumatraCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thailandCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etimorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wtimorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(burmaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(philippinesCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(bruneiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(sabahCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cambodiaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(sarawakCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(javaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(sulawesiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(kalimantanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(sumatraCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(laosCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(thailandCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(malayaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(etimorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(papuaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(wtimorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(vietnamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        map.setIcon(resourceMap.getIcon("map.icon")); // NOI18N
        map.setText(resourceMap.getString("map.text")); // NOI18N
        map.setName("map"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(map)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(map)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel2, "game_play");

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
    public void tempMessage(String message) {
    jTextField1.setText(message);
    }*/
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        if (isConnected == false) {
            System.out.println("Connecting!");
            try {
                //host = InetAddress.getLocalHost();
                //host= InetAddress.getByName("169.254.152.205");
                host = InetAddress.getByName(targetIP);
            } catch (UnknownHostException ex) {
                System.err.println(ex.getMessage());
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket = new Socket(host.getHostName(), port);
                setSocket(socket);

                cl.show(jPanel4, "countryselect");
                setSize(660, 300);
            } catch (UnknownHostException ex) {
                System.err.println(ex.getMessage());
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            //else
            //    System.out.println("Connecting failed. Please try again later.");
        } else {
            System.out.println("Already connected!");
        }


    }//GEN-LAST:event_connectActionPerformed

    /**
     * locks sending of actions while choosing items etc
     * @param lock if true, action cannot be sent
     */
    public void setActionLock(boolean lock) {
        actionLock = lock;
    }

    public void setProductTargetted(String productTargetted) {
        this.productTargetted = productTargetted;
    }
    public void setActionText(SoutheastAsiaAction a) {
        if (a == null) {
            a = new SoutheastAsiaAction("", "", 0, 0, 0, 0);
        }
        jComboBox1.setSelectedItem(null);
        jComboBox1.setEditable(true);
        jComboBox1.setSelectedItem((Object) a.name);
        jTextArea1.setText(a.description);
        jTextField3.setText(a.statModifiers.cultural + "");
        jTextField4.setText(a.statModifiers.economic + "");
        jTextField5.setText(a.statModifiers.military + "");
        jTextField6.setText(a.statModifiers.political + "");

        territoryTargetted = a.landing;
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        chat.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        territoryTargetted = -1;
        productTargetted = "";
        invasionDeclared = false;
        unlockAction();
        SoutheastAsiaAction a;
        try {
            a = (SoutheastAsiaAction) jComboBox1.getSelectedItem();
            setActionText(a);
            if (a.item == SoutheastAsiaAction.ITEM_GAIN) {
                setActionLock(true);
                //popup box
                ItemWindow iw=new ItemWindow();
                iw.setListenersGather(stats.getPossibleItemListOf(clientCode), this);
                iw.setVisible(true);

                tradeOrGain = false;
            } else if (a.item == SoutheastAsiaAction.ITEM_TRADE) {
                setActionLock(true);
                ItemWindow iw=new ItemWindow();
                iw.setListenersGather(stats.getItemsOf(clientCode), this);
                iw.setVisible(true);
                tradeOrGain = true;
            } else if (a.war == SoutheastAsiaAction.WAR_ATTACK) {
                //probably should not be here; should be chosen from the map
            } else if (a.war == SoutheastAsiaAction.WAR_GIVEUP) {
            } else {
                setActionLock(false);
            }
        } catch (java.lang.ClassCastException cce) {
            a = null;
        } catch (java.lang.NullPointerException npe) {
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!actionLock) {

            String message = "sendaction#";
            message += getClientCode();
            message += "#";
            message += jComboBox1.getSelectedItem().toString();
            message += "#";
            message += jTextArea1.getText();
            message += "#";
            message += jTextField3.getText();
            message += "#";
            message += jTextField4.getText();
            message += "#";
            message += jTextField5.getText();
            message += "#";
            message += jTextField6.getText();

            //super secret fields! territory conquered
            if (invasionDeclared) {
                if (territoryTargetted != -1) {
                    //war!
                    message += "#invasion#";
                    message += territoryTargetted;
                }
            } else if (territoryTargetted != -1) {
                message += "#landing#";
                message += territoryTargetted;
            }

            if (!productTargetted.isEmpty()) {
                //target!
                if (tradeOrGain) {
                    //trade!
                    message += "#trade#";
                } else {
                    //gain!
                    message += "#gather#";
                }
                message += productTargetted;

            }

            sendMessage(message);
        } else {
            JOptionPane.showMessageDialog(null, "Error! Action incomplete, please reselect action");
        }
}//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
        // TODO add your handling code here:
}//GEN-LAST:event_jTextArea1KeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                //new SoutheastAsiaClientApp().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton britainbtn;
    private javax.swing.JComboBox bruneiCB;
    private javax.swing.JComboBox burmaCB;
    private javax.swing.JComboBox cambodiaCB;
    private javax.swing.JButton connect;
    private javax.swing.JTextField countryField;
    private javax.swing.JTextField culturalField;
    private javax.swing.JTextField culturalPenalty;
    private javax.swing.JTextField culturalSolve;
    private javax.swing.JButton dutchbtn;
    private javax.swing.JTextField economicField;
    private javax.swing.JTextField economicPenalty;
    private javax.swing.JTextField economicSolve;
    private javax.swing.JComboBox etimorCB;
    private javax.swing.JButton frenchbtn;
    private javax.swing.JTextArea itemArea;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JComboBox javaCB;
    private javax.swing.JComboBox kalimantanCB;
    private javax.swing.JComboBox laosCB;
    private javax.swing.JComboBox malayaCB;
    private javax.swing.JLabel map;
    private javax.swing.JTextField militaryField;
    private javax.swing.JTextField militaryPenalty;
    private javax.swing.JTextField militarySolve;
    private javax.swing.JComboBox papuaCB;
    private javax.swing.JComboBox philippinesCB;
    private javax.swing.JTextField politicalField;
    private javax.swing.JTextField politicalPenalty;
    private javax.swing.JTextField politicalSolve;
    private javax.swing.JButton portugalbtn;
    private javax.swing.JTextArea problemField;
    private javax.swing.JComboBox sabahCB;
    private javax.swing.JComboBox sarawakCB;
    private javax.swing.JButton spainbtn;
    private javax.swing.JComboBox sulawesiCB;
    private javax.swing.JComboBox sumatraCB;
    private javax.swing.JComboBox thailandCB;
    private javax.swing.JButton usbtn;
    private javax.swing.JComboBox vietnamCB;
    private javax.swing.JComboBox wtimorCB;
    // End of variables declaration//GEN-END:variables
    //i tried to combine app and client starting here:
    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    //private Socket socket;
    private PlayRunner runner;
    private PrintWriter sender;

    public void setSocket(Socket s) {
        socket = s;
        runner = new PlayRunner(socket, this);
        runner.start();
    }

    public void setFakeSocket(FakeSockets fs) {
        useFakeSockets = true;
        fakesockets = fs;
    }

    // to add: interpret messages, and send them out
    // NOTE TO SELF: THIS IS GOLDEN.
    public void sendMessage(String message) // This sends a message to the server.
    {
        if (useFakeSockets) {
            //fakesockets.serverRecieveTransmission(message);
        } else {
            // put not fake sockets here

            //System.out.println("HI ENZO SoutheastAsiaClientApp.sendMessage");

            if (sender != null && isConnected) {
                sender.println(message);    // I'm wondering if these conditions are redundant
            } else {
                System.out.println("Not connected!");              // but I'll keep them there just in case.
            }
        }
    }

    public int getClientCode() {
//        if(useFakeSockets)
//            return fakesockets.getClientCode(this);
//        else
//
//            System.out.println("EDIT SoutheastAsiaClientApp.getClientCode to use not-fake-sockets!");
//            // Note to self: figure this out - A.


        return clientCode;
    }

    public void recieveMessage(String message) {
        //this will be replaced by Interpreter
        //System.out.println(message);
        southeastasia.client.Interpreter.interpret(this, message);

        /*
        String[]splitMessage = message.split("#");

        if(splitMessage[0].equals("verified"))
        {
        isConnected = true;
        //System.out.println("yoyoyo"+message);
        clientCode = Integer.parseInt(splitMessage[1]);
        System.out.println(message);
        System.out.println("Connection established.");
        }
        else if(splitMessage[0].equalsIgnoreCase("warn"))
        {
        new AlertWindow(message.substring(5)).setVisible(true);
        }
        else
        {
        if(message.equals("startgame"))
        {
        startGameScreen();
        //switch screen
        }
        else
        tempMessage(message);
        }*/
    }

    class PlayRunner extends Thread //Copy-pasted from ServerSockets
    {

        Socket playSocket;
        SoutheastAsiaClientApp app;

        public PlayRunner(Socket socket, SoutheastAsiaClientApp app) {
            this.playSocket = socket;
            this.app = app;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                sender = new PrintWriter(socket.getOutputStream(), true);
                Scanner in = new Scanner(new BufferedInputStream(is));

                while (true) {
                    String msg = in.nextLine();
                    if (!(msg.equals("") || msg == null)) {
                        //interpret(msg); ? Is this how it should be done?
                        app.recieveMessage(msg);   //temporary
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Yikes! Something happened.");
            }


        }
    }

    //some methods for parsing
    public void addChat(String message) {
        chat.addMessage(message);
    }
// begin interpreter methods

    public void receiveChat(String source, String message) {
        addChat(source + ": " + message);
    }

    public void receiveWarn(String message) {
        new AlertWindow(message).setVisible(true);
    }

    //startgamescreen implemented above.
    public void receiveStart() {
        startGameScreen();
    }

    public void updateTerritories(int[] territories) {
        for (int i = 0; i < territories.length; i++) {
            territoryCBs[i].setSelectedIndex(territories[i] + 1);
            stats.setTerritory(i, territories[i]);
        }
    }

    public void receiveVerify(int clientCode) {
        isConnected = true;
        this.clientCode = clientCode;
        System.out.println("Connection established.");
        getStats(clientCode).name = "Player " + clientCode;
    }

    public void updateStats(int playerCode, int c, int e, int m, int p) {
        getStats(playerCode).cultural = c;
        getStats(playerCode).economic = e;
        getStats(playerCode).military = m;
        getStats(playerCode).political = p;
        //update gui!!!
        updateGUI();
    }

    public void updateItems(int playerCode, String[] items)
    {
        if(playerCode==clientCode)
        {//itemPanel.initButtonsLogo(stats.getItemsOf(playerCode));
            
            itemArea.setText("");
            for(String s:items)
            {
                itemArea.append(s+"\n");
            }
        }


        ArrayList<ItemDetails> c=new ArrayList<ItemDetails>();
        for(String s:items)
        {
            c.add(ItemDetailsFactory.getItem(s));
        }
        stats.setItems(c, clientCode);
    }

    public void updateGUI() {
        CountryVariables vars = getStats(clientCode);
        countryField.setText(vars.name);
        culturalField.setText("" + vars.cultural);
        economicField.setText("" + vars.economic);
        militaryField.setText("" + vars.military);
        politicalField.setText("" + vars.political);


        int numCountries = stats.countSelectedCountries();

        countriesTableModel.setRowCount(numCountries);

        for (int i = 0; i < numCountries; i++) {

            vars = stats.getStats(i);
            countriesTableModel.setValueAt(vars.name, i, 0);
            countriesTableModel.setValueAt(vars.cultural, i, 1);
            countriesTableModel.setValueAt(vars.economic, i, 2);
            countriesTableModel.setValueAt(vars.political, i, 3);
            countriesTableModel.setValueAt(vars.military, i, 4);
        }

        Problem p=stats.getProblemData(clientCode);

        if(!p.isNull)
        {
            problemField.setText(p.description);
            culturalPenalty.setText(""+p.statModifiers.cultural);
            militaryPenalty.setText(""+p.statModifiers.military);
            economicPenalty.setText(""+p.statModifiers.economic);
            politicalPenalty.setText(""+p.statModifiers.political);

            culturalSolve.setText(""+p.solveModifiers.cultural);
            militarySolve.setText(""+p.solveModifiers.military);
            economicSolve.setText(""+p.solveModifiers.economic);
            politicalSolve.setText(""+p.solveModifiers.political);
        }

    }

    public SoutheastAsiaServerStats getServerStats() {
        return stats;
    }

    public CountryVariables getStats(int playerCode) {
        return stats.getStats(playerCode);
    }

    public void updatePlayerCountry(int player, int country, String name) {
        stats.getStats(player).name = name;
        stats.setCountry(player, country);
    }

    //sets all text areas to uneditable
    //use this for territory taking and war
    public void lockAction() {
        jTextArea1.setEditable(false);
        jTextField3.setEditable(false);
        jTextField4.setEditable(false);
        jTextField5.setEditable(false);
        jTextField6.setEditable(false);
    }

    public void unlockAction() {

        jTextArea1.setEditable(true);
        jTextField3.setEditable(true);
        jTextField4.setEditable(true);
        jTextField5.setEditable(true);
        jTextField6.setEditable(true);
    }

    public void setTabAtAction() {
        jTabbedPane1.setSelectedComponent(jPanel3);
    }

    public void setInvasion() {
        invasionDeclared = true;
    }
    public void receiveProblem(String netcode[])
    {
        Problem p = new Problem(netcode[1], netcode[2], Integer.parseInt(netcode[3]),
                Integer.parseInt(netcode[4]), Integer.parseInt(netcode[5]),
                Integer.parseInt(netcode[6]), Integer.parseInt(netcode[7]),
                Integer.parseInt(netcode[8]), Integer.parseInt(netcode[9]), Integer.parseInt(netcode[10]));

        stats.setProblem(p, clientCode);

    }
}

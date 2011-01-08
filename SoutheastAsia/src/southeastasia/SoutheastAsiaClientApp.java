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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.awt.CardLayout;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import southeastasia.networking.SoutheastAsiaServerSockets;

/**
 *
 * @author Enzo
 */
public class SoutheastAsiaClientApp extends javax.swing.JFrame {

    /** Creates new form SoutheastAsiaClientApp */
    public SoutheastAsiaClientApp() {
        port=7777;
        initComponents();
        cl=(CardLayout)jPanel4.getLayout();
        isConnected = false;
        useFakeSockets = false;
        loadActions();

    }

	private ArrayList<SoutheastAsiaAction> actions;
    private void loadActions() //loads the actions in; hardcoding ftw
    {
        actions=new ArrayList<SoutheastAsiaAction>();
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
		

                jComboBox1.removeAllItems();

		for(SoutheastAsiaAction a:actions)
		{
                    jComboBox1.addItem(a);
		}
    }

    //private FakeSockets sockets;
    private Socket socket;
    private InetAddress host;
    private int port;
    private boolean isConnected;
    //private SoutheastAsiaClient client;

    private CardLayout cl;
    /**
     * after the game starts, the server sends a message and the
     * client switches to the game screen.
     */
    public void startGameScreen()
    {
        cl.show(jPanel4, "game_play");
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
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        connect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel1.setName("jPanel1"); // NOI18N

        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getResourceMap(SoutheastAsiaClientApp.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        connect.setName("connect"); // NOI18N
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(connect, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(connect)
                .addContainerGap())
        );

        jPanel4.add(jPanel1, "country_select");

        jPanel2.setName("jPanel2"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
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

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
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
                        .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 333, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jPanel4.add(jPanel2, "game_play");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //sockets.serverRecieveTransmission(jTextField1.getText());
        sendMessage(jTextField1.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    public void tempMessage(String message)
    {
        jTextField1.setText(message);
    }

    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        if(isConnected == false)
        {
            System.out.println("Connecting!");
            try {
                host = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket = new Socket(host.getHostName(), port);
                setSocket(socket);
            } catch (UnknownHostException ex) {
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SoutheastAsiaClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            //else
            //    System.out.println("Connecting failed. Please try again later.");
        }

        else System.out.println("Already connected!");


    }//GEN-LAST:event_connectActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String message="sendaction#";
        message+=getClientCode();
        message+="#";
        message+=jComboBox1.getSelectedItem().toString();
        message+="#";
        message+=jTextArea1.getText();
        message+=" \n#";
        message+=jTextField3.getText();
        message+="#";
        message+=jTextField4.getText();
        message+="#";
        message+=jTextField5.getText();
        message+="#";
        message+=jTextField6.getText();
        sendMessage(message);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        SoutheastAsiaAction a;
        try
        {
            a=(SoutheastAsiaAction)jComboBox1.getSelectedItem();
        }
        catch(java.lang.ClassCastException cce)
        {
            a=null;
        }

        if(a==null)
        {
            a=new SoutheastAsiaAction("", "", 0,0,0,0);
        }
            jTextArea1.setText(a.description);
            jTextField3.setText(a.statModifiers.cultural+"");
            jTextField4.setText(a.statModifiers.economic+"");
            jTextField5.setText(a.statModifiers.military+"");
            jTextField6.setText(a.statModifiers.political+"");
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SoutheastAsiaClientApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connect;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

    //i tried to combine app and client starting here:


    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    //private Socket socket;
    private PlayRunner runner;
    private PrintWriter sender;


    public void setSocket(Socket s)
    {
        socket = s;
        runner = new PlayRunner(socket, this);
        runner.start();
    }

    public void setFakeSocket(FakeSockets fs)
    {
        useFakeSockets=true;
        fakesockets=fs;
    }

    // to add: interpret messages, and send them out

    public void sendMessage(String message)     // This sends a message to the server.
    {
        if(useFakeSockets)
        {
            fakesockets.serverRecieveTransmission(message);
        }
        else
        {
            // put not fake sockets here

            //System.out.println("HI ENZO SoutheastAsiaClientApp.sendMessage");

            if (sender != null && isConnected) sender.println(message);    // I'm wondering if these conditions are redundant
                    else System.out.println("Not connected!");              // but I'll keep them there just in case.
        }
    }

    public int getClientCode()
    {
        if(useFakeSockets)
            return fakesockets.getClientCode(this);
        else
            System.out.println("EDIT SoutheastAsiaClientApp.getClientCode to use not-fake-sockets!");
            // Note to self: figure this out - A.
        return -1;
    }

    public void recieveMessage(String message)
    {
        //System.out.println(message);
        
        if(message.equals("verified"))
        {
            isConnected = true;
            System.out.println("Connection established.");
        }
        else if(message.length() > 5 && message.substring(0, 5).equalsIgnoreCase("warn:"))
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
        }
    }



            class PlayRunner extends Thread //Copy-pasted from ServerSockets
    {
            Socket playSocket;
            SoutheastAsiaClientApp app;

            public PlayRunner(Socket socket, SoutheastAsiaClientApp app)
            {
                this.playSocket = socket;
                this.app = app;
            }

                @Override
                public void run() {
                    try {
                        InputStream is = socket.getInputStream();
                        sender = new PrintWriter(socket.getOutputStream(), true);
                        Scanner in = new Scanner(new BufferedInputStream(is));

                    while(true)
                    {
                        String msg = in.nextLine();
                        if (!(msg.equals("")||msg==null))
                        {
                            //interpret(msg); ? Is this how it should be done?
                            app.recieveMessage(msg);   //temporary
                        }

                    }
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SoutheastAsiaServerSockets.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Yikes! Something happened.");
                }


            }

    }
}

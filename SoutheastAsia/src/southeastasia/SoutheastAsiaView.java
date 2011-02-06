/*
 * SoutheastAsiaView.java
 */
package southeastasia;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.awt.Component;
import java.io.IOException;
import southeastasia.loader.ProblemsLoader;
import southeastasia.networking.SoutheastAsiaServerSockets;

/**
 * The application's main frame.
 */
public class SoutheastAsiaView extends FrameView {

    public static final String PROBLEMSPATH = "src\\southeastasia\\resources\\problems.xml";
    public ArrayList<String> countries;
    public ArrayList<JComboBox> comboboxes;
    //public SoutheastAsiaServer server;
    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    private CardLayout cl;
    private ChatWindow chat;
    private SoutheastAsiaServerStats stats;
    private boolean gameStarted;
    private SoutheastAsiaServerSockets ss;
    private boolean boxesSet;
    private CountryVariables[] defaults;
    private ArrayList<Problem> problems;

    public void setFakeSockets(FakeSockets sock) {
        useFakeSockets = true;
        fakesockets = sock;
    }
    public static final int numTerritories = 16;
    public static final int territoriesPerColumn = 8; //how many territories in a column, in the territory tab
    //16 territories, 2 columns
    private String[] territories; //array of all territories
    private JComboBox[] territoryCBs; //array of all jcomboboxes

    public SoutheastAsiaView(SingleFrameApplication app) {
        super(app);
        useFakeSockets = false;

        boxesSet = false;
        chat = new ChatWindow(this);

        stats = new SoutheastAsiaServerStats();

        try {
            ss = new SoutheastAsiaServerSockets();
            ss.setChat(chat);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }


        defaults = new CountryVariables[6];
        defaults[0] = new CountryVariables("Portugal", 50, 40, 55, 55);
        defaults[1] = new CountryVariables("Spain", 60, 45, 45, 50);
        defaults[2] = new CountryVariables("Britain", 50, 60, 45, 45);
        defaults[3] = new CountryVariables("Netherlands", 50, 55, 55, 40);
        defaults[4] = new CountryVariables("France", 45, 55, 55, 45);
        defaults[5] = new CountryVariables("America", 40, 55, 60, 45);

        territories = new String[numTerritories];
        territories[0] = "Burma";
        territories[1] = "Brunei";
        territories[2] = "Cambodia";
        territories[3] = "Java";
        territories[4] = "Kalimantan";
        territories[5] = "Laos";
        territories[6] = "Malaya & Singapore";
        territories[7] = "Papua New Guinea";
        territories[8] = "The Philippines";
        territories[9] = "Sabah";
        territories[10] = "Sarawak";
        territories[11] = "Sulawesi & Moluccas";
        territories[12] = "Sumatra";
        territories[13] = "Thailand";
        territories[14] = "Timor";
        territories[15] = "Vietnam";


        problems = ProblemsLoader.loadProblems(PROBLEMSPATH);

        initComponents();
        cl = (CardLayout) (mainPanel.getLayout());
        countries = new ArrayList<String>();
        countries.add("Select country");
        countries.add("Portugal");
        countries.add("Spain");
        countries.add("Britain");
        countries.add("Netherlands");
        countries.add("France");
        countries.add("America");

        comboboxes = new ArrayList<JComboBox>();
        comboboxes.add(jComboBox1);
        comboboxes.add(jComboBox2);
        comboboxes.add(jComboBox3);
        comboboxes.add(jComboBox4);
        comboboxes.add(jComboBox5);
        comboboxes.add(jComboBox6);

        allowActions = new boolean[SoutheastAsiaApp.MAX_PLAYERS];


        for (JComboBox jcb : comboboxes) {
            jcb.removeAllItems();
            for (String country : countries) {
                jcb.addItem(country);

            }
        }

        territoryCBs = new JComboBox[SoutheastAsiaServerStats.NUM_TERRITORIES];
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
        territoryCBs[14] = (timorCB);
        territoryCBs[15] = (vietnamCB);

        boxesSet = true;

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SoutheastAsiaApp.getApplication().getMainFrame();
            aboutBox = new SoutheastAsiaAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SoutheastAsiaApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        actionTable = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        solveProblem = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        countryTable = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        territoriesTab = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        burmaCB = new javax.swing.JComboBox();
        bruneiCB = new javax.swing.JComboBox();
        cambodiaCB = new javax.swing.JComboBox();
        javaCB = new javax.swing.JComboBox();
        kalimantanCB = new javax.swing.JComboBox();
        laosCB = new javax.swing.JComboBox();
        malayaCB = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        papuaCB = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        philippinesCB = new javax.swing.JComboBox();
        sabahCB = new javax.swing.JComboBox();
        sarawakCB = new javax.swing.JComboBox();
        sulawesiCB = new javax.swing.JComboBox();
        sumatraCB = new javax.swing.JComboBox();
        thailandCB = new javax.swing.JComboBox();
        timorCB = new javax.swing.JComboBox();
        vietnamCB = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.CardLayout());

        jPanel1.setName("assigncountries"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getResourceMap(SoutheastAsiaView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setName("jLabel6"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.setName("jComboBox5"); // NOI18N
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel5.setName("jLabel5"); // NOI18N

        jLabel4.setName("jLabel4"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setName("jComboBox2"); // NOI18N
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.setName("jComboBox3"); // NOI18N
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.setName("jComboBox6"); // NOI18N
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jLabel3.setName("jLabel3"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("jComboBox1"); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.setName("jComboBox4"); // NOI18N
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jSpinner1.setName("jSpinner1"); // NOI18N

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(273, 273, 273))
        );

        mainPanel.add(jPanel1, "assigncountries");

        jPanel2.setName("jPanel2"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        actionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, new Boolean(false), null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Country", "Action", "Approved?", "Problem", "Solved?"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        actionTable.setName("actionTable"); // NOI18N
        actionTable.getTableHeader().setResizingAllowed(false);
        actionTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(actionTable);
        actionTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("actionTable.columnModel.title0")); // NOI18N
        actionTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("actionTable.columnModel.title1")); // NOI18N
        actionTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("actionTable.columnModel.title2")); // NOI18N
        actionTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("actionTable.columnModel.title3")); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        solveProblem.setText(resourceMap.getString("solveProblem.text")); // NOI18N
        solveProblem.setName("solveProblem"); // NOI18N
        solveProblem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solveProblemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(solveProblem)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(solveProblem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        countryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Country", "Cultural", "Economic", "Military", "Political", "Items", "Agreements"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        countryTable.setName("countryTable"); // NOI18N
        jScrollPane4.setViewportView(countryTable);

        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        territoriesTab.setName("territoriesTab"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        burmaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        burmaCB.setName("burmaCB"); // NOI18N

        bruneiCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        bruneiCB.setName("bruneiCB"); // NOI18N

        cambodiaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cambodiaCB.setName("cambodiaCB"); // NOI18N

        javaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javaCB.setName("javaCB"); // NOI18N

        kalimantanCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        kalimantanCB.setName("kalimantanCB"); // NOI18N

        laosCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        laosCB.setName("laosCB"); // NOI18N

        malayaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        malayaCB.setName("malayaCB"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        papuaCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        papuaCB.setName("papuaCB"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        philippinesCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        philippinesCB.setName("philippinesCB"); // NOI18N

        sabahCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sabahCB.setName("sabahCB"); // NOI18N

        sarawakCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sarawakCB.setName("sarawakCB"); // NOI18N

        sulawesiCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sulawesiCB.setName("sulawesiCB"); // NOI18N

        sumatraCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sumatraCB.setName("sumatraCB"); // NOI18N

        thailandCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        thailandCB.setName("thailandCB"); // NOI18N

        timorCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        timorCB.setName("timorCB"); // NOI18N

        vietnamCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        vietnamCB.setName("vietnamCB"); // NOI18N

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout territoriesTabLayout = new javax.swing.GroupLayout(territoriesTab);
        territoriesTab.setLayout(territoriesTabLayout);
        territoriesTabLayout.setHorizontalGroup(
            territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(territoriesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(territoriesTabLayout.createSequentialGroup()
                        .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(malayaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(laosCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(kalimantanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(javaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cambodiaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bruneiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(86, 86, 86)
                                .addComponent(burmaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(territoriesTabLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(papuaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(philippinesCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sabahCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sarawakCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sulawesiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sumatraCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thailandCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vietnamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(58, Short.MAX_VALUE))
                    .addGroup(territoriesTabLayout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addContainerGap(404, Short.MAX_VALUE))))
        );
        territoriesTabLayout.setVerticalGroup(
            territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(territoriesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(burmaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(philippinesCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(bruneiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(sabahCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cambodiaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(sarawakCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(javaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(sulawesiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(kalimantanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(sumatraCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(laosCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(thailandCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(malayaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(timorCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(territoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(papuaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(vietnamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("territoriesTab.TabConstraints.tabTitle"), territoriesTab); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel6);

        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton9))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel2, "gamescreen");

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getActionMap(SoutheastAsiaView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 552, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void initializeTerritories() {
        /*this was a stupid idea
        int numterr=0;
        int numterr2=0;

        while(numterr<numTerritories)
        {
        for(int i=0;i<territoriesPerColumn;i++)
        {
        if(numterr<numTerritories)
        {
        territoriesTab.add(new JLabel(territories[numterr]));
        numterr++;
        }
        }

        for(int i=0;i<territoriesPerColumn;i++)
        {
        if(numterr2<numterr)
        {
        territoriesTab.add(new JComboBox(stats.getCountryNames()));
        numterr2++;
        }
        }
        }*/

        //this sets the contents of the territory combo boxes
        for (JComboBox jcb : territoryCBs) {
            jcb.removeAllItems();

            jcb.addItem("---");
            for (String s : stats.getCountryNames()) {
                jcb.addItem(s);
            }
        }

    }

    private void updateComboBoxes() {

        if (boxesSet) {
            jComboBox1.setSelectedIndex(getCountry(0) + 1);
            jComboBox2.setSelectedIndex(getCountry(1) + 1);
            jComboBox3.setSelectedIndex(getCountry(2) + 1);
            jComboBox4.setSelectedIndex(getCountry(3) + 1);
            jComboBox5.setSelectedIndex(getCountry(4) + 1);
            jComboBox6.setSelectedIndex(getCountry(5) + 1);
        }
    }
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //System.out.println((String)jComboBox1.getSelectedItem());
        //System.out.println(getCountryNumber((String)jComboBox1.getSelectedItem()));
        selectCountry(jComboBox1, 0);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        selectCountry(jComboBox2, 1);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        selectCountry(jComboBox3, 2);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        selectCountry(jComboBox4, 3);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        selectCountry(jComboBox5, 4);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        selectCountry(jComboBox6, 5);
        updateComboBoxes();
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        accept();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // start game
        //check if all countries are selected
        //if overridden, can start with less than 6 players
        boolean override = false; //change this

        int val=startGame(override);

        if (val == 1) {
            //send a message to all the clients telling them to start the game
            sendClientMessage(-1, "startgame");
            //change screen to game screen
            cl.show(mainPanel, "gamescreen");

            startNewTurn(true);
        }
        else if(val==-1)
        {
            JOptionPane.showMessageDialog(null, (Object)"Not all players have a country!");

        }


    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * use this for sending messages to clients
     *
     * @param playerCode -1 if sending to all
     */
    private void sendClientMessage(int playerCode, String message) {
        if (playerCode == -1) {
            ss.sendToAll(message);
        } else {
            ss.sendToOne(message, playerCode);
        }
//        if(playerCode==-1)
//        {
//            if(useFakeSockets)
//            {
//                for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
//                {
//                    //fakesockets.clientRecieveTransmission("hellow", i);
//                    if(getCountry(i)!=-1)
//                    {
//                        fakesockets.clientRecieveTransmission(message, i);
//                    }
//                }
//            }
//            else
//            {
//                System.out.println("HI ALBERT MODIFY SOUTHEASTASIAVIEW.SENDCLIENTMESSAGE");
//                //CHANGE THIS !!!
//            }
//        }
//        else
//        {
//            if(useFakeSockets)
//            {
//                fakesockets.clientRecieveTransmission(message, playerCode);
//
//            }
//            else
//            {
//                System.out.println("HI ALBERT MODIFY SOUTHEASTASIAVIEW.SENDCLIENTMESSAGE");
//                //CHANGE THIS !!!
//            }
//
//        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        chat.setVisible(!chat.isVisible());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (actionTable.getSelectedRowCount() > 0) {
            int playerCode = getPlayerCode(actionTable.getValueAt(actionTable.getSelectedRow(), 0).toString());

            if (!stats.getActionData(playerCode).isNull) {
                new ActionViewerFrame(getActionData(playerCode), this, playerCode).setVisible(true);
            }

        } else {
            System.out.println("Select some rows!");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        ss.sendToAll("huy");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (startNewTurn(false) == 0) {

            startNewTurn(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Some actions have not been sent/approved.\nAre you sure you want to start a new round?", "End turn", JOptionPane.YES_NO_OPTION));


        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        assignProblems();
        updateActionTables();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void solveProblemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solveProblemActionPerformed
        //removes the problem
        if (actionTable.getSelectedRowCount() > 0) {
            int playerCode = getPlayerCode(actionTable.getValueAt(actionTable.getSelectedRow(), 0).toString());
            //setSolved(!stats.getProblemSolved(playerCode), playerCode);
            //updateActionTables();

            if (!stats.getProblemData(playerCode).isNull) {
                new ProblemViewerFrame(stats.getProblemData(playerCode), this, playerCode).setVisible(true);
            }

        } else {
            System.out.println("Select some rows!");
        }
    }//GEN-LAST:event_solveProblemActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (countryTable.getSelectedRowCount() > 0) {
            int playerCode = getPlayerCode(countryTable.getValueAt(countryTable.getSelectedRow(), 0).toString());

            new CountryViewerFrame(playerCode, this).setVisible(true);

        } else {
            System.out.println("Select some rows!");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //save territories per country
        for (int i = 0; i < SoutheastAsiaServerStats.NUM_TERRITORIES; i++) {
            stats.setTerritory(i, territoryCBs[i].getSelectedIndex() - 1);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void assignProblems() {
        for (int i = 0; i < stats.countSelectedCountries(); i++) {
            stats.setProblem(getRandomProblem(), i);
        }
    }

    private Problem getRandomProblem() {
        int ret = (int) (Math.random() * problems.size());
        return problems.get(ret);
    }

    public void sendProblemUpdate(int playerCode) {
        Problem p = stats.getProblemData(playerCode);
        String message;
        message = "prob:";
        message += p.name + "#";
        message += p.description + "#";
        message += p.statModifiers.cultural + "#";
        message += p.statModifiers.economic + "#";
        message += p.statModifiers.military + "#";
        message += p.statModifiers.political + "#";
        message += p.solveModifiers.cultural + "#";
        message += p.solveModifiers.economic + "#";
        message += p.solveModifiers.military + "#";
        message += p.solveModifiers.political + "#";

        sendClientMessage(playerCode, message);
    }

    private void selectCountry(JComboBox jcb, int playerno) {
        //System.out.println(getCountryNumber((String)jcb.getSelectedItem()));
        int result = chooseCountry(playerno, getCountryNumber((String) jcb.getSelectedItem()), false);

        if (result != 1) {
            jcb.setSelectedIndex(getCountry(playerno));
        }
    }

    public void tempAddMessage(String message) {
        //jTextArea1.append(message+"\n");
        //change this next part, specifically the delimiter
        String[] input = message.split("#");

        if (input[0].equals("sendaction")) {
            setAction(message.substring("sendaction#".length()), true);


        }


        chat.addMessage(message);
    }

    //kind of a hacky way to get a country's code but WHATEVER
    private int getCountryNumber(String name) {
        int i = -1;

        if (name != null) {
            for (String country : countries) {
                if (name.equals(country)) {
                    return i;
                }
                i++;
            }
        }

        return -1;

    }

    public void updateActionTables() {
        for (int i = 0; i < SoutheastAsiaApp.MAX_PLAYERS; i++) {
            if (getCountry(i) != -1) {
                actionTable.setValueAt(stats.getStats(i).name, i, 0);
                actionTable.setValueAt(getActionName(i), i, 1);
                actionTable.setValueAt(getActionApproved(i), i, 2);
                actionTable.setValueAt(getProblemName(i), i, 3);
                actionTable.setValueAt(stats.getProblemSolved(i), i, 4);
            }
        }
    }

    public void updateCountryTables() {
        for (int i = 0; i < SoutheastAsiaApp.MAX_PLAYERS; i++) {
            if (getCountry(i) != -1) {
                countryTable.setValueAt(stats.getStats(i).name, i, 0);
                countryTable.setValueAt(stats.getStats(i).cultural, i, 1);
                countryTable.setValueAt(stats.getStats(i).economic, i, 2);
                countryTable.setValueAt(stats.getStats(i).military, i, 3);
                countryTable.setValueAt(stats.getStats(i).political, i, 4);

            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable actionTable;
    private javax.swing.JComboBox bruneiCB;
    private javax.swing.JComboBox burmaCB;
    private javax.swing.JComboBox cambodiaCB;
    private javax.swing.JTable countryTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox javaCB;
    private javax.swing.JComboBox kalimantanCB;
    private javax.swing.JComboBox laosCB;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox malayaCB;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JComboBox papuaCB;
    private javax.swing.JComboBox philippinesCB;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JComboBox sabahCB;
    private javax.swing.JComboBox sarawakCB;
    private javax.swing.JButton solveProblem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JComboBox sulawesiCB;
    private javax.swing.JComboBox sumatraCB;
    private javax.swing.JPanel territoriesTab;
    private javax.swing.JComboBox thailandCB;
    private javax.swing.JComboBox timorCB;
    private javax.swing.JComboBox vietnamCB;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    //OLD SOUTHEASTASIASERVER
    //public SoutheastAsiaView window;
    private boolean[] allowActions; //set to true at the start of every turn;
    /*set to false after a player has sent an action that is neither
     * approved nor disapproved para bawal mag spam ng action
     *
     * set to true after disapproving
     */

    public void allowActions(int playerCode, boolean allowed) {
        allowActions[playerCode] = allowed;
    }

    /*
    public SoutheastAsiaServer()
    {
    stats=new SoutheastAsiaServerStats();
    gameStarted=false;
    }*/
    /**
     * call this class whenever a new turn starts
     * (possibly the teacher presses a button)
     * wipes all actions, problems, and sets stats
     *
     * if a country has not submitted an action, override must
     * be "true" in order to continue
     *
     * returns 1 if all countries submitted, 2 if not all submitted
     * but was overridden, 0 if not all submitted and was not overridden
     *
     * @param override true if next turn starts with not all countries ready with an action
     */
    public int startNewTurn(boolean override) {
        boolean overridden = false;
        if (gameStarted) {

            bigPlayerUpdate();



            for (int i = 0; i < stats.countSelectedCountries(); i++) {
                if (getActionData(i).isNull || !stats.getActionApproved(i)) {

                    if (override) {
                        overridden = true;
                    } else {
                        return 0;
                    }
                }
            }



            if (stats.newTurn()) {
                //game won! now check to see who won and in what category
            } else {
                updateActionTables();
                updateCountryTables();
                for (int i = 0; i < stats.countSelectedCountries(); i++) {
                    allowActions(i, true);

                }
            }
            return 1;
        }
        return 0;
    }

    /**
     *
     * call this function whenever a chat message is received
     * from the players or the sent by the teacher
     *
     * adds the chat to the log in serverstats
     *
     * @param chat the chat message in some format (handle plz)
     * @return
     */
    public int recieveChatMessage(String chat) {
        stats.updateChatlog(chat);

        return 1;
    }

    /**
     * this method returns the action corresponding to the
     * country
     *
     * i guess kung wala pang action it returns something else
     *
     * @param playerCode the player's number
     * @return the action in String format
     */
    public String getAction(int playerCode) {
        return stats.getAction(playerCode).toString();
    }

    public String getActionName(int playerCode) {
        return stats.getActionName(playerCode);
    }

    public boolean getActionApproved(int playerCode) {
        return stats.getActionApproved(playerCode);
    }

    public String getProblem(int playerCode) {
        return stats.getProblem(playerCode);
    }

    public String getProblemName(int playerCode) {
        return stats.getProblemName(playerCode);
    }

    /**
     *
     * call this method during country select
     *
     * once picked, countries cannot be picked again unless overridden
     *
     * @param playerCode a number from 0-5 representing a player
     * @param countryCode a number from 0-5 representing the country
     * @param override if true, can replace country already picked
     * @return 1 if successful 0 if country already chosen -1 if playercode invalid -2 if countrycode invalid
     */
    public int chooseCountry(int playerCode, int countryCode, boolean override) {
        if (override) {
            stats.replaceAllCountryChoices(countryCode); //all players who have picked the country will be reset
            return stats.setCountry(playerCode, countryCode);
        } else {
            if (stats.countryIsAlreadySelected(countryCode)) {
                return 0;
            } else {
                return stats.setCountry(playerCode, countryCode);
            }

        }
    }

    public int getCountry(int playerCode) {
        return stats.getCountry(playerCode);
    }

    /**
     * call this method when done with country select
     *
     * will not start if not all slots are filled and override is false
     * will not start if 2+ players have the same country (probably shouldnt happen but whatever)
     *
     * sets gameStarted to true
     * prepares ServerStats
     *
     * @param override set to true if starting with incomplete players
     * @return 0 if not all slots filled 1 if all slots filled OR overridden 2 if country overlap
     */
    public int startGame(boolean override) {
        //put some code in here

        if(stats.countSelectedCountries()<ss.getNumPlayers())
        {
            //not all players have selected a country
            return -1;
        }

        int c;
        CountryVariables cv;
        for (int i = 0; i < stats.countSelectedCountries(); i++) {
            c = stats.getCountry(i);
            cv = stats.getStats(i);
            cv.cultural = defaults[c].cultural;
            cv.economic = defaults[c].economic;
            cv.military = defaults[c].military;
            cv.political = defaults[c].political;
            cv.name = defaults[c].name;
        }

        gameStarted = true;
        //startNewTurn(true);
        initializeTerritories();
        return 1;
    }

    /**
     *
     * generates the text log for this turn
     *
     * includes: stats at the start, problems generated, actions, whether the
     * problem was solved, stats at the end
     *
     * @return the log for this turn
     */
    public String generateTurnLog() {
        //stuff here
        return "";
    }

    /**
     * solves the problem belonging to a player
     * (called before the end of the turn)
     * @param playerCode
     * @return 1 if problem exists, 0 otherwise
     */
    public int solveProblem(int playerCode) {
        return stats.solveProblem(playerCode);

    }

    /**
     * gets the stats of a player
     * @param playerCode the player to query
     * @return the stats
     */
    public CountryVariables getStats(int playerCode) {
        return stats.getStats(playerCode);

    }

    public static SoutheastAsiaAction parseAction(String actionCode) {
        //change the delimiter here
        String[] newaction = actionCode.split("#");
        return new SoutheastAsiaAction(newaction[1], newaction[2], Integer.parseInt(newaction[3]), Integer.parseInt(newaction[4]), Integer.parseInt(newaction[5]), Integer.parseInt(newaction[6]));
        //cultural, economic, military, political
    }

    /**
     * call this method to send in actions
     * @param playerCode the player sending in an action
     * @param actionCode the action in encoded string format
     * @param override if set to false, will not change existing action
     * @return 1 if action set, 0 if there is an existing action, 2 if there is an existing action but was overridden
     */
    public int setAction(String actionCode, boolean override) {
        int playerCode; //do something to get playerCode from actionCode

        String parsedCode[] = actionCode.split("#");
        playerCode = Integer.parseInt(parsedCode[0]);

        if (allowActions[playerCode]) {

            if (stats.hasAction(playerCode)) {
                //if(override) not using override any more, instead allowActions
                if (true) {
                    //parse actioncode, turn it into an action
                    stats.setAction(parseAction(actionCode), playerCode);
                    allowActions(playerCode, false);

                    //pass to serverstats
                    stats.setApproval(playerCode, false);

                    updateActionTables();

                    return 2;
                }

                return 0;
            }

            //parse actioncode, turn it into an action
            //pass it to serverstats

            stats.setAction(parseAction(actionCode), playerCode);
            //set action approval to false
            stats.setApproval(playerCode, false);
            allowActions(playerCode, false);

            updateActionTables();

            return 1;
        } else {
            //send a message back to the player saying that actions
            //not allowed yet
            sendClientMessage(playerCode, "warn#action not allowed");
            return 0;
        }
    }

    public static Problem generateProblem() {
        return Problem.noProblem();
    }

    public void accept() {
        ss.acceptSockets();
    }

    public int countPlayers() {
        return stats.countSelectedCountries();
    }

    public void setAction(SoutheastAsiaAction seact, int playerCode) {
        stats.setAction(seact, playerCode);
    }

    public SoutheastAsiaAction getActionData(int playerCode) {
        return stats.getActionData(playerCode);
    }

    public int getPlayerCode(int countryCode) {
        return stats.getPlayerCode(countryCode);
    }

    public int getPlayerCode(String countryCode) {
        return stats.getPlayerCode(countryCode);
    }

    public void setApproval(boolean approval, int playerCode) {
        stats.setApproval(playerCode, approval);
    }

    public void setSolved(boolean solved, int playerCode) {
        stats.setSolved(playerCode, solved);
    }

    public void bigPlayerUpdate() {
        String playerCountryUpdate="player";
        for(int k=0;k<countPlayers();k++)
        {
            playerCountryUpdate+="#"+k+"#"+stats.getCountry(k)+"#"+stats.getStats(k).name;
        }

        String terrupdate = "terrs";
        for (int j = 0; j < SoutheastAsiaServerStats.NUM_TERRITORIES; j++) {
            terrupdate += "#" + (territoryCBs[j].getSelectedIndex() - 1);
        }

        String s;
        for (int i = 0; i < countPlayers(); i++) {
            s = "stats#";
            s+=i;
            s+="#";
            s += stats.getStats(i).cultural;
            s += "#";
            s += stats.getStats(i).economic;
            s += "#";
            s += stats.getStats(i).military;
            s += "#";
            s += stats.getStats(i).political;

            ss.sendToAll(playerCountryUpdate);

            ss.sendToAll(s);

            ss.sendToAll(terrupdate);

        }

    }

    public void sendMessage(String s) {
        ss.sendToAll(s);
    }

    public void sendMessage(int playerindex, String s) {
        ss.sendToOne(s, playerindex);
    }

    public void sendPrivateMessage(int playerindex, int senderindex, String message) {
        sendMessage(playerindex, "privmsg#"+defaults[senderindex].name + "#" + message);
    }

    //temporarily going to change this
    public void sendChat(int source, String name, String message) {
        sendMessage("chat#"+/*defaults[source].*/name + "#" + message);
        chat.addMessage(name+": "+message);
    }
}

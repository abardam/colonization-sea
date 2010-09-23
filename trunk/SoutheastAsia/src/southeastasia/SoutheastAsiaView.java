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
import java.util.StringTokenizer;
import java.awt.CardLayout;

/**
 * The application's main frame.
 */
public class SoutheastAsiaView extends FrameView {

    public ArrayList<String> countries;
    public ArrayList<JComboBox> comboboxes;
    //public SoutheastAsiaServer server;

    private boolean useFakeSockets;
    private FakeSockets fakesockets;
    private CardLayout cl;
    private ChatWindow chat;

    private boolean boxesSet;

    public void setFakeSockets(FakeSockets sock)
    {
        useFakeSockets=true;
        fakesockets=sock;
    }

    public SoutheastAsiaView(SingleFrameApplication app) {
        super(app);
        useFakeSockets=false;

        boxesSet=false;
        chat=new ChatWindow();

        stats=new SoutheastAsiaServerStats();

        initComponents();
        cl = (CardLayout)(mainPanel.getLayout());
        countries=new ArrayList<String>();
        countries.add("Select country");
        countries.add("Portugal");
        countries.add("Spain");
        countries.add("Britain");
        countries.add("Netherlands");
        countries.add("France");
        countries.add("America");

        comboboxes=new ArrayList<JComboBox>();
        comboboxes.add(jComboBox1);
        comboboxes.add(jComboBox2);
        comboboxes.add(jComboBox3);
        comboboxes.add(jComboBox4);
        comboboxes.add(jComboBox5);
        comboboxes.add(jComboBox6);

        allowActions=new boolean[SoutheastAsiaApp.MAX_PLAYERS];
        

        for(JComboBox jcb:comboboxes)
        {
            jcb.removeAllItems();
            for(String country:countries)
            {
                jcb.addItem(country);

            }
        }

        boxesSet=true;

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
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
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
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        mainPanel.add(jPanel1, "assigncountries");

        jPanel2.setName("jPanel2"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, new Boolean(false), null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Country", "Action", "Approved?", "Problem"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setName("jButton5"); // NOI18N

        jButton6.setName("jButton6"); // NOI18N

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
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel4.setName("jPanel4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel4);

        jPanel5.setName("jPanel5"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab3", jPanel5);

        jPanel6.setName("jPanel6"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(311, Short.MAX_VALUE))
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

    private void updateComboBoxes()
    {

        if(boxesSet)
        {
            jComboBox1.setSelectedIndex(getCountry(0)+1);
            jComboBox2.setSelectedIndex(getCountry(1)+1);
            jComboBox3.setSelectedIndex(getCountry(2)+1);
            jComboBox4.setSelectedIndex(getCountry(3)+1);
            jComboBox5.setSelectedIndex(getCountry(4)+1);
            jComboBox6.setSelectedIndex(getCountry(5)+1);
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
        boolean override=true; //change this

        if(startGame(override)==1)
        {
            //send a message to all the clients telling them to start the game
            sendClientMessage(-1, "startgame");
            //change screen to game screen
            cl.show(mainPanel, "gamescreen");
            startGame(true);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * use this for sending messages to clients
     *
     * @param playerCode -1 if sending to all
     */
    private void sendClientMessage(int playerCode, String message)
    {
        if(playerCode==-1)
        {
            if(useFakeSockets)
            {
                for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
                {
                    //fakesockets.clientRecieveTransmission("hellow", i);
                    if(getCountry(i)!=-1)
                    {
                        fakesockets.clientRecieveTransmission(message, i);
                    }
                }
            }
            else
            {
                System.out.println("HI ALBERT MODIFY SOUTHEASTASIAVIEW.SENDCLIENTMESSAGE");
                //CHANGE THIS !!!
            }
        }
        else
        {
            if(useFakeSockets)
            {
                fakesockets.clientRecieveTransmission(message, playerCode);

            }
            else
            {
                System.out.println("HI ALBERT MODIFY SOUTHEASTASIAVIEW.SENDCLIENTMESSAGE");
                //CHANGE THIS !!!
            }

        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        chat.setVisible(!chat.isVisible());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if(jTable1.getSelectedRowCount()>0)
        {
            int playerCode=getPlayerCode(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
            new ActionViewerFrame(getActionData(playerCode), this,  playerCode).setVisible(true);
            
        }

        else

        {
            System.out.println("Select some rows!");
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    private void selectCountry(JComboBox jcb, int playerno)
    {
        //System.out.println(getCountryNumber((String)jcb.getSelectedItem()));
        int result=chooseCountry(playerno, getCountryNumber((String)jcb.getSelectedItem()), false);

        if(result!=1)
        {
            jcb.setSelectedIndex(getCountry(playerno));
        }
    }

    public void tempAddMessage(String message)
    {
        //jTextArea1.append(message+"\n");
        //change this next part, specifically the delimiter
        String[] input=message.split("#");

        if(input[0].equals("sendaction"))
        {
            setAction(message.substring("sendaction#".length()), true);
            

        }


        chat.tempAddMessage(message);
    }

    //kind of a hacky way to get a country's code but WHATEVER
    private int getCountryNumber(String name)
    {
        int i=-1;

        if(name!=null)
        {
            for(String country:countries)
            {
                if(name.equals(country))
                {
                    return i;
                }
                i++;
            }
        }

        return -1;
        
    }

    public void updateActionTables()
    {
        for(int i=0;i<SoutheastAsiaApp.MAX_PLAYERS;i++)
        {
            if(getCountry(i)!=-1)
            {
                jTable1.setValueAt(getCountry(i), i, 0);
                jTable1.setValueAt(getActionName(i), i, 1);
                jTable1.setValueAt(getActionApproved(i), i, 2);
                jTable1.setValueAt(getProblemName(i), i, 3);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;




    //OLD SOUTHEASTASIASERVER


    private SoutheastAsiaServerStats stats;
    private boolean gameStarted;
    private SoutheastAsiaServerSockets ss;
    //public SoutheastAsiaView window;

    private boolean[] allowActions; //set to true at the start of every turn;
    /*set to false after a player has sent an action that is neither
     * approved nor disapproved para bawal mag spam ng action
     *
     * set to true after disapproving
     */

    public void allowActions(int playerCode, boolean allowed)
    {
        allowActions[playerCode]=allowed;
    }

    /*
    public SoutheastAsiaServer()
    {
        try
        {
            ss = new SoutheastAsiaServerSockets();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
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
    public int startNewTurn(boolean override)
    {
        if(gameStarted)
        {



            if(stats.newTurn())
            {
                //game won! now check to see who won and in what category
            }


            for(int i=0;i<stats.countSelectedCountries();i++)
            {
                stats.setAction(new SoutheastAsiaAction(true), i);
                //problems?
                allowActions(i, true);

            }
        }
        return 0;
    }

    /**
     *
     * call this function whenever a chat message is recieved
     * from the players or the sent by the teacher
     *
     * adds the chat to the log in serverstats
     *
     * @param chat the chat message in some format (handle plz)
     * @return
     */
    public int recieveChatMessage(String chat)
    {
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
    public String getAction(int playerCode)
    {
        return stats.getAction(playerCode).toString();
    }

    public String getActionName(int playerCode)
    {
        return stats.getActionName(playerCode);
    }

    public boolean getActionApproved(int playerCode)
    {
        return stats.getActionApproved(playerCode);
    }

    public String getProblem(int playerCode)
    {
        return stats.getProblem(playerCode);
    }

    public String getProblemName(int playerCode)
    {
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
    public int chooseCountry(int playerCode, int countryCode, boolean override)
    {
        if(override)
        {
            stats.replaceAllCountryChoices(countryCode); //all players who have picked the country will be reset
            return stats.setCountry(playerCode, countryCode);
        }
        else
        {
            if(stats.countryIsAlreadySelected(countryCode))
            {
                return 0;
            }
            else
            {
                return stats.setCountry(playerCode, countryCode);
            }

        }
    }

    public int getCountry(int playerCode)
    {
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
    public int startGame(boolean override)
    {
        //put some code in here
        gameStarted=true;
        startNewTurn(true);
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
    public String generateTurnLog()
    {
        //stuff here
        return "";
    }

    /**
     * solves the problem belonging to a player
     * (called before the end of the turn)
     * @param playerCode
     * @return 1 if problem exists, 0 otherwise
     */
    public int solveProblem(int playerCode)
    {
        return stats.solveProblem(playerCode);

    }

    /**
     * gets the stats of a player
     * @param playerCode the player to query
     * @return the stats
     */
    public CountryVariables getStats(int playerCode)
    {
        return stats.getStats(playerCode);

    }

    public static SoutheastAsiaAction parseAction(String actionCode)
    {
        //change the delimiter here
        String[] newaction= actionCode.split("#");
        return new SoutheastAsiaAction(newaction[1],newaction[2],Integer.parseInt(newaction[3]),Integer.parseInt(newaction[4]),Integer.parseInt(newaction[5]),Integer.parseInt(newaction[6]));
        //cultural, economic, military, political
    }

    /**
     * call this method to send in actions
     * @param playerCode the player sending in an action
     * @param actionCode the action in encoded string format
     * @param override if set to false, will not change existing action
     * @return 1 if action set, 0 if there is an existing action, 2 if there is an existing action but was overridden
     */
    public int setAction(String actionCode, boolean override)
    {
        int playerCode; //do something to get playerCode from actionCode

        String parsedCode[]=actionCode.split("#");
        playerCode=Integer.parseInt(parsedCode[0]);

        if(allowActions[playerCode])
        {

            if(stats.hasAction(playerCode))
            {
                //if(override) not using override any more, instead allowActions
                if(true)
                {
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
        }
        else
        {
            //send a message back to the player saying that actions
            //not allowed yet
            sendClientMessage(playerCode, "warn:action not allowed");
            return 0;
        }
    }

    public static Problem generateProblem()
    {
        return Problem.noProblem();
    }

    public void accept()
    {
        ss.acceptSockets();
    }

    public int countPlayers()
    {
        return stats.countSelectedCountries();
    }

    public void setAction(SoutheastAsiaAction seact, int playerCode)
    {
        stats.setAction(seact, playerCode);
    }

    public SoutheastAsiaAction getActionData(int playerCode)
    {
        return stats.getActionData(playerCode);
    }

    public int getPlayerCode(int countryCode)
    {
        return stats.getPlayerCode(countryCode);
    }

    public int getPlayerCode(String countryCode)
    {
        return stats.getPlayerCode(Integer.parseInt(countryCode));
    }

    public void setApproval(boolean approval, int playerCode)
    {
        stats.setApproval(playerCode, approval);
    }


}

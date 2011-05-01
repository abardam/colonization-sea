/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatWindow.java
 *
 * Created on 08 21, 10, 9:05:49 AM
 */

package southeastasia;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Enzo
 */
public class ChatWindow extends javax.swing.JFrame {

    /** Creates new form ChatWindow */
    public static final int CLIENT=0;
    public static final int SERVER=1;
    private int type;
    private SoutheastAsiaClientApp client;
    private SoutheastAsiaView server;
    private HashMap<String, String> players;

    /*
    public ChatWindow() {
        initComponents();

    }*/

    public ChatWindow(SoutheastAsiaClientApp client)
    {
        initComponents();
        this.client=client;
        type=CLIENT;
        players=new HashMap<String,String>();
    }

    public ChatWindow(SoutheastAsiaView server)
    {
        initComponents();
        this.server=server;
        type=SERVER;
        players=new HashMap<String,String>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jTextArea2.setName("jTextArea2"); // NOI18N
        jScrollPane2.setViewportView(jTextArea2);

        jSplitPane1.setRightComponent(jScrollPane2);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getResourceMap(ChatWindow.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(jTextField1.getText().length()>0)
        {
            if(type==CLIENT)
            {
                client.sendMessage("chat#"+client.getStats(client.getClientCode()).name+"#"+client.getStats(client.getClientCode()).playerName+"#"+jTextField1.getText());
            }
                
            else if(type==SERVER)
            {
                server.sendMessage("chat#SERVER#"+jTextField1.getText());
                jTextArea2.append("SERVER: "+jTextField1.getText()+"\n");
            }

            jTextField1.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER)
        {
            jButton1ActionPerformed(null);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyPressed

    /**
    * @param args the command line arguments
    */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatWindow().setVisible(true);
            }
        });
    }*/

    public void addMessage(String message)
    {
        jTextArea2.append(message+"\n");
    }

    public void addPlayer(String player, String country)
    {
        for(String p:players.keySet())
        {
            if(p.equalsIgnoreCase(player))
            {
                players.put(p, country);
                return;
            }
        }

        players.put(player, country);
        updatePlayers();
    }

    public void updatePlayers()
    {
        jTextArea1.setText("");
        for(Entry<String,String> s:players.entrySet())
        {
            jTextArea1.append(s.getKey()+"-"+s.getValue()+"\n");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}

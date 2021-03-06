/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TerritoryViewerFrame.java
 *
 * Created on 02 9, 11, 3:19:30 PM
 */

package southeastasia;

import southeastasia.game.SoutheastAsiaAction;

/**
 *
 * @author Enzo
 */
public class TerritoryViewerFrame extends javax.swing.JFrame {

    /** Creates new form TerritoryViewerFrame */
    private String territoryName;
    private int territoryCode;
    private SoutheastAsiaClientApp app;
    public TerritoryViewerFrame(String territoryName, int territoryCode, SoutheastAsiaClientApp a) {
        initComponents();
        this.territoryName=territoryName;
        this.territoryCode=territoryCode;
        app=a;
        countryLabel.setText(territoryName);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        countryLabel = new javax.swing.JLabel();
        militaryButton = new javax.swing.JButton();
        economicButton = new javax.swing.JButton();
        missionaryButton = new javax.swing.JButton();
        invasionBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(southeastasia.SoutheastAsiaApp.class).getContext().getResourceMap(TerritoryViewerFrame.class);
        countryLabel.setText(resourceMap.getString("countryLabel.text")); // NOI18N
        countryLabel.setName("countryLabel"); // NOI18N

        militaryButton.setText(resourceMap.getString("militaryButton.text")); // NOI18N
        militaryButton.setName("militaryButton"); // NOI18N
        militaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                militaryButtonActionPerformed(evt);
            }
        });

        economicButton.setText(resourceMap.getString("economicButton.text")); // NOI18N
        economicButton.setName("economicButton"); // NOI18N
        economicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                economicButtonActionPerformed(evt);
            }
        });

        missionaryButton.setText(resourceMap.getString("missionaryButton.text")); // NOI18N
        missionaryButton.setName("missionaryButton"); // NOI18N
        missionaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missionaryButtonActionPerformed(evt);
            }
        });

        invasionBtn.setText(resourceMap.getString("invasionBtn.text")); // NOI18N
        invasionBtn.setName("invasionBtn"); // NOI18N
        invasionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invasionBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(countryLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(militaryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(economicButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(missionaryButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(invasionBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(countryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(militaryButton)
                    .addComponent(economicButton)
                    .addComponent(missionaryButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invasionBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void doLanding(SoutheastAsiaAction action)
    {
        action.name+=" ("+territoryName+")";
        action.landing=territoryCode;
        app.setActionText(action);
        app.setTabAtAction();
        app.setActionLock(false);
        app.lockAction();
        this.dispose();
    }
    private void militaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_militaryButtonActionPerformed

        String name="Military Landing";
        String desc="After sailing in the sea for weeks, you decide to land on this land with military to secure your landing. Your military presence will warn all exisiting rulers of your offensive capability. This step is a must to prepare your military for war. Your military have to be in close proximity of your enemy before you can launch an invasion.";
        int c=0;
        int e=-2;
        int m=5;
        int p=0;

        doLanding(new SoutheastAsiaAction(name, desc, c, e, m, p));
    }//GEN-LAST:event_militaryButtonActionPerformed

    private void economicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_economicButtonActionPerformed
        String name="Economic Landing";
        String desc="After sailing in the sea for weeks, you decide to land on this land in peace and seek out the existing rulers and negotiate for trade, allow your people to settle a part of this land or bribe them into submission so that you are the true governor of this land.";
        int c=0;
        int e=3;
        int m=0;
        int p=0;

                doLanding(new SoutheastAsiaAction(name, desc, c, e, m, p));

    }//GEN-LAST:event_economicButtonActionPerformed

    private void missionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missionaryButtonActionPerformed
        String name="Missionary Landing";
        String desc="After sailing in the sea for weeks, you decide to land on this land to promote your faith as the one true and only faith in the world.";
        int c=5;
        int e=-2;
        int m=0;
        int p=0;

        doLanding(new SoutheastAsiaAction(name, desc, c, e, m, p));
    }//GEN-LAST:event_missionaryButtonActionPerformed

    private void invasionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invasionBtnActionPerformed
        String name="Invasion";
        String desc="You must have at least 5E to execute an invasion. Results of the battle are based on your and the defending country's Military stat.";
        int c=0;
        int e=-5;
        int m=0;
        int p=0;

        SoutheastAsiaAction seact=new SoutheastAsiaAction(name, desc, c, e, m, p);
        seact.war=SoutheastAsiaAction.WAR_ATTACK;
        doLanding(seact);
    }//GEN-LAST:event_invasionBtnActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel countryLabel;
    private javax.swing.JButton economicButton;
    private javax.swing.JButton invasionBtn;
    private javax.swing.JButton militaryButton;
    private javax.swing.JButton missionaryButton;
    // End of variables declaration//GEN-END:variables

}

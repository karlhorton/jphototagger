/*
 * FlickrUpload - plugin for JPhotoTagger
 * Copyright (C) 2009 by Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.plugin.flickrupload;

import java.util.Properties;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2010-02-13
 */
public class SettingsPanel extends javax.swing.JPanel {

    private static final long       serialVersionUID = -7766362003081534388L;
    private              Settings   settings;
    private              Properties properties;

    public SettingsPanel() {
        initComponents();
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        settings = new Settings(properties);
        readProperties();
    }

    private void deleteToken() {
        assert properties != null;
        new Authorization(properties).deleteToken();
    }

    private void readProperties() {
        checkBoxDcDescription.setSelected(settings.isAddDcDescription());
        checkBoxDcSubjects.setSelected(settings.isAddDcSubjects());
        checkBoxPhotoshopHeadline.setSelected(settings.isAddPhotoshopHeadline());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        buttonDeleteToken = new javax.swing.JButton();
        panelXmp = new javax.swing.JPanel();
        checkBoxDcSubjects = new javax.swing.JCheckBox();
        checkBoxPhotoshopHeadline = new javax.swing.JCheckBox();
        checkBoxDcDescription = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/jpt/plugin/flickrupload/Bundle"); // NOI18N
        label.setText(bundle.getString("SettingsPanel.label.text")); // NOI18N

        buttonDeleteToken.setText(bundle.getString("SettingsPanel.buttonDeleteToken.text")); // NOI18N
        buttonDeleteToken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteTokenActionPerformed(evt);
            }
        });

        panelXmp.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SettingsPanel.panelXmp.border.title"))); // NOI18N

        checkBoxDcSubjects.setText(bundle.getString("SettingsPanel.checkBoxDcSubjects.text")); // NOI18N
        checkBoxDcSubjects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxDcSubjectsActionPerformed(evt);
            }
        });

        checkBoxPhotoshopHeadline.setText(bundle.getString("SettingsPanel.checkBoxPhotoshopHeadline.text")); // NOI18N
        checkBoxPhotoshopHeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxPhotoshopHeadlineActionPerformed(evt);
            }
        });

        checkBoxDcDescription.setText(bundle.getString("SettingsPanel.checkBoxDcDescription.text")); // NOI18N
        checkBoxDcDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxDcDescriptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelXmpLayout = new javax.swing.GroupLayout(panelXmp);
        panelXmp.setLayout(panelXmpLayout);
        panelXmpLayout.setHorizontalGroup(
            panelXmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelXmpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelXmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxDcSubjects)
                    .addComponent(checkBoxPhotoshopHeadline)
                    .addComponent(checkBoxDcDescription))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        panelXmpLayout.setVerticalGroup(
            panelXmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelXmpLayout.createSequentialGroup()
                .addComponent(checkBoxDcSubjects)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxPhotoshopHeadline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxDcDescription)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelXmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDeleteToken)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(buttonDeleteToken))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelXmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonDeleteTokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteTokenActionPerformed
        deleteToken();
    }//GEN-LAST:event_buttonDeleteTokenActionPerformed

    private void checkBoxDcSubjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxDcSubjectsActionPerformed
        if (settings != null) {
            settings.setAddDcSubjects(checkBoxDcSubjects.isSelected());
        }
    }//GEN-LAST:event_checkBoxDcSubjectsActionPerformed

    private void checkBoxPhotoshopHeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxPhotoshopHeadlineActionPerformed
        if (settings != null) {
            settings.setAddPhotoshopHeadline(checkBoxPhotoshopHeadline.isSelected());
        }
    }//GEN-LAST:event_checkBoxPhotoshopHeadlineActionPerformed

    private void checkBoxDcDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxDcDescriptionActionPerformed
        if (settings != null) {
            settings.setAddDcDescription(checkBoxDcDescription.isSelected());
        }
    }//GEN-LAST:event_checkBoxDcDescriptionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDeleteToken;
    private javax.swing.JCheckBox checkBoxDcDescription;
    private javax.swing.JCheckBox checkBoxDcSubjects;
    private javax.swing.JCheckBox checkBoxPhotoshopHeadline;
    private javax.swing.JLabel label;
    private javax.swing.JPanel panelXmp;
    // End of variables declaration//GEN-END:variables
}
package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.resource.Bundle;
import javax.swing.JComboBox;

/**
 * Panel for action sources (buttons) related to edit metadata.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public final class EditMetadataActionsPanel extends javax.swing.JPanel {

    /** Creates new form EditMetadataActionsPanel */
    public EditMetadataActionsPanel() {
        initComponents();
    }
    
    public JComboBox getComboBoxMetadataTemplates() {
        return comboBoxMetadataTemplates;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        panelGroupMetadataEdit = new javax.swing.JPanel();
        labelMetadataInfoEditable = new javax.swing.JLabel();
        buttonSaveMetadata = new javax.swing.JButton();
        buttonMetadataTemplateCreate = new javax.swing.JButton();
        buttonEmptyMetadata = new javax.swing.JButton();
        buttonMetadataTemplateInsert = new javax.swing.JButton();
        panelGroupMetadataTemplates = new javax.swing.JPanel();
        comboBoxMetadataTemplates = new javax.swing.JComboBox();
        buttonMetadataTemplateRename = new javax.swing.JButton();
        buttonMetadataTemplateUpdate = new javax.swing.JButton();
        buttonMetadataTemplateDelete = new javax.swing.JButton();

        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        labelMetadataInfoEditable.setText(Bundle.getString("EditMetadataActionsPanel.labelMetadataInfoEditable.text")); // NOI18N

        buttonSaveMetadata.setMnemonic('s');
        buttonSaveMetadata.setText(Bundle.getString("EditMetadataActionsPanel.buttonSaveMetadata.text")); // NOI18N
        buttonSaveMetadata.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonSaveMetadata.toolTipText")); // NOI18N
        buttonSaveMetadata.setEnabled(false);

        buttonMetadataTemplateCreate.setMnemonic('v');
        buttonMetadataTemplateCreate.setText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateCreate.text")); // NOI18N
        buttonMetadataTemplateCreate.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateCreate.toolTipText")); // NOI18N

        buttonEmptyMetadata.setMnemonic('l');
        buttonEmptyMetadata.setText(Bundle.getString("EditMetadataActionsPanel.buttonEmptyMetadata.text")); // NOI18N
        buttonEmptyMetadata.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonEmptyMetadata.toolTipText")); // NOI18N
        buttonEmptyMetadata.setEnabled(false);

        buttonMetadataTemplateInsert.setMnemonic('e');
        buttonMetadataTemplateInsert.setText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateInsert.text")); // NOI18N
        buttonMetadataTemplateInsert.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateInsert.toolTipText")); // NOI18N
        buttonMetadataTemplateInsert.setEnabled(false);

        javax.swing.GroupLayout panelGroupMetadataEditLayout = new javax.swing.GroupLayout(panelGroupMetadataEdit);
        panelGroupMetadataEdit.setLayout(panelGroupMetadataEditLayout);
        panelGroupMetadataEditLayout.setHorizontalGroup(
            panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                        .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonSaveMetadata)
                            .addComponent(buttonEmptyMetadata))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonMetadataTemplateInsert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonMetadataTemplateCreate, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addComponent(labelMetadataInfoEditable, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelGroupMetadataEditLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonMetadataTemplateCreate, buttonMetadataTemplateInsert});

        panelGroupMetadataEditLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonEmptyMetadata, buttonSaveMetadata});

        panelGroupMetadataEditLayout.setVerticalGroup(
            panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(labelMetadataInfoEditable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSaveMetadata)
                    .addComponent(buttonMetadataTemplateCreate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonMetadataTemplateInsert)
                    .addComponent(buttonEmptyMetadata))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tabbedPane.addTab(Bundle.getString("EditMetadataActionsPanel.panelGroupMetadataEdit.TabConstraints.tabTitle"), panelGroupMetadataEdit); // NOI18N

        comboBoxMetadataTemplates.setToolTipText(Bundle.getString("EditMetadataActionsPanel.comboBoxMetadataTemplates.toolTipText")); // NOI18N

        buttonMetadataTemplateRename.setMnemonic('m');
        buttonMetadataTemplateRename.setText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateRename.text")); // NOI18N
        buttonMetadataTemplateRename.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateRename.toolTipText")); // NOI18N
        buttonMetadataTemplateRename.setEnabled(false);

        buttonMetadataTemplateUpdate.setMnemonic('a');
        buttonMetadataTemplateUpdate.setText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateUpdate.text")); // NOI18N
        buttonMetadataTemplateUpdate.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateUpdate.toolTipText")); // NOI18N
        buttonMetadataTemplateUpdate.setEnabled(false);

        buttonMetadataTemplateDelete.setMnemonic('\u00f6');
        buttonMetadataTemplateDelete.setText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateDelete.text")); // NOI18N
        buttonMetadataTemplateDelete.setToolTipText(Bundle.getString("EditMetadataActionsPanel.buttonMetadataTemplateDelete.toolTipText")); // NOI18N
        buttonMetadataTemplateDelete.setEnabled(false);

        javax.swing.GroupLayout panelGroupMetadataTemplatesLayout = new javax.swing.GroupLayout(panelGroupMetadataTemplates);
        panelGroupMetadataTemplates.setLayout(panelGroupMetadataTemplatesLayout);
        panelGroupMetadataTemplatesLayout.setHorizontalGroup(
            panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxMetadataTemplates, 0, 236, Short.MAX_VALUE)
                    .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                        .addComponent(buttonMetadataTemplateRename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMetadataTemplateUpdate))
                    .addComponent(buttonMetadataTemplateDelete))
                .addContainerGap())
        );

        panelGroupMetadataTemplatesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonMetadataTemplateDelete, buttonMetadataTemplateRename, buttonMetadataTemplateUpdate});

        panelGroupMetadataTemplatesLayout.setVerticalGroup(
            panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBoxMetadataTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonMetadataTemplateRename)
                    .addComponent(buttonMetadataTemplateUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonMetadataTemplateDelete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(Bundle.getString("EditMetadataActionsPanel.panelGroupMetadataTemplates.TabConstraints.tabTitle"), panelGroupMetadataTemplates); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton buttonEmptyMetadata;
    public javax.swing.JButton buttonMetadataTemplateCreate;
    public javax.swing.JButton buttonMetadataTemplateDelete;
    public javax.swing.JButton buttonMetadataTemplateInsert;
    public javax.swing.JButton buttonMetadataTemplateRename;
    public javax.swing.JButton buttonMetadataTemplateUpdate;
    public javax.swing.JButton buttonSaveMetadata;
    public javax.swing.JComboBox comboBoxMetadataTemplates;
    public javax.swing.JLabel labelMetadataInfoEditable;
    private javax.swing.JPanel panelGroupMetadataEdit;
    private javax.swing.JPanel panelGroupMetadataTemplates;
    public javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

}

package org.jphototagger.program.module.editmetadata;

/**
 * @author Elmar Baumann
 */
class EditMetaDataPanelsWrapperPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private transient EditMetaDataPanels editMetadtaPanels;

    EditMetaDataPanelsWrapperPanel() {
        initComponents();
        editMetadtaPanels = new EditMetaDataPanels(panelEditMetadata);
    }

    EditMetaDataPanels getEditMetadtaPanels() {
        return editMetadtaPanels;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrollPaneEditMetadata = new javax.swing.JScrollPane();
        panelEditMetadata = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout panelEditMetadataLayout = new javax.swing.GroupLayout(panelEditMetadata);
        panelEditMetadata.setLayout(panelEditMetadataLayout);
        panelEditMetadataLayout.setHorizontalGroup(
            panelEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 923, Short.MAX_VALUE)
        );
        panelEditMetadataLayout.setVerticalGroup(
            panelEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );

        scrollPaneEditMetadata.setViewportView(panelEditMetadata);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrollPaneEditMetadata, gridBagConstraints);
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelEditMetadata;
    private javax.swing.JScrollPane scrollPaneEditMetadata;
    // End of variables declaration//GEN-END:variables
}
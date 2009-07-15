package de.elmar_baumann.imv.view.dialogs;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.app.AppIcons;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel;
import de.elmar_baumann.lib.dialog.Dialog;

/**
 * Dialog holding a {@link HierarchicalKeywordsPanel}.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2007/07/10
 */
public class HierarchicalKeywordsDialog extends Dialog {

    public static final HierarchicalKeywordsDialog INSTANCE =
            new HierarchicalKeywordsDialog();

    public HierarchicalKeywordsDialog() {
        super((java.awt.Frame) null, false);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setIconImages(AppIcons.getAppIcons());
        setHelpContentsUrl(Bundle.getString("Help.Url.Contents")); // NOI18N
        setHelpPageUrl(Bundle.getString("Help.Url.HierarchicalKeywords"));
        registerKeyStrokes();
    }

    private void readProperties() {
        UserSettings.INSTANCE.getSettings().getSizeAndLocation(this);
        panelHierarchicalKeywords.readProperties();
    }

    private void writeProperties() {
        UserSettings.INSTANCE.getSettings().setSizeAndLocation(this);
        UserSettings.INSTANCE.writeToFile();
        panelHierarchicalKeywords.writeProperties();
    }

    public HierarchicalKeywordsPanel getPanel() {
        return panelHierarchicalKeywords;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            readProperties();
        } else {
            writeProperties();
        }
        super.setVisible(visible);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHierarchicalKeywords = new de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Bundle.getString("HierarchicalKeywordsDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHierarchicalKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHierarchicalKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                HierarchicalKeywordsDialog dialog =
                        new HierarchicalKeywordsDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel panelHierarchicalKeywords;
    // End of variables declaration//GEN-END:variables
}

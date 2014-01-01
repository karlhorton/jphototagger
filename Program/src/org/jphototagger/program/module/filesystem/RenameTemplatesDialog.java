package org.jphototagger.program.module.filesystem;

import java.awt.Frame;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public class RenameTemplatesDialog extends Dialog {

    private static final long serialVersionUID = 1L;

    public RenameTemplatesDialog() {
        this(ComponentUtil.findFrameWithIcon());
    }

    public RenameTemplatesDialog(Frame parentFrame) {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPageUrl(Bundle.getString(RenameTemplatesDialog.class, "RenameTemplatesDialog.HelpPage"));
    }

    private void dialogCloses() {
        panelRenameTemplates.checkDirty();
    }

    @Override
    protected void escape() {
        dialogCloses();
        super.escape();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panelRenameTemplates = new org.jphototagger.program.module.filesystem.RenameTemplatesPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/filesystem/Bundle"); // NOI18N
        setTitle(bundle.getString("RenameTemplatesDialog.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        getContentPane().add(panelRenameTemplates, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dialogCloses();
    }//GEN-LAST:event_formWindowClosing

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                RenameTemplatesDialog dialog = new RenameTemplatesDialog();
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
    private org.jphototagger.program.module.filesystem.RenameTemplatesPanel panelRenameTemplates;
    // End of variables declaration//GEN-END:variables
}

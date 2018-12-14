package org.jphototagger.maintainance;

import javax.swing.SwingUtilities;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public final class UpdateMetadataOfDirectoriesDialog extends Dialog {

    public static final UpdateMetadataOfDirectoriesDialog INSTANCE = new UpdateMetadataOfDirectoriesDialog();
    private static final long serialVersionUID = 1L;

    private UpdateMetadataOfDirectoriesDialog() {
        super(ComponentUtil.findFrameWithIcon(), false);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
        AnnotationProcessor.process(this);
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(UpdateMetadataOfDirectoriesDialog.class, "UpdateMetadataOfDirectoriesDialog.HelpPage"));
    }

    private void endDialog() {
        panel.willDispose();
        setVisible(false);
    }

    @Override
    protected void escape() {
        endDialog();
    }

    @EventSubscriber(eventClass = LookAndFeelChangedEvent.class)
    public void lookAndFeelChanged(LookAndFeelChangedEvent evt) {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panel = new org.jphototagger.maintainance.UpdateMetadataOfDirectoriesPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/maintainance/Bundle"); // NOI18N
        setTitle(Bundle.getString(getClass(), "UpdateMetadataOfDirectoriesDialog.title")); // NOI18N
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panel.setName("panel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = org.jphototagger.resources.UiFactory.insets(7, 7, 7, 7);
        getContentPane().add(panel, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        endDialog();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jphototagger.maintainance.UpdateMetadataOfDirectoriesPanel panel;
    // End of variables declaration//GEN-END:variables
}

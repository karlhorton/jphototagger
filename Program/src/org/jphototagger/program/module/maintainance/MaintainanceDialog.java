package org.jphototagger.program.module.maintainance;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import org.jphototagger.lib.dialog.Dialog;
import org.jphototagger.program.resource.GUI;

/**
 *
 *
 * @author Elmar Baumann
 */
public class MaintainanceDialog extends Dialog {

    private static final long serialVersionUID = 1L;

    public MaintainanceDialog() {
        super(GUI.getAppFrame(), true);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        GridBagConstraints gridBagConstraints; // NOI18N

        tabbedPane = new JTabbedPane();
        panelMaintainanceCaches = new MaintainanceCachesPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("org/jphototagger/program/module/maintainance/Bundle");
        setTitle(bundle.getString("MaintainanceDialog.title")); // NOI18N
        setIconImage(null);
        setName("MaintainanceDialog"); // NOI18N
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new GridBagLayout());

        tabbedPane.setName("tabbedPane"); // NOI18N

        panelMaintainanceCaches.setName("panelMaintainanceCaches"); // NOI18N
        tabbedPane.addTab(bundle.getString("MaintainanceDialog.panelMaintainanceCaches.TabConstraints.tabTitle"), panelMaintainanceCaches); // NOI18N

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(tabbedPane, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        super.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MaintainanceDialog dialog = new MaintainanceDialog();
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
    private MaintainanceCachesPanel panelMaintainanceCaches;
    private JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
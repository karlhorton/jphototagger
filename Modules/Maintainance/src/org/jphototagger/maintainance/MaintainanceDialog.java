package org.jphototagger.maintainance;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public class MaintainanceDialog extends Dialog {

    private static final long serialVersionUID = 1L;

    public MaintainanceDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        GridBagConstraints gridBagConstraints; 

        tabbedPane = new JTabbedPane();
        panelMaintainanceCaches = new MaintainanceCachesPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("org/jphototagger/maintainance/Bundle"); // NOI18N
        setTitle(Bundle.getString(getClass(), "MaintainanceDialog.title")); // NOI18N
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
        tabbedPane.addTab(Bundle.getString(getClass(), "MaintainanceDialog.panelMaintainanceCaches.TabConstraints.tabTitle"), panelMaintainanceCaches); // NOI18N

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = org.jphototagger.resources.UiFactory.insets(10, 10, 10, 10);
        getContentPane().add(tabbedPane, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        super.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private MaintainanceCachesPanel panelMaintainanceCaches;
    private JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}

package org.jphototagger.program.module.thumbnails;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jphototagger.domain.thumbnails.ThumbnailCreator;
import org.jphototagger.lib.api.PositionProviderAscendingComparator;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class ThumbnailCreatorSettingsDialog extends Dialog {

    private static final long serialVersionUID = 1L;

    public ThumbnailCreatorSettingsDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        List<ThumbnailCreator> creators = new ArrayList<ThumbnailCreator>(Lookup.getDefault().lookupAll(ThumbnailCreator.class));
        Collections.sort(creators, PositionProviderAscendingComparator.INSTANCE);
        for (ThumbnailCreator creator : creators) {
            Component settingsComponent = creator.getSettingsComponent();
            if (settingsComponent != null) {
                tabbedPane.add(creator.getDisplayName(), settingsComponent);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Bundle.getString(getClass(), "ThumbnailCreatorSettingsDialog.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabbedPane.setPreferredSize(org.jphototagger.resources.UiFactory.dimension(300, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = org.jphototagger.resources.UiFactory.insets(7, 7, 7, 7);
        getContentPane().add(tabbedPane, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}

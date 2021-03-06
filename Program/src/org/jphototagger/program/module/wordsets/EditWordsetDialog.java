package org.jphototagger.program.module.wordsets;

import org.jphototagger.domain.wordsets.Wordset;
import org.jphototagger.lib.swing.DialogExt;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.UiFactory;

/**
 * @author Elmar Baumann
 */
public class EditWordsetDialog extends DialogExt {

    private static final long serialVersionUID = 1L;
    private final Wordset wordset;
    private boolean selectNameTextfield;

    public EditWordsetDialog() {
        this(new Wordset(Bundle.getString(EditWordsetDialog.class, "EditWordset.DefaultWordsetName")));
        selectNameTextfield = true;
    }

    public EditWordsetDialog(Wordset wordset) {
        super(ComponentUtil.findFrameWithIcon(), true);
        this.wordset = wordset;
        initComponents();
    }

    private void checkDirty() {
        if (panelEditWordset.isDirty() && MessageDisplayer.confirmYesNo(this,
                Bundle.getString(EditWordsetDialog.class, "EditWordsetDialog.Confirm.Save"))) {
            panelEditWordset.saveWordset();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panelEditWordset.enableAutocomplete();
            if (selectNameTextfield) {
                panelEditWordset.selectNameTextField();
            } else {
                panelEditWordset.selectWordTextField();
            }
        }
        super.setVisible(visible);
    }

    @Override
    protected void escape() {
        checkDirty();
        super.escape();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelEditWordset = new EditWordsetPanel(wordset);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Bundle.getString(getClass(), "EditWordsetDialog.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelEditWordset.setPreferredSize(UiFactory.dimension(300, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 10, 10);
        getContentPane().add(panelEditWordset, gridBagConstraints);

        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        checkDirty();
    }

    private org.jphototagger.program.module.wordsets.EditWordsetPanel panelEditWordset;
}

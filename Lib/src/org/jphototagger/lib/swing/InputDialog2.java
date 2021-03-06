package org.jphototagger.lib.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.UiFactory;

/**
 * Input dialog for arbitrary Components.
 *
 * @author Elmar Baumann
 */
public class InputDialog2 extends DialogExt {

    private static final long serialVersionUID = 1L;
    private boolean accepted;

    public InputDialog2(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    public InputDialog2() {
        init();
    }

    private void init() {
        setPersistSizeAndLocation(false);
        initComponents();
        buttonOk.setAction(closeAction);
        buttonCancel.setAction(cancelAction);
    }

    /**
     * Sets the component containing the input controls.
     *
     * @param component Component
     */
    public void setComponent(Component component) {
        Objects.requireNonNull(component, "component == null");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        panelComponent.add(component, gbc);
    }

    public void setShowCancelButton(boolean show) {
        buttonCancel.setVisible(show);
    }

    public void setShowOkButton(boolean show) {
        buttonOk.setVisible(show);
    }

    public void setCancelButtonText(String text) {
        buttonCancel.setText(text);
    }

    public void setOkButtonText(String text) {
        buttonOk.setText(text);
    }

    /**
     * @return true if the OK button was clicked by it's default action, which
     *         closes this dialog
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Returns the cancel button which closes the dialog by default and sets
     * {@link #isAccepted()} to false. The action can be replaced by set an
     * other action to this button.
     *
     * @return Cancel button
     */
    public JButton getButtonCancel() {
        return buttonCancel;
    }

    /**
     * Returns the OK button which closes the dialog by default and sets
     * {@link #isAccepted()} to true. The action can be replaced by set an other
     * action to this button.
     *
     * @return OK button.
     */
    public JButton getButtonOk() {
        return buttonOk;
    }

    private final Action closeAction = new AbstractAction(Bundle.getString(InputDialog2.class, "InputDialog.CloseAction.Name")) {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            accepted = true;
            setVisible(false);
        }
    };

    private final Action cancelAction = new AbstractAction(Bundle.getString(InputDialog2.class, "InputDialog.CancelAction.Name")) {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            accepted = false;
            setVisible(false);
        }
    };

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelContents = UiFactory.panel();
        panelComponent = UiFactory.panel();
        panelButtons = UiFactory.panel();
        buttonOk = UiFactory.button();
        buttonCancel = UiFactory.button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelContents.setLayout(new java.awt.GridBagLayout());

        panelComponent.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelContents.add(panelComponent, gridBagConstraints);

        panelButtons.setLayout(new java.awt.GridBagLayout());

        buttonOk.setText(Bundle.getString(getClass(), "InputDialog2.buttonOk.text")); // NOI18N
        panelButtons.add(buttonOk, new java.awt.GridBagConstraints());

        buttonCancel.setText(Bundle.getString(getClass(), "InputDialog2.buttonCancel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelButtons.add(buttonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelContents.add(panelButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = UiFactory.insets(7, 7, 7, 7);
        getContentPane().add(panelContents, gridBagConstraints);

        pack();
    }

    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelComponent;
    private javax.swing.JPanel panelContents;
}

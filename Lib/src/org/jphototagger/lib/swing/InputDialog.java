package org.jphototagger.lib.swing;

import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import org.jphototagger.lib.swing.util.ComponentUtil;

/**
 * Modal text input dialog writing it's location to a properties object on demand.
 *
 * @author Elmar Baumann
 */
public final class InputDialog extends Dialog {

    private static final long  serialVersionUID = 1L;
    private boolean accepted;

    public InputDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
    }

    public InputDialog(JDialog owner) {
        super(owner, true);
        initComponents();
    }

    public InputDialog(String info, String input) {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
    }

    public InputDialog(JDialog owner, String info, String input) {
        super(owner, true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
    }

    public void setInfo(String info) {
        if (info == null) {
            throw new NullPointerException("info == null");
        }
        labelPrompt.setText(info);
    }

    public void setInput(String input) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        textFieldInput.setText(input);
    }

    /**
     *
     * @return true if closed with OK
     */
    public boolean isAccepted() {
        return accepted;
    }

    public String getInput() {
        return textFieldInput.getText();
    }

    @Override
    protected void escape() {
        accepted = false;
        setVisible(false);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        labelPrompt = new javax.swing.JLabel();
        textFieldInput = new javax.swing.JTextField();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/lib/swing/Bundle"); // NOI18N
        setTitle(bundle.getString("InputDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        labelPrompt.setText(bundle.getString("InputDialog.labelPrompt.text")); // NOI18N
        labelPrompt.setName("labelPrompt"); // NOI18N

        textFieldInput.setName("textFieldInput"); // NOI18N
        textFieldInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldInputKeyPressed(evt);
            }
        });

        buttonCancel.setMnemonic('a');
        buttonCancel.setText(bundle.getString("InputDialog.buttonCancel.text")); // NOI18N
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOk.setMnemonic('o');
        buttonOk.setText(bundle.getString("InputDialog.buttonOk.text")); // NOI18N
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldInput, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(labelPrompt)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOk)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPrompt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        accepted = true;
        setVisible(false);
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        accepted = false;
        setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void textFieldInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            accepted = true;
            setVisible(false);
        }
    }//GEN-LAST:event_textFieldInputKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                InputDialog dialog = new InputDialog();

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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JLabel labelPrompt;
    private javax.swing.JTextField textFieldInput;
    // End of variables declaration//GEN-END:variables
}

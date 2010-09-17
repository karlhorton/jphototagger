/*
 * @(#)EditUserDefinedFileFilterDialog.java    Created on 2010-03-30
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.view.dialogs;

import java.awt.Container;
import org.jphototagger.lib.beansbinding.MaxLengthValidator;
import org.jphototagger.lib.componentutil.MnemonicUtil;
import org.jphototagger.lib.dialog.Dialog;
import org.jphototagger.program.UserSettings;
import org.jphototagger.program.app.MessageDisplayer;
import org.jphototagger.program.data.UserDefinedFileFilter;
import org.jphototagger.program.database.DatabaseUserDefinedFileFilters;
import org.jphototagger.program.model.ComboBoxModelUserDefinedFileFilterType;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.resource.JptBundle;
import org.jphototagger.program.view.renderer.ListCellRendererUserDefinedFileFilterType;

/**
 *
 *
 * @author Elmar Baumann
 */
public class EditUserDefinedFileFilterDialog extends Dialog {
    private static final long serialVersionUID = 9122178822987764160L;
    private boolean accepted;
    private final UserDefinedFileFilter udf = new UserDefinedFileFilter();

    public EditUserDefinedFileFilterDialog() {
        super(GUI.getAppFrame(), true,
                UserSettings.INSTANCE.getSettings(), null);
        initComponents();
        postInitComponents();
    }

    public EditUserDefinedFileFilterDialog(UserDefinedFileFilter filter) {
        super(GUI.getAppFrame(), true,
                UserSettings.INSTANCE.getSettings(), null);
        if (filter == null) {
            throw new NullPointerException("filter == null");
        }

        udf.set(filter);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpContentsUrl(JptBundle.INSTANCE.getString("Help.Url.Contents"));
        setHelpPageUrl(JptBundle.INSTANCE.getString(
            "Help.Url.UserDefinedFileFilter.Edit"));
        MnemonicUtil.setMnemonics((Container) this);
    }

    public UserDefinedFileFilter getFilter() {
        return new UserDefinedFileFilter(filter);
    }

    public boolean isAccepted() {
        return accepted;
    }

    private void handleOkButtonClicked() {
        if (filter.isValid()) {
            if (checkName(textFieldName.getText())) {
                accepted = true;
                setVisible(false);
            }
        } else {
            MessageDisplayer.error(this,
                    "EditUserDefinedFileFilterDialog.Error.Valid");
        }
    }

    private boolean checkName(String name) {
        if (DatabaseUserDefinedFileFilters.INSTANCE.exists(name)) {
            MessageDisplayer.error(this,
                    "EditUserDefinedFileFilterDialog.Error.NameExists", name);
            return false;
        }
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        filter = udf;
        labelName = new javax.swing.JLabel();
        textFieldName = new javax.swing.JTextField();
        comboBoxType = new javax.swing.JComboBox();
        textFieldExpression = new javax.swing.JTextField();
        checkBoxNot = new javax.swing.JCheckBox();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(JptBundle.INSTANCE.getString("EditUserDefinedFileFilterDialog.title")); // NOI18N

        labelName.setLabelFor(textFieldName);
        labelName.setText(JptBundle.INSTANCE.getString("EditUserDefinedFileFilterDialog.labelName.text")); // NOI18N

        textFieldName.setColumns(45);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${name}"), textFieldName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setValidator(new MaxLengthValidator(45));
        bindingGroup.addBinding(binding);

        comboBoxType.setModel(new ComboBoxModelUserDefinedFileFilterType());
        comboBoxType.setRenderer(new ListCellRendererUserDefinedFileFilterType());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${type}"), comboBoxType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${expression}"), textFieldExpression, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setValidator(new MaxLengthValidator(128));
        bindingGroup.addBinding(binding);

        checkBoxNot.setText(JptBundle.INSTANCE.getString("EditUserDefinedFileFilterDialog.checkBoxNot.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${isNot}"), checkBoxNot, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        buttonCancel.setText(JptBundle.INSTANCE.getString("EditUserDefinedFileFilterDialog.buttonCancel.text")); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOk.setText(JptBundle.INSTANCE.getString("EditUserDefinedFileFilterDialog.buttonOk.text")); // NOI18N
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldExpression, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkBoxNot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOk))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldExpression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxNot))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonOk)
                            .addComponent(buttonCancel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        handleOkButtonClicked();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        accepted = false;
        setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                EditUserDefinedFileFilterDialog dialog = new EditUserDefinedFileFilterDialog();
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
    private javax.swing.JCheckBox checkBoxNot;
    private javax.swing.JComboBox comboBoxType;
    private org.jphototagger.program.data.UserDefinedFileFilter filter;
    private javax.swing.JLabel labelName;
    private javax.swing.JTextField textFieldExpression;
    private javax.swing.JTextField textFieldName;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}

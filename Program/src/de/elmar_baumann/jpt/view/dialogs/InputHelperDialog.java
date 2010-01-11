/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.view.dialogs;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.app.AppLookAndFeel;
import de.elmar_baumann.jpt.datatransfer.TransferHandlerDragListItems;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.view.panels.KeywordsPanel;
import de.elmar_baumann.jpt.view.panels.MetaDataTemplatesPanel;
import de.elmar_baumann.lib.dialog.Dialog;
import de.elmar_baumann.lib.util.Settings;
import javax.swing.ListModel;

/**
 * Dialog for input assistance.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2007-07-10
 */
public class InputHelperDialog extends Dialog {

    public static final  InputHelperDialog INSTANCE                  = new InputHelperDialog();
    private static final String            KEY_SEL_INDEX_TABBED_PANE = "InputHelperDialog.SelIndexTabbedPane";
    private static final long              serialVersionUID          = 38960516048549937L;

    public InputHelperDialog() {
        super((java.awt.Frame) null, false);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setIconImages(AppLookAndFeel.getAppIcons());
        setHelpContentsUrl(Bundle.getString("Help.Url.Contents"));
        setHelpPageUrl(Bundle.getString("Help.Url.InputHelpers"));
        registerKeyStrokes();
        panelKeywords.setKeyCard("InputHelperDialog.Keywords.Card");
        panelKeywords.setKeyTree("InputHelperDialog.Keywords.Tree");
    }

    public void setModelKeywords(ListModel model) {
        panelKeywords.getList().setModel(model);
    }

    private void readProperties() {
        Settings settings = UserSettings.INSTANCE.getSettings();
        panelKeywords.readProperties();
        int selIndexTabbedPane = settings.getInt(KEY_SEL_INDEX_TABBED_PANE);
        if (selIndexTabbedPane >= 0 && selIndexTabbedPane < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(selIndexTabbedPane);
        }
        settings.getSizeAndLocation(this);
    }

    private void writeProperties() {
        Settings settings = UserSettings.INSTANCE.getSettings();
        settings.setSizeAndLocation(this);
        settings.setInt(tabbedPane.getSelectedIndex(), KEY_SEL_INDEX_TABBED_PANE);
        panelKeywords.writeProperties();
        UserSettings.INSTANCE.writeToFile();
    }

    public KeywordsPanel getPanelKeywords() {
        return panelKeywords;
    }

    public MetaDataTemplatesPanel getPanelMetaDataTemplates() {
        return panelMetaDataTemplates;
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

        tabbedPane = new javax.swing.JTabbedPane();
        panelKeywords = new de.elmar_baumann.jpt.view.panels.KeywordsPanel();
        panelMetaDataTemplates = new de.elmar_baumann.jpt.view.panels.MetaDataTemplatesPanel();
        panelMetaDataTemplates.getList().setTransferHandler(new TransferHandlerDragListItems(de.elmar_baumann.jpt.datatransfer.Flavor.METADATA_TEMPLATES));
        labelInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Bundle.getString("InputHelperDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addTab(Bundle.getString("InputHelperDialog.panelKeywords.TabConstraints.tabTitle"), panelKeywords); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/jpt/resource/properties/Bundle"); // NOI18N
        tabbedPane.addTab(bundle.getString("InputHelperDialog.panelMetaDataTemplates.TabConstraints.tabTitle"), panelMetaDataTemplates); // NOI18N

        labelInfo.setText(bundle.getString("InputHelperDialog.labelInfo.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(labelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                InputHelperDialog dialog =
                        new InputHelperDialog();
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
    private javax.swing.JLabel labelInfo;
    private de.elmar_baumann.jpt.view.panels.KeywordsPanel panelKeywords;
    private de.elmar_baumann.jpt.view.panels.MetaDataTemplatesPanel panelMetaDataTemplates;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}

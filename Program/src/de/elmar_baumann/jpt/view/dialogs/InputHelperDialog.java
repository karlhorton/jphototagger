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
import de.elmar_baumann.jpt.datatransfer.Flavors;
import de.elmar_baumann.jpt.datatransfer.TransferHandlerDragListItems;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.view.panels.HierarchicalKeywordsPanel;
import de.elmar_baumann.jpt.view.panels.MetaDataEditTemplatesPanel;
import de.elmar_baumann.jpt.view.renderer.ListCellRendererKeywords;
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

    public static final InputHelperDialog INSTANCE =
            new InputHelperDialog();
    private static final String KEY_SEL_INDEX_TABBED_PANE =
            "InputHelperDialog.SelIndexTabbedPane";

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
    }

    public void setModelKeywords(ListModel model) {
        listKeywords.setModel(model);
    }

    private void readProperties() {
        Settings settings = UserSettings.INSTANCE.getSettings();
        panelHierarchicalKeywords.readProperties();
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
        panelHierarchicalKeywords.writeProperties();
        UserSettings.INSTANCE.writeToFile();
    }

    public HierarchicalKeywordsPanel getPanelKeywords() {
        return panelHierarchicalKeywords;
    }

    public MetaDataEditTemplatesPanel getPanelMetaDataEditTemplates() {
        return panelMetaDataEditTemplates;
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
        panelHierarchicalKeywords = new de.elmar_baumann.jpt.view.panels.HierarchicalKeywordsPanel();
        panelKeywords = new javax.swing.JPanel();
        scrollPaneKeywords = new javax.swing.JScrollPane();
        listKeywords = new javax.swing.JList();
        panelMetaDataEditTemplates = new de.elmar_baumann.jpt.view.panels.MetaDataEditTemplatesPanel();
        panelMetaDataEditTemplates.getList().setTransferHandler(new TransferHandlerDragListItems(Flavors.METADATA_EDIT_TEMPLATES));
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
        tabbedPane.addTab(Bundle.getString("InputHelperDialog.panelHierarchicalKeywords.TabConstraints.tabTitle"), panelHierarchicalKeywords); // NOI18N

        listKeywords.setToolTipText(Bundle.getString("InputHelperDialog.listKeywords.toolTipText")); // NOI18N
        listKeywords.setCellRenderer(new ListCellRendererKeywords());
        listKeywords.setDragEnabled(true);
        scrollPaneKeywords.setViewportView(listKeywords);
        listKeywords.setTransferHandler(new TransferHandlerDragListItems(Flavors.KEYWORDS_FLAVOR));

        javax.swing.GroupLayout panelKeywordsLayout = new javax.swing.GroupLayout(panelKeywords);
        panelKeywords.setLayout(panelKeywordsLayout);
        panelKeywordsLayout.setHorizontalGroup(
            panelKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
        );
        panelKeywordsLayout.setVerticalGroup(
            panelKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
        );

        tabbedPane.addTab(Bundle.getString("InputHelperDialog.panelKeywords.TabConstraints.tabTitle"), panelKeywords); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/jpt/resource/properties/Bundle"); // NOI18N
        tabbedPane.addTab(bundle.getString("InputHelperDialog.panelMetaDataEditTemplates.TabConstraints.tabTitle"), panelMetaDataEditTemplates); // NOI18N

        labelInfo.setText(bundle.getString("InputHelperDialog.labelInfo.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(labelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
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
    private javax.swing.JList listKeywords;
    private de.elmar_baumann.jpt.view.panels.HierarchicalKeywordsPanel panelHierarchicalKeywords;
    private javax.swing.JPanel panelKeywords;
    private de.elmar_baumann.jpt.view.panels.MetaDataEditTemplatesPanel panelMetaDataEditTemplates;
    private javax.swing.JScrollPane scrollPaneKeywords;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}

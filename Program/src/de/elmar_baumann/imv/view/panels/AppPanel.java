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
package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.app.AppInit;
import de.elmar_baumann.imv.app.AppLog;
import de.elmar_baumann.imv.app.AppTexts;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerAddHierarchicalKeyword;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerAddHierarchicalKeywordsToEditPanel;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerCutPasteHierarchicalKeyword;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerRemoveHierarchicalKeywordFromEditPanel;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerRemoveHierarchicalKeyword;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerRenameHierarchicalKeyword;
import de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerToggleRealHierarchicalKeyword;
import de.elmar_baumann.imv.datatransfer.TransferHandlerListKeywords;
import de.elmar_baumann.imv.event.listener.AppExitListener;
import de.elmar_baumann.imv.model.ComboBoxModelFastSearch;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.ViewUtil;
import de.elmar_baumann.imv.view.renderer.ListCellRendererCategories;
import de.elmar_baumann.imv.view.renderer.ListCellRendererFastSearchColumns;
import de.elmar_baumann.imv.view.renderer.ListCellRendererImageCollections;
import de.elmar_baumann.imv.view.renderer.ListCellRendererKeywords;
import de.elmar_baumann.imv.view.renderer.ListCellRendererSavedSearches;
import de.elmar_baumann.imv.view.renderer.TreeCellRendererHierarchicalKeywords;
import de.elmar_baumann.imv.view.renderer.TreeCellRendererMiscMetadata;
import de.elmar_baumann.imv.view.renderer.TreeCellRendererTimeline;
import de.elmar_baumann.lib.event.listener.TableButtonMouseListener;
import de.elmar_baumann.lib.util.Settings;
import de.elmar_baumann.lib.util.SettingsHints;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.tree.TreeSelectionModel;

/**
 * Panel der Anwendung.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>, Tobias Stening <info@swts.net>
 * @version 2008-10-05
 */
public final class AppPanel extends javax.swing.JPanel implements
        AppExitListener {

    private static final String KEY_DIVIDER_LOCATION_MAIN =
            "AppPanel.DividerLocationMain"; // NOI18N
    private static final String KEY_DIVIDER_LOCATION_THUMBNAILS =
            "AppPanel.DividerLocationThumbnails"; // NOI18N
    private static final int MIN_DIVIDER_LOCATION_MAIN = 100;
    private static final int MIN_DIVIDER_LOCATION_THUMBNAILS = 200;
    private final List<JTable> xmpTables = new ArrayList<JTable>();
    private final List<JTable> metadataTables = new ArrayList<JTable>();
    private final List<JTree> selectionTrees = new ArrayList<JTree>();
    private final List<JList> selectionLists = new ArrayList<JList>();
    private EditMetadataPanelsArray editPanelsArray;
    private EditMetadataActionsPanel editActionsPanel;

    public AppPanel() {
        GUI.INSTANCE.setAppPanel(this);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        editPanelsArray = new EditMetadataPanelsArray(panelEditMetadata);
        panelThumbnails.setViewport(scrollPaneThumbnails.getViewport());
        setBackgroundColorTablesScrollPanes();
        setDisableTreeMultipleSelection();
        initArrays();
        tableExif.addMouseListener(new TableButtonMouseListener(tableExif));
        scrollPaneThumbnails.getVerticalScrollBar().setUnitIncrement(30);
    }

    private void initArrays() {
        initTableArrays();
        initSelectionTreeArray();
        initSelectionListArray();
    }

    public EditMetadataPanelsArray getMetadataEditPanelsArray() {
        return editPanelsArray;
    }

    public EditMetadataActionsPanel getMetadataEditActionsPanel() {
        if (editActionsPanel == null) {
            editActionsPanel = new EditMetadataActionsPanel();
        }
        return editActionsPanel;
    }

    public JScrollPane getScrollPaneThumbnailsPanel() {
        return scrollPaneThumbnails;
    }

    public JSlider getSliderThumbnailSize() {
        return sliderThumbnailSize;
    }

    public JTabbedPane getTabbedPaneMetadata() {
        return tabbedPaneMetadata;
    }

    public JTabbedPane getTabbedPaneSelection() {
        return tabbedPaneSelection;
    }

    public Component getTabMetadataIptc() {
        return panelIptc;
    }

    public Component getTabMetadataExif() {
        return scrollPaneExif;
    }

    public Component getTabMetadataXmp() {
        return tabbedPaneXmp;
    }

    public Component getTabMetadataHierarchicaKeywords() {
        return panelHierarchicalKeywords;
    }

    public Component getTabMetadataEdit() {
        return panelTabEditMetadata;
    }

    public Component getTabSelectionDirectories() {
        return panelDirectories;
    }

    public Component getTabSelectionFavoriteDirectories() {
        return panelFavorites;
    }

    public Component getTabSelectionImageCollections() {
        return panelImageCollections;
    }

    public Component getTabSelectionTimeline() {
        return panelTimeline;
    }

    public Component getTabSelectionMiscMetadata() {
        return panelMiscMetadata;
    }

    public JPanel getTabSelectionSavedSearches() {
        return panelSavedSearches;
    }

    public JPanel getTabSelectionKeywords() {
        return panelKeywords;
    }

    public JPanel getTabSelectionHierarchicalKeywords() {
        return panelSelHierarchicalKeywords;
    }

    public JPanel getTabSelectionCategories() {
        return panelCategories;
    }

    public JTree getTreeTimeline() {
        return treeTimeline;
    }

    public JTree getTreeMiscMetadata() {
        return treeMiscMetadata;
    }

    public JTree getTreeHierarchicalKeywords() {
        return panelHierarchicalKeywords.getTree();
    }

    public JTree getTreeSelHierarchicalKeywords() {
        return treeSelHierarchicalKeywords;
    }

    JProgressBar getProgressBarAutomaticTasks() {
        return progressBarAutomaticTasks;
    }

    JProgressBar getProgressBarUserTasks() {
        return progressBarUserTasks;
    }

    JProgressBar getProgressBarScheduledTasks() {
        return progressBarScheduledTasks;
    }

    public List<JTree> getSelectionTrees() {
        return selectionTrees;
    }

    public List<JList> getSelectionLists() {
        return selectionLists;
    }

    public ThumbnailsPanel getPanelThumbnails() {
        return panelThumbnails;
    }

    public HierarchicalKeywordsPanel getPanelHierarchicalKeywords() {
        return panelHierarchicalKeywords;
    }

    public JList getListImageCollections() {
        return listImageCollections;
    }

    public JList getListSavedSearches() {
        return listSavedSearches;
    }

    public JTree getTreeDirectories() {
        return treeDirectories;
    }

    public JTree getTreeFavorites() {
        return treeFavorites;
    }

    public JList getListCategories() {
        return listCategories;
    }

    public JList getListKeywords() {
        return listKeywords;
    }

    public JList getListNoMetadata() {
        return listNoMetadata;
    }

    public JButton getButtonLogfileDialog() {
        return buttonLogfileDialog;
    }

    public JButton getButtonSystemOutput() {
        return buttonSystemOutput;
    }

    public JButton getButtonEmptyMetadata() {
        return editActionsPanel.buttonEmptyMetadata;
    }

    public JButton getButtonMetadataTemplateCreate() {
        return editActionsPanel.buttonMetadataTemplateCreate;
    }

    public JButton getButtonMetadataTemplateUpdate() {
        return editActionsPanel.buttonMetadataTemplateUpdate;
    }

    public JButton getButtonMetadataTemplateRename() {
        return editActionsPanel.buttonMetadataTemplateRename;
    }

    public JButton getButtonMetadataTemplateInsert() {
        return editActionsPanel.buttonMetadataTemplateInsert;
    }

    public JButton getButtonMetadataTemplateDelete() {
        return editActionsPanel.buttonMetadataTemplateDelete;
    }

    public JButton getButtonStopScheduledTasks() {
        return buttonStopScheduledTasks;
    }

    public JButton getButtonIptcToXmp() {
        return buttonIptcToXmp;
    }

    public JComboBox getComboBoxMetadataTemplates() {
        return editActionsPanel.comboBoxMetadataTemplates;
    }

    public JLabel getLabelMetadataInfoEditable() {
        return editActionsPanel.labelMetadataInfoEditable;
    }

    public JLabel getLabelStatusbar() {
        return labelStatusbar;
    }

    public JLabel getLabelMetadataFilename() {
        return labelMetadataFilename;
    }

    public JTextField getTextFieldSearch() {
        return textFieldSearch;
    }

    public EditMetadataPanelsArray getEditPanelsArray() {
        return editPanelsArray;
    }

    public List<JTable> getMetadataTables() {
        return metadataTables;
    }

    public List<JTable> getXmpTables() {
        return xmpTables;
    }

    public JTable getTableIptc() {
        return tableIptc;
    }

    public JTable getTableExif() {
        return tableExif;
    }

    public JTable getTableXmpCameraRawSettings() {
        return tableXmpCameraRawSettings;
    }

    public JTable getTableXmpDc() {
        return tableXmpDc;
    }

    public JTable getTableXmpExif() {
        return tableXmpExif;
    }

    public JTable getTableXmpIptc() {
        return tableXmpIptc;
    }

    public JTable getTableXmpLightroom() {
        return tableXmpLightroom;
    }

    public JTable getTableXmpPhotoshop() {
        return tableXmpPhotoshop;
    }

    public JTable getTableXmpTiff() {
        return tableXmpTiff;
    }

    public JTable getTableXmpXap() {
        return tableXmpXap;
    }

    public JComboBox getComboBoxFastSearch() {
        return comboBoxFastSearch;
    }

    public JRadioButton getRadioButtonKeywordsMultipleSelAll() {
        return radioButtonKeywordsMultipleSelAll;
    }

    public JRadioButton getRadioButtonKeywordsMultipleSelOne() {
        return radioButtonKeywordsMultipleSelOne;
    }

    private void initTableArrays() {
        initXmpTableArray();
        initMetadataTablesArray();
    }

    private void initMetadataTablesArray() {
        metadataTables.addAll(xmpTables);
        metadataTables.add(tableExif);
        metadataTables.add(tableIptc);
    }

    private void initXmpTableArray() {
        xmpTables.add(tableXmpCameraRawSettings);
        xmpTables.add(tableXmpDc);
        xmpTables.add(tableXmpExif);
        xmpTables.add(tableXmpIptc);
        xmpTables.add(tableXmpLightroom);
        xmpTables.add(tableXmpPhotoshop);
        xmpTables.add(tableXmpTiff);
        xmpTables.add(tableXmpXap);
    }

    private void initSelectionTreeArray() {
        selectionTrees.add(treeDirectories);
        selectionTrees.add(treeFavorites);
        selectionTrees.add(treeMiscMetadata);
        selectionTrees.add(treeTimeline);
        selectionTrees.add(treeSelHierarchicalKeywords);
    }

    private void initSelectionListArray() {
        selectionLists.add(listCategories);
        selectionLists.add(listImageCollections);
        selectionLists.add(listKeywords);
        selectionLists.add(listSavedSearches);
        selectionLists.add(listNoMetadata);
    }

    private void setBackgroundColorTablesScrollPanes() {
        for (JTable table : metadataTables) {
            Container container = table.getParent();
            if (container instanceof JViewport) {
                JViewport viewport = (JViewport) container;
                viewport.setBackground(table.getBackground());
            }
        }
    }

    @Override
    public void appWillExit() {
        writeProperties();
    }

    private void writeProperties() {
        Settings settings = UserSettings.INSTANCE.getSettings();
        settings.setComponent(this, getPersistentSettingsHints());
        ViewUtil.writeTreeDirectoriesToProperties();
        UserSettings.INSTANCE.writeToFile();
    }

    public SettingsHints getPersistentSettingsHints() {
        SettingsHints hints = new SettingsHints(EnumSet.of(
                SettingsHints.Option.SET_TABBED_PANE_CONTENT));
        String className = getClass().getName();
        hints.addExclude(className + ".textFieldSearch"); // NOI18N
        hints.addExclude(className + ".panelEditMetadata"); // NOI18N
        hints.addExclude(className + ".treeDirectories"); // NOI18N
        hints.addExclude(className + ".treeFavorites"); // NOI18N
        //hints.addExclude(className + ".listNoMetadata"); // NOI18N
        return hints;
    }

    private int getDividerLocationThumbnails() {
        int location = UserSettings.INSTANCE.getSettings().getInt(
                KEY_DIVIDER_LOCATION_THUMBNAILS);
        return location > MIN_DIVIDER_LOCATION_THUMBNAILS
               ? location
               : MIN_DIVIDER_LOCATION_THUMBNAILS;
    }

    private int getDividerLocationMain() {
        int location = UserSettings.INSTANCE.getSettings().getInt(
                KEY_DIVIDER_LOCATION_MAIN);
        if (location <= splitPaneMain.getDividerSize()) location = 0;
        return location >= 0
               ? location
               : MIN_DIVIDER_LOCATION_MAIN;
    }

    private void setDisableTreeMultipleSelection() {
        int singleSelecion = TreeSelectionModel.SINGLE_TREE_SELECTION;
        treeDirectories.getSelectionModel().setSelectionMode(singleSelecion);
        treeFavorites.getSelectionModel().setSelectionMode(
                singleSelecion);
        treeMiscMetadata.getSelectionModel().setSelectionMode(singleSelecion);
        treeTimeline.getSelectionModel().setSelectionMode(singleSelecion);
    }

    public void showMessage(final String message, final long milliseconds) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                PopupFactory factory = PopupFactory.getSharedInstance();
                MessagePopupPanel messagePanel = new MessagePopupPanel(message);
                ThumbnailsPanel tnPanel =
                        GUI.INSTANCE.getAppPanel().getPanelThumbnails();
                int x = progressBarAutomaticTasks.getLocationOnScreen().x + 1;
                int y = progressBarAutomaticTasks.getLocationOnScreen().y + 1;
                Dimension d = new Dimension(
                        progressBarAutomaticTasks.getWidth() - 2    ,
                        progressBarAutomaticTasks.getHeight() - 2);
                messagePanel.setPreferredSize(d);
                messagePanel.setSize(d);
                ProgressBarAutomaticTasks.INSTANCE.releaseResource(this);
                Popup popup = factory.getPopup(tnPanel, messagePanel, x, y);
                popup.show();
                Thread thread = new Thread(new HidePopup(popup, milliseconds));
                thread.setName("Hiding message popup @ " + getClass().getName()); // NOI18N
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setName("Showing message popup @ " + ViewUtil.class.getName()); // NOI18N
        thread.start();
    }

    private static class HidePopup implements Runnable {

        private final Popup popup;
        private final long milliseconds;

        public HidePopup(Popup popup, long milliseconds) {
            this.popup = popup;
            this.milliseconds = milliseconds;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException ex) {
                AppLog.logSevere(ViewUtil.class, ex);
            }
            popup.hide();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupKeywordsMultipleSel = new javax.swing.ButtonGroup();
        splitPaneMain = new javax.swing.JSplitPane();
        splitPaneMain.setDividerLocation(getDividerLocationMain());
        panelSelection = new javax.swing.JPanel();
        tabbedPaneSelection = new javax.swing.JTabbedPane();
        panelDirectories = new javax.swing.JPanel();
        scrollPaneDirectories = new javax.swing.JScrollPane();
        treeDirectories = new javax.swing.JTree();
        panelSavedSearches = new javax.swing.JPanel();
        scrollPaneSavedSearches = new javax.swing.JScrollPane();
        listSavedSearches = new javax.swing.JList();
        panelImageCollections = new javax.swing.JPanel();
        scrollPaneImageCollections = new javax.swing.JScrollPane();
        listImageCollections = new javax.swing.JList();
        panelCategories = new javax.swing.JPanel();
        scrollPaneCategories = new javax.swing.JScrollPane();
        listCategories = new javax.swing.JList();
        panelFavorites = new javax.swing.JPanel();
        scrollPaneFavorites = new javax.swing.JScrollPane();
        treeFavorites = new javax.swing.JTree();
        panelKeywords = new javax.swing.JPanel();
        scrollPaneKeywords = new javax.swing.JScrollPane();
        listKeywords = new javax.swing.JList();
        panelKeywordsMultipleSelection = new javax.swing.JPanel();
        radioButtonKeywordsMultipleSelAll = new javax.swing.JRadioButton();
        radioButtonKeywordsMultipleSelOne = new javax.swing.JRadioButton();
        panelSelHierarchicalKeywords = new javax.swing.JPanel();
        scrollPaneSelHierarchicalKeywords = new javax.swing.JScrollPane();
        treeSelHierarchicalKeywords = new javax.swing.JTree();
        panelTimeline = new javax.swing.JPanel();
        scrollPaneTimeline = new javax.swing.JScrollPane();
        treeTimeline = new javax.swing.JTree();
        panelMiscMetadata = new javax.swing.JPanel();
        scrollPaneMiscMetadata = new javax.swing.JScrollPane();
        treeMiscMetadata = new javax.swing.JTree();
        panelNoMetadata = new javax.swing.JPanel();
        scrollPaneNoMetadata = new javax.swing.JScrollPane();
        listNoMetadata = new javax.swing.JList();
        panelThumbnailsMetadata = new javax.swing.JPanel();
        splitPaneThumbnailsMetadata = new javax.swing.JSplitPane();
        splitPaneThumbnailsMetadata.setDividerLocation(getDividerLocationThumbnails());
        panelThumbnailsContent = new javax.swing.JPanel();
        scrollPaneThumbnails = new javax.swing.JScrollPane();
        panelThumbnails = new de.elmar_baumann.imv.view.panels.ThumbnailsPanel();
        panelMetadata = new javax.swing.JPanel();
        labelMetadataFilename = new javax.swing.JLabel();
        tabbedPaneMetadata = new javax.swing.JTabbedPane();
        panelIptc = new javax.swing.JPanel();
        scrollPaneIptc = new javax.swing.JScrollPane();
        tableIptc = new javax.swing.JTable();
        buttonIptcToXmp = new javax.swing.JButton();
        scrollPaneExif = new javax.swing.JScrollPane();
        tableExif = new javax.swing.JTable();
        tabbedPaneXmp = new javax.swing.JTabbedPane();
        scrollPaneXmpTiff = new javax.swing.JScrollPane();
        tableXmpTiff = new javax.swing.JTable();
        scrollPaneXmpExif = new javax.swing.JScrollPane();
        tableXmpExif = new javax.swing.JTable();
        scrollPaneXmpDc = new javax.swing.JScrollPane();
        tableXmpDc = new javax.swing.JTable();
        scrollPaneXmpIptc = new javax.swing.JScrollPane();
        tableXmpIptc = new javax.swing.JTable();
        scrollPaneXmpPhotoshop = new javax.swing.JScrollPane();
        tableXmpPhotoshop = new javax.swing.JTable();
        scrollPaneXmpXap = new javax.swing.JScrollPane();
        tableXmpXap = new javax.swing.JTable();
        scrollPaneXmpLightroom = new javax.swing.JScrollPane();
        tableXmpLightroom = new javax.swing.JTable();
        scrollPaneXmpCameraRawSettings = new javax.swing.JScrollPane();
        tableXmpCameraRawSettings = new javax.swing.JTable();
        panelTabEditMetadata = new javax.swing.JPanel();
        panelScrollPaneEditMetadata = new javax.swing.JPanel();
        scrollPaneEditMetadata = new javax.swing.JScrollPane();
        panelEditMetadata = new javax.swing.JPanel();
        panelHierarchicalKeywords = new de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel();
        panelStatusbar = new javax.swing.JPanel();
        sliderThumbnailSize = new javax.swing.JSlider();
        panelSearch = new javax.swing.JPanel();
        labelSearch = new javax.swing.JLabel();
        textFieldSearch = new javax.swing.JTextField();
        comboBoxFastSearch = new javax.swing.JComboBox();
        panelMetadataProgress = new javax.swing.JPanel();
        progressBarScheduledTasks = new javax.swing.JProgressBar();
        progressBarScheduledTasks.setPreferredSize(new Dimension(progressBarScheduledTasks.getPreferredSize().width, textFieldSearch.getPreferredSize().height));
        buttonStopScheduledTasks = new javax.swing.JButton();
        labelStatusbar = new javax.swing.JLabel();
        buttonLogfileDialog = new javax.swing.JButton();
        buttonSystemOutput = new javax.swing.JButton();
        progressBarAutomaticTasks = new javax.swing.JProgressBar();
        progressBarUserTasks = new javax.swing.JProgressBar();

        splitPaneMain.setDividerSize(6);
        splitPaneMain.setOneTouchExpandable(true);

        tabbedPaneSelection.setName(""); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Lade...");
        treeDirectories.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeDirectories.setCellRenderer(new de.elmar_baumann.lib.renderer.TreeCellRendererAllSystemDirectories());
        treeDirectories.setDragEnabled(true);
        treeDirectories.setName("treeDirectories"); // NOI18N
        scrollPaneDirectories.setViewportView(treeDirectories);
        treeDirectories.setTransferHandler(new de.elmar_baumann.imv.datatransfer.TransferHandlerTreeDirectories());
        treeFavorites.setTransferHandler(new de.elmar_baumann.imv.datatransfer.TransferHandlerTreeDirectories());

        javax.swing.GroupLayout panelDirectoriesLayout = new javax.swing.GroupLayout(panelDirectories);
        panelDirectories.setLayout(panelDirectoriesLayout);
        panelDirectoriesLayout.setHorizontalGroup(
            panelDirectoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneDirectories, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelDirectoriesLayout.setVerticalGroup(
            panelDirectoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneDirectories, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelDirectories.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_folder.png")), panelDirectories); // NOI18N

        listSavedSearches.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Lade..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listSavedSearches.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listSavedSearches.setCellRenderer(new ListCellRendererSavedSearches());
        listSavedSearches.setName("listSavedSearches"); // NOI18N
        scrollPaneSavedSearches.setViewportView(listSavedSearches);

        javax.swing.GroupLayout panelSavedSearchesLayout = new javax.swing.GroupLayout(panelSavedSearches);
        panelSavedSearches.setLayout(panelSavedSearchesLayout);
        panelSavedSearchesLayout.setHorizontalGroup(
            panelSavedSearchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneSavedSearches, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelSavedSearchesLayout.setVerticalGroup(
            panelSavedSearchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneSavedSearches, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelSavedSearches.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_search.png")), panelSavedSearches); // NOI18N

        listImageCollections.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Lade..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listImageCollections.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listImageCollections.setCellRenderer(new ListCellRendererImageCollections());
        listImageCollections.setDragEnabled(true);
        listImageCollections.setName("listImageCollections"); // NOI18N
        scrollPaneImageCollections.setViewportView(listImageCollections);
        listImageCollections.setTransferHandler(new de.elmar_baumann.imv.datatransfer.TransferHandlerListImageCollections());

        javax.swing.GroupLayout panelImageCollectionsLayout = new javax.swing.GroupLayout(panelImageCollections);
        panelImageCollections.setLayout(panelImageCollectionsLayout);
        panelImageCollectionsLayout.setHorizontalGroup(
            panelImageCollectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneImageCollections, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelImageCollectionsLayout.setVerticalGroup(
            panelImageCollectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneImageCollections, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelImageCollections.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_imagecollection.png")), panelImageCollections); // NOI18N

        listCategories.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Lade..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listCategories.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listCategories.setCellRenderer(new ListCellRendererCategories());
        listCategories.setDropMode(javax.swing.DropMode.ON);
        listCategories.setName("listCategories"); // NOI18N
        scrollPaneCategories.setViewportView(listCategories);

        javax.swing.GroupLayout panelCategoriesLayout = new javax.swing.GroupLayout(panelCategories);
        panelCategories.setLayout(panelCategoriesLayout);
        panelCategoriesLayout.setHorizontalGroup(
            panelCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneCategories, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelCategoriesLayout.setVerticalGroup(
            panelCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneCategories, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelCategories.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_category.png")), panelCategories); // NOI18N

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bitte warten");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Lade...");
        treeNode1.add(treeNode2);
        treeFavorites.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeFavorites.setCellRenderer(new de.elmar_baumann.imv.view.renderer.TreeCellRendererFavorites());
        treeFavorites.setDragEnabled(true);
        treeFavorites.setRootVisible(false);
        treeFavorites.setShowsRootHandles(true);
        scrollPaneFavorites.setViewportView(treeFavorites);

        javax.swing.GroupLayout panelFavoritesLayout = new javax.swing.GroupLayout(panelFavorites);
        panelFavorites.setLayout(panelFavoritesLayout);
        panelFavoritesLayout.setHorizontalGroup(
            panelFavoritesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneFavorites, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelFavoritesLayout.setVerticalGroup(
            panelFavoritesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneFavorites, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelFavorites.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_favorite.png")), panelFavorites); // NOI18N

        listKeywords.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Lade..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listKeywords.setCellRenderer(new ListCellRendererKeywords());
        listKeywords.setDragEnabled(true);
        listKeywords.setName("listKeywords"); // NOI18N
        scrollPaneKeywords.setViewportView(listKeywords);
        listKeywords.setTransferHandler(new TransferHandlerListKeywords());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/imv/resource/properties/Bundle"); // NOI18N
        panelKeywordsMultipleSelection.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AppPanel.panelKeywordsMultipleSelection.border.title"))); // NOI18N

        buttonGroupKeywordsMultipleSel.add(radioButtonKeywordsMultipleSelAll);
        radioButtonKeywordsMultipleSelAll.setText(bundle.getString("AppPanel.radioButtonKeywordsMultipleSelAll.text")); // NOI18N

        buttonGroupKeywordsMultipleSel.add(radioButtonKeywordsMultipleSelOne);
        radioButtonKeywordsMultipleSelOne.setText(bundle.getString("AppPanel.radioButtonKeywordsMultipleSelOne.text")); // NOI18N

        javax.swing.GroupLayout panelKeywordsMultipleSelectionLayout = new javax.swing.GroupLayout(panelKeywordsMultipleSelection);
        panelKeywordsMultipleSelection.setLayout(panelKeywordsMultipleSelectionLayout);
        panelKeywordsMultipleSelectionLayout.setHorizontalGroup(
            panelKeywordsMultipleSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKeywordsMultipleSelectionLayout.createSequentialGroup()
                .addGroup(panelKeywordsMultipleSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioButtonKeywordsMultipleSelAll)
                    .addComponent(radioButtonKeywordsMultipleSelOne))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelKeywordsMultipleSelectionLayout.setVerticalGroup(
            panelKeywordsMultipleSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKeywordsMultipleSelectionLayout.createSequentialGroup()
                .addComponent(radioButtonKeywordsMultipleSelAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonKeywordsMultipleSelOne)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelKeywordsLayout = new javax.swing.GroupLayout(panelKeywords);
        panelKeywords.setLayout(panelKeywordsLayout);
        panelKeywordsLayout.setHorizontalGroup(
            panelKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelKeywordsMultipleSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPaneKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        );
        panelKeywordsLayout.setVerticalGroup(
            panelKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKeywordsLayout.createSequentialGroup()
                .addComponent(scrollPaneKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelKeywordsMultipleSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelKeywords.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_keyword.png")), panelKeywords); // NOI18N

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Laden...");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Laden...");
        treeNode1.add(treeNode2);
        treeSelHierarchicalKeywords.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeSelHierarchicalKeywords.setCellRenderer(new TreeCellRendererHierarchicalKeywords());
        treeSelHierarchicalKeywords.setRootVisible(false);
        treeSelHierarchicalKeywords.setShowsRootHandles(true);
        scrollPaneSelHierarchicalKeywords.setViewportView(treeSelHierarchicalKeywords);

        javax.swing.GroupLayout panelSelHierarchicalKeywordsLayout = new javax.swing.GroupLayout(panelSelHierarchicalKeywords);
        panelSelHierarchicalKeywords.setLayout(panelSelHierarchicalKeywordsLayout);
        panelSelHierarchicalKeywordsLayout.setHorizontalGroup(
            panelSelHierarchicalKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneSelHierarchicalKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelSelHierarchicalKeywordsLayout.setVerticalGroup(
            panelSelHierarchicalKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneSelHierarchicalKeywords, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelSelHierarchicalKeywords.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_tree.png")), panelSelHierarchicalKeywords); // NOI18N

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bitte warten");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Lade...");
        treeNode1.add(treeNode2);
        treeTimeline.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeTimeline.setCellRenderer(new TreeCellRendererTimeline());
        treeTimeline.setRootVisible(false);
        treeTimeline.setShowsRootHandles(true);
        scrollPaneTimeline.setViewportView(treeTimeline);

        javax.swing.GroupLayout panelTimelineLayout = new javax.swing.GroupLayout(panelTimeline);
        panelTimeline.setLayout(panelTimelineLayout);
        panelTimelineLayout.setHorizontalGroup(
            panelTimelineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneTimeline, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelTimelineLayout.setVerticalGroup(
            panelTimelineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneTimeline, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelTimeline.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_timeline.png")), panelTimeline); // NOI18N

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bitte warten");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Lade...");
        treeNode1.add(treeNode2);
        treeMiscMetadata.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeMiscMetadata.setCellRenderer(new TreeCellRendererMiscMetadata());
        treeMiscMetadata.setRootVisible(false);
        treeMiscMetadata.setShowsRootHandles(true);
        scrollPaneMiscMetadata.setViewportView(treeMiscMetadata);

        javax.swing.GroupLayout panelMiscMetadataLayout = new javax.swing.GroupLayout(panelMiscMetadata);
        panelMiscMetadata.setLayout(panelMiscMetadataLayout);
        panelMiscMetadataLayout.setHorizontalGroup(
            panelMiscMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneMiscMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        panelMiscMetadataLayout.setVerticalGroup(
            panelMiscMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneMiscMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        tabbedPaneSelection.addTab(Bundle.getString("AppPanel.panelMiscMetadata.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_misc_metadata.png")), panelMiscMetadata); // NOI18N

        listNoMetadata.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listNoMetadata.setCellRenderer(new de.elmar_baumann.imv.view.renderer.ListCellRendererNoMetadata());
        scrollPaneNoMetadata.setViewportView(listNoMetadata);

        javax.swing.GroupLayout panelNoMetadataLayout = new javax.swing.GroupLayout(panelNoMetadata);
        panelNoMetadata.setLayout(panelNoMetadataLayout);
        panelNoMetadataLayout.setHorizontalGroup(
            panelNoMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
            .addGroup(panelNoMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneNoMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
        );
        panelNoMetadataLayout.setVerticalGroup(
            panelNoMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
            .addGroup(panelNoMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneNoMetadata, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
        );

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelNoMetadata.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_no_metadata.png")), panelNoMetadata); // NOI18N

        javax.swing.GroupLayout panelSelectionLayout = new javax.swing.GroupLayout(panelSelection);
        panelSelection.setLayout(panelSelectionLayout);
        panelSelectionLayout.setHorizontalGroup(
            panelSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
            .addGroup(panelSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPaneSelection, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
        );
        panelSelectionLayout.setVerticalGroup(
            panelSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
            .addGroup(panelSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPaneSelection, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
        );

        splitPaneMain.setLeftComponent(panelSelection);

        splitPaneThumbnailsMetadata.setDividerSize(6);
        splitPaneThumbnailsMetadata.setResizeWeight(1.0);
        splitPaneThumbnailsMetadata.setOneTouchExpandable(true);

        panelThumbnailsContent.setMinimumSize(new java.awt.Dimension(180, 0));

        javax.swing.GroupLayout panelThumbnailsLayout = new javax.swing.GroupLayout(panelThumbnails);
        panelThumbnails.setLayout(panelThumbnailsLayout);
        panelThumbnailsLayout.setHorizontalGroup(
            panelThumbnailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
        );
        panelThumbnailsLayout.setVerticalGroup(
            panelThumbnailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );

        scrollPaneThumbnails.setViewportView(panelThumbnails);

        javax.swing.GroupLayout panelThumbnailsContentLayout = new javax.swing.GroupLayout(panelThumbnailsContent);
        panelThumbnailsContent.setLayout(panelThumbnailsContentLayout);
        panelThumbnailsContentLayout.setHorizontalGroup(
            panelThumbnailsContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
            .addGroup(panelThumbnailsContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneThumbnails, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
        );
        panelThumbnailsContentLayout.setVerticalGroup(
            panelThumbnailsContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(panelThumbnailsContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneThumbnails, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
        );

        splitPaneThumbnailsMetadata.setLeftComponent(panelThumbnailsContent);

        labelMetadataFilename.setBackground(new java.awt.Color(255, 255, 255));
        labelMetadataFilename.setText(Bundle.getString("AppPanel.labelMetadataFilename.text")); // NOI18N
        labelMetadataFilename.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelMetadataFilename.setOpaque(true);

        tabbedPaneMetadata.setOpaque(true);

        tableIptc.setAutoCreateRowSorter(true);
        tableIptc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableIptc.setName("tableIptc"); // NOI18N
        scrollPaneIptc.setViewportView(tableIptc);

        buttonIptcToXmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_xmp.png"))); // NOI18N
        buttonIptcToXmp.setMnemonic('x');
        buttonIptcToXmp.setText(Bundle.getString("AppPanel.buttonIptcToXmp.text")); // NOI18N
        buttonIptcToXmp.setToolTipText(Bundle.getString("AppPanel.buttonIptcToXmp.toolTipText")); // NOI18N

        javax.swing.GroupLayout panelIptcLayout = new javax.swing.GroupLayout(panelIptc);
        panelIptc.setLayout(panelIptcLayout);
        panelIptcLayout.setHorizontalGroup(
            panelIptcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneIptc, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
            .addGroup(panelIptcLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonIptcToXmp)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelIptcLayout.setVerticalGroup(
            panelIptcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelIptcLayout.createSequentialGroup()
                .addComponent(scrollPaneIptc, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonIptcToXmp)
                .addContainerGap())
        );

        tabbedPaneMetadata.addTab(Bundle.getString("AppPanel.panelIptc.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_iptc.png")), panelIptc); // NOI18N

        tableExif.setAutoCreateRowSorter(true);
        tableExif.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableExif.setName("tableExif"); // NOI18N
        scrollPaneExif.setViewportView(tableExif);

        tabbedPaneMetadata.addTab(Bundle.getString("AppPanel.scrollPaneExif.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_exif.png")), scrollPaneExif); // NOI18N

        tabbedPaneXmp.setOpaque(true);

        tableXmpTiff.setAutoCreateRowSorter(true);
        tableXmpTiff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpTiff.setName("tableXmpTiff"); // NOI18N
        scrollPaneXmpTiff.setViewportView(tableXmpTiff);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpTiff.TabConstraints.tabTitle"), scrollPaneXmpTiff); // NOI18N

        tableXmpExif.setAutoCreateRowSorter(true);
        tableXmpExif.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpExif.setName("tableXmpExif"); // NOI18N
        scrollPaneXmpExif.setViewportView(tableXmpExif);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpExif.TabConstraints.tabTitle"), scrollPaneXmpExif); // NOI18N

        tableXmpDc.setAutoCreateRowSorter(true);
        tableXmpDc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpDc.setName("tableXmpDc"); // NOI18N
        scrollPaneXmpDc.setViewportView(tableXmpDc);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpDc.TabConstraints.tabTitle"), scrollPaneXmpDc); // NOI18N

        tableXmpIptc.setAutoCreateRowSorter(true);
        tableXmpIptc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpIptc.setName("tableXmpIptc"); // NOI18N
        scrollPaneXmpIptc.setViewportView(tableXmpIptc);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpIptc.TabConstraints.tabTitle"), scrollPaneXmpIptc); // NOI18N

        tableXmpPhotoshop.setAutoCreateRowSorter(true);
        tableXmpPhotoshop.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpPhotoshop.setName("tableXmpPhotoshop"); // NOI18N
        scrollPaneXmpPhotoshop.setViewportView(tableXmpPhotoshop);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpPhotoshop.TabConstraints.tabTitle"), scrollPaneXmpPhotoshop); // NOI18N

        tableXmpXap.setAutoCreateRowSorter(true);
        tableXmpXap.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpXap.setName("tableXmpXap"); // NOI18N
        scrollPaneXmpXap.setViewportView(tableXmpXap);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpXap.TabConstraints.tabTitle"), scrollPaneXmpXap); // NOI18N

        tableXmpLightroom.setAutoCreateRowSorter(true);
        tableXmpLightroom.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpLightroom.setName("tableXmpLightroom"); // NOI18N
        scrollPaneXmpLightroom.setViewportView(tableXmpLightroom);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpLightroom.TabConstraints.tabTitle"), scrollPaneXmpLightroom); // NOI18N

        tableXmpCameraRawSettings.setAutoCreateRowSorter(true);
        tableXmpCameraRawSettings.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableXmpCameraRawSettings.setName("tableXmpCameraRawSettings"); // NOI18N
        scrollPaneXmpCameraRawSettings.setViewportView(tableXmpCameraRawSettings);

        tabbedPaneXmp.addTab(Bundle.getString("AppPanel.scrollPaneXmpCameraRawSettings.TabConstraints.tabTitle"), scrollPaneXmpCameraRawSettings); // NOI18N

        tabbedPaneMetadata.addTab(Bundle.getString("AppPanel.tabbedPaneXmp.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_xmp.png")), tabbedPaneXmp); // NOI18N

        javax.swing.GroupLayout panelEditMetadataLayout = new javax.swing.GroupLayout(panelEditMetadata);
        panelEditMetadata.setLayout(panelEditMetadataLayout);
        panelEditMetadataLayout.setHorizontalGroup(
            panelEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 923, Short.MAX_VALUE)
        );
        panelEditMetadataLayout.setVerticalGroup(
            panelEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );

        scrollPaneEditMetadata.setViewportView(panelEditMetadata);

        javax.swing.GroupLayout panelScrollPaneEditMetadataLayout = new javax.swing.GroupLayout(panelScrollPaneEditMetadata);
        panelScrollPaneEditMetadata.setLayout(panelScrollPaneEditMetadataLayout);
        panelScrollPaneEditMetadataLayout.setHorizontalGroup(
            panelScrollPaneEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneEditMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
        );
        panelScrollPaneEditMetadataLayout.setVerticalGroup(
            panelScrollPaneEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneEditMetadata, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelTabEditMetadataLayout = new javax.swing.GroupLayout(panelTabEditMetadata);
        panelTabEditMetadata.setLayout(panelTabEditMetadataLayout);
        panelTabEditMetadataLayout.setHorizontalGroup(
            panelTabEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelScrollPaneEditMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTabEditMetadataLayout.setVerticalGroup(
            panelTabEditMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelScrollPaneEditMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPaneMetadata.addTab(Bundle.getString("AppPanel.panelTabEditMetadata.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_workspace.png")), panelTabEditMetadata); // NOI18N
        tabbedPaneMetadata.addTab(bundle.getString("AppPanel.panelHierarchicalKeywords.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_tree.png")), panelHierarchicalKeywords); // NOI18N
        new ControllerToggleRealHierarchicalKeyword(panelHierarchicalKeywords);
        new ControllerRenameHierarchicalKeyword(panelHierarchicalKeywords);
        new ControllerAddHierarchicalKeyword(panelHierarchicalKeywords);
        new ControllerRemoveHierarchicalKeyword(panelHierarchicalKeywords);
        new ControllerAddHierarchicalKeywordsToEditPanel(panelHierarchicalKeywords);
        new ControllerRemoveHierarchicalKeywordFromEditPanel(panelHierarchicalKeywords);
        new ControllerCutPasteHierarchicalKeyword(panelHierarchicalKeywords);
        new de.elmar_baumann.imv.controller.hierarchicalkeywords.ControllerHierarchicalKeywordsDisplayImages();

        javax.swing.GroupLayout panelMetadataLayout = new javax.swing.GroupLayout(panelMetadata);
        panelMetadata.setLayout(panelMetadataLayout);
        panelMetadataLayout.setHorizontalGroup(
            panelMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelMetadataFilename, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
            .addComponent(tabbedPaneMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );
        panelMetadataLayout.setVerticalGroup(
            panelMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetadataLayout.createSequentialGroup()
                .addComponent(labelMetadataFilename)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPaneMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );

        splitPaneThumbnailsMetadata.setRightComponent(panelMetadata);

        javax.swing.GroupLayout panelThumbnailsMetadataLayout = new javax.swing.GroupLayout(panelThumbnailsMetadata);
        panelThumbnailsMetadata.setLayout(panelThumbnailsMetadataLayout);
        panelThumbnailsMetadataLayout.setHorizontalGroup(
            panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
            .addGroup(panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(splitPaneThumbnailsMetadata, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
        );
        panelThumbnailsMetadataLayout.setVerticalGroup(
            panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
            .addGroup(panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(splitPaneThumbnailsMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
        );

        splitPaneMain.setRightComponent(panelThumbnailsMetadata);

        sliderThumbnailSize.setMajorTickSpacing(5);
        sliderThumbnailSize.setMinimum(10);
        sliderThumbnailSize.setSnapToTicks(true);
        sliderThumbnailSize.setToolTipText(Bundle.getString("AppPanel.sliderThumbnailSize.toolTipText")); // NOI18N

        labelSearch.setText(Bundle.getString("AppPanel.labelSearch.text")); // NOI18N

        textFieldSearch.setToolTipText(Bundle.getString("AppPanel.textFieldSearch.toolTipText")); // NOI18N

        comboBoxFastSearch.setModel(new ComboBoxModelFastSearch());
        comboBoxFastSearch.setRenderer(new ListCellRendererFastSearchColumns());

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addComponent(labelSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxFastSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labelSearch)
                .addComponent(comboBoxFastSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        progressBarScheduledTasks.setToolTipText(AppTexts.TOOLTIP_TEXT_PROGRESSBAR_SCHEDULED_TASKS);
        progressBarScheduledTasks.setName("progressBarScheduledTasks"); // NOI18N

        buttonStopScheduledTasks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_stop_scheduled_tasks_enabled.png"))); // NOI18N
        buttonStopScheduledTasks.setToolTipText(bundle.getString("AppPanel.buttonStopScheduledTasks.toolTipText")); // NOI18N
        buttonStopScheduledTasks.setAlignmentY(0.0F);
        buttonStopScheduledTasks.setBorder(null);
        buttonStopScheduledTasks.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_stop_scheduled_tasks_disabled.png"))); // NOI18N
        buttonStopScheduledTasks.setEnabled(false);
        buttonStopScheduledTasks.setMaximumSize(new java.awt.Dimension(16, 16));
        buttonStopScheduledTasks.setMinimumSize(new java.awt.Dimension(16, 16));
        buttonStopScheduledTasks.setPreferredSize(new java.awt.Dimension(16, 16));

        javax.swing.GroupLayout panelMetadataProgressLayout = new javax.swing.GroupLayout(panelMetadataProgress);
        panelMetadataProgress.setLayout(panelMetadataProgressLayout);
        panelMetadataProgressLayout.setHorizontalGroup(
            panelMetadataProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetadataProgressLayout.createSequentialGroup()
                .addComponent(progressBarScheduledTasks, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonStopScheduledTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelMetadataProgressLayout.setVerticalGroup(
            panelMetadataProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetadataProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(progressBarScheduledTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(buttonStopScheduledTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        labelStatusbar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        buttonLogfileDialog.setToolTipText(Bundle.getString("AppPanel.buttonLogfileDialog.toolTipText")); // NOI18N
        buttonLogfileDialog.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonLogfileDialog.setPreferredSize(new java.awt.Dimension(18, 18));

        buttonSystemOutput.setMnemonic('u');
        buttonSystemOutput.setText(Bundle.getString("AppPanel.buttonSystemOutput.text")); // NOI18N
        buttonSystemOutput.setToolTipText(Bundle.getString("AppPanel.buttonSystemOutput.toolTipText")); // NOI18N
        buttonSystemOutput.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonSystemOutput.setEnabled(AppInit.isCaptureOutput());
        buttonSystemOutput.setPreferredSize(new java.awt.Dimension(16, 16));

        progressBarAutomaticTasks.setToolTipText(AppTexts.TOOLTIP_TEXT_PROGRESSBAR_DIRECTORY);
        progressBarAutomaticTasks.setName("progressBarAutomaticTasks"); // NOI18N

        progressBarUserTasks.setToolTipText(AppTexts.TOOLTIP_TEXT_PROGRESSBAR_CURRENT_TASKS);
        progressBarUserTasks.setName("progressBarUserTasks"); // NOI18N

        javax.swing.GroupLayout panelStatusbarLayout = new javax.swing.GroupLayout(panelStatusbar);
        panelStatusbar.setLayout(panelStatusbarLayout);
        panelStatusbarLayout.setHorizontalGroup(
            panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStatusbarLayout.createSequentialGroup()
                .addGroup(panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderThumbnailSize, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelStatusbar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonLogfileDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSystemOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBarAutomaticTasks, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(panelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMetadataProgress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBarUserTasks, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
        );

        panelStatusbarLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {labelStatusbar, sliderThumbnailSize});

        panelStatusbarLayout.setVerticalGroup(
            panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStatusbarLayout.createSequentialGroup()
                .addGroup(panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelMetadataProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelStatusbar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonLogfileDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSystemOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressBarAutomaticTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressBarUserTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(sliderThumbnailSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelStatusbarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buttonLogfileDialog, buttonSystemOutput, progressBarAutomaticTasks, progressBarUserTasks});

        panelStatusbarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {panelMetadataProgress, panelSearch});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(splitPaneMain, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(panelStatusbar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(splitPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelStatusbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupKeywordsMultipleSel;
    private javax.swing.JButton buttonIptcToXmp;
    private javax.swing.JButton buttonLogfileDialog;
    private javax.swing.JButton buttonStopScheduledTasks;
    private javax.swing.JButton buttonSystemOutput;
    private javax.swing.JComboBox comboBoxFastSearch;
    private javax.swing.JLabel labelMetadataFilename;
    private javax.swing.JLabel labelSearch;
    private javax.swing.JLabel labelStatusbar;
    private javax.swing.JList listCategories;
    private javax.swing.JList listImageCollections;
    private javax.swing.JList listKeywords;
    private javax.swing.JList listNoMetadata;
    private javax.swing.JList listSavedSearches;
    private javax.swing.JPanel panelCategories;
    private javax.swing.JPanel panelDirectories;
    private javax.swing.JPanel panelEditMetadata;
    private javax.swing.JPanel panelFavorites;
    private de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel panelHierarchicalKeywords;
    private javax.swing.JPanel panelImageCollections;
    private javax.swing.JPanel panelIptc;
    private javax.swing.JPanel panelKeywords;
    private javax.swing.JPanel panelKeywordsMultipleSelection;
    private javax.swing.JPanel panelMetadata;
    private javax.swing.JPanel panelMetadataProgress;
    private javax.swing.JPanel panelMiscMetadata;
    private javax.swing.JPanel panelNoMetadata;
    private javax.swing.JPanel panelSavedSearches;
    private javax.swing.JPanel panelScrollPaneEditMetadata;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelSelHierarchicalKeywords;
    private javax.swing.JPanel panelSelection;
    private javax.swing.JPanel panelStatusbar;
    private javax.swing.JPanel panelTabEditMetadata;
    private de.elmar_baumann.imv.view.panels.ThumbnailsPanel panelThumbnails;
    private javax.swing.JPanel panelThumbnailsContent;
    private javax.swing.JPanel panelThumbnailsMetadata;
    private javax.swing.JPanel panelTimeline;
    private javax.swing.JProgressBar progressBarAutomaticTasks;
    private javax.swing.JProgressBar progressBarScheduledTasks;
    private javax.swing.JProgressBar progressBarUserTasks;
    private javax.swing.JRadioButton radioButtonKeywordsMultipleSelAll;
    private javax.swing.JRadioButton radioButtonKeywordsMultipleSelOne;
    private javax.swing.JScrollPane scrollPaneCategories;
    private javax.swing.JScrollPane scrollPaneDirectories;
    private javax.swing.JScrollPane scrollPaneEditMetadata;
    private javax.swing.JScrollPane scrollPaneExif;
    private javax.swing.JScrollPane scrollPaneFavorites;
    private javax.swing.JScrollPane scrollPaneImageCollections;
    private javax.swing.JScrollPane scrollPaneIptc;
    private javax.swing.JScrollPane scrollPaneKeywords;
    private javax.swing.JScrollPane scrollPaneMiscMetadata;
    private javax.swing.JScrollPane scrollPaneNoMetadata;
    private javax.swing.JScrollPane scrollPaneSavedSearches;
    private javax.swing.JScrollPane scrollPaneSelHierarchicalKeywords;
    private javax.swing.JScrollPane scrollPaneThumbnails;
    private javax.swing.JScrollPane scrollPaneTimeline;
    private javax.swing.JScrollPane scrollPaneXmpCameraRawSettings;
    private javax.swing.JScrollPane scrollPaneXmpDc;
    private javax.swing.JScrollPane scrollPaneXmpExif;
    private javax.swing.JScrollPane scrollPaneXmpIptc;
    private javax.swing.JScrollPane scrollPaneXmpLightroom;
    private javax.swing.JScrollPane scrollPaneXmpPhotoshop;
    private javax.swing.JScrollPane scrollPaneXmpTiff;
    private javax.swing.JScrollPane scrollPaneXmpXap;
    private javax.swing.JSlider sliderThumbnailSize;
    private javax.swing.JSplitPane splitPaneMain;
    private javax.swing.JSplitPane splitPaneThumbnailsMetadata;
    private javax.swing.JTabbedPane tabbedPaneMetadata;
    private javax.swing.JTabbedPane tabbedPaneSelection;
    private javax.swing.JTabbedPane tabbedPaneXmp;
    private javax.swing.JTable tableExif;
    private javax.swing.JTable tableIptc;
    private javax.swing.JTable tableXmpCameraRawSettings;
    private javax.swing.JTable tableXmpDc;
    private javax.swing.JTable tableXmpExif;
    private javax.swing.JTable tableXmpIptc;
    private javax.swing.JTable tableXmpLightroom;
    private javax.swing.JTable tableXmpPhotoshop;
    private javax.swing.JTable tableXmpTiff;
    private javax.swing.JTable tableXmpXap;
    private javax.swing.JTextField textFieldSearch;
    private javax.swing.JTree treeDirectories;
    private javax.swing.JTree treeFavorites;
    private javax.swing.JTree treeMiscMetadata;
    private javax.swing.JTree treeSelHierarchicalKeywords;
    private javax.swing.JTree treeTimeline;
    // End of variables declaration//GEN-END:variables
}
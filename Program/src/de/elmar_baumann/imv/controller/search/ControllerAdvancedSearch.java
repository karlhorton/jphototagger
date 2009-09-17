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
package de.elmar_baumann.imv.controller.search;

import de.elmar_baumann.imv.data.SavedSearch;
import de.elmar_baumann.imv.data.SavedSearchParamStatement;
import de.elmar_baumann.imv.database.DatabaseSearch;
import de.elmar_baumann.imv.event.listener.impl.ListenerProvider;
import de.elmar_baumann.imv.event.SearchEvent;
import de.elmar_baumann.imv.event.listener.SearchListener;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.dialogs.AdvancedSearchDialog;
import de.elmar_baumann.imv.view.panels.AppPanel;
import de.elmar_baumann.imv.types.Content;
import de.elmar_baumann.imv.view.panels.EditMetadataPanelsArray;
import de.elmar_baumann.imv.view.panels.ThumbnailsPanel;
import de.elmar_baumann.lib.componentutil.TreeUtil;
import de.elmar_baumann.lib.io.FileUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 * Kontrolliert die Aktionen: Erweiterter Suchdialog soll angezeigt werden sowie
 * eine Suche soll durchgeführt werden, ausgelöst vom Suchdialog.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>, Tobias Stening <info@swts.net>
 * @version 2008-10-05
 */
public final class ControllerAdvancedSearch implements ActionListener,
                                                       SearchListener {

    private final DatabaseSearch db = DatabaseSearch.INSTANCE;
    private final AppPanel appPanel = GUI.INSTANCE.getAppPanel();
    private final ThumbnailsPanel thumbnailsPanel =
            appPanel.getPanelThumbnails();
    private final List<JTree> selectionTrees = appPanel.getSelectionTrees();
    private final EditMetadataPanelsArray editPanels =
            appPanel.getEditPanelsArray();

    public ControllerAdvancedSearch() {
        listen();
    }

    private void listen() {
        ListenerProvider.INSTANCE.addSearchListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showAdvancedSearchDialog();
    }

    @Override
    public void actionPerformed(SearchEvent e) {
        if (e.getType().equals(SearchEvent.Type.START)) {
            applySavedSearch(e);
            setMetadataEditable();
        }
    }

    private void showAdvancedSearchDialog() {
        AdvancedSearchDialog dialogAdvancedSearch =
                AdvancedSearchDialog.INSTANCE;
        if (dialogAdvancedSearch.isVisible()) {
            dialogAdvancedSearch.toFront();
        } else {
            dialogAdvancedSearch.setVisible(true);
        }
    }

    private void applySavedSearch(final SearchEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SavedSearch savedSearch = e.getSavedSearch();
                if (savedSearch != null) {
                    SavedSearchParamStatement paramStmt =
                            savedSearch.getParamStatement();
                    if (paramStmt != null) {
                        TreeUtil.clearSelection(selectionTrees);
                        List<String> filenames = db.searchFilenames(
                                paramStmt.createStatement());

                        thumbnailsPanel.setFiles(FileUtil.getAsFiles(filenames),
                                Content.SAVED_SEARCH);
                    }
                }
            }
        });
    }

    private void setMetadataEditable() {
        if (thumbnailsPanel.getSelectionCount() <= 0) {
            editPanels.setEditable(false);
        }
    }
}
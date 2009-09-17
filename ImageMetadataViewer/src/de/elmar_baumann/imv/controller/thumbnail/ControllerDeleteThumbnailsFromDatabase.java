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
package de.elmar_baumann.imv.controller.thumbnail;

import de.elmar_baumann.imv.app.MessageDisplayer;
import de.elmar_baumann.imv.database.DatabaseImageFiles;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.panels.ThumbnailsPanel;
import de.elmar_baumann.imv.view.popupmenus.PopupMenuThumbnails;
import de.elmar_baumann.lib.io.FileUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Kontrolliert die Aktion: Lösche selektierte Thumbnails,
 * ausgelöst von {@link de.elmar_baumann.imv.view.popupmenus.PopupMenuThumbnails}.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-09-10
 */
public final class ControllerDeleteThumbnailsFromDatabase implements
        ActionListener {

    private final DatabaseImageFiles db = DatabaseImageFiles.INSTANCE;
    private final PopupMenuThumbnails popupMenu =
            PopupMenuThumbnails.INSTANCE;
    private final ThumbnailsPanel thumbnailsPanel =
            GUI.INSTANCE.getAppPanel().getPanelThumbnails();

    public ControllerDeleteThumbnailsFromDatabase() {
        listen();
    }

    private void listen() {
        popupMenu.getItemDeleteImageFromDatabase().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        deleteSelectedThumbnails();
    }

    private void deleteSelectedThumbnails() {
        if (confirmDelete()) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    List<String> files = FileUtil.getAsFilenames(
                            thumbnailsPanel.getSelectedFiles());
                    int countFiles = files.size();
                    int countDeleted = db.deleteImageFiles(files);
                    if (countDeleted != countFiles) {
                        errorMessageDeleteImageFiles(countFiles, countDeleted);
                    }
                    repaint(files);
                    thumbnailsPanel.repaint();
                }
            });
        }
    }

    private void repaint(final List<String> filenames) {
        List<String> deleted = new ArrayList<String>(filenames.size());
        for (String filename : filenames) {
            if (!db.existsFilename(filename)) {
                deleted.add(filename);
            }
        }
        thumbnailsPanel.remove(FileUtil.getAsFiles(deleted));
    }

    private boolean confirmDelete() {
        return MessageDisplayer.confirm(null,
                "ControllerDeleteThumbnailsFromDatabase.Confirm.DeleteSelectedFiles", // NOI18N
                MessageDisplayer.CancelButton.HIDE,
                thumbnailsPanel.getSelectionCount()).equals(
                MessageDisplayer.ConfirmAction.YES);
    }

    private void errorMessageDeleteImageFiles(int countFiles, int countDeleted) {
        MessageDisplayer.error(null,
                "ControllerDeleteThumbnailsFromDatabase.Error.DeleteSelectedFiles", // NOI18N
                countFiles, countDeleted);
    }
}
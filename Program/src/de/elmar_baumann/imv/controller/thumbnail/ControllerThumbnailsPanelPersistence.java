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

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.app.AppLifeCycle;
import de.elmar_baumann.imv.app.AppLog;
import de.elmar_baumann.imv.event.listener.AppExitListener;
import de.elmar_baumann.imv.event.listener.ThumbnailsPanelListener;
import de.elmar_baumann.lib.comparator.FileSort;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.panels.ThumbnailsPanel;
import de.elmar_baumann.lib.io.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Applies persistent settings to the thumbnails panel.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-10-15
 */
public final class ControllerThumbnailsPanelPersistence
        implements ThumbnailsPanelListener, AppExitListener {

    private boolean propertiesRead = false;
    private static final String KEY_SELECTED_FILES =
            "de.elmar_baumann.imv.view.controller.ControllerThumbnailsPanelPersistence.SelectedFiles"; // NOI18N
    private static final String KEY_SORT =
            "de.elmar_baumann.imv.view.controller.ControllerThumbnailsPanelPersistence.Sort"; // NOI18N
    private static final String KEY_THUMBNAIL_PANEL_VIEWPORT_VIEW_POSITION =
            "de.elmar_baumann.imv.view.panels.controller.ViewportViewPosition"; // NOI18N
    private final ThumbnailsPanel thumbnailsPanel =
            GUI.INSTANCE.getAppPanel().getPanelThumbnails();
    private List<File> persistentSelectedFiles = new ArrayList<File>();

    public ControllerThumbnailsPanelPersistence() {
        listen();
        readProperties();
    }

    private void listen() {
        thumbnailsPanel.addThumbnailsPanelListener(this);
        AppLifeCycle.INSTANCE.addAppExitListener(this);
    }

    @Override
    public void thumbnailsSelectionChanged() {
        writeSelectionToProperties();
    }

    @Override
    public void thumbnailsChanged() {
        checkFirstChange();
        UserSettings.INSTANCE.getSettings().setString(
                thumbnailsPanel.getSort().name(), KEY_SORT);
    }

    private void checkFirstChange() {
        if (!propertiesRead) {
            readSelectedFilesFromProperties();
            readViewportViewPositionFromProperties();
            propertiesRead = true;
        }
    }

    private void writeSelectionToProperties() {
        UserSettings.INSTANCE.getSettings().setStringArray(
                FileUtil.getAsFilenames(thumbnailsPanel.getSelectedFiles()),
                KEY_SELECTED_FILES);
        UserSettings.INSTANCE.writeToFile();
    }

    private void readSelectedFilesFromProperties() {
        List<Integer> indices = new ArrayList<Integer>();
        for (File file : persistentSelectedFiles) {
            int index = thumbnailsPanel.getIndexOf(file);
            if (index >= 0) {
                indices.add(index);
            }
        }
        thumbnailsPanel.setSelected(indices);
    }

    private void readProperties() {
        persistentSelectedFiles = FileUtil.getAsFiles(
                UserSettings.INSTANCE.getSettings().getStringArray(
                KEY_SELECTED_FILES));
        readSortFromProperties();
    }

    private void readSortFromProperties() {
        String name = UserSettings.INSTANCE.getSettings().getString(KEY_SORT);
        try {
            if (!name.isEmpty()) {
                thumbnailsPanel.setSort(FileSort.valueOf(name));
            }
        } catch (Exception ex) {
            AppLog.logSevere(ControllerThumbnailsPanelPersistence.class, ex);
        }
    }

    private void readViewportViewPositionFromProperties() {
        UserSettings.INSTANCE.getSettings().getScrollPane(
                GUI.INSTANCE.getAppPanel().getScrollPaneThumbnailsPanel(),
                KEY_THUMBNAIL_PANEL_VIEWPORT_VIEW_POSITION);
    }

    @Override
    public void appWillExit() {
        writeViewportViewPositionToProperties();
    }

    private void writeViewportViewPositionToProperties() {
        UserSettings.INSTANCE.getSettings().setScrollPane(
                GUI.INSTANCE.getAppPanel().getScrollPaneThumbnailsPanel(),
                KEY_THUMBNAIL_PANEL_VIEWPORT_VIEW_POSITION);
        UserSettings.INSTANCE.writeToFile();
    }
}
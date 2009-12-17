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
package de.elmar_baumann.jpt.controller.metadata;

import de.elmar_baumann.jpt.app.AppFileFilter;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.resource.GUI;
import de.elmar_baumann.jpt.helper.ExtractEmbeddedXmp;
import de.elmar_baumann.jpt.view.dialogs.FileEditorDialog;
import de.elmar_baumann.jpt.view.panels.FileEditorPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Starts a {@link de.elmar_baumann.jpt.view.dialogs.FileEditorDialog} with
 * an {@link de.elmar_baumann.jpt.helper.ExtractEmbeddedXmp} editor.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009-05-22
 */
public final class ControllerExtractEmbeddedXmp implements ActionListener {

    public ControllerExtractEmbeddedXmp() {
        listen();
    }

    private void listen() {
        GUI.INSTANCE.getAppFrame().getMenuItemExtractEmbeddedXmp().
                addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showDialog();
    }

    private void showDialog() {
        FileEditorDialog dialog =
                new FileEditorDialog(GUI.INSTANCE.getAppFrame());
        FileEditorPanel panel = dialog.getFileEditorPanel();
        panel.setEditor(new ExtractEmbeddedXmp());
        panel.setTitle(Bundle.getString(
                "ControllerExtractEmbeddedXmp.Panel.Title")); // NOI18N
        panel.setDescription(Bundle.getString(
                "ControllerExtractEmbeddedXmp.Panel.Description")); // NOI18N
        panel.setDirChooserFileFilter(AppFileFilter.ACCEPTED_IMAGE_FILE_FORMATS);
        panel.setSelectDirs(true);
        dialog.setHelpPageUrl(Bundle.getString("Help.Url.ExtractEmbeddedXmp")); // NOI18N
        dialog.setVisible(true);
    }
}
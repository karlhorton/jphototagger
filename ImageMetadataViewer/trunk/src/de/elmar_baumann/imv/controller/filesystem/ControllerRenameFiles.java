package de.elmar_baumann.imv.controller.filesystem;

import de.elmar_baumann.imv.app.AppLog;
import de.elmar_baumann.imv.database.DatabaseImageFiles;
import de.elmar_baumann.imv.event.listener.impl.ListenerProvider;
import de.elmar_baumann.imv.event.RenameFileEvent;
import de.elmar_baumann.imv.event.listener.RenameFileListener;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.dialogs.RenameDialog;
import de.elmar_baumann.imv.view.panels.ImageFileThumbnailsPanel;
import de.elmar_baumann.imv.view.popupmenus.PopupMenuThumbnails;
import de.elmar_baumann.lib.event.util.KeyEventUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 * Listens to key events of {@link ImageFileThumbnailsPanel} and when
 * <code>F2</code> was pressed shows the {@link RenameDialog} to rename the
 * selected files.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/13
 */
public final class ControllerRenameFiles
        implements ActionListener, KeyListener, RenameFileListener {

    private final ImageFileThumbnailsPanel thumbnailsPanel =
            GUI.INSTANCE.getAppPanel().getPanelThumbnails();
    private final JMenuItem menuItemRename =
            PopupMenuThumbnails.INSTANCE.getItemFileSystemRenameFiles();
    private final DatabaseImageFiles db = DatabaseImageFiles.INSTANCE;

    public ControllerRenameFiles() {
        listen();
    }

    private void listen() {
        thumbnailsPanel.addKeyListener(this);
        menuItemRename.addActionListener(this);
        ListenerProvider.INSTANCE.addRenameFileListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F2 ||
                KeyEventUtil.isControl(e, KeyEvent.VK_R)) {
            renameSelectedFiles();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuItemRename)) {
            renameSelectedFiles();
        }
    }

    private void renameSelectedFiles() {
        List<File> files = thumbnailsPanel.getSelectedFiles();
        if (files.size() > 0) {
            RenameDialog dialog = new RenameDialog();
            Collections.sort(files);
            dialog.setFiles(files);
            dialog.setEnabledTemplates(
                    thumbnailsPanel.getContent().isUniqueFileSystemDirectory());
            dialog.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(final RenameFileEvent action) {
        File oldFile = action.getOldFile();
        File newFile = action.getNewFile();
        AppLog.logInfo(ControllerRenameFiles.class, Bundle.getString(
                "ControllerRenameFiles.Info.Rename", oldFile, newFile)); // NOI18N
        db.updateRenameImageFilename(
                oldFile.getAbsolutePath(), newFile.getAbsolutePath());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                thumbnailsPanel.rename(action.getOldFile(), action.getNewFile());
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }
}

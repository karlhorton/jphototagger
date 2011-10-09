package org.jphototagger.program.module.directories;

import java.io.File;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.jphototagger.domain.thumbnails.ThumbnailsPanelSettings;
import org.jphototagger.api.image.thumbnails.OriginOfDisplayedThumbnails;
import org.jphototagger.domain.thumbnails.event.ThumbnailsPanelRefreshEvent;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.module.thumbnails.SortThumbnailsController;
import org.jphototagger.program.io.ImageFileFilterer;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.app.ui.WaitDisplay;

/**
 * Listens for selections of items in the directory tree view. A tree item
 * represents a directory. If a new item is selected, this controller sets the
 * files of the selected directory to the image file thumbnails panel.
 *
 * @author Elmar Baumann
 */
public final class DirectorySelectedController implements TreeSelectionListener {

    public DirectorySelectedController() {
        listen();
    }

    private void listen() {
        GUI.getDirectoriesTree().addTreeSelectionListener(this);
        AnnotationProcessor.process(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent evt) {
        if (evt.isAddedPath() && !DirectoriesPopupMenu.INSTANCE.isTreeSelected()) {
            setFilesToThumbnailsPanel(null);
        }
    }

    @EventSubscriber(eventClass = ThumbnailsPanelRefreshEvent.class)
    public void refresh(ThumbnailsPanelRefreshEvent evt) {
        OriginOfDisplayedThumbnails typeOfDisplayedImages = evt.getTypeOfDisplayedImages();

        if (OriginOfDisplayedThumbnails.FILES_IN_SAME_DIRECTORY.equals(typeOfDisplayedImages)) {
            setFilesToThumbnailsPanel(evt.getThumbnailsPanelSettings());
        }
    }

    private void setFilesToThumbnailsPanel(ThumbnailsPanelSettings settings) {
        EventQueueUtil.invokeInDispatchThread(new ShowThumbnails(settings));
    }

    private class ShowThumbnails implements Runnable {

        private final ThumbnailsPanelSettings panelSettings;

        ShowThumbnails(ThumbnailsPanelSettings settings) {
            panelSettings = settings;
        }

        @Override
        public void run() {
            EventQueueUtil.invokeInDispatchThread(new Runnable() {

                @Override
                public void run() {
                    showThumbnails();
                }
            });
        }

        private void showThumbnails() {
            if (GUI.getDirectoriesTree().getSelectionCount() > 0) {
                WaitDisplay.INSTANCE.show();

                File selectedDirectory = new File(getDirectorynameFromTree());
                List<File> files = ImageFileFilterer.getImageFilesOfDirectory(selectedDirectory);

                setTitle(selectedDirectory);
                SortThumbnailsController.setLastSort();
                GUI.getThumbnailsPanel().setFiles(files, OriginOfDisplayedThumbnails.FILES_IN_SAME_DIRECTORY);
                GUI.getThumbnailsPanel().apply(panelSettings);
                setMetadataEditable();
                WaitDisplay.INSTANCE.hide();
            }
        }

        private void setTitle(File selectedDirectory) {
            String title = Bundle.getString(ShowThumbnails.class, "ControllerDirectorySelected.AppFrame.Title.Directory", selectedDirectory);

            GUI.getAppFrame().setTitle(title);
        }

        private String getDirectorynameFromTree() {
            TreePath treePath = GUI.getDirectoriesTree().getSelectionPath();

            if (treePath.getLastPathComponent() instanceof File) {
                return ((File) treePath.getLastPathComponent()).getAbsolutePath();
            } else {
                return treePath.getLastPathComponent().toString();
            }
        }

        private void setMetadataEditable() {
            if (!GUI.getThumbnailsPanel().isAFileSelected()) {
                GUI.getEditPanel().setEditable(false);
            }
        }
    }
}
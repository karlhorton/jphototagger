package de.elmar_baumann.imv.datatransfer;

import de.elmar_baumann.imv.model.ListModelImageCollections;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.tasks.ImageCollectionDatabaseUtils;
import java.util.List;
import javax.swing.JList;
import javax.swing.SwingUtilities;

/**
 * Adds images to an image collection (item hitted) or creates a new one (free
 * list area hitted) if thumbnails are dropped on the list with image 
 * collections.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/24
 */
public final class TransferHandlerListImageCollections extends TransferHandlerListThumbnails {

    @Override
    protected void handleDroppedThumbnails(int itemIndex, List<String> filenames) {
        if (itemIndex >= 0) {
            addToImageCollection(itemIndex, filenames);
        } else {
            createImageCollection(filenames);
        }
    }

    private void addToImageCollection(int itemIndex, List<String> filenames) {
        boolean added =
                ImageCollectionDatabaseUtils.addImagesToCollection(
                getImageCollectionName(itemIndex), filenames);
        if (added) {
            refreshThumbnailsPanel();
        }
    }

    private void createImageCollection(final List<String> filenames) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                String newCollectionName = ImageCollectionDatabaseUtils.
                        insertImageCollection(filenames);
                if (newCollectionName != null) {
                    ((ListModelImageCollections) GUI.INSTANCE.getAppPanel().
                            getListImageCollections().getModel()).addElement(
                            newCollectionName);
                }
            }
        });
    }

    private String getImageCollectionName(int itemIndex) {
        JList list = GUI.INSTANCE.getAppPanel().getListImageCollections();
        return list.getModel().getElementAt(itemIndex).toString();
    }

    private void refreshThumbnailsPanel() {
        GUI.INSTANCE.getAppPanel().getPanelThumbnails().refresh();
    }
}

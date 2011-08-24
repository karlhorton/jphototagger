package org.jphototagger.program.controller.nometadata;

import org.jphototagger.program.controller.thumbnail.ControllerSortThumbnails;
import org.jphototagger.program.database.DatabaseImageFiles;
import org.jphototagger.domain.database.Column;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.domain.thumbnails.TypeOfDisplayedImages;
import org.jphototagger.program.view.panels.AppPanel;
import org.jphototagger.program.view.panels.ThumbnailsPanel;
import org.jphototagger.program.view.WaitDisplay;
import java.io.File;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jphototagger.lib.util.Bundle;

/**
 * Listens to selections within the list {@link AppPanel#getListNoMetadata()}
 * and when an item was selected, sets files without metadata related to the
 * selected item to the thumbnails panel.
 *
 * @author Elmar Baumann Elmar Baumann
 */
public final class ControllerNoMetadataItemSelected implements ListSelectionListener {
    public ControllerNoMetadataItemSelected() {
        listen();
    }

    private void listen() {
        GUI.getNoMetadataList().addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            setFiles();
        }
    }

    private void setFiles() {
        WaitDisplay.show();

        Object selValue = GUI.getNoMetadataList().getSelectedValue();

        if (selValue instanceof Column) {
            List<File> imageFiles = DatabaseImageFiles.INSTANCE.getImageFilesWithoutMetadataInColumn((Column) selValue);

            setTitle((Column) selValue);

            ThumbnailsPanel tnPanel = GUI.getThumbnailsPanel();

            ControllerSortThumbnails.setLastSort();
            tnPanel.setFiles(imageFiles, TypeOfDisplayedImages.MISSING_METADATA);
            WaitDisplay.hide();
        }
    }

    private void setTitle(Column column) {
        GUI.getAppFrame().setTitle(
                Bundle.getString(ControllerNoMetadataItemSelected.class,
                "ControllerNoMetadataItemSelected.AppFrame.Title.WithoutMetadata", column.getDescription()));
    }
}

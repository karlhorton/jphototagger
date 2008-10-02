package de.elmar_baumann.imagemetadataviewer.controller.files;

import de.elmar_baumann.imagemetadataviewer.UserSettings;
import de.elmar_baumann.imagemetadataviewer.controller.Controller;
import de.elmar_baumann.imagemetadataviewer.io.IoUtil;
import de.elmar_baumann.imagemetadataviewer.view.popupmenus.PopupMenuPanelThumbnails;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller für die Aktion: Dateien ausgewählter Thumbnails öffnen,
 * ausgelöst von {@link de.elmar_baumann.imagemetadataviewer.view.popupmenus.PopupMenuPanelThumbnails}.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/10
 */
public class ControllerOpenFilesWithStandardApp extends Controller
    implements ActionListener {

    private PopupMenuPanelThumbnails popup = PopupMenuPanelThumbnails.getInstance();

    public ControllerOpenFilesWithStandardApp() {
        listenToActionSource();
    }

    private void listenToActionSource() {
        popup.addActionListenerOpenFilesWithStandardApp(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStarted()) {
            openFiles();
        }
    }

    private void openFiles() {
        String allFilenames = IoUtil.getArgsAsCommandline(
            popup.getThumbnailsPanel().getSelectedFilenames());
        if (!allFilenames.isEmpty()) {
            IoUtil.startApplication(
                UserSettings.getInstance().getDefaultImageOpenApp(),
                allFilenames);
        }
    }
}

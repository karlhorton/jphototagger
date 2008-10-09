package de.elmar_baumann.imv.controller.thumbnail;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.controller.Controller;
import de.elmar_baumann.imv.io.IoUtil;
import de.elmar_baumann.imv.view.panels.ImageFileThumbnailsPanel;

/**
 * Kontroller für die Aktion: Doppelklick auf ein Thumbnail ausgelöst von
 * {@link de.elmar_baumann.imv.view.panels.ImageFileThumbnailsPanel}.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/10
 */
public class ControllerDoubleklickThumbnail extends Controller {

    private ImageFileThumbnailsPanel panel;

    public ControllerDoubleklickThumbnail(ImageFileThumbnailsPanel panel) {
        this.panel = panel;
    }

    public void doubleClickAtIndex(int index) {
        if (isStarted()) {
            openImage(index);
        }
    }

    private void openImage(int index) {
        if (panel.isThumbnailIndex(index)) {
            IoUtil.startApplication(UserSettings.getInstance().getDefaultImageOpenApp(),
                panel.getThumbnailFilenameAtIndex(index));
        }
    }
}

package de.elmar_baumann.imagemetadataviewer.controller.favoritedirectories;

import de.elmar_baumann.imagemetadataviewer.controller.Controller;
import de.elmar_baumann.imagemetadataviewer.data.FavoriteDirectory;
import de.elmar_baumann.imagemetadataviewer.model.ListModelFavoriteDirectories;
import de.elmar_baumann.imagemetadataviewer.resource.Panels;
import de.elmar_baumann.imagemetadataviewer.view.dialogs.FavoriteDirectoryPropertiesDialog;
import de.elmar_baumann.imagemetadataviewer.view.panels.AppPanel;
import de.elmar_baumann.imagemetadataviewer.view.popupmenus.PopupMenuListFavoriteDirectories;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Kontrolliert die Aktion: Favoritenverzeichnis aktualisieren.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/23
 */
public class ControllerUpdateFavoriteDirectory extends Controller
    implements ActionListener {

    private PopupMenuListFavoriteDirectories popup = PopupMenuListFavoriteDirectories.getInstance();
    private AppPanel appPanel = Panels.getInstance().getAppPanel();
    private ListModelFavoriteDirectories model = (ListModelFavoriteDirectories) appPanel.getListFavoriteDirectories().getModel();

    public ControllerUpdateFavoriteDirectory() {
        listenToActionSource();
    }

    private void listenToActionSource() {
        popup.addActionListenerUpdate(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStarted()) {
            updateFavorite();
        }
    }

    private void updateFavorite() {
        FavoriteDirectory favorite = popup.getFavoriteDirectory();
        FavoriteDirectoryPropertiesDialog dialog = new FavoriteDirectoryPropertiesDialog();
        dialog.setFavoriteName(favorite.getFavoriteName());
        dialog.setDirectoryName(favorite.getDirectoryName());
        dialog.setVisible(true);
        if (dialog.isOk()) {
            model.replaceFavorite(favorite, new FavoriteDirectory(
                dialog.getFavoriteName(),
                dialog.getDirectoryName(),
                favorite.getIndex()));
        }
    }
}

package org.jphototagger.repositoryfilebrowser;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

import javax.swing.JMenuItem;
import org.jphototagger.api.windows.MenuItemProvider;
import org.jphototagger.lib.swing.IconUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public final class BrowseRepositoryFilesAction extends AbstractAction implements MenuItemProvider {

    private static final long serialVersionUID = 1L;
    public static final BrowseRepositoryFilesAction INSTANCE = new BrowseRepositoryFilesAction();
    public static final JMenuItem MENU_ITEM = new JMenuItem(new BrowseRepositoryFilesAction());

    private BrowseRepositoryFilesAction() {
        super(Bundle.getString(BrowseRepositoryFilesAction.class, "DisplayFileBrowserAction.Name"));

        putValue(Action.SMALL_ICON, IconUtil.getImageIcon(BrowseRepositoryFilesAction.class, "icon_database.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RepositoryFileBrowserDialog dialog = new RepositoryFileBrowserDialog(null, true);

        dialog.setVisible(true);
    }

    @Override
    public JMenuItem getMenuItem() {
        return MENU_ITEM;
    }

    @Override
    public int getPosition() {
        return -1;
    }

    @Override
    public boolean isSeparatorBefore() {
        return true;
    }
}
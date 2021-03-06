package org.jphototagger.program.module.filesystem;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.Icons;

/**
 * @author Elmar Baumann
 */
public final class ShowRenameTemplatesDialogAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public ShowRenameTemplatesDialogAction() {
        super(Bundle.getString(ShowRenameTemplatesDialogAction.class, "ShowRenameTemplatesDialogAction.Name"));
        putValue(Action.SMALL_ICON, Icons.getIcon("icon_rename.png"));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        RenameTemplatesDialog dialog = new RenameTemplatesDialog();
        dialog.setVisible(true);
    }
}

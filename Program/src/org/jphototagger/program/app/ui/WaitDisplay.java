package org.jphototagger.program.app.ui;

import org.jphototagger.lib.swing.GlassPaneWaitCursor;
import org.jphototagger.program.resource.GUI;

/**
 * Displays on the (entire) application frame a wait symbol (currently a wait cursor).
 *
 * @author Elmar Baumann
 */
public final class WaitDisplay {

    private final GlassPaneWaitCursor waitCursor;
    public static final WaitDisplay INSTANCE = new WaitDisplay();

    public void show() {
        waitCursor.show();
    }

    public void hide() {
        waitCursor.hide();
    }

    public boolean isShow() {
        return waitCursor.isShow();
    }

    private WaitDisplay() {
        waitCursor = new GlassPaneWaitCursor(GUI.getAppFrame());
    }
}

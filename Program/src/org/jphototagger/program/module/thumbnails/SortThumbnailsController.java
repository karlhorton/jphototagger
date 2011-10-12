package org.jphototagger.program.module.thumbnails;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;

import javax.swing.JRadioButtonMenuItem;

import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.program.factory.ControllerFactory;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.app.ui.WaitDisplay;

/**
 * @author Elmar Baumann
 */
public final class SortThumbnailsController implements ActionListener {

    public SortThumbnailsController() {
        listen();
        GUI.getAppFrame().getMenuItemOfSortCmp(GUI.getThumbnailsPanel().getFileSortComparator()).setSelected(true);
    }

    private void listen() {
        for (JRadioButtonMenuItem item : GUI.getAppFrame().getSortMenuItems()) {
            item.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        sortThumbnails(evt);
    }

    public static void setLastSort() {
        Comparator<File> cmp = ThumbnailsPanelPersistenceController.getFileSortComparator();

        GUI.getThumbnailsPanel().setFileSortComparator(cmp);
        GUI.getAppFrame().getMenuItemOfSortCmp(cmp).setSelected(true);
    }

    private void sortThumbnails(final ActionEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                WaitDisplay.INSTANCE.show();

                JRadioButtonMenuItem item = (JRadioButtonMenuItem) evt.getSource();
                Comparator<File> sortCmp = GUI.getAppFrame().getSortCmpOfMenuItem(item);
                ThumbnailsPanel tnPanel = GUI.getThumbnailsPanel();

                ControllerFactory.INSTANCE.getController(
                        ThumbnailsPanelPersistenceController.class).setFileSortComparator(sortCmp);
                item.setSelected(true);
                tnPanel.setFileSortComparator(sortCmp);
                tnPanel.sort();
                WaitDisplay.INSTANCE.hide();
            }
        });
    }
}

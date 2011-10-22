package org.jphototagger.program.module.thumbnails;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JMenuItem;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.openide.util.Lookup;

import org.jphototagger.domain.metadata.xmp.Xmp;
import org.jphototagger.domain.thumbnails.event.ThumbnailsSelectionChangedEvent;
import org.jphototagger.lib.swing.KeyEventUtil;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.module.editmetadata.EditMetaDataPanels;
import org.jphototagger.program.module.editmetadata.EditMetaDataPanelsProvider;
import org.jphototagger.program.resource.GUI;

/**
 * @author Elmar Baumann
 */
public final class CopyPasteMetadataController implements ActionListener, KeyListener {

    private Xmp xmp;

    public CopyPasteMetadataController() {
        listen();
    }

    private void listen() {
        getCopyItem().addActionListener(this);
        getPasteItem().addActionListener(this);
        GUI.getThumbnailsPanel().addKeyListener(this);
        AnnotationProcessor.process(this);
    }

    private JMenuItem getCopyItem() {
        return ThumbnailsPopupMenu.INSTANCE.getItemCopyMetadata();
    }

    private JMenuItem getPasteItem() {
        return ThumbnailsPopupMenu.INSTANCE.getItemPasteMetadata();
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (KeyEventUtil.isMenuShortcutWithShiftDown(evt, KeyEvent.VK_C)) {
            copy();
        } else if (KeyEventUtil.isMenuShortcutWithShiftDown(evt, KeyEvent.VK_V)) {
            paste();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == getCopyItem()) {
            copy();
        } else if (evt.getSource() == getPasteItem()) {
            paste();
        }
    }

    private void copy() {
        EditMetaDataPanelsProvider provider = Lookup.getDefault().lookup(EditMetaDataPanelsProvider.class);
        EditMetaDataPanels editPanel = provider.getEditMetadataPanels();
        Xmp xmpOfEditPanel = editPanel.createXmpFromInput();
        this.xmp = new Xmp(xmpOfEditPanel);
        setPasteItemEnabled(true);
    }

    private void paste() {
        if (xmp == null) {
            return;
        }

        EditMetaDataPanelsProvider provider = Lookup.getDefault().lookup(EditMetaDataPanelsProvider.class);
        EditMetaDataPanels editPanel = provider.getEditMetadataPanels();

        if (!checkSelected() || !checkCanEdit(editPanel)) {
            return;
        }

        editPanel.setXmp(xmp);
        setPasteItemEnabled(false);
        xmp = null;
    }

    private void setPasteItemEnabled(boolean enabled) {
        JMenuItem pasteItem = getPasteItem();

        pasteItem.setEnabled(enabled);
    }

    private boolean checkSelected() {
        int selCount = GUI.getThumbnailsPanel().getSelectionCount();

        if (selCount <= 0) {
            String message = Bundle.getString(CopyPasteMetadataController.class, "CopyPasteMetadataController.Error.NoSelection");
            MessageDisplayer.error(null, message);

            return false;
        }

        return true;
    }

    private boolean checkCanEdit(EditMetaDataPanels editPanel) {
        if (!editPanel.isEditable()) {
            String message = Bundle.getString(CopyPasteMetadataController.class, "CopyPasteMetadataController.Error.NotEditable");
            MessageDisplayer.error(null, message);

            return false;
        }

        return true;
    }

    @EventSubscriber(eventClass = ThumbnailsSelectionChangedEvent.class)
    public void thumbnailsSelectionChanged(final ThumbnailsSelectionChangedEvent evt) {
        boolean aFileIsSelected = evt.isAFileSelected();
        JMenuItem copyItem = getCopyItem();

        copyItem.setEnabled(aFileIsSelected);
    }

    @Override
    public void keyTyped(KeyEvent evt) {
        // ignore
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        // ignore
    }
}

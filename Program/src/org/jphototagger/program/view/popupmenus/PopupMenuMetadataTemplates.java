/*
 * @(#)PopupMenuMetadataTemplates.java    Created on 2010-01-08
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.view.popupmenus;

import org.jphototagger.lib.event.util.KeyEventUtil;
import org.jphototagger.program.app.AppLookAndFeel;
import org.jphototagger.program.data.MetadataTemplate;
import org.jphototagger.program.resource.JptBundle;

import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

/**
 * Popup menu for {@link MetadataTemplate}s.
 *
 * @author  Elmar Baumann
 */
public final class PopupMenuMetadataTemplates extends JPopupMenu {
    private static final long                      serialVersionUID =
        5476440706471574353L;
    public static final PopupMenuMetadataTemplates INSTANCE         =
        new PopupMenuMetadataTemplates();
    private final JMenuItem itemSetToSelImages =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuMetadataTemplates.DisplayName.Action.SetToSelImages"), AppLookAndFeel
                        .getIcon("icon_image.png"));
    private final JMenuItem itemRename =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuMetadataTemplates.DisplayName.Action.Rename"), AppLookAndFeel
                        .ICON_RENAME);
    private final JMenuItem itemEdit =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuMetadataTemplates.DisplayName.Action.Edit"), AppLookAndFeel
                        .ICON_EDIT);
    private final JMenuItem itemDelete =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuMetadataTemplates.DisplayName.Action.Delete"), AppLookAndFeel
                        .ICON_DELETE);
    private final JMenuItem itemAdd =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuMetadataTemplates.DisplayName.Action.Add"), AppLookAndFeel
                        .ICON_NEW);
    private JList list;
    private int   selIndex;

    private PopupMenuMetadataTemplates() {
        addItems();
        setAccelerators();
    }

    public int getSelIndex() {
        return selIndex;
    }

    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
    }

    public JList getList() {
        return list;
    }

    public void setList(JList list) {
        this.list = list;
    }

    public JMenuItem getItemDelete() {
        return itemDelete;
    }

    public JMenuItem getItemRename() {
        return itemRename;
    }

    public JMenuItem getItemAdd() {
        return itemAdd;
    }

    public JMenuItem getItemEdit() {
        return itemEdit;
    }

    public JMenuItem getItemSetToSelImages() {
        return itemSetToSelImages;
    }

    private void addItems() {
        add(itemSetToSelImages);
        add(new Separator());
        add(itemAdd);
        add(new Separator());
        add(itemEdit);
        add(itemRename);
        add(itemDelete);
    }

    private void setAccelerators() {
        itemSetToSelImages.setAccelerator(
            KeyEventUtil.getKeyStroke(KeyEvent.VK_INSERT));
        itemAdd.setAccelerator(
            KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_N));
        itemEdit.setAccelerator(
            KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_E));
        itemRename.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_F2));
        itemDelete.setAccelerator(
            KeyEventUtil.getKeyStroke(KeyEvent.VK_DELETE));
    }
}
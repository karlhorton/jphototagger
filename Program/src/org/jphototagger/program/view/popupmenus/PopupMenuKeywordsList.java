/*
 * @(#)PopupMenuKeywordsList.java    Created on 2010-01-07
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
import org.jphototagger.program.resource.JptBundle;

import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

/**
 * Do not use this class as template for implemention! Instead extend
 * {@link org.jphototagger.lib.event.listener.PopupMenuList}.
 *
 * Popup menu for a keywords list, such as
 * {@link org.jphototagger.program.view.panels.KeywordsPanel#getList()}.
 *
 * @author  Elmar Baumann
 */
public final class PopupMenuKeywordsList extends JPopupMenu {
    private static final long                 serialVersionUID =
        -552638878495121120L;
    public static final PopupMenuKeywordsList INSTANCE         =
        new PopupMenuKeywordsList();
    private final JMenuItem itemInsert =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuKeywordsList.DisplayName.Action.Insert"), AppLookAndFeel
                        .ICON_NEW);
    private final JMenuItem itemRename =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuKeywordsList.DisplayName.Action.Rename"), AppLookAndFeel
                        .ICON_RENAME);
    private final JMenuItem itemRemoveFromEditPanel =
        new JMenuItem(
            JptBundle.INSTANCE.getString(
                "PopupMenuKeywordsList.DisplayName.ActionRemoveFromEditPanel"));
    private final JMenuItem itemEditSynonyms =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuKeywordsList.DisplayName.Action.EditSynonyms"), AppLookAndFeel
                        .ICON_EDIT);
    private final JMenuItem itemDisplayImages =
        new JMenuItem(
            JptBundle.INSTANCE.getString(
                "PopupMenuKeywordsList.DisplayName.Action.DisplayImages"));
    private final JMenuItem itemDelete =
        new JMenuItem(
            JptBundle.INSTANCE
                .getString(
                    "PopupMenuKeywordsList.DisplayName.Action.Delete"), AppLookAndFeel
                        .ICON_DELETE);
    private final JMenuItem itemAddToEditPanel =
        new JMenuItem(
            JptBundle.INSTANCE.getString(
                "PopupMenuKeywordsList.DisplayName.ActionAddToEditPanel"));
    private JList list;
    private int   selIndex;

    private PopupMenuKeywordsList() {
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

    public JMenuItem getItemDisplayImages() {
        return itemDisplayImages;
    }

    public JMenuItem getItemEditSynonyms() {
        return itemEditSynonyms;
    }

    public JMenuItem getItemInsert() {
        return itemInsert;
    }

    public JMenuItem getItemAddToEditPanel() {
        return itemAddToEditPanel;
    }

    public JMenuItem getItemRemoveFromEditPanel() {
        return itemRemoveFromEditPanel;
    }

    private void addItems() {
        add(itemInsert);
        add(itemRename);
        add(itemDelete);
        add(new Separator());
        add(itemEditSynonyms);
        add(new Separator());
        add(itemAddToEditPanel);
        add(itemRemoveFromEditPanel);
        add(new Separator());
        add(itemDisplayImages);
    }

    private void setAccelerators() {
        itemInsert.setAccelerator(
            KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_N));
        itemRename.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_F2));
        itemDelete.setAccelerator(
            KeyEventUtil.getKeyStroke(KeyEvent.VK_DELETE));
        itemEditSynonyms.setAccelerator(
            KeyEventUtil.getKeyStrokeMenuShortcutWithAltDown(KeyEvent.VK_S));
        itemAddToEditPanel.setAccelerator(
            KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_B));
        itemRemoveFromEditPanel.setAccelerator(
            KeyEventUtil.getKeyStroke(KeyEvent.VK_BACK_SPACE));
    }
}
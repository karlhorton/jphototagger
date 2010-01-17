/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.controller.keywords.tree;

import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.resource.GUI;
import de.elmar_baumann.jpt.view.panels.AppPanel;
import de.elmar_baumann.lib.componentutil.TreeUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/**
 * Listens to {@link AppPanel#getToggleButtonSelKeywords()} and expands or
 * collapses all keyword nodes on action.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2010-01-15
 */
public final class ControllerToggleButtonSelKeywords implements ActionListener {

    private final JToggleButton toggleButtonExpandAllNodesSelKeywords = GUI.INSTANCE.getAppPanel().getToggleButtonSelKeywords();

    public ControllerToggleButtonSelKeywords() {
        listen();
    }

    private void listen() {
        toggleButtonExpandAllNodesSelKeywords.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean selected = toggleButtonExpandAllNodesSelKeywords.isSelected();
        TreeUtil.expandAll(GUI.INSTANCE.getAppPanel().getTreeSelKeywords(), selected);
        toggleButtonExpandAllNodesSelKeywords.setText(
                selected
                ? Bundle.getString("KeywordsPanel.ButtonToggleExpandAllNodes.Selected")
                : Bundle.getString("KeywordsPanel.ButtonToggleExpandAllNodes.DeSelected"));
    }

}
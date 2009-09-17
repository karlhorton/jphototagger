/*
 * JavaStandardLibrary JSL - subproject of JPhotoTagger
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
package de.elmar_baumann.lib.model;

import java.util.Collections;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * A mutable tree node sorting it's children.
 *
 * Compares the user object's strings ignoring the case.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009-07-02
 */
public final class TreeNodeSortedChildren extends DefaultMutableTreeNode
        implements Comparable {

    public TreeNodeSortedChildren() {
    }

    public TreeNodeSortedChildren(Object userObject) {
        super(userObject);
    }

    public TreeNodeSortedChildren(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(final MutableTreeNode newChild) {
        super.add(newChild);
        Collections.sort(this.children);
    }

    @Override
    public int compareTo(final Object o) {
        return this.toString().compareToIgnoreCase(o.toString());
    }
}
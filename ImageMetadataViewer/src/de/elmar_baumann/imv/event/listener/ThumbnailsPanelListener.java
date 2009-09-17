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
package de.elmar_baumann.imv.event.listener;

/**
 * Listens to {@link de.elmar_baumann.imv.view.panels.ThumbnailsPanel}.
 * 
 * @author Elmar Baumann <eb@elmar-baumann.de>
 */
public interface ThumbnailsPanelListener {

    /**
     * Thumbnails were selected or deselected.
     */
    public void thumbnailsSelectionChanged();

    /**
     * Count and/or order of the thumbnails changed.
     */
    public void thumbnailsChanged();
}
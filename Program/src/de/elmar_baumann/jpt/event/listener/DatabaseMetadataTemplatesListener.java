/*
 * @(#)DatabaseMetadataTemplatesListener.java    Created on 2010-01-05
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

package de.elmar_baumann.jpt.event.listener;

import de.elmar_baumann.jpt.data.MetadataTemplate;

/**
 * Listens to events in
 * {@link de.elmar_baumann.jpt.database.DatabaseMetadataTemplates}.
 *
 * @author  Elmar Baumann
 */
public interface DatabaseMetadataTemplatesListener {

    /**
     * Called if a template was deleted from
     * {@link de.elmar_baumann.jpt.database.DatabaseMetadataTemplates}.
     *
     * @param template  template
     */
    public void templateDeleted(MetadataTemplate template);

    /**
     * Called if a template was inserted into
     * {@link de.elmar_baumann.jpt.database.DatabaseMetadataTemplates}.
     *
     * @param template inserted template
     */
    public void templateInserted(MetadataTemplate template);

    /**
     * Called if a template was updated in
     * {@link de.elmar_baumann.jpt.database.DatabaseMetadataTemplates}.
     *
     * @param oldTemplate     old template before update
     * @param updatedTemplate updated template
     */
    public void templateUpdated(MetadataTemplate oldTemplate,
                                MetadataTemplate updatedTemplate);

    /**
     * Called if a template was renamed in
     * {@link de.elmar_baumann.jpt.database.DatabaseMetadataTemplates}.
     *
     * @param oldName old template name
     * @param newName new template name
     */
    public void templateRenamed(String oldName, String newName);
}

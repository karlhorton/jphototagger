/*
 * JPhotoTagger tags and finds images fast.
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

package de.elmar_baumann.jpt.database.metadata.file;

import de.elmar_baumann.jpt.database.metadata.Column;
import de.elmar_baumann.jpt.database.metadata.Column.DataType;
import de.elmar_baumann.jpt.resource.JptBundle;

/**
 * Tabellenspalte <code>filename</code> der Tabelle <code>files</code>.
 *
 * @author  Elmar Baumann
 * @version 2007-07-29
 */
public final class ColumnFilesFilename extends Column {
    public static final ColumnFilesFilename INSTANCE =
        new ColumnFilesFilename();

    private ColumnFilesFilename() {
        super("filename", "files", DataType.STRING);
        setLength(512);
        setDescription(
            JptBundle.INSTANCE.getString("ColumnFilesFilename.Description"));
    }
}

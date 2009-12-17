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
package de.elmar_baumann.jpt.database.metadata.collections;

import de.elmar_baumann.jpt.database.metadata.Column;
import de.elmar_baumann.jpt.database.metadata.Column.DataType;

/**
 * Spalte <code>id_collectionnnames</code> der Tabelle <code>collections</code>.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-09-07
 */
public final class ColumnCollectionsIdCollectionNames extends Column {

    public static final ColumnCollectionsIdCollectionNames INSTANCE = new ColumnCollectionsIdCollectionNames();

    private ColumnCollectionsIdCollectionNames() {
        super(
            TableCollections.INSTANCE,
            "id_collectionnnames", // NOI18N
            DataType.BIGINT);

        setIsPrimaryKey(true);
    }
}
/*
 * @(#)ColumnXmpPhotoshopCaptionwriter.java    Created on 2008-08-23
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

package org.jphototagger.program.database.metadata.xmp;

import org.jphototagger.program.database.metadata.Column;
import org.jphototagger.program.database.metadata.Column.DataType;
import org.jphototagger.program.resource.JptBundle;

/**
 * Spalte <code>photoshop_captionwriters</code> der Tabelle <code>xmp</code>.
 *
 * @author Elmar Baumann
 */
public final class ColumnXmpPhotoshopCaptionwriter extends Column {
    public static final ColumnXmpPhotoshopCaptionwriter INSTANCE =
        new ColumnXmpPhotoshopCaptionwriter();

    private ColumnXmpPhotoshopCaptionwriter() {
        super("captionwriter", "photoshop_captionwriters", DataType.STRING);
        setLength(32);
        setDescription(
            JptBundle.INSTANCE.getString(
                "ColumnXmpPhotoshopCaptionwriter.Description"));
        setLongerDescription(
            JptBundle.INSTANCE.getString(
                "ColumnXmpPhotoshopCaptionwriter.LongerDescription"));
    }
}

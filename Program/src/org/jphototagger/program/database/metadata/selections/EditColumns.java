/*
 * @(#)EditColumns.java    Created on 2008-10-05
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

package org.jphototagger.program.database.metadata.selections;

import org.jphototagger.program.database.metadata.Column;
import org.jphototagger.program.database.metadata.selections.EditHints
    .SizeEditField;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcCreator;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcDescription;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcRights;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcSubjectsSubject;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcTitle;
import org.jphototagger.program.database.metadata.xmp
    .ColumnXmpIptc4XmpCoreDateCreated;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpIptc4xmpcoreLocation;
import org.jphototagger.program.database.metadata.xmp
    .ColumnXmpPhotoshopAuthorsposition;
import org.jphototagger.program.database.metadata.xmp
    .ColumnXmpPhotoshopCaptionwriter;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCity;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCountry;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCredit;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopHeadline;
import org.jphototagger.program.database.metadata.xmp
    .ColumnXmpPhotoshopInstructions;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopSource;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopState;
import org.jphototagger.program.database.metadata.xmp
    .ColumnXmpPhotoshopTransmissionReference;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpRating;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Supported XMP columns for editing and updating XMP sidecar files.
 *
 * @author  Elmar Baumann
 */
public final class EditColumns {
    private static final Map<Column, EditHints> EDIT_HINT_OF_COLUMN =
        new LinkedHashMap<Column, EditHints>();

    static {
        EditHints notRepeatableHintSmall = new EditHints(false,
                                               SizeEditField.SMALL);
        EditHints notRepeatableHintLarge = new EditHints(false,
                                               SizeEditField.LARGE);
        EditHints repeatableHint = new EditHints(true, SizeEditField.LARGE);

        EDIT_HINT_OF_COLUMN.put(ColumnXmpDcSubjectsSubject.INSTANCE,
                                repeatableHint);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpDcTitle.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpDcDescription.INSTANCE,
                                notRepeatableHintLarge);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopHeadline.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpIptc4xmpcoreLocation.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpIptc4XmpCoreDateCreated.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopAuthorsposition.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpDcCreator.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopCity.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopState.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopCountry.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpDcRights.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopCredit.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopSource.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(
            ColumnXmpPhotoshopTransmissionReference.INSTANCE,
            notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopInstructions.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpPhotoshopCaptionwriter.INSTANCE,
                                notRepeatableHintSmall);
        EDIT_HINT_OF_COLUMN.put(ColumnXmpRating.INSTANCE,
                                notRepeatableHintSmall);
    }

    public static List<Column> get() {
        return new ArrayList<Column>(EDIT_HINT_OF_COLUMN.keySet());
    }

    public static EditHints getEditHints(Column column) {
        return EDIT_HINT_OF_COLUMN.get(column);
    }

    private EditColumns() {}
}
package org.jphototagger.program.database.metadata.mapping;

import org.jphototagger.program.database.metadata.Column;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcCreator;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcDescription;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcRights;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcSubjectsSubject;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpDcTitle;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpIptc4XmpCoreDateCreated;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpIptc4xmpcoreLocation;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpLastModified;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopAuthorsposition;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCaptionwriter;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCity;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCountry;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopCredit;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopHeadline;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopInstructions;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopSource;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopState;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpPhotoshopTransmissionReference;
import org.jphototagger.program.database.metadata.xmp.ColumnXmpRating;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns, whether a XMP column has repeatable values.
 *
 * @author Elmar Baumann
 */
public final class XmpRepeatableValues {
    private static final Map<Column, Boolean> IS_REPEATABLE = new HashMap<Column, Boolean>();

    static {
        IS_REPEATABLE.put(ColumnXmpDcCreator.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpDcDescription.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpDcRights.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpDcSubjectsSubject.INSTANCE, true);
        IS_REPEATABLE.put(ColumnXmpDcTitle.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpIptc4xmpcoreLocation.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpIptc4XmpCoreDateCreated.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopAuthorsposition.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopCaptionwriter.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopCity.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopCountry.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopCredit.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopHeadline.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopInstructions.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopSource.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopState.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpPhotoshopTransmissionReference.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpLastModified.INSTANCE, false);
        IS_REPEATABLE.put(ColumnXmpRating.INSTANCE, false);
    }

    /**
     * Returns, whether a XMP column has repeatable values.
     *
     * @param  xmpColumn  XMP column
     * @return true if the column contains repeatable values
     * @throws IllegalArgumentException if there is no information whether
     *         the column has repeatable values
     */
    public static boolean isRepeatable(Column xmpColumn) {
        if (xmpColumn == null) {
            throw new NullPointerException("xmpColumn == null");
        }

        Boolean repeatable = IS_REPEATABLE.get(xmpColumn);

        if (repeatable == null) {
            throw new IllegalArgumentException("Unknown column: " + xmpColumn);
        }

        return repeatable;
    }

    private XmpRepeatableValues() {}
}

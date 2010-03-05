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
package de.elmar_baumann.jpt.image.metadata.exif.formatter;

import de.elmar_baumann.jpt.image.metadata.exif.Ensure;
import de.elmar_baumann.jpt.image.metadata.exif.datatype.ExifShort;
import de.elmar_baumann.jpt.image.metadata.exif.ExifTag;

/**
 * Formats an EXIF entry of the dataType {@code ExifTag.Id#ISO_SPEED_RATINGS}.
 *
 * @author  Elmar Baumann
 * @version 2009-06-10
 */
public final class ExifFormatterIsoSpeedRatings extends ExifFormatter {

    public static final  ExifFormatterIsoSpeedRatings INSTANCE = new ExifFormatterIsoSpeedRatings();
    private static final String                       POSTFIX  = " ISO";

    private ExifFormatterIsoSpeedRatings() {
    }

    @Override
    public String format(ExifTag exifTag) {

        Ensure.exifTagId(exifTag, ExifTag.Id.ISO_SPEED_RATINGS);

        if (ExifShort.byteCountOk(exifTag.rawValue())) {

            ExifShort es = new ExifShort(exifTag.rawValue(),exifTag.byteOrder());

            return Integer.toString(es.value()) + POSTFIX;
        }
        return "?" + POSTFIX;
    }
}

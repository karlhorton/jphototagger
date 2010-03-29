/*
 * @(#)ExifFormatterGpsSatellites.java    Created on 2009-06-10
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

package org.jphototagger.program.image.metadata.exif.formatter;

import org.jphototagger.program.image.metadata.exif.datatype.ExifAscii;
import org.jphototagger.program.image.metadata.exif.Ensure;
import org.jphototagger.program.image.metadata.exif.ExifTag;

/**
 * Formats an EXIF entry of the dataType {@code ExifTag.Id#GPS_SATELLITES}.
 *
 * @author  Elmar Baumann
 */
public final class ExifFormatterGpsSatellites extends ExifFormatter {
    public static final ExifFormatterGpsSatellites INSTANCE =
        new ExifFormatterGpsSatellites();

    private ExifFormatterGpsSatellites() {}

    @Override
    public String format(ExifTag exifTag) {
        Ensure.exifTagId(exifTag, ExifTag.Id.GPS_SATELLITES);

        return ExifAscii.decode(exifTag.rawValue());
    }
}
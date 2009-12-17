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
package de.elmar_baumann.jpt.image.metadata.exif.format;

import de.elmar_baumann.jpt.image.metadata.exif.ExifTag;
import de.elmar_baumann.jpt.image.metadata.exif.IdfEntryProxy;

/**
 * Formats an EXIF entry of the type {@link ExifTag#FILE_SOURCE}.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009-06-10
 */
public final class ExifFormatterFileSource extends ExifFormatter {

    public static final ExifFormatterFileSource INSTANCE =
            new ExifFormatterFileSource();

    private ExifFormatterFileSource() {
    }

    @Override
    public String format(IdfEntryProxy entry) {
        if (entry.getTag() != ExifTag.FILE_SOURCE.getId())
            throw new IllegalArgumentException("Wrong tag: " + entry); // NOI18N
        byte[] rawValue = entry.getRawValue();
        if (rawValue.length >= 1) {
            int value = rawValue[0];
            if (value == 3) {
                return TRANSLATION.translate("FileSourceDigitalCamera"); // NOI18N
            }
        }
        return "?"; // NOI18N
    }
}
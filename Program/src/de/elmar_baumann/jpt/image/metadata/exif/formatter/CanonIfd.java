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
package de.elmar_baumann.jpt.image.metadata.exif.formatter;

import de.elmar_baumann.jpt.image.metadata.exif.datatype.ExifDataType;
import de.elmar_baumann.jpt.image.metadata.exif.datatype.ExifDatatypeUtil;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2010-01-12
 */
public final class CanonIfd {

    private final byte[]    rawValue;
    private final ByteOrder byteOrder;
    private final int       entryCount;
    private final Entry[]   entries;
    private final Map<Integer, Entry> entryOfTag = new HashMap<Integer, Entry>();

    public static class Entry {
        private final int    tag;
        private final int    fieldType;
        private final int    valueNumber;
        private final int    valueOffset;
        private final byte[] raw;

        public Entry(int tag, int fieldType, int valueNumber, int valueOffset, byte[] raw) {
            this.tag         = tag;
            this.fieldType   = fieldType;
            this.valueNumber = valueNumber;
            this.valueOffset = valueOffset;
            this.raw         = copy(raw);
        }

        public int getFieldType() {
            return fieldType;
        }

        public ExifDataType dataType() {
            return ExifDataType.fromType(fieldType);
        }

        public byte[] getRaw() {
            byte[] r = new byte[12];
            System.arraycopy(raw, 0, r, 0, 12);
            return r;
        }

        public int getTag() {
            return tag;
        }

        public int getValueNumber() {
            return valueNumber;
        }

        public int getValueOffset() {
            return valueOffset;
        }
    }

    public CanonIfd(byte[] rawValue, ByteOrder byteOrder) {
        this.rawValue   = copy(rawValue);
        this.byteOrder  = byteOrder;
        this.entryCount = entryCountFromRaw();
        this.entries    = new Entry[entryCount];
        setEntries();
    }

    private static byte[] copy(byte[] bytes) {
        byte[] copy = new byte[bytes.length];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);
        return copy;
    }

    private int entryCountFromRaw() {
        byte[] raw = new byte[2];
        System.arraycopy(rawValue, 0, raw, 0, 2);
        return ExifDatatypeUtil.shortFromRawValue(raw, byteOrder);
    }

    private void setEntries() {
        assert rawValue.length >= 2 + entryCount * 12;
        int entryIndex = 0;
        for (int i = 0; i < entryCount; i++) {
            byte[] r = new byte[12];
            System.arraycopy(rawValue, 2 + i * 12, r, 0, 12);
            Entry entry = createEntry(r);
            entries[entryIndex++] = entry;
            entryOfTag.put(entry.getTag(), entry);
        }
    }

    private Entry createEntry(byte[] raw) {
        assert raw.length == 12;

        byte[] tagBytes         = new byte[2];
        byte[] fieldTypeBytes   = new byte[2];
        byte[] valueNumberBytes = new byte[4];
        byte[] valueOffsetBytes = new byte[4];

        System.arraycopy(raw, 0, tagBytes        , 0, 2);
        System.arraycopy(raw, 2, fieldTypeBytes  , 0, 2);
        System.arraycopy(raw, 4, valueNumberBytes, 0, 4);
        System.arraycopy(raw, 8, valueOffsetBytes, 0, 4);

        int          tag         = ExifDatatypeUtil.shortFromRawValue(tagBytes        , byteOrder);
        int          fieldType   = ExifDatatypeUtil.shortFromRawValue(fieldTypeBytes  , byteOrder);
        ExifDataType dataType    = ExifDataType.fromType(fieldType);
        int          valueNumber = ExifDatatypeUtil.intFromRawValue  (valueNumberBytes, byteOrder);
        int          offsetBytes = dataType.bitCount() * valueNumber > 32
                                      ? ExifDatatypeUtil.intFromRawValue(valueOffsetBytes, byteOrder)
                                      : -1;

        return new Entry(tag, fieldType, valueNumber, offsetBytes, raw);
    }

    public int getEntryCount() {
        return entryCount;
    }

    public byte[] getRawValueOfTag(int tag) {
        Entry entry = entryOfTag.get(tag);

        if (entry == null) return null;

        int byteOffset = entry.getValueOffset();
        int byteCount  = entry.getValueNumber() * entry.dataType().bitCount() / 8;

        if (rawValue.length < byteOffset + byteCount) return null;

        byte[] raw = new byte[byteCount];
        System.arraycopy(rawValue, byteOffset, raw, 0, byteCount);
        return raw;
    }
}
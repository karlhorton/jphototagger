/*
 * JavaStandardLibrary JSL - subproject of JPhotoTagger
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
package de.elmar_baumann.lib.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * Sorting of files.
 * 
 * @author Elmar Baumann <eb@elmar-baumann.de>
 */
public enum FileSort {

    NAMES_ASCENDING(ComparatorFilesNames.ASCENDING_IGNORE_CASE),
    NAMES_DESCENDING(ComparatorFilesNames.DESCENDING_IGNORE_CASE),
    TYPES_ASCENDING(ComparatorFilesSuffixes.ASCENDING_IGNORE_CASE),
    TYPES_DESCENDING(ComparatorFilesSuffixes.DESCENDING_IGNORE_CASE),
    LAST_MODIFIED_ASCENDING(ComparatorFilesLastModified.ASCENDING),
    LAST_MODIFIED_DESCENDING(ComparatorFilesLastModified.DESCENDING);
    private final Comparator<File> comparator;

    private FileSort(Comparator<File> comparator) {
        this.comparator = comparator;
    }

    public Comparator<File> getComparator() {
        return comparator;
    }
}
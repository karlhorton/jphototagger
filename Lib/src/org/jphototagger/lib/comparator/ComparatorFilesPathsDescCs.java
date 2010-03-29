/*
 * @(#)ComparatorFilesPathsDescCs.java    Created on 2009-12-14
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

package org.jphototagger.lib.comparator;

import org.jphototagger.lib.util.ClassEquality;

import java.io.File;
import java.io.Serializable;

import java.util.Comparator;

/**
 * Compares the absolute path names of two files descending case sensitive.
 *
 * @author  Elmar Baumann
 */
public final class ComparatorFilesPathsDescCs extends ClassEquality
        implements Comparator<File>, Serializable {
    private static final long serialVersionUID = -6952867012863772402L;

    @Override
    public int compare(File leftFile, File rightFile) {
        return leftFile.getAbsolutePath().compareTo(
            rightFile.getAbsolutePath()) * -1;
    }
}
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
package de.elmar_baumann.lib.io.filefilter;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.filechooser.FileSystemView;

/**
 * Accepts only directories.
 *
 * All functions with object-reference-parameters are throwing a
 * <code>NullPointerException</code> if an object reference is null and it is
 * not documentet that it can be null.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-07-23
 */
public final class DirectoryFilter implements java.io.FileFilter {

    /**
     * Instance which accepts hidden files.
     */
    public static final DirectoryFilter ACCEPT_HIDDEN_FILES =
            new DirectoryFilter(EnumSet.of(Option.ACCEPT_HIDDEN_FILES));
    /**
     * Instance which rejects hidden files.
     */
    public static final DirectoryFilter REJECT_HIDDEN_FILES =
            new DirectoryFilter(EnumSet.of(Option.REJECT_HIDDEN_FILES));
    private static final FileSystemView FILE_SYSTEM_VIEW =
            FileSystemView.getFileSystemView();
    private final Set<Option> options;

    public enum Option {

        ACCEPT_HIDDEN_FILES,
        REJECT_HIDDEN_FILES,
    }

    /**
     * Constructor.
     * 
     * @param options  options
     */
    public DirectoryFilter(Set<Option> options) {
        if (options == null)
            throw new NullPointerException("options == null"); // NOI18N

        this.options = options;
    }

    @Override
    public boolean accept(File file) {
        boolean isDirectory = file.isDirectory();
        return options.contains(Option.ACCEPT_HIDDEN_FILES)
               ? isDirectory
               : isDirectory && !FILE_SYSTEM_VIEW.isHiddenFile(file);
    }

    /**
     * Returns a file filter for f file chooser.
     *
     * @param  description  description
     * @return file filter
     */
    public javax.swing.filechooser.FileFilter forFileChooser(String description) {
        return new FileChooserFilter(this, description);
    }
}
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
package de.elmar_baumann.lib.clipboard;

import de.elmar_baumann.lib.datatransfer.TransferUtil;
import de.elmar_baumann.lib.datatransfer.TransferableFileCollection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

/**
 *
 * All functions with object-reference-parameters are throwing a
 * <code>NullPointerException</code> if an object reference is null and it is
 * not documentet that it can be null.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-10-26
 */
public final class ClipboardUtil {

    private static final DataFlavor STRING_FLAVOR    = DataFlavor.stringFlavor;
    private static final DataFlavor FILE_LIST_FLAVOR = DataFlavor.javaFileListFlavor;
    private static final DataFlavor URI_LIST_FLAVOR  = TransferUtil.getUriListFlavor();

    /**
     * Copies files to the system clipboard.
     *
     * @param files  files
     * @param owner  owner of the clipboard, can be null
     */
    public static void copyToSystemClipboard(List<File> files, ClipboardOwner owner) {
        if (files == null) throw new NullPointerException("files == null");

        copyToClipboard(files, Toolkit.getDefaultToolkit().getSystemClipboard(), owner);
    }

    /**
     * Copies files to a clipboard.
     *
     * @param files      files
     * @param clipboard  clipboard
     * @param owner      owner of the clipboard, can be null
     */
    public static void copyToClipboard(List<File> files, Clipboard clipboard, ClipboardOwner owner) {
        if (files     == null) throw new NullPointerException("files == null");
        if (clipboard == null) throw new NullPointerException("clipboard == null");

        clipboard.setContents(new TransferableFileCollection(files), owner);
    }

    /**
     * Returns a list of files from the system clipboard.
     *
     * @param delimiter delimiter
     * @return          list of files or null if no files in the clipboard
     */
    public static List<File> getFilesFromSystemClipboard(TransferUtil.FilenameDelimiter delimiter) {
        if (delimiter == null) throw new NullPointerException("delimiter == null");

        return getFilesFromClipboard(Toolkit.getDefaultToolkit().getSystemClipboard(), delimiter);
    }

    /**
     * Returns a list of files from a clipboard.
     *
     * @param  clipboard  clipboard
     * @param  delimiter  delimiter
     * @return                      list of files or null if no files in the
     *                              clipboard
     */
    public static List<File> getFilesFromClipboard(Clipboard clipboard, TransferUtil.FilenameDelimiter delimiter) {
        if (clipboard == null) throw new NullPointerException("files == null");
        if (delimiter == null) throw new NullPointerException("delimiter == null");

        List<File>   files        = null;
        DataFlavor[] flavors      = clipboard.getAvailableDataFlavors();
        Transferable transferable = clipboard.getContents(ClipboardUtil.class);

        if (TransferUtil.isDataFlavorSupported(flavors, FILE_LIST_FLAVOR)) {
            return TransferUtil.getFilesFromJavaFileList(transferable);
        } else if (TransferUtil.isDataFlavorSupported(flavors, URI_LIST_FLAVOR)) {
            return TransferUtil.getFilesFromUriList(transferable);
        } else if (TransferUtil.isDataFlavorSupported(flavors, STRING_FLAVOR)) {
            return TransferUtil.getFilesFromTokenString(transferable, delimiter);
        }
        return files;
    }

    private ClipboardUtil() {
    }
}

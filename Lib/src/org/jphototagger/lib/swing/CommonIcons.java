package org.jphototagger.lib.swing;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import org.jphototagger.api.preferences.CommonPreferences;

/**
 * @author Elmar Baumann
 */
public final class CommonIcons {

    public static final Icon ICON_FOLDER = getScaledFolderIcon();
    public static final Icon ICON_FILE = getScaledFileIcon();
    public static final boolean FONTS_SCALED = CommonPreferences.getFontScale() >= 1.5;
    private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    private static Icon getScaledFolderIcon() {
        return org.jphototagger.resources.Icons.getIcon("icon_folder.png");
    }

    private static Icon getScaledFileIcon() {
        return org.jphototagger.resources.Icons.getIcon("icon_file.png");
    }

    /**
     * @param file
     * @return Scaled icon if fonts scaled, else icon from FileSystemView
     */
    public static Icon getIcon(File file) {
        return file == null
                ? null
                : FONTS_SCALED
                ? (file.isDirectory() ? ICON_FOLDER : ICON_FILE)
                : FILE_SYSTEM_VIEW.getSystemIcon(file);

    }

    private CommonIcons() {
    }
}

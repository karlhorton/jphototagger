package org.jphototagger.lib.swing;

import java.awt.Component;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renders an file specific icon for cell values that are an instance of
 * {@code java.io.File}. Uses
 * {@code javax.swing.filechooser.FileSystemView#getSystemIcon(java.io.File)}.
 *
 * @author Elmar Baumann
 */
public final class FileSystemListCellRenderer extends DefaultListCellRenderer {

    private static final Object MONITOR = new Object();
    private static final long serialVersionUID = 1L;
    private final boolean absolutePathName;

    /**
     * Constructor.
     *
     * @param absolutePathName true, if the absolute path shall be displayed and
     *                     false, if only the file name shall be displayed.
     *                     Default: false (only the file name shall be displayed).
     */
    public FileSystemListCellRenderer(boolean absolutePathName) {
        this.absolutePathName = absolutePathName;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof File) {
            File file = (File) value;

            if (file.exists()) {
                synchronized (MONITOR) {
                    try {
                        label.setIcon(CommonIcons.getIcon(file));
                    } catch (Throwable t) {
                        Logger.getLogger(FileSystemListCellRenderer.class.getName()).log(Level.WARNING, null, t);
                    }
                }
            }

            label.setText(absolutePathName
                    ? file.getAbsolutePath()
                    : file.getName());
        }

        return label;
    }
}

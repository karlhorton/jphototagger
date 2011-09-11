package org.jphototagger.program.exporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.jphototagger.domain.repository.Exporter;
import org.jphototagger.domain.repository.FileExcludePatternRepository;
import org.jphototagger.lib.io.FileUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.xml.bind.XmlObjectExporter;
import org.jphototagger.program.app.AppLookAndFeel;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 *
 * @author Elmar Baumann
 */
@ServiceProvider(service = Exporter.class)
public final class FileExcludePatternsExporter implements Exporter {

    public static final String DEFAULT_FILENAME = "JptFileExludePatterns.xml";
    public static final String DISPLAY_NAME = Bundle.getString(FileExcludePatternsExporter.class, "FileExcludePatternsExporter.DisplayName");
    public static final FileFilter FILE_FILTER = new FileNameExtensionFilter(Bundle.getString(FileExcludePatternsExporter.class, "FileExcludePatternsExporter.DisplayName.FileFilter"), "xml");
    public static final ImageIcon ICON = AppLookAndFeel.getIcon("icon_export.png");
    public static final int POSITION = 100;
    private final FileExcludePatternRepository repo = Lookup.getDefault().lookup(FileExcludePatternRepository.class);

    @Override
    public void exportFile(File file) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }

        File xmlFile = FileUtil.ensureSuffix(file, ".xml");

        try {
            List<String> patterns = repo.getAllFileExcludePatterns();

            XmlObjectExporter.export(new CollectionWrapper(StringWrapper.getWrappedStrings(patterns)), xmlFile);
        } catch (Exception ex) {
            Logger.getLogger(FileExcludePatternsExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public FileFilter getFileFilter() {
        return FILE_FILTER;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getDefaultFilename() {
        return DEFAULT_FILENAME;
    }

    @XmlRootElement
    public static class CollectionWrapper {

        @XmlElementWrapper(name = "FileExludePatterns")
        @XmlElement(type = StringWrapper.class)
        private final ArrayList<StringWrapper> collection = new ArrayList<StringWrapper>();

        public CollectionWrapper() {
        }

        public CollectionWrapper(Collection<StringWrapper> collection) {
            this.collection.addAll(collection);
        }

        public List<StringWrapper> getCollection() {
            return new ArrayList<StringWrapper>(collection);
        }
    }

    @Override
    public boolean isJPhotoTaggerData() {
        return true;
    }

    @Override
    public int getPosition() {
        return POSITION;
    }
}

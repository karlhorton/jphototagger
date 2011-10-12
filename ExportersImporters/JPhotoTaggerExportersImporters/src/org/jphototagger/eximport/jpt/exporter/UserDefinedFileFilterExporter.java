package org.jphototagger.eximport.jpt.exporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import org.jphototagger.domain.filefilter.UserDefinedFileFilter;
import org.jphototagger.domain.repository.RepositoryDataExporter;
import org.jphototagger.domain.repository.UserDefinedFileFiltersRepository;
import org.jphototagger.lib.io.FileUtil;
import org.jphototagger.lib.swing.IconUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.xml.bind.XmlObjectExporter;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = RepositoryDataExporter.class)
public final class UserDefinedFileFilterExporter implements RepositoryDataExporter {

    public static final String DEFAULT_FILENAME = "JptFileFilters.xml";
    public static final String DISPLAY_NAME = Bundle.getString(UserDefinedFileFilterExporter.class, "UserDefinedFileFilterExporter.DisplayName");
    public static final FileFilter FILE_FILTER = new FileNameExtensionFilter(Bundle.getString(UserDefinedFileFilterExporter.class, "UserDefinedFileFilterExporter.DisplayName"), "xml");
    private static final ImageIcon ICON = IconUtil.getImageIcon("/org/jphototagger/eximport/jpt/icons/icon_export.png");
    public static final int POSITION = 110;
    private final UserDefinedFileFiltersRepository repo = Lookup.getDefault().lookup(UserDefinedFileFiltersRepository.class);

    @Override
    public void exportFile(File file) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }

        File xmpFile = FileUtil.ensureSuffix(file, ".xml");

        try {
            Set<UserDefinedFileFilter> filter = repo.findAllUserDefinedFileFilters();

            XmlObjectExporter.export(new CollectionWrapper(filter), xmpFile);
        } catch (Exception ex) {
            Logger.getLogger(UserDefinedFileFilterExporter.class.getName()).log(Level.SEVERE, null, ex);
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

        @XmlElementWrapper(name = "FileFilter")
        @XmlElement(type = UserDefinedFileFilter.class)
        private final ArrayList<UserDefinedFileFilter> collection = new ArrayList<UserDefinedFileFilter>();

        public CollectionWrapper() {
        }

        public CollectionWrapper(Collection<UserDefinedFileFilter> collection) {
            this.collection.addAll(collection);
        }

        public List<UserDefinedFileFilter> getCollection() {
            return new ArrayList<UserDefinedFileFilter>(collection);
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

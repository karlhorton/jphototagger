package org.jphototagger.api.image.thumbnails;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author Elmar Baumann
 */
public interface ThumbnailsDisplayer {

    void displayThumbnailsOfFiles(Collection<? extends File> files);
}
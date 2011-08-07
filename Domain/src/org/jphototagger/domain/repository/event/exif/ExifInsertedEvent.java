package org.jphototagger.domain.repository.event.exif;

import java.io.File;
import org.jphototagger.domain.exif.Exif;

/**
 *
 *
 * @author Elmar Baumann
 */
public final class ExifInsertedEvent {

    private final Object source;
    private final File imageFile;
    private final Exif exif;

    public ExifInsertedEvent(Object source, File imageFile, Exif exif) {
        if (imageFile == null) {
            throw new NullPointerException("imageFile == null");
        }

        if (exif == null) {
            throw new NullPointerException("exif == null");
        }

        this.source = source;
        this.imageFile = imageFile;
        this.exif = exif;
    }

    public Exif getExif() {
        return exif;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Object getSource() {
        return source;
    }
}

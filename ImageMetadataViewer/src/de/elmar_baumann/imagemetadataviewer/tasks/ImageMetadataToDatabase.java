package de.elmar_baumann.imagemetadataviewer.tasks;

import de.elmar_baumann.imagemetadataviewer.data.Exif;
import de.elmar_baumann.imagemetadataviewer.data.ImageFile;
import de.elmar_baumann.imagemetadataviewer.data.Xmp;
import de.elmar_baumann.imagemetadataviewer.UserSettings;
import de.elmar_baumann.imagemetadataviewer.database.Database;
import de.elmar_baumann.imagemetadataviewer.event.ErrorEvent;
import de.elmar_baumann.imagemetadataviewer.event.listener.ErrorListeners;
import de.elmar_baumann.imagemetadataviewer.event.ProgressEvent;
import de.elmar_baumann.imagemetadataviewer.event.ProgressListener;
import de.elmar_baumann.imagemetadataviewer.image.thumbnail.ThumbnailUtil;
import de.elmar_baumann.imagemetadataviewer.image.metadata.exif.ExifMetadata;
import de.elmar_baumann.imagemetadataviewer.image.metadata.xmp.XmpMetadata;
import de.elmar_baumann.imagemetadataviewer.io.IoUtil;
import de.elmar_baumann.imagemetadataviewer.resource.Bundle;
import de.elmar_baumann.lib.io.FileUtil;
import java.awt.Image;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Speichert Informationen über Bilddateien in der Datenbank.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/07/25
 */
public class ImageMetadataToDatabase implements Runnable {

    private static Database db = Database.getInstance();
    private ArrayList<ProgressListener> progressListeners = new ArrayList<ProgressListener>();
    private boolean stop = false;
    private int maxThumbnailLength;
    private ArrayList<String> filenames;
    private long startTime = 0;
    private boolean forceUpdate = false;
    private boolean createThumbnails = true;
    private boolean readXmp = true;
    private boolean readExif = true;
    private boolean onlyXmpIsNewer = false;
    private boolean useEmbeddedThumbnails = UserSettings.getInstance().isUseEmbeddedThumbnails();

    /**
     * Konstruktor.
     * 
     * @param filenames          Namen der abzuarbeitenden Bilddateien
     * @param maxThumbnailLength Maximale Länge der längeren Thumbnailseite
     *                           in Pixel für Thumbnails, die aus den Bildern
     *                           skaliert werden
     */
    public ImageMetadataToDatabase(ArrayList<String> filenames, int maxThumbnailLength) {
        this.filenames = filenames;
        this.maxThumbnailLength = maxThumbnailLength;
    }

    /**
     * Fügt einen Fortschrittsbeobachter hinzu. Dieser wird benachrichtigt,
     * bevor die erste Bilddatei abgearbeitet wurde
     * ({@link de.elmar_baumann.imagemetadataviewer.event.ProgressListener#progressStarted(de.elmar_baumann.imagemetadataviewer.event.ProgressEvent)}),
     * nach dem Abarbeiten jeder Bilddatei
     * ({@link de.elmar_baumann.imagemetadataviewer.event.ProgressListener#progressPerformed(de.elmar_baumann.imagemetadataviewer.event.ProgressEvent)})
     * und nachdem alle Bilddateien abgearbeitet sind
     * ({@link de.elmar_baumann.imagemetadataviewer.event.ProgressListener#progressEnded(de.elmar_baumann.imagemetadataviewer.event.ProgressEvent)}).
     * 
     * @param listener Fortschrittsbeobachter
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    /**
     * Entfernt einen Fortschrittsbeobachter.
     * 
     * @param listener Fortschrittsbeobachter
     */
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

    /**
     * Setzt, dass die Metadaten auf jeden Fall aktualisiert werden, auch
     * wenn diese in der Datenbank aktuell sind.
     * 
     * @param force true, wenn die Daten auf jeden Fall aktualisiert werden
     *              sollen. Default: false
     */
    public void setForceUpdate(boolean force) {
        forceUpdate = force;
    }

    /**
     * Setzt, dass außer den Text-Metadaten auch Thumbnails erzeugt werden.
     * 
     * @param create true, wenn zusätzlich Thumbnails erzeugt werden sollen.
     *               Default: true.
     */
    public void setCreateThumbnails(boolean create) {
        createThumbnails = create;
    }

    /**
     * Liefert, ob Thumbnails erzeugt werden sollen.
     * 
     * @return true, wenn Thumbnails erzeugt werden sollen
     */
    public boolean isCreateThumbnails() {
        return createThumbnails;
    }

    /**
     * Liefert, ob die Datenbank auf jeden Fall aktualisiert werden soll.
     * 
     * @return true, wenn die Datenbank aktualisiert werden soll unabhängig
     *         von der Zeit der letzten Aktualisierung
     */
    public boolean isForceUpdate() {
        return forceUpdate;
    }

    /**
     * Liefert die maximale Länge der längeren Thumbnailseite.
     * 
     * @return Länge
     */
    public int getMaxThumbnailLength() {
        return maxThumbnailLength;
    }

    /**
     * Liefert, ob eingebettete Thumbnails gelesen werden sollen.
     * 
     * @return true, wenn eingebettete Thumbnails gelesen werden sollen
     *         und nicht neue aus den Bilddaten erzeugt
     */
    public boolean isUseEmbeddedThumbnails() {
        return useEmbeddedThumbnails;
    }

    /**
     * Liefert, ob die Datenbank mit EXIF-Metadaten aktualisiert werden soll.
     * 
     * @return true, wenn die Datenbank mit EXIF-Metadaten aktualisiert
     * werden soll
     */
    public boolean isReadExif() {
        return readExif;
    }

    /**
     * Setzt, ob die Datenbank mit EXIF-Metadaten aktualisiert werden soll.
     * 
     * @param readExif  true, wenn die Datenbank mit EXIF-Metadaten aktualisiert
     *                  werden soll. Default: true.
     */
    public void setReadExif(boolean readExif) {
        this.readExif = readExif;
    }

    /**
     * Liefert, ob die Datenbank mit XMP-Metadaten aktualisiert werden soll.
     * 
     * @return true, wenn die Datenbank mit XMP-Metadaten aktualisiert werden
     *         soll
     */
    public boolean isReadXmp() {
        return readXmp;
    }

    /**
     * Setzt, ob die Datenbank mit XMP-Metadaten aktualisiert werden soll.
     * 
     * @param readXmp  true, wenn die Datenbank mit XMP-Metadaten aktualisiert
     *                 werden soll. Default: true.
     */
    public void setReadXmp(boolean readXmp) {
        this.readXmp = readXmp;
    }

    @Override
    public void run() {
        notifyProgressStarted();
        int count = filenames.size();
        startTime = System.currentTimeMillis();
        for (int index = 0; !stop && index < count; index++) {
            String filename = filenames.get(index);
            if ((isForceUpdate() || !isImageFileUpToDate(filename))) {
                ImageFile data = getImageFileData(filename);
                if (data != null) {
                    db.insertImageFile(data);
                }
            }
            notifyProgressPerformed(index + 1, filename);
        }
        notifyProgressEnded();
    }

    private boolean isImageFileUpToDate(String filename) {
        long dbTime = db.getLastModified(filename);
        long fileTime = FileUtil.getLastModified(filename);
        long compareTime = fileTime;
        long sidecarFileTime = -1;
        String sidecarFileName = XmpMetadata.getSidecarFilename(filename);
        if (sidecarFileName != null) {
            sidecarFileTime = FileUtil.getLastModified(sidecarFileName);
            if (sidecarFileTime > fileTime) {
                compareTime = sidecarFileTime;
            }
        }
        onlyXmpIsNewer = fileTime <= dbTime && sidecarFileTime > dbTime;
        return compareTime <= dbTime;
    }

    private ImageFile getImageFileData(String filename) {
        ImageFile imageFileData = new ImageFile();
        imageFileData.setFilename(filename);
        imageFileData.setLastmodified(IoUtil.getFileTime(filename));
        if (!onlyXmpIsNewer && isCreateThumbnails()) {
            imageFileData.setThumbnail(getThumbnail(filename));
        }
        if (isReadXmp()) {
            setXmp(imageFileData);
        }
        if (!onlyXmpIsNewer && isReadExif()) {
            setExif(imageFileData);
        }
        return imageFileData;
    }

    private Image getThumbnail(String filename) {
        UserSettings settings = UserSettings.getInstance();
        Image thumbnail = null;
        if (settings.isCreateThumbnailsWithExternalApp()) {
            thumbnail = ThumbnailUtil.getThumbnailFromExternalApplication(
                filename,
                settings.getExternalThumbnailCreationCommand(), getMaxThumbnailLength());
        } else {
            thumbnail = ThumbnailUtil.getThumbnail(
                filename, getMaxThumbnailLength(), isUseEmbeddedThumbnails());
        }
        if (thumbnail == null) {
            notifyNullThumbnail(filename);
        }
        return thumbnail;
    }

    private void setExif(ImageFile imageFileData) {
        Exif exif = ExifMetadata.getExif(imageFileData.getFilename());
        if (exif != null && !exif.isEmpty()) {
            imageFileData.setExif(exif);
        }
    }

    private void setXmp(ImageFile imageFileData) {
        Xmp xmp = XmpMetadata.getXmp(imageFileData.getFilename());
        if (xmp != null && !xmp.isEmpty()) {
            imageFileData.setXmp(xmp);
        }
    }

    /**
     * Unterbricht die Abarbeitung der Bilddateien.
     */
    public void stop() {
        stop = true;
    }

    private void notifyNullThumbnail(String filename) {
        MessageFormat msg = new MessageFormat(Bundle.getString("ImageMetadataToDatabase.ErrorMessage.NullThumbnail")); // NOI18N
        Object[] params = {filename};
        String formattedMessage = msg.format(params);
        Logger.getLogger(ImageMetadataToDatabase.class.getName()).log(Level.SEVERE, formattedMessage);
        notifyErrorListener(formattedMessage);
    }

    private void notifyErrorListener(String message) {
        ErrorListeners.getInstance().notifyErrorListener(new ErrorEvent(message, this));
    }

    private void notifyProgressStarted() {
        for (ProgressListener listener : progressListeners) {
            listener.progressStarted(getProgressEvent(0, Bundle.getString("ImageMetadataToDatabase.InformationMessage.ProgressStarted"))); // NOI18N
        }
    }

    private void notifyProgressPerformed(int value, String filename) {
        for (ProgressListener listener : progressListeners) {
            ProgressEvent event = getProgressEvent(value, filename);
            listener.progressPerformed(event);
            if (event.isStop()) {
                stop();
            }
        }
    }

    private void notifyProgressEnded() {
        for (ProgressListener listener : progressListeners) {
            listener.progressEnded(getProgressEvent(filenames.size(), Bundle.getString("ImageMetadataToDatabase.InformationMessage.ProgressEnded"))); // NOI18N
        }
    }

    private ProgressEvent getProgressEvent(int value, Object info) {
        int taskCount = filenames.size();
        ProgressEvent evt = new ProgressEvent(this, 0, taskCount, value, info);
        if (value > 0) {
            long usedTime = System.currentTimeMillis() - startTime;
            long timePerTask = usedTime / value;
            int remainingTaskCount = taskCount - value;
            evt.setMilliSecondsRemaining(timePerTask * remainingTaskCount);
        }
        return evt;
    }
}

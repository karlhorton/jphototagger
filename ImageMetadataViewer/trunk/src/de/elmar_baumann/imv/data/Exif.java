package de.elmar_baumann.imv.data;

import java.sql.Date;

/**
 * EXIF metadata or an image file.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/08/27
 */
public final class Exif {

    private Date dateTimeOriginal;
    private double focalLength = -1;
    private short isoSpeedRatings = -1;
    private String recordingEquipment;

    /**
     * Returns the date when the image was created.
     * 
     * @return date
     */
    public Date getDateTimeOriginal() {
        return dateTimeOriginal;
    }

    /**
     * Sets the date when the image was created.
     * 
     * @param dateTimeOriginal date
     */
    public void setDateTimeOriginal(Date dateTimeOriginal) {
        this.dateTimeOriginal = dateTimeOriginal;
    }

    /**
     * Returns the focal length of the camera's lens which projected the image.
     * 
     * @return focal length in mm
     */
    public double getFocalLength() {
        return focalLength;
    }

    /**
     * Sets the focal length of the camera's lens which projected the image.
     * 
     * @param focalLength focal length in mm
     */
    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
    }

    /**
     * Returns the ISO adjustment of the camera which took the image.
     * 
     * @return ISO
     */
    public short getIsoSpeedRatings() {
        return isoSpeedRatings;
    }

    /**
     * Sets the ISO adjustment of the camera which took the image
     * 
     * @param isoSpeedRatings ISO
     */
    public void setIsoSpeedRatings(short isoSpeedRatings) {
        this.isoSpeedRatings = isoSpeedRatings;
    }

    /**
     * Returns the camera which took the image.
     * 
     * @return camera
     */
    public String getRecordingEquipment() {
        return recordingEquipment;
    }

    /**
     * Sets the camera which took the image.
     * 
     * @param recordingEquipment camera
     */
    public void setRecordingEquipment(String recordingEquipment) {
        // Bugfix imagero: when first byte of RAW data is 0, then the returned
        // string is "0"
        this.recordingEquipment = recordingEquipment == null ||
                recordingEquipment.equals("0") // NOI18N
                                  ? null
                                  : recordingEquipment;
    }

    /**
     * Returns wheter no EXIF field of this class was set.
     * 
     * @return true if no EXIF field was set
     */
    public boolean isEmpty() {
        return dateTimeOriginal == null &&
                focalLength < 0 &&
                isoSpeedRatings < 0 &&
                recordingEquipment == null;
    }
}

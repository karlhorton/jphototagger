package org.jphototagger.domain.metadata.exif;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import org.jphototagger.domain.metadata.MetaDataValue;
import org.jphototagger.lib.util.StringUtil;

/**
 * @author Elmar Baumann
 */
public final class Exif {

    private Date dateTimeOriginal;
    private double focalLength = -1;
    private double gpsLatitude = Integer.MIN_VALUE;
    private double gpsLongitude = Integer.MIN_VALUE;
    private short isoSpeedRatings = -1;
    private String recordingEquipment;
    private String lens;
    private long dateTimeOriginalTimestamp = -1;
    @XmlTransient
    private final Map<MetaDataValue, Object> metadataValues = new HashMap<>();
    @XmlTransient
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @return date when the image was created or null
     */
    public Date getDateTimeOriginal() {
        return dateTimeOriginal == null
                ? null
                : new Date(dateTimeOriginal.getTime());
    }

    /**
     * @param dateTimeOriginal date when the image was created or null
     */
    public void setDateTimeOriginal(Date dateTimeOriginal) {
        this.dateTimeOriginal = dateTimeOriginal == null
                ? null
                : new Date(dateTimeOriginal.getTime());
        metadataValues.put(ExifDateTimeOriginalMetaDataValue.INSTANCE, this.dateTimeOriginal);
    }

    /**
     * @param timestamp time when the image was created in milliseconds since 1970/01/01 00:00:00 or negative value
     */
    public void setDateTimeOriginalTimestamp(long timestamp) {
        dateTimeOriginalTimestamp = timestamp;
    }

    /**
     * @return time when the image was created in milliseconds since 1970/01/01 00:00:00 or negative value
     */
    public long getDateTimeOriginalTimestamp() {
        return dateTimeOriginalTimestamp;
    }

    public Long getDateTimeOriginalTimestampGreaterZeroOrNull() {
        return dateTimeOriginalTimestamp > 0
                ? dateTimeOriginalTimestamp
                : null;
    }

    /**
     * @return focal length in mm
     */
    public double getFocalLength() {
        return focalLength;
    }

    public Double getFocalLengthGreaterZeroOrNull() {
        return focalLength > 0
                ? focalLength
                : null;
    }

    /**
     * @param focalLength focal length in mm
     */
    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
        metadataValues.put(ExifFocalLengthMetaDataValue.INSTANCE, this.focalLength);
    }

    /**
     * @return ISO setting
     */
    public short getIsoSpeedRatings() {
        return isoSpeedRatings;
    }

    public Short getIsoSpeedRatingsGreaterZeroOrNull() {
        return isoSpeedRatings > 0
                ? isoSpeedRatings
                : null;
    }

    /**
     * @param isoSpeedRatings ISO setting
     */
    public void setIsoSpeedRatings(short isoSpeedRatings) {
        this.isoSpeedRatings = isoSpeedRatings;
        metadataValues.put(ExifIsoSpeedRatingsMetaDataValue.INSTANCE, this.isoSpeedRatings);
    }

    /**
     * @return camera
     */
    public String getRecordingEquipment() {
        return recordingEquipment;
    }

    /**
     * @param recordingEquipment camera
     */
    public void setRecordingEquipment(String recordingEquipment) {
        // Bugfix imagero: If first byte of RAW data is 0, then the returned string is "0"
        this.recordingEquipment = recordingEquipment == null || recordingEquipment.equals("0")
                ? null
                : recordingEquipment;
        metadataValues.put(ExifRecordingEquipmentMetaDataValue.INSTANCE, this.recordingEquipment);
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
        metadataValues.put(ExifLensMetaDataValue.INSTANCE, this.lens);
    }

    public void resetGps() {
        gpsLatitude = Integer.MIN_VALUE;
        gpsLongitude = Integer.MIN_VALUE;
    }

    /**
     * @return Degrees from -180 to +180, South negative, North positive
     */
    public double getGpsLatitude() {
        return gpsLatitude;
    }

    /**
     * @param gpsLatitudeDegrees Degrees from -180 to +180, South negative, North positive
     */
    public void setGpsLatitude(double gpsLatitudeDegrees) {
        this.gpsLatitude = gpsLatitudeDegrees;
    }

    /**
     * @return Degrees from -180 to +180, West negative, East positive
     */
    public double getGpsLongitude() {
        return gpsLongitude;
    }

    /**
     * @param gpsLongitudeDegrees  Degrees from -180 to +180, West negative, East positive
     */
    public void setGpsLongitude(double gpsLongitudeDegrees) {
        this.gpsLongitude = gpsLongitudeDegrees;
    }

    /**
     * @return true if latitude and longitude defined within their accepted range
     */
    public boolean hasGpsCoordinates() {
        return gpsLatitude >= -180.0 && gpsLatitude <= 180.0 && gpsLongitude >= -180.0 && gpsLongitude <= 180.0;
    }

    public String getXmpDateCreated() {
        if (dateTimeOriginal == null) {
            return "";
        }
        return DATE_FORMAT.format(dateTimeOriginal);
    }

    public Object getValue(MetaDataValue mdv) {
        return metadataValues.get(mdv);
    }

    public boolean isEmpty() {
        return dateTimeOriginal == null
                && focalLength < 0
                && isoSpeedRatings < 0
                && dateTimeOriginalTimestamp < 0
                && !StringUtil.hasContent(lens)
                && !StringUtil.hasContent(recordingEquipment);
    }
}

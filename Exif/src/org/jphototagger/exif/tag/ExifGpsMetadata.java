package org.jphototagger.exif.tag;

import org.jphototagger.exif.datatype.ExifRational;

/**
 * GPS information in the EXIF metadata.
 *
 * @author Elmar Baumann
 */
public final class ExifGpsMetadata {

    /**
     * Status of the GPS receiver when the image is recorded
     */
    public enum GpsStatus {

        MEASUREMENT_IS_IN_PROGRESS, MEASUREMENT_IS_INTEROPERABILITY
    }

    /**
     * GPS measurement mode
     */
    public enum GpsMeasureMode {

        TWO_DIMENSIONAL, THREE_DIMENSIONAL
    }

    /**
     * Unit used to express the GPS receiver speed of movement
     */
    public enum GpsSpeedRef {

        KILOMETERS_PER_HOUR, MILES_PER_HOUR, KNOTS
    }

    /**
     * Reference for giving the direction of GPS receiver movement
     */
    public enum GpsTrackRef {

        TRUE_DIRECTION, MAGNETIC_DIRECTION
    }

    /**
     * Reference for giving the direction of the image when it is
     * captured
     */
    public enum GpsImgDirectionRef {

        TRUE_DIRECTION, MAGNETIC_DIRECTION
    }

    /**
     * Reference used for giving the bearing to the destination point
     */
    public enum GpsDestBearingRef {

        TRUE_DIRECTION, MAGNETIC_DIRECTION
    }

    /**
     * Unit used to express the distance to the destination point
     */
    public enum GpsDestDistanceRef {

        KILOMETERS, MILES, KNOTS
    }
    private ExifGpsVersion version;
    private ExifGpsLatitude latitude;
    private ExifGpsLongitude longitude;
    private ExifGpsAltitude altitude;
    private ExifGpsTimeStamp timeStamp;
    private String gpsSatellites;
    private GpsStatus gpsReceiverStatus;
    private GpsMeasureMode gpsMeasurementMode;
    private ExifRational gpsDop;
    private GpsSpeedRef gpsSpeedRef;
    private ExifRational gpsSpeed;
    private GpsTrackRef gpsTrackRef;
    private ExifRational gpsTrack;
    private GpsImgDirectionRef gpsImgDirectionRef;
    private ExifRational gpsImgDirection;
    private String gpsMapDatum;
    private ExifGpsLatitude destLatitude;
    private ExifGpsLongitude destLongitude;
    private GpsDestBearingRef gpsDestBearingRef;
    private ExifRational gpsDestBearing;
    private GpsDestDistanceRef gpsDestDistanceRef;
    private ExifRational gpsDistanceToDestination;
    private String gpsProcessingMethod;
    private String gpsAreaInformation;
    private ExifGpsDateStamp gpsDateStamp;
    private int gpsDifferential;

    public ExifGpsAltitude getAltitude() {
        return altitude;
    }

    public void setAltitude(ExifGpsAltitude altitude) {
        this.altitude = altitude;
    }

    public ExifGpsLatitude getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(ExifGpsLatitude destLatitude) {
        this.destLatitude = destLatitude;
    }

    public ExifGpsLongitude getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(ExifGpsLongitude destLongitude) {
        this.destLongitude = destLongitude;
    }

    public String getGpsAreaInformation() {
        return gpsAreaInformation;
    }

    public void setGpsAreaInformation(String gpsAreaInformation) {
        this.gpsAreaInformation = gpsAreaInformation;
    }

    public ExifGpsDateStamp getGpsDateStamp() {
        return gpsDateStamp;
    }

    public void setGpsDateStamp(ExifGpsDateStamp gpsDateStamp) {
        this.gpsDateStamp = gpsDateStamp;
    }

    /**
     * Returns the bearing to the destination point. The range of values is
     * from 0.00 to 359.99.
     *
     * @return bearing
     */
    public ExifRational getGpsDestBearing() {
        return gpsDestBearing;
    }

    /**
     * Sets the bearing to the destination point. The range of values is
     * from 0.00 to 359.99.
     *
     * @param gpsDestBearing bearing
     */
    public void setGpsDestBearing(ExifRational gpsDestBearing) {
        this.gpsDestBearing = gpsDestBearing;
    }

    /**
     * Returns the reference used for giving the bearing to the destination
     * point.
     *
     * @return reference
     */
    public GpsDestBearingRef getGpsDestBearingRef() {
        return gpsDestBearingRef;
    }

    /**
     * Sets the the reference used for giving the bearing to the destination
     * point.
     *
     * @param gpsDestBearingRef reference
     */
    public void setGpsDestBearingRef(GpsDestBearingRef gpsDestBearingRef) {
        this.gpsDestBearingRef = gpsDestBearingRef;
    }

    /**
     * Returns the unit used to express the distance to the destination point.
     *
     * @return unit
     */
    public GpsDestDistanceRef getGpsDestDistanceRef() {
        return gpsDestDistanceRef;
    }

    /**
     * Sets the unit used to express the distance to the destination point.
     *
     * @param gpsDestDistanceRef unit
     */
    public void setGpsDestDistanceRef(GpsDestDistanceRef gpsDestDistanceRef) {
        this.gpsDestDistanceRef = gpsDestDistanceRef;
    }

    /**
     * Returns the GPS differential correction.
     *
     * @return differential correction
     */
    public int getGpsDifferential() {
        return gpsDifferential;
    }

    /**
     * Sets the GPS differential correction.
     *
     * @param gpsDifferential differential correction
     */
    public void setGpsDifferential(int gpsDifferential) {
        this.gpsDifferential = gpsDifferential;
    }

    /**
     * Returns the distance to destination.
     *
     * @return distance to destination
     */
    public ExifRational getGpsDistanceToDestination() {
        return gpsDistanceToDestination;
    }

    /**
     * Sets the distance to destination.
     *
     * @param gpsDistanceToDestination distance to destination
     */
    public void setGpsDistanceToDestination(ExifRational gpsDistanceToDestination) {
        this.gpsDistanceToDestination = gpsDistanceToDestination;
    }

    /**
     * Returns the GPS DOP (data degree of precision). An HDOP value is written
     * during two-dimensional measurement, and PDOP during three-dimensional
     * measurement.
     *
     * @return GPS DOP
     */
    public ExifRational getGpsDop() {
        return gpsDop;
    }

    /**
     * Sets the GPS DOP (data degree of precision). An HDOP value is written
     * during two-dimensional measurement, and PDOP during three-dimensional
     * measurement.
     *
     * @param gpsDop GPS DOP
     */
    public void setGpsDop(ExifRational gpsDop) {
        this.gpsDop = gpsDop;
    }

    /**
     * Returns the direction of the image when it was captured. The range of
     * values is from 0.00 to 359.99.
     *
     * @return direction
     */
    public ExifRational getGpsImgDirection() {
        return gpsImgDirection;
    }

    /**
     * Sets the direction of the image when it was captured. The range of
     * values is from 0.00 to 359.99.
     *
     * @param gpsImgDirection direction
     */
    public void setGpsImgDirection(ExifRational gpsImgDirection) {
        this.gpsImgDirection = gpsImgDirection;
    }

    /**
     * Returns the reference for giving the direction of the image when it is
     * captured.
     *
     * @return reference
     */
    public GpsImgDirectionRef getGpsImgDirectionRef() {
        return gpsImgDirectionRef;
    }

    /**
     * Sets the reference for giving the direction of the image when it is
     * captured.
     *
     * @param gpsImgDirectionRef reference
     */
    public void setGpsImgDirectionRef(GpsImgDirectionRef gpsImgDirectionRef) {
        this.gpsImgDirectionRef = gpsImgDirectionRef;
    }

    /**
     * Returns the geodetic survey data used by the GPS receiver. If the
     * survey data is restricted to Japan, the value of this tag is 'TOKYO'
     * or 'WGS-84'. If a GPS Info tag is recorded, it is strongly recommended
     * that this tag be recorded.
     *
     * @return data
     */
    public String getGpsMapDatum() {
        return gpsMapDatum;
    }

    /**
     * Sets the geodetic survey data used by the GPS receiver. If the
     * survey data is restricted to Japan, the value of this tag is 'TOKYO'
     * or 'WGS-84'. If a GPS Info tag is recorded, it is strongly recommended
     * that this tag be recorded.
     *
     * @param gpsMapDatum data
     */
    public void setGpsMapDatum(String gpsMapDatum) {
        this.gpsMapDatum = gpsMapDatum;
    }

    /**
     * Returns the GPS measurement mode.
     *
     * @return GPS measurement mode
     */
    public GpsMeasureMode getGpsMeasurementMode() {
        return gpsMeasurementMode;
    }

    /**
     * Sets the GPS measurement mode.
     *
     * @param gpsMeasurementMode GPS measurement mode
     */
    public void setGpsMeasurementMode(GpsMeasureMode gpsMeasurementMode) {
        this.gpsMeasurementMode = gpsMeasurementMode;
    }

    /**
     * Returns the name of GPS processing method.
     *
     * @return name
     */
    public String getGpsProcessingMethod() {
        return gpsProcessingMethod;
    }

    /**
     * Sets the name of GPS processing method.
     *
     * @param gpsProcessingMethod name
     */
    public void setGpsProcessingMethod(String gpsProcessingMethod) {
        this.gpsProcessingMethod = gpsProcessingMethod;
    }

    /**
     * Returns the status of the GPS receiver when the image is recorded.
     *
     * @return status
     */
    public GpsStatus getGpsReceiverStatus() {
        return gpsReceiverStatus;
    }

    /**
     * Sets the status of the GPS receiver when the image is recorded.
     *
     * @param gpsReceiverStatus status
     */
    public void setGpsReceiverStatus(GpsStatus gpsReceiverStatus) {
        this.gpsReceiverStatus = gpsReceiverStatus;
    }

    /**
     * Returns the GPS satellites used for measurements. This tag can be used
     * to describe the number of satellites, their ID number, angle of elevation,
     * azimuth, SNR and other information in ASCII notation.
     *
     * @return satellites
     */
    public String getGpsSatellites() {
        return gpsSatellites;
    }

    /**
     * Sets the GPS satellites used for measurements. This tag can be used
     * to describe the number of satellites, their ID number, angle of elevation,
     * azimuth, SNR and other information in ASCII notation.
     *
     * @param gpsSatellites satellites
     */
    public void setGpsSatellites(String gpsSatellites) {
        this.gpsSatellites = gpsSatellites;
    }

    /**
     * Returns the the speed of GPS receiver movement.
     *
     * @return speed of GPS receiver movement
     */
    public ExifRational getGpsSpeed() {
        return gpsSpeed;
    }

    /**
     * Sets the speed of GPS receiver movement.
     *
     * @param gpsSpeed speed of GPS receiver movement
     */
    public void setGpsSpeed(ExifRational gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    /**
     * Returns the unit used to express the GPS receiver speed of movement.
     *
     * @return unit
     */
    public GpsSpeedRef getGpsSpeedRef() {
        return gpsSpeedRef;
    }

    /**
     * Sets the unit used to express the GPS receiver speed of movement.
     *
     * @param gpsSpeedRef unit
     */
    public void setGpsSpeedRef(GpsSpeedRef gpsSpeedRef) {
        this.gpsSpeedRef = gpsSpeedRef;
    }

    /**
     * Returns the direction of GPS receiver movement. The range of values is
     * from 0.00 to 359.99.
     *
     * @return direction
     */
    public ExifRational getGpsTrack() {
        return gpsTrack;
    }

    /**
     * Sets the direction of GPS receiver movement. The range of values is
     * from 0.00 to 359.99.
     *
     * @param gpsTrack direction
     */
    public void setGpsTrack(ExifRational gpsTrack) {
        this.gpsTrack = gpsTrack;
    }

    /**
     * Returns the the reference for giving the direction of GPS receiver
     * movement.
     *
     * @return reference
     */
    public GpsTrackRef getGpsTrackRef() {
        return gpsTrackRef;
    }

    /**
     * Sets the reference for giving the direction of GPS receiver movement.
     *
     * @param gpsTrackRef reference
     */
    public void setGpsTrackRef(GpsTrackRef gpsTrackRef) {
        this.gpsTrackRef = gpsTrackRef;
    }

    /**
     * Returns the getLatitude.
     *
     * @return getLatitude
     */
    public ExifGpsLatitude getLatitude() {
        return latitude;
    }

    /**
     * Sets the getLatitude.
     *
     * @param latitude latitude
     */
    public void setLatitude(ExifGpsLatitude latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the getLongitude.
     *
     * @return getLongitude
     */
    public ExifGpsLongitude getLongitude() {
        return longitude;
    }

    /**
     * Sets the getLongitude.
     *
     * @param longitude longitude
     */
    public void setLongitude(ExifGpsLongitude longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the GPS time (atomic clock).
     *
     * @return time
     */
    public ExifGpsTimeStamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the GPS time (atomic clock).
     * @param timeStamp time
     */
    public void setTimeStamp(ExifGpsTimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Returns the GPS tag getVersion.
     *
     * @return GPS tag getVersion
     */
    public ExifGpsVersion getVersion() {
        return version;
    }

    /**
     * Sets the GPS tag getVersion.
     *
     * @param version GPS tag getVersion
     */
    public void setVersion(ExifGpsVersion version) {
        this.version = version;
    }

    public boolean hasCoordinates() {
        return (latitude != null) && (longitude != null);
    }
}

package de.elmar_baumann.imagemetadataviewer.data;

import de.elmar_baumann.imagemetadataviewer.resource.Bundle;
import java.awt.Color;

/**
 * Flag für ein Thumbnail.
 * 
 * @author  Elmar Baumann <eb@elmar-aumann.de>
 * @version 2008/09/09
 */
public class ThumbnailFlag {

    private Color color;
    private String string;
    /**
     * Flag: Datei wurde nicht gefunden.
     */
    public static final ThumbnailFlag ErrorFileNotFound =
        new ThumbnailFlag(Color.RED, Bundle.getString("ThumbnailFlag.ErrorMessage.FileNotFound"));

    /**
     * Konstruktor.
     * 
     * @param color  Farbe des Flags
     * @param string String mit Bedeutung des Flags
     */
    public ThumbnailFlag(Color color, String string) {
        this.color = color;
        this.string = string;
    }

    /**
     * Liefert die Farbe des Flags.
     * 
     * @return Farbe
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setzt die Farbe des Flags.
     * 
     * @param color Farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Liefert den String mit der Bedeutung des Flags.
     * 
     * @return String mit Bedeutung
     */
    public String getString() {
        return string;
    }

    /**
     * Setzt den String mit der Bedeutung des Flags.
     * 
     * @param string Bedeutung
     */
    public void setString(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThumbnailFlag other = (ThumbnailFlag) obj;
        if (this.color != other.color && (this.color == null || !this.color.equals(other.color))) {
            return false;
        }
        if (this.string == null || !this.string.equals(other.string)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 71 * hash + (this.string != null ? this.string.hashCode() : 0);
        return hash;
    }
}

package de.elmar_baumann.imagemetadataviewer.image.metadata.iptc;

import java.util.Comparator;

/**
 * Vergleicht IptcEntry-Objekte. Vergleichskriterien:
 * <ol>
 * <li>Recordnummer</li>
 * <li>Datensatznummer</li>
 * <li>Daten</li>
 * </ol>
 * Der Name wird nicht verglichen, denn Recordnummer und Datensatznummer
 * identifizieren diesen eindeutig; er ist redundand.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/02/17
 * @see     IptcEntry
 */
public class IptcEntryComparator implements Comparator<IptcEntry> {

    @Override
    public int compare(IptcEntry o1, IptcEntry o2) {
        if (o1.getRecordNumber() < o2.getRecordNumber()) {
            return -1;
        }
        if (o1.getRecordNumber() > o2.getRecordNumber()) {
            return +1;
        }
        if (o1.getRecordNumber() == o2.getRecordNumber()) {
            if (o1.getDataSetNumber() < o2.getDataSetNumber()) {
                return -1;
            }
            if (o1.getDataSetNumber() > o2.getDataSetNumber()) {
                return +1;
            }
        }
        return o1.getData().compareTo(o2.getData());
    }
}

/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.model;

import com.adobe.xmp.properties.XMPPropertyInfo;
import de.elmar_baumann.jpt.resource.Bundle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Ansammlung von XMPPropertyInfo-Objekten.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>, Tobias Stening <info@swts.net>
 * @version 2008-10-05
 * @see     com.adobe.xmp.properties.XMPPropertyInfo
 */
public final class TableModelXmp extends DefaultTableModel {

    private List<XMPPropertyInfo> propertyInfos;
    private String filename;

    public TableModelXmp() {
        setRowHeaders();
    }

    /**
     * Setzt die Property-Infos, deren Daten übernommen werden.
     * 
     * @param filename       Name der Datei, aus der die Property-Infos
     *                       ermittelt wurden oder null, falls diese
     *                       Inforation unwichtig ist
     * @param propertyInfos  Property-Infos
     */
    public void setPropertyInfosOfFile(String filename,
            List<XMPPropertyInfo> propertyInfos) {
        this.filename = filename;
        this.propertyInfos = new ArrayList<XMPPropertyInfo>(propertyInfos);
        removeAllRows();
        setXmpData();
    }

    /**
     * Liefert den Namen der Datei, deren Property-Infos das Model enthält.
     * 
     * @return Dateiname oder null, wenn die Property-Infos entfernt wurden
     *         oder null gesetzt wurde mit
     *         {@link #setPropertyInfosOfFile(java.lang.String, java.util.List)}
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Entfernt alle XMP-Daten.
     */
    public void removeAllRows() {
        getDataVector().clear();
        filename = null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    private void setXmpData() {
        if (propertyInfos != null) {
            for (XMPPropertyInfo xmpPropertyInfo : propertyInfos) {
                addRow(xmpPropertyInfo);
            }
        }
    }

    private void addRow(XMPPropertyInfo xmpPropertyInfo) {
        String path = xmpPropertyInfo.getPath();
        Object value = xmpPropertyInfo.getValue();
        if (path != null && value != null && !path.contains("Digest")) { // NOI18N
            List<XMPPropertyInfo> newRow = new ArrayList<XMPPropertyInfo>();
            newRow.add(xmpPropertyInfo);
            newRow.add(xmpPropertyInfo);
            super.addRow(newRow.toArray(new XMPPropertyInfo[newRow.size()]));
        }
    }

    private void setRowHeaders() {
        addColumn(Bundle.getString("TableModelXmp.HeaderColumn.1")); // NOI18N
        addColumn(Bundle.getString("TableModelXmp.HeaderColumn.2")); // NOI18N
    }
}
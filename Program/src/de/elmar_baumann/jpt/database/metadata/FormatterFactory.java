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
package de.elmar_baumann.jpt.database.metadata;

import de.elmar_baumann.jpt.app.AppLog;
import de.elmar_baumann.jpt.database.metadata.Column.DataType;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-10-28
 */
public final class FormatterFactory {

    private static DefaultFormatterFactory integerFormatterFactory;
    private static DefaultFormatterFactory doubleFormatterFactory;
    private static DefaultFormatterFactory dateFormatterFactory;
    private static DefaultFormatterFactory defaultFormatterFactory =
            new DefaultFormatterFactory(new DefaultFormatter());
    

    static {
        try {
            NumberFormat integerFormat = NumberFormat.getIntegerInstance();
            integerFormat.setGroupingUsed(false);
            NumberFormatter integerFormatter = new NumberFormatter(integerFormat);
            integerFormatter.setAllowsInvalid(false);
            MaskFormatter doubleFormatter = new MaskFormatter("####.##"); // NOI18N
            doubleFormatter.setAllowsInvalid(false);

            integerFormatterFactory = new DefaultFormatterFactory(integerFormatter);
            doubleFormatterFactory = new DefaultFormatterFactory(doubleFormatter);
            dateFormatterFactory = new DefaultFormatterFactory(new MaskFormatter("####-##-##")); // NOI18N
        } catch (ParseException ex) {
            AppLog.logSevere(FormatterFactory.class, ex);
        }
    }

    public static DefaultFormatterFactory getFormatterFactory(Column column) {
        DataType type = column.getDataType();
        if (type.equals(DataType.DATE)) {
            return dateFormatterFactory;
        } else if (type.equals(DataType.BIGINT) || type.equals(DataType.INTEGER) ||
            type.equals(DataType.SMALLINT)) {
            return integerFormatterFactory;
        } else if (type.equals(DataType.REAL)) {
            return doubleFormatterFactory;
        } else {
            return defaultFormatterFactory;
        }
    }

    private FormatterFactory() {
    }
}

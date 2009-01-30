package de.elmar_baumann.imv.database.metadata.collections;

import de.elmar_baumann.imv.database.metadata.Column;
import de.elmar_baumann.imv.database.metadata.Column.DataType;
import de.elmar_baumann.imv.resource.Bundle;

/**
 * Spalte <code>name</code> der Tabelle <code>collection_names</code>.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/07
 */
public final class ColumnCollectionnamesName extends Column {

    private static final ColumnCollectionnamesName instance = new ColumnCollectionnamesName();

    public static ColumnCollectionnamesName getInstance() {
        return instance;
    }

    private ColumnCollectionnamesName() {
        super(
            TableCollectionNames.getInstance(),
            "name", // NOI18N
            DataType.Integer);

        setDescription(Bundle.getString("ColumnCollectionnamesName.Description"));
    }
}

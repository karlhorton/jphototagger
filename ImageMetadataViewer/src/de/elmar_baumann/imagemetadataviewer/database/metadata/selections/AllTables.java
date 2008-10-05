package de.elmar_baumann.imagemetadataviewer.database.metadata.selections;

import de.elmar_baumann.imagemetadataviewer.database.metadata.Table;
import de.elmar_baumann.imagemetadataviewer.database.metadata.collections.TableCollectionNames;
import de.elmar_baumann.imagemetadataviewer.database.metadata.collections.TableCollections;
import de.elmar_baumann.imagemetadataviewer.database.metadata.exif.TableExif;
import de.elmar_baumann.imagemetadataviewer.database.metadata.file.TableFiles;
import de.elmar_baumann.imagemetadataviewer.database.metadata.savedsearches.TableSavedSearches;
import de.elmar_baumann.imagemetadataviewer.database.metadata.xmp.TableXmp;
import de.elmar_baumann.imagemetadataviewer.database.metadata.xmp.TableXmpDcSubjects;
import de.elmar_baumann.imagemetadataviewer.database.metadata.xmp.TableXmpPhotoshopSupplementalCategories;
import java.util.ArrayList;

/**
 * Alle Tabellen der Anwendung.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/18
 */
public class AllTables {

    private static ArrayList<Table> allTables = new ArrayList<Table>();    // TODO PERMANENT: Neue Tabellen eintragen, Reihenfolge NIE verändern, da Wechselwirkung mit
    // de.elmar_baumann.imagemetadataviewer.database.metadata.ColumnIds.initColumnOfIdMap()
    

    static {
        allTables.add(TableExif.getInstance());
        allTables.add(TableFiles.getInstance());
        allTables.add(TableXmp.getInstance());
        allTables.add(TableXmpDcSubjects.getInstance());
        allTables.add(TableXmpPhotoshopSupplementalCategories.getInstance());
        allTables.add(TableCollections.getInstance());
        allTables.add(TableCollectionNames.getInstance());
        allTables.add(TableSavedSearches.getInstance());
    }

    public static ArrayList<Table> get() {
        return allTables;
    }
}

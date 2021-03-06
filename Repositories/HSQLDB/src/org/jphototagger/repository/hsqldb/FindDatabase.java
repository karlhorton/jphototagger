package org.jphototagger.repository.hsqldb;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jphototagger.domain.metadata.MetaDataValue;
import org.jphototagger.domain.metadata.search.Join;
import org.jphototagger.domain.metadata.search.Join.Type;
import org.jphototagger.domain.metadata.search.ParamStatement;
import org.jphototagger.domain.metadata.xmp.XmpDcSubjectsSubjectMetaDataValue;
import org.jphototagger.domain.repository.SynonymsRepository;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
final class FindDatabase extends Database {

    static final FindDatabase INSTANCE = new FindDatabase();
    private static final Logger LOGGER = Logger.getLogger(FindDatabase.class.getName());

    private FindDatabase() {
    }

    List<File> findImageFiles(ParamStatement paramStatement) {
        if (paramStatement == null) {
            throw new NullPointerException("paramStatement == null");
        }
        List<File> imageFiles = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = paramStatement.getSql();
            stmt = con.prepareStatement(sql);
            List<String> values = paramStatement.getValues();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                imageFiles.add(new File(rs.getString(1)));
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            imageFiles.clear();
        } finally {
            close(rs, stmt);
            free(con);
        }
        return imageFiles;
    }

    /**
     * Liefert alle Dateien, der Metadaten bestimmte Suchbegriffe enthalten.
     * Gesucht wird in allen Spalten mit TabelleA.SpalteB LIKE '%Suchbegriff%'
     * OR TabelleC.SpalteD LIKE '%Suchbegriff%' ...
     *
     * @param searchColumns Spalten, in denen der Suchbegriff vorkommen soll
     * @param searchString  Suchteilzeichenkette
     * @return              Alle gefundenen Dateien
     */
    List<File> findImageFilesLikeOr(List<MetaDataValue> searchColumns, String searchString) {
        if (searchColumns == null) {
            throw new NullPointerException("searchColumns == null");
        }
        if (searchString == null) {
            throw new NullPointerException("searchString == null");
        }
        List<File> imageFiles = new ArrayList<>();
        Map<String, List<MetaDataValue>> columnsOfTable = getColumnsSeparatedByTables(searchColumns);
        for (String tablename : columnsOfTable.keySet()) {
            addImageFilesSearchImageFilesLikeOr(columnsOfTable.get(tablename), searchString, imageFiles, tablename);
        }
        return imageFiles;
    }

    private Map<String, List<MetaDataValue>> getColumnsSeparatedByTables(Collection<? extends MetaDataValue> columns) {
        if (columns == null) {
            throw new NullPointerException("columns == null");
        }
        Map<String, List<MetaDataValue>> columnsOfTable = new HashMap<>();
        for (MetaDataValue col : columns) {
            String tablename = col.getCategory();
            List<MetaDataValue> cols = columnsOfTable.get(tablename);
            if (cols == null) {
                cols = new ArrayList<>();
            }
            cols.add(col);
            columnsOfTable.put(tablename, cols);
        }
        return columnsOfTable;
    }

    private void addImageFilesSearchImageFilesLikeOr(List<MetaDataValue> searchColumns, String searchString, List<File> imageFiles, String tablename) {
        if (searchColumns.size() > 0) {
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                con = getConnection();
                stmt = con.prepareStatement(getSqlFindImageFilesLikeOr(searchColumns, tablename, searchString));
                for (int i = 0; i < searchColumns.size(); i++) {
                    stmt.setString(i + 1, "%" + searchString + "%");
                }
                addSynonyms(searchColumns, searchString, stmt);
                LOGGER.log(Level.FINEST, stmt.toString());
                rs = stmt.executeQuery();
                File imageFile;
                while (rs.next()) {
                    imageFile = new File(rs.getString(1));
                    if (!imageFiles.contains(imageFile)) {
                        imageFiles.add(imageFile);
                    }
                }
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, null, t);
                imageFiles.clear();
            } finally {
                close(rs, stmt);
                free(con);
            }
        }
    }

    private void addSynonyms(List<MetaDataValue> searchColumns, String searchString, PreparedStatement stmt) throws SQLException {
        if (searchColumns.contains(XmpDcSubjectsSubjectMetaDataValue.INSTANCE)) {
            int paramIndex = searchColumns.size() + 1;
            SynonymsRepository synonymsRepo = Lookup.getDefault().lookup(SynonymsRepository.class);
            for (String synonym : synonymsRepo.findSynonymsOfWord(searchString)) {
                stmt.setString(paramIndex++, synonym);
            }
        }
    }

    private String getSqlFindImageFilesLikeOr(List<MetaDataValue> searchColumns, String tablename, String searchString) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT files.filename FROM ");
        sql.append("files").append(Join.getJoinToFiles(tablename, Type.INNER)).append(" WHERE ");
        boolean isFirstColumn = true;
        for (MetaDataValue column : searchColumns) {
            sql.append(!isFirstColumn
                    ? " OR "
                    : "").append(column.getCategory()).append(".").append(column.getValueName()).append(" LIKE ?");
            isFirstColumn = false;
        }
        if (searchColumns.contains(XmpDcSubjectsSubjectMetaDataValue.INSTANCE)) {
            addSynonyms(sql, searchString);
        }
        sql.append(" ORDER BY files.filename ASC");
        return sql.toString();
    }

    private void addSynonyms(StringBuilder sb, String searchString) {
        SynonymsRepository synonymsRepo = Lookup.getDefault().lookup(SynonymsRepository.class);
        int count = synonymsRepo.findSynonymsOfWord(searchString).size();
        String colName = XmpDcSubjectsSubjectMetaDataValue.INSTANCE.getCategory() + "."
                + XmpDcSubjectsSubjectMetaDataValue.INSTANCE.getValueName();
        for (int i = 0; i < count; i++) {
            sb.append(" OR ");
            sb.append(colName);
            sb.append(" = ?");
        }
    }
}

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
package de.elmar_baumann.jpt.app.update.tables;

import de.elmar_baumann.jpt.database.DatabaseMetadata;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.lib.dialog.ProgressDialog;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds new columns to the database.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-11-06
 */
final class UpdateTablesAddColumns {

    private final UpdateTablesMessages messages = UpdateTablesMessages.INSTANCE;
    private final ProgressDialog dialog = messages.getProgressDialog();
    private final List<ColumnInfo> missingColumns = new ArrayList<ColumnInfo>();
    private static final List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

    static {
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "parameters_after_filename", // NOI18N
                "BINARY", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "action", // NOI18N
                "BOOLEAN", // NOI18N
                new IndexOfColumn(
                "programs", // NOI18N
                "action", // NOI18N
                "idx_programs_action", // NOI18N
                false)));
        columns.add(new ColumnInfo("programs", // NOI18N
                "input_before_execute", // NOI18N
                "BOOLEAN", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "input_before_execute_per_file", // NOI18N
                "BOOLEAN", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "single_file_processing", // NOI18N
                "BOOLEAN", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "change_file", // NOI18N
                "BOOLEAN", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "use_pattern", // NOI18N
                "BOOLEAN", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "programs", // NOI18N
                "pattern", // NOI18N
                "BINARY", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "hierarchical_subjects", // NOI18N
                "real", // NOI18N
                "BOOLEAN", // NOI18N
                new IndexOfColumn(
                "hierarchical_subjects", // NOI18N
                "real", // NOI18N
                "idx_hierarchical_subjects_real", // NOI18N
                false))); // NOI18N
        columns.add(new ColumnInfo(
                "xmp", // NOI18N
                "rating", // NOI18N
                "BIGINT", // NOI18N
                null));
        columns.add(new ColumnInfo(
                "metadata_edit_templates", // NOI18N
                "rating", // NOI18N
                "BINARY", // NOI18N
                null));
    }

    void update(Connection connection) throws SQLException {
        fixBugs(connection);
        setColumns(connection);
        if (missingColumns.size() > 0) {
            addColumns(connection);
        }
    }

    private void setColumns(Connection connection) throws SQLException {
        DatabaseMetadata dbMeta = DatabaseMetadata.INSTANCE;
        missingColumns.clear();
        for (ColumnInfo info : columns) {
            if (!dbMeta.existsColumn(
                    connection, info.getTableName(), info.getColumnName())) {
                missingColumns.add(info);
            }
        }
    }

    private void addColumns(Connection connection) throws SQLException {
        dialog.setIndeterminate(true);
        messages.message(Bundle.getString("UpdateTablesAddColumns.Info.update")); // NOI18N
        for (ColumnInfo info : missingColumns) {
            addColumn(connection, info);
        }
        dialog.setIndeterminate(false);
    }

    private void addColumn(Connection connection, ColumnInfo info) throws
            SQLException {
        setMessage(info.getTableName(), info.getColumnName());
        Statement stmt = connection.createStatement();
        stmt.execute("ALTER TABLE " + info.getTableName() + " ADD COLUMN " + // NOI18N
                info.getColumnName() + " " + info.getDataType()); // NOI18N
        if (info.getIndex() != null) {
            stmt.execute(info.getIndex().getSql());
        }
    }

    private void setMessage(String tableName, String columnName) {
        messages.message(Bundle.getString(
                "UpdateTablesAddColumns.Info.AddColumns", // NOI18N
                tableName, columnName));
    }

    private void fixBugs(Connection connection) throws SQLException {
        fixBugsMetaDataEditTemplates(connection);
    }

    private void fixBugsMetaDataEditTemplates(Connection connection) throws SQLException {
        final String tableName  = "metadata_edit_templates";
        final String columnName = "rating";
        if (!DatabaseMetadata.INSTANCE.existsColumn(connection, tableName, columnName)) {
            return;
        }
        List<DatabaseMetadata.ColumnInfo> infos =
                DatabaseMetadata.INSTANCE.getColumnInfo(connection, tableName, columnName);
        boolean hasInfo = infos.size() == 1;
        assert  hasInfo : infos.size();
        if (hasInfo) {
            DatabaseMetadata.ColumnInfo info    = infos.get(0);
            boolean                     typeOk  = info.DATA_TYPE == java.sql.Types.BINARY;
            boolean                     indexOk = info.ORDINAL_POSITION == 21;
            boolean                     isOk    = typeOk && indexOk;
            if (!isOk) {
                messages.message(Bundle.getString("UpdateTablesAddColumns.Info.DropColumnMetaDataEditTemplates",
                        tableName, columnName, typeOk, indexOk));
                dropColumn(connection, tableName, columnName);
            }
        }
    }

    void dropColumn(Connection connection, String tableName, String columnName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("ALTER TABLE " + tableName + " DROP "+columnName);
    }
}
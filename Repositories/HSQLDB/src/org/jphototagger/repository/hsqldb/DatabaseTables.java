package org.jphototagger.repository.hsqldb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * All database tables.
 * <p>
 * Rules:
 * <ul>
 * <li>Table- and column names are written in <strong>lowercase</strong>, parts
 *     of compound names are separated by an underscore "<strong>_</strong>".
 *     Table names are in plural and column names in singular. Examples for
 *     table names: <code>"files", "saved_searches", "collection_names".
 *     </code> Examples for column names: <code>"name", "sequence_number",
 *     "subject"</code>.
 * </li>
 * <li>With a few exceptions, all tables have a surrogate primary key. It's
 *     name <em>has to be</em> "<strong>id</strong>" (Identifier, lowercase)
 * </li>
 * <li>"Link tables" resolving n:m relations are containing the names of the
 *     linked tables separated by an underscore
 * </li>
 * <li>Foreign key columns having a compound name starting with
 *     "<strong>id_</strong>" followed by the name of the referenced table
 *     in singular. Examples: "<code>"id_file"</code>" (references table
 *     "files", column "id"), <code>"id_saved_search"</code> (references table
 *     "saved_searches", column "id").
 * </li>
 * <li>Let the DBMS do the work: Generating unique sourrogate primary keys and
 *     setting null or deleting cascade on deletion of "linked" rows
 * </li>
 * <li>Create an index for columns which maybe queried. The name of an index
 *     starts with "<strong>idx_</strong>" followed by the table name followed
 *     by the indexed column name(s). Examples: <code>"idx_dc_subjects_subject"
 *     </code> (index of column "subject" in the table "dc_subjects"),
 *     <code>"idx_synonyms_synonym"</code> (index of column "synonym" in table
 *     "synonyms").
 * </li>
 * </ul>
 *
 * @author Elmar Baumann
 */
final class DatabaseTables extends Database {

    static final DatabaseTables INSTANCE = new DatabaseTables();

    private DatabaseTables() {
    }

    public void createTables() throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(true);
            stmt = con.createStatement();
            // Do not forget updating AppDatabase.DATABASE_VERSION!
            createAppTable(con, stmt);    // prior to all other tables!
            createFilesTable(con, stmt);
            createXmpTables(con, stmt);
            createExifTables(con, stmt);
            createCollectionsTables(con, stmt);
            createSavedSearchesTables(con, stmt);
            createAutoScanDirectoriesTable(con, stmt);
            createMetadataTemplateTable(con, stmt);
            createFavoriteDirectoriesTable(con, stmt);
            createFileExcludePatternsTable(con, stmt);
            createProgramsTable(con, stmt);
            createActionsAfterDbInsertionTable(con, stmt);
            createDefaultProgramsTable(con, stmt);
            createHierarchicalSubjectsTable(con, stmt);
            createSynonymsTable(con, stmt);
            createRenameTemplatesTable(con, stmt);
            createUserDefinedFileFiltersTable(con, stmt);
            createUserDefinedFileTypesTable(con, stmt);
            createWordsetTables(con, stmt);
            // Do not forget updating AppDatabase.DATABASE_VERSION!
        } finally {
            close(stmt);
            free(con);
        }
    }

    private void createFilesTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "files")) {
            stmt.execute("CREATE CACHED TABLE files"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", filename VARCHAR_IGNORECASE(512) NOT NULL"
                    + ", size_in_bytes BIGINT"
                    + ", lastmodified BIGINT"
                    + ", xmp_lastmodified BIGINT"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_files ON files (filename)");
        }
    }

    private void createXmpTables(Connection con, Statement stmt) throws SQLException {
        create1nTable(con, stmt, "dc_creators", "creator", 128);
        create1nTable(con, stmt, "dc_rights", "rights", 128);
        create1nTable(con, stmt, "iptc4xmpcore_locations", "location", 64);
        create1nTable(con, stmt, "photoshop_authorspositions", "authorsposition", 32);
        create1nTable(con, stmt, "photoshop_captionwriters", "captionwriter", 32);
        create1nTable(con, stmt, "photoshop_cities", "city", 32);
        create1nTable(con, stmt, "photoshop_countries", "country", 64);
        create1nTable(con, stmt, "photoshop_credits", "credit", 32);
        create1nTable(con, stmt, "photoshop_sources", "source", 32);
        create1nTable(con, stmt, "photoshop_states", "state", 32);

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "dc_subjects")) {
            stmt.execute("CREATE CACHED TABLE dc_subjects"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", subject VARCHAR_IGNORECASE(64)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_dc_subjects_id ON dc_subjects (id)");
            stmt.execute("CREATE UNIQUE INDEX idx_dc_subjects_subject ON dc_subjects (subject)");
        }

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "xmp")) {
            stmt.execute("CREATE CACHED TABLE xmp"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", id_file BIGINT NOT NULL"
                    + ", id_dc_creator BIGINT"
                    + ", dc_description VARCHAR_IGNORECASE(2000)"
                    + ", id_dc_rights BIGINT"
                    + ", dc_title VARCHAR_IGNORECASE(64)"
                    + ", id_iptc4xmpcore_location BIGINT"
                    + ", id_photoshop_authorsposition BIGINT"
                    + ", id_photoshop_captionwriter BIGINT"
                    + ", id_photoshop_city BIGINT"
                    + ", id_photoshop_country BIGINT"
                    + ", id_photoshop_credit BIGINT"
                    + ", photoshop_headline VARCHAR_IGNORECASE(256)"
                    + ", photoshop_instructions VARCHAR_IGNORECASE(256)"
                    + ", id_photoshop_source BIGINT"
                    + ", id_photoshop_state BIGINT"
                    + ", photoshop_transmissionReference VARCHAR_IGNORECASE(32)"
                    + ", rating BIGINT"
                    + ", iptc4xmpcore_datecreated VARCHAR_IGNORECASE(10)"
                    + ", FOREIGN KEY (id_file) REFERENCES files (id) ON DELETE CASCADE"
                    + ", FOREIGN KEY (id_dc_creator) REFERENCES dc_creators (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_dc_rights) REFERENCES dc_rights (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_iptc4xmpcore_location) REFERENCES iptc4xmpcore_locations (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_authorsposition) REFERENCES photoshop_authorspositions (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_captionwriter) REFERENCES photoshop_captionwriters (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_city) REFERENCES photoshop_cities (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_country) REFERENCES photoshop_countries (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_credit) REFERENCES photoshop_credits (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_source) REFERENCES photoshop_sources (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_photoshop_state) REFERENCES photoshop_states (id) ON DELETE SET NULL"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_xmp_id_files ON xmp (id_file)");
            stmt.execute("CREATE INDEX idx_xmp_dc_description ON xmp (dc_description)");
            stmt.execute("CREATE INDEX idx_xmp_id_dc_rights ON xmp (id_dc_rights)");
            stmt.execute("CREATE INDEX idx_xmp_dc_title ON xmp (dc_title)");
            stmt.execute("CREATE INDEX idx_xmp_iptc4xmpcore_location ON xmp (id_iptc4xmpcore_location)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_authorsposition ON xmp (id_photoshop_authorsposition)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_captionwriter ON xmp (id_photoshop_captionwriter)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_city ON xmp (id_photoshop_city)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_country ON xmp (id_photoshop_country)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_credit ON xmp (id_photoshop_credit)");
            stmt.execute("CREATE INDEX idx_xmp_photoshop_headline ON xmp (photoshop_headline)");
            stmt.execute("CREATE INDEX idx_xmp_photoshop_instructions ON xmp (photoshop_instructions)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_source ON xmp (id_photoshop_source)");
            stmt.execute("CREATE INDEX idx_xmp_id_photoshop_state ON xmp (id_photoshop_state)");
            stmt.execute("CREATE INDEX idx_xmp_photoshop_transmissionReference ON xmp (photoshop_transmissionReference)");
            stmt.execute("CREATE INDEX idx_iptc4xmpcore_datecreated ON xmp (iptc4xmpcore_datecreated)");
        }

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "xmp_dc_subject")) {
            stmt.execute("CREATE CACHED TABLE xmp_dc_subject"
                    + " (id_xmp BIGINT"
                    + ", id_dc_subject BIGINT"
                    + ", PRIMARY KEY (id_xmp, id_dc_subject)"
                    + ", FOREIGN KEY (id_xmp) REFERENCES xmp (id) ON DELETE CASCADE"
                    + ", FOREIGN KEY (id_dc_subject) REFERENCES dc_subjects (id) ON DELETE CASCADE"
                    + ");");
            stmt.execute("CREATE INDEX idx_xmp_dc_subject_pk ON xmp_dc_subject (id_xmp, id_dc_subject)");
            stmt.execute("CREATE INDEX idx_xmp_dc_subject_id_xmp ON xmp_dc_subject (id_xmp)");
            stmt.execute("CREATE INDEX idx_xmp_dc_subject_id_dc_subject ON xmp_dc_subject (id_dc_subject)");
        }
    }

    private void create1nTable(Connection con, Statement stmt, String tablename, String columnname, int length)
            throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, tablename)) {
            stmt.execute("CREATE CACHED TABLE " + tablename
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY, "
                    + columnname + " VARCHAR_IGNORECASE(" + Integer.toString(length)
                    + "))");
            stmt.execute("CREATE UNIQUE INDEX idx_" + tablename + "_id" + " ON " + tablename + " (id)");
            stmt.execute("CREATE UNIQUE INDEX idx_" + tablename + "_" + columnname + " ON " + tablename + " (" + columnname + ")");
        }
    }

    private void createExifTables(Connection con, Statement stmt) throws SQLException {
        create1nTable(con, stmt, "exif_recording_equipment", "equipment", 125);
        create1nTable(con, stmt, "exif_lenses", "lens", 256);

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "exif")) {
            stmt.execute("CREATE CACHED TABLE exif"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", id_file BIGINT NOT NULL"
                    + ", id_exif_recording_equipment BIGINT"
                    + ", exif_date_time_original DATE"
                    + ", exif_focal_length REAL"
                    + ", exif_iso_speed_ratings SMALLINT"
                    + ", id_exif_lens BIGINT"
                    + ", exif_date_time_original_timestamp BIGINT"
                    + ", exif_gps_latitude DOUBLE"
                    + ", exif_gps_longitude DOUBLE"
                    + ", FOREIGN KEY (id_file) REFERENCES files (id) ON DELETE CASCADE"
                    + ", FOREIGN KEY (id_exif_recording_equipment) REFERENCES exif_recording_equipment (id) ON DELETE SET NULL"
                    + ", FOREIGN KEY (id_exif_lens) REFERENCES exif_lenses (id) ON DELETE SET NULL"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_exif_id_files ON exif (id_file)");
            stmt.execute("CREATE INDEX idx_exif_id_recording_equipment ON exif (id_exif_recording_equipment)");
            stmt.execute("CREATE INDEX idx_exif_date_time_original ON exif (exif_date_time_original)");
            stmt.execute("CREATE INDEX idx_exif_focal_length ON exif (exif_focal_length)");
            stmt.execute("CREATE INDEX idx_exif_iso_speed_ratings ON exif (exif_iso_speed_ratings)");
            stmt.execute("CREATE INDEX idx_exif_id_exif_lens ON exif (id_exif_lens)");
        }
    }

    private void createCollectionsTables(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "collection_names")) {
            stmt.execute("CREATE CACHED TABLE collection_names"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", name VARCHAR_IGNORECASE(256)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_collection_names_id ON collection_names (id)");
            stmt.execute("CREATE INDEX idx_collection_names_name ON collection_names (name)");
        }

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "collections")) {
            stmt.execute("CREATE CACHED TABLE collections"
                    + " (id_collectionnname BIGINT"
                    + ", id_file BIGINT"
                    + ", sequence_number INTEGER"
                    + ", FOREIGN KEY (id_collectionnname) REFERENCES collection_names (id) ON DELETE CASCADE"
                    + ", FOREIGN KEY (id_file) REFERENCES files (id) ON DELETE CASCADE"
                    + ");");
            stmt.execute("CREATE INDEX idx_collections_id_collectionnnames ON collections (id_collectionnname)");
            stmt.execute("CREATE INDEX idx_collections_id_files ON collections (id_file)");
            stmt.execute("CREATE INDEX idx_collections_sequence_number ON collections (sequence_number)");
        }
    }

    private void createSavedSearchesTables(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "saved_searches")) {
            stmt.execute("CREATE CACHED TABLE saved_searches"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", name VARCHAR_IGNORECASE(125)"
                    + ", custom_sql VARBINARY(32000)"
                    + ", search_type SMALLINT"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_saved_searches_id ON saved_searches (id)");
            stmt.execute("CREATE UNIQUE INDEX idx_saved_searches_name ON saved_searches (name)");
        }

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "saved_searches_panels")) {
            stmt.execute("CREATE CACHED TABLE saved_searches_panels"
                    + " (id_saved_search BIGINT"
                    + ", panel_index INTEGER"
                    + ", bracket_left_1 BOOLEAN"
                    + ", operator_id INTEGER"
                    + ", bracket_left_2 BOOLEAN"
                    + ", column_id INTEGER"
                    + ", comparator_id  INTEGER"
                    + ", value VARCHAR(256)"
                    + ", bracket_right BOOLEAN"
                    + ", FOREIGN KEY (id_saved_search) REFERENCES saved_searches (id) ON DELETE CASCADE"
                    + ");");
            stmt.execute("CREATE INDEX idx_saved_searches_panels_id_saved_search ON saved_searches_panels (id_saved_search)");
            stmt.execute("CREATE INDEX idx_saved_searches_panels_panel_index ON saved_searches_panels (panel_index)");
        }

        if (!DatabaseMetadata.INSTANCE.existsTable(con, "saved_searches_keywords")) {
            stmt.execute("CREATE CACHED TABLE saved_searches_keywords"
                    + " (id_saved_search BIGINT"
                    + ", keyword VARCHAR_IGNORECASE(64)"
                    + ", FOREIGN KEY (id_saved_search) REFERENCES saved_searches (id) ON DELETE CASCADE"
                    + ");");
            stmt.execute("CREATE INDEX idx_saved_searches_keywords_id_saved_search ON saved_searches_keywords (id_saved_search)");
        }
    }

    private void createAutoScanDirectoriesTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "autoscan_directories")) {
            stmt.execute("CREATE CACHED TABLE autoscan_directories"
                    + " (directory VARCHAR_IGNORECASE(1024)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_autoscan_directories_directory ON autoscan_directories (directory)");
        }
    }

    private void createMetadataTemplateTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "metadata_edit_templates")) {
            stmt.execute("CREATE CACHED TABLE metadata_edit_templates"
                    + " (name VARCHAR_IGNORECASE(256)"
                    + ", dcSubjects VARBINARY(32000)"
                    + ", dcTitle VARBINARY(32000)"
                    + ", photoshopHeadline VARBINARY(32000)"
                    + ", dcDescription VARBINARY(32000)"
                    + ", photoshopCaptionwriter VARBINARY(32000)"
                    + ", iptc4xmpcoreLocation VARBINARY(32000)"
                    + ", dcRights VARBINARY(32000)"
                    + ", dcCreator VARBINARY(32000)"
                    + ", photoshopAuthorsposition VARBINARY(32000)"
                    + ", photoshopCity VARBINARY(32000)"
                    + ", photoshopState VARBINARY(32000)"
                    + ", photoshopCountry VARBINARY(32000)"
                    + ", photoshopTransmissionReference VARBINARY(32000)"
                    + ", photoshopInstructions VARBINARY(32000)"
                    + ", photoshopCredit VARBINARY(32000)"
                    + ", photoshopSource VARBINARY(32000)"
                    + ", rating VARBINARY(32000)"
                    + ", iptc4xmpcore_datecreated VARBINARY(32000)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_metadata_edit_templates_name ON metadata_edit_templates (name)");
        }
    }

    private void createFavoriteDirectoriesTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "favorite_directories")) {
            stmt.execute("CREATE CACHED TABLE favorite_directories"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", favorite_name VARCHAR_IGNORECASE(256)"
                    + ", directory_name VARCHAR(512)"
                    + ", favorite_index INTEGER"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_favorite_directories_favorite_name ON favorite_directories (favorite_name)");
        }
    }

    private void createFileExcludePatternsTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "file_exclude_patterns")) {
            stmt.execute("CREATE CACHED TABLE file_exclude_patterns"
                    + " (pattern VARCHAR_IGNORECASE(256)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_file_exclude_pattern_pattern ON file_exclude_patterns (pattern)");
        }
    }

    private void createProgramsTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "programs")) {
            stmt.execute("CREATE CACHED TABLE programs"
                    + " (id BIGINT NOT NULL"
                    + ", action BOOLEAN"
                    + ", filename VARCHAR(512) NOT NULL"
                    + ", alias VARCHAR_IGNORECASE(250) NOT NULL"
                    + ", parameters_before_filename VARBINARY(32000)"
                    + ", parameters_after_filename VARBINARY(32000)"
                    + ", input_before_execute BOOLEAN"
                    + ", input_before_execute_per_file BOOLEAN"
                    + ", single_file_processing BOOLEAN"
                    + ", change_file BOOLEAN"
                    + ", sequence_number INTEGER"
                    + ", use_pattern BOOLEAN"
                    + ", pattern VARBINARY(32000)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_programs_id ON programs (id)");
            stmt.execute("CREATE INDEX idx_programs_filename ON programs (filename)");
            stmt.execute("CREATE INDEX idx_programs_alias ON programs (alias)");
            stmt.execute("CREATE INDEX idx_programs_sequence_number ON programs (sequence_number)");
            stmt.execute("CREATE INDEX idx_programs_action ON programs (action)");
        }
    }

    private void createActionsAfterDbInsertionTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "actions_after_db_insertion")) {
            stmt.execute("CREATE CACHED TABLE actions_after_db_insertion"
                    + " (id_program BIGINT NOT NULL"
                    + ", action_order INTEGER"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_actions_after_db_insertion_id_programs ON actions_after_db_insertion (id_program)");
            stmt.execute("CREATE INDEX idx_actions_after_db_insertion_action_order ON actions_after_db_insertion (action_order)");
        }
    }

    private void createDefaultProgramsTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "default_programs")) {
            stmt.execute("CREATE CACHED TABLE default_programs"
                    + " (id_program BIGINT NOT NULL"
                    + " , filename_suffix VARCHAR_IGNORECASE(64)"
                    + ");");
            stmt.execute("CREATE INDEX idx_default_programs_id_program ON default_programs (id_program)");
            stmt.execute("CREATE UNIQUE INDEX idx_default_programs_filename_suffix ON default_programs (filename_suffix)");
        }
    }

    private void createHierarchicalSubjectsTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "hierarchical_subjects")) {
            stmt.execute("CREATE CACHED TABLE hierarchical_subjects"
                    + " (id BIGINT NOT NULL"
                    + ", id_parent BIGINT"
                    + ", subject VARCHAR_IGNORECASE(64) NOT NULL"
                    + ", real BOOLEAN"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_hierarchical_subjects_id ON hierarchical_subjects (id)");
            stmt.execute("CREATE INDEX idx_hierarchical_subjects_id_parent ON hierarchical_subjects (id_parent)");
            stmt.execute("CREATE INDEX idx_hierarchical_subjects_subject ON hierarchical_subjects (subject)");
            stmt.execute("CREATE INDEX idx_hierarchical_subjects_real ON hierarchical_subjects (real)");
        }
    }

    private void createAppTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "application")) {
            stmt.execute("CREATE CACHED TABLE application"
                    + " (key VARCHAR(128) PRIMARY KEY"
                    + ", value VARBINARY(32000)"
                    + ");");
        }
    }

    private void createSynonymsTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "synonyms")) {
            stmt.execute("CREATE CACHED TABLE synonyms"
                    + " (word VARCHAR(128)"
                    + ", synonym VARCHAR(128)"
                    + ", PRIMARY KEY (word, synonym)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_synonyms ON synonyms (word, synonym)");
            stmt.execute("CREATE INDEX idx_synonyms_word ON synonyms (word)");
            stmt.execute("CREATE INDEX idx_synonyms_synonym ON synonyms (synonym)");
        }
    }

    private void createRenameTemplatesTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "rename_templates")) {
            stmt.execute("CREATE CACHED TABLE rename_templates"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", name VARCHAR(128) NOT NULL"
                    + ", start_number INTEGER"
                    + ", step_width INTEGER"
                    + ", number_count INTEGER"
                    + ", date_delimiter VARCHAR(5)"
                    + ", format_class_at_begin VARCHAR(512)"
                    + ", delimiter_1 VARCHAR(25)"
                    + ", format_class_in_the_middle VARCHAR(512)"
                    + ", delimiter_2 VARCHAR(25)"
                    + ", format_class_at_end VARCHAR(512)"
                    + ", text_at_begin VARCHAR(512)"
                    + ", text_in_the_middle VARCHAR(512)"
                    + ", text_at_end VARCHAR(512)"
                    + ", UNIQUE(name)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_rename_templates_name ON rename_templates (name)");
        }
    }

    private void createUserDefinedFileFiltersTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "user_defined_file_filters")) {
            stmt.execute("CREATE CACHED TABLE user_defined_file_filters"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", is_not BOOLEAN, type INTEGER"
                    + ", name VARCHAR_IGNORECASE(45) NOT NULL"
                    + ", expression VARCHAR(128) NOT NULL"
                    + ", UNIQUE(name)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_user_defined_file_filters_name ON user_defined_file_filters (name)");
        }
    }

    private void createUserDefinedFileTypesTable(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "user_defined_file_types")) {
            stmt.execute("CREATE CACHED TABLE user_defined_file_types"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", suffix VARCHAR_IGNORECASE(45) NOT NULL"
                    + ", description VARCHAR_IGNORECASE(255)"
                    + ", external_thumbnail_creator BOOLEAN"
                    + ");");
            stmt.execute("CREATE INDEX idx_user_defined_file_types_suffix ON user_defined_file_types (suffix)");
        }
    }

    private void createWordsetTables(Connection con, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "wordsets")) {
            stmt.execute("CREATE CACHED TABLE wordsets"
                    + " (id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY"
                    + ", name VARCHAR_IGNORECASE(255)"
                    + ");");
            stmt.execute("CREATE UNIQUE INDEX idx_wordsets_name ON wordsets (name)");
        }
        if (!DatabaseMetadata.INSTANCE.existsTable(con, "wordsets_words")) {
            stmt.execute("CREATE CACHED TABLE wordsets_words"
                    + " (id_wordsets BIGINT"
                    + ", word VARCHAR_IGNORECASE(255)"
                    + ", word_order BIGINT"
                    + ", FOREIGN KEY (id_wordsets) REFERENCES wordsets (id) ON DELETE CASCADE"
                    + ");");
            stmt.execute("CREATE INDEX idx_wordsets_words_id_wordsets ON wordsets_words (id_wordsets)");
        }
    }
}

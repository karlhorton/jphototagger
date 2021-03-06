package org.jphototagger.repository.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushe.swing.event.EventBus;
import org.jphototagger.domain.filetypes.UserDefinedFileType;
import org.jphototagger.domain.repository.event.userdefinedfiletypes.UserDefinedFileTypeDeletedEvent;
import org.jphototagger.domain.repository.event.userdefinedfiletypes.UserDefinedFileTypeInsertedEvent;
import org.jphototagger.domain.repository.event.userdefinedfiletypes.UserDefinedFileTypeUpdatedEvent;

/**
 * @author Elmar Baumann
 */
final class UserDefinedFileTypesDatabase extends Database {

    static final UserDefinedFileTypesDatabase INSTANCE = new UserDefinedFileTypesDatabase();
    private static final Logger LOGGER = Logger.getLogger(UserDefinedFileTypesDatabase.class.getName());

    int insert(UserDefinedFileType fileType) {
        if (fileType == null) {
            throw new NullPointerException("fileType == null");
        }
        int count = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        String suffix = fileType.getSuffix();
        String description = fileType.getDescription();
        boolean externalThumbnailCreator = fileType.isExternalThumbnailCreator();
        try {
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(
                    "INSERT INTO user_defined_file_types (suffix, description, external_thumbnail_creator) VALUES (?, ?, ?)");
            stmt.setString(1, suffix);
            stmt.setString(2, description);
            stmt.setBoolean(3, externalThumbnailCreator);
            LOGGER.log(Level.FINER, stmt.toString());
            count = stmt.executeUpdate();
            con.commit();
            if (count > 0) {
                fileType.setId(findIdOfSuffix(con, suffix));
                notifyFileTypeInserted(fileType);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            count = 0;
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return count;
    }

    int update(UserDefinedFileType oldFileType, UserDefinedFileType newFileType) {
        if (newFileType == null) {
            throw new NullPointerException("fileType == null");
        }
        int count = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        Long id = newFileType.getId();
        String suffix = newFileType.getSuffix();
        String description = newFileType.getDescription();
        boolean externalThumbnailCreator = newFileType.isExternalThumbnailCreator();
        try {
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(
                    "UPDATE user_defined_file_types SET suffix = ?, description = ?, external_thumbnail_creator = ?  WHERE id = ?");
            stmt.setString(1, suffix);
            stmt.setString(2, description);
            stmt.setBoolean(3, externalThumbnailCreator);
            stmt.setLong(4, id);
            LOGGER.log(Level.FINER, stmt.toString());
            count = stmt.executeUpdate();
            con.commit();
            if (count > 0) {
                notifyFileTypeUpdated(oldFileType, newFileType);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            count = 0;
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return count;
    }

    /**
     *
     * @param  fileType only the suffix must exist
     * @return
     */
    int delete(UserDefinedFileType fileType) {
        if (fileType == null) {
            throw new NullPointerException("fileType == null");
        }
        int count = 0;
        String suffix = fileType.getSuffix();
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("DELETE FROM user_defined_file_types WHERE suffix = ?");
            stmt.setString(1, suffix);
            LOGGER.log(Level.FINER, stmt.toString());
            count = stmt.executeUpdate();
            con.commit();
            if (count > 0) {
                notifyFileTypeDeleted(fileType);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            count = 0;
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return count;
    }

    List<UserDefinedFileType> getAll() {
        List<UserDefinedFileType> fileTypes = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT id, suffix, description, external_thumbnail_creator FROM user_defined_file_types ORDER BY suffix";
            stmt = con.createStatement();
            LOGGER.log(Level.FINEST, sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserDefinedFileType fileType = new UserDefinedFileType();
                long id = rs.getLong(1);
                String suffix = rs.getString(2);
                String description = rs.getString(3);
                boolean externalThumbnailCreator = rs.getBoolean(4);
                fileType.setId(id);
                fileType.setSuffix(suffix);
                fileType.setDescription(description);
                fileType.setExternalThumbnailCreator(externalThumbnailCreator);
                fileTypes.add(fileType);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            fileTypes.clear();
        } finally {
            close(rs, stmt);
            free(con);
        }
        return fileTypes;
    }

    static int getMaxLengthSuffix() {
        return 45;
    }

    boolean existsSuffix(String suffix) {
        if (suffix == null) {
            throw new NullPointerException("suffix == null");
        }
        long count = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT COUNT(*) FROM user_defined_file_types WHERE suffix = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, suffix);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            count = 0;
            rollback(con);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return count > 0;
    }

    UserDefinedFileType findBySuffix(String suffix) {
        if (suffix == null) {
            throw new NullPointerException("suffix == null");
        }
        UserDefinedFileType fileType = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT id, description, external_thumbnail_creator FROM user_defined_file_types WHERE suffix = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, suffix);
            LOGGER.log(Level.FINEST, sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                fileType = new UserDefinedFileType();
                long id = rs.getLong(1);
                String description = rs.getString(2);
                boolean externalThumbnailCreator = rs.getBoolean(3);
                fileType.setId(id);
                fileType.setSuffix(suffix);
                fileType.setDescription(description);
                fileType.setExternalThumbnailCreator(externalThumbnailCreator);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return fileType;
    }

    private Long findIdOfSuffix(Connection con, String suffix) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long id = null;
        try {
            stmt = con.prepareStatement("SELECT id FROM user_defined_file_types WHERE suffix = ?");
            stmt.setString(1, suffix);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
        }
        return id;
    }

    private void notifyFileTypeInserted(UserDefinedFileType fileType) {
        EventBus.publish(new UserDefinedFileTypeInsertedEvent(this, fileType));
    }

    private void notifyFileTypeDeleted(UserDefinedFileType fileType) {
        EventBus.publish(new UserDefinedFileTypeDeletedEvent(this, fileType));
    }

    private void notifyFileTypeUpdated(UserDefinedFileType oldFileType, UserDefinedFileType newFileType) {
        EventBus.publish(new UserDefinedFileTypeUpdatedEvent(this, oldFileType, newFileType));
    }

    private UserDefinedFileTypesDatabase() {
    }
}

package org.jphototagger.program.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bushe.swing.event.EventBus;
import org.jphototagger.domain.repository.event.fileexcludepattern.FileExcludePatternDeletedEvent;
import org.jphototagger.domain.repository.event.fileexcludepattern.FileExcludePatternInsertedEvent;
import org.jphototagger.api.event.ProgressEvent;
import org.jphototagger.api.event.ProgressListener;
import org.jphototagger.domain.repository.ImageFileRepository;
import org.jphototagger.domain.repository.event.imagefiles.ImageFileDeletedEvent;
import org.jphototagger.program.cache.PersistentThumbnails;
import org.openide.util.Lookup;

/**
 *
 *
 * @author Elmar Baumann
 */
final class DatabaseFileExcludePatterns extends Database {

    static final DatabaseFileExcludePatterns INSTANCE = new DatabaseFileExcludePatterns();
    private final ImageFileRepository repo = Lookup.getDefault().lookup(ImageFileRepository.class);

    private DatabaseFileExcludePatterns() {
    }

    /**
     * Inserts a file exclude pattern.
     *
     * @param  pattern  pattern
     * @return true if inserted
     * @see    #existsValueInColumn(java.lang.String)
     */
    boolean insertFileExcludePattern(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern == null");
        }

        boolean inserted = false;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("INSERT INTO file_exclude_patterns (pattern) VALUES (?)");
            stmt.setString(1, pattern);
            logFiner(stmt);

            int count = stmt.executeUpdate();

            con.commit();
            inserted = count > 0;

            if (inserted) {
                notifyInserted(pattern);
            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseFileExcludePatterns.class.getName()).log(Level.SEVERE, null, ex);
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }

        return inserted;
    }

    /**
     * Deletes a file exclude pattern from the database.
     *
     * @param  pattern  pattern
     * @return true if deleted
     */
    boolean deleteFileExcludePattern(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern == null");
        }

        boolean deleted = false;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("DELETE FROM file_exclude_patterns WHERE pattern = ?");
            stmt.setString(1, pattern);
            logFiner(stmt);

            int count = stmt.executeUpdate();

            con.commit();
            deleted = count > 0;

            if (deleted) {
                notifyDeleted(pattern);
            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseFileExcludePatterns.class.getName()).log(Level.SEVERE, null, ex);
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }

        return deleted;
    }

    /**
     * Returns wheter a file exclude pattern existsValueIn.
     *
     * @param  pattern pattern
     * @return true if existsValueIn
     */
    boolean existsFileExcludePattern(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern == null");
        }

        boolean exists = false;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement("SELECT COUNT(*) FROM file_exclude_patterns WHERE pattern = ?");
            stmt.setString(1, pattern);
            logFinest(stmt);
            rs = stmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseFileExcludePatterns.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(rs, stmt);
            free(con);
        }

        return exists;
    }

    /**
     * Returns all file exclude patterns.
     *
     * @return patterns
     */
    List<String> getAllFileExcludePatterns() {
        List<String> patterns = new LinkedList<String>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "SELECT pattern FROM file_exclude_patterns ORDER BY pattern ASC";

            logFinest(sql);
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                patterns.add(rs.getString(1));
            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseFileExcludePatterns.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(rs, stmt);
            free(con);
        }

        return patterns;
    }

    /**
     * Deletes files from the database which matches against some patterns.
     *
     * @param   patterns  patterns
     * @param   listener  progress listener or null, can cancel the action
     * @return  count of deleted files
     */
    int deleteMatchingFiles(List<String> patterns, ProgressListener listener) {
        if (patterns == null) {
            throw new NullPointerException("patterns == null");
        }

        int count = 0;
        Connection con = null;
        PreparedStatement stmtUpdate = null;
        Statement stmtQuery = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            List<String> deletedFiles = new LinkedList<String>();
            String sqlUpdate = "DELETE FROM files WHERE filename = ?";
            String sqlQuery = "SELECT filename FROM files";

            stmtQuery = con.createStatement();
            stmtUpdate = con.prepareStatement(sqlUpdate);
            logFinest(sqlQuery);
            rs = stmtQuery.executeQuery(sqlQuery);

            int patternCount = patterns.size();
            int progress = 0;
            ProgressEvent event = new ProgressEvent(this, 0, DatabaseStatistics.INSTANCE.getFileCount() * patternCount,
                    0, null);

            notifyProgressListenerStart(listener, event);

            boolean cancel = event.isCancel();

            while (!cancel && rs.next()) {
                String filepath = rs.getString(1);

                for (int i = 0; !cancel && (i < patternCount); i++) {
                    progress++;

                    String pattern = patterns.get(i);

                    if (filepath.matches(pattern)) {
                        stmtUpdate.setString(1, filepath);
                        deletedFiles.add(filepath);
                        logFiner(stmtUpdate);

                        int affectedRows = stmtUpdate.executeUpdate();

                        count += affectedRows;

                        if (affectedRows > 0) {
                            File imageFile = getFile(filepath);

                            PersistentThumbnails.deleteThumbnail(imageFile);
                            EventBus.publish(new ImageFileDeletedEvent(this, imageFile));
                        }

                        cancel = event.isCancel();
                    }

                    event.setInfo(filepath);
                    event.setValue(progress);
                    notifyProgressListenerPerformed(listener, event);
                }
            }

            con.commit();
            notifyProgressListenerEnd(listener, event);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseFileExcludePatterns.class.getName()).log(Level.SEVERE, null, ex);
            rollback(con);
        } finally {
            close(rs, stmtQuery);
            close(stmtUpdate);
            free(con);
        }

        return count;
    }

    private void notifyInserted(String pattern) {
        EventBus.publish(new FileExcludePatternInsertedEvent(this, pattern));
    }

    private void notifyDeleted(String pattern) {
        EventBus.publish(new FileExcludePatternDeletedEvent(this, pattern));
    }
}

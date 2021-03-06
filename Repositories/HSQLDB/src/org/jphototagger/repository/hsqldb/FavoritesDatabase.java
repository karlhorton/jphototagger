package org.jphototagger.repository.hsqldb;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushe.swing.event.EventBus;
import org.jphototagger.domain.favorites.Favorite;
import org.jphototagger.domain.repository.event.favorites.FavoriteDeletedEvent;
import org.jphototagger.domain.repository.event.favorites.FavoriteInsertedEvent;
import org.jphototagger.domain.repository.event.favorites.FavoriteUpdatedEvent;

/**
 * @author Elmar Baumann
 */
final class FavoritesDatabase extends Database {

    static final FavoritesDatabase INSTANCE = new FavoritesDatabase();
    private static final Logger LOGGER = Logger.getLogger(FavoritesDatabase.class.getName());

    private FavoritesDatabase() {
    }

    boolean insertOrUpdateFavorite(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        boolean inserted = false;
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            if (existsFavorite(favorite.getName())) {
                return updateFavorite(favorite);
            }
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("INSERT INTO favorite_directories"
                    + " (favorite_name, directory_name, favorite_index)"
                    + " VALUES (?, ?, ?)");
            stmt.setString(1, favorite.getName());
            stmt.setString(2, favorite.getDirectory().getAbsolutePath());
            stmt.setInt(3, favorite.getIndex());
            LOGGER.log(Level.FINER, stmt.toString());
            int count = stmt.executeUpdate();
            con.commit();
            inserted = count > 0;
            if (inserted) {
                favorite.setId(findIdByFavoriteName(favorite.getName()));
                notifyInserted(favorite);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return inserted;
    }

    boolean deleteFavorite(String favoriteName) {
        if (favoriteName == null) {
            throw new NullPointerException("favoriteName == null");
        }
        boolean deleted = false;
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            Favorite delFavorite = find(favoriteName);
            con = getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("DELETE FROM favorite_directories WHERE favorite_name = ?");
            stmt.setString(1, favoriteName);
            LOGGER.log(Level.FINER, stmt.toString());
            int count = stmt.executeUpdate();
            con.commit();
            deleted = count > 0;
            if (deleted && (delFavorite != null)) {
                notifyDeleted(delFavorite);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return deleted;
    }

    boolean updateRenameFavorite(String fromFavoriteName, String toFavoriteName) {
        if (fromFavoriteName == null) {
            throw new NullPointerException("fromFavoriteName == null");
        }
        if (toFavoriteName == null) {
            throw new NullPointerException("toFavoriteName == null");
        }
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            Favorite oldFavorite = find(fromFavoriteName);
            con = getConnection();
            con.setAutoCommit(true);
            String sql = "UPDATE favorite_directories SET favorite_name = ? WHERE favorite_name = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, toFavoriteName);
            stmt.setString(2, fromFavoriteName);
            LOGGER.log(Level.FINER, stmt.toString());
            count = stmt.executeUpdate();
            if (count > 0) {
                notifyUpdated(oldFavorite, find(toFavoriteName));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return count > 0;
    }

    /**
     * Calling if - <em>and only if</em> - some data in the favorite has been
     * updated <em>with exception of the favorite name</em>
     * ({@code Favorite#getName()}).
     * <p>
     * To rename a favorite, call {@code #updateRenameFavorite(String, String)}.
     *
     * @param  favorite favorite
     * @return          true if updated
     */
    boolean updateFavorite(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        boolean updated = false;
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            Favorite oldFavorite = find(con, favorite.getId());
            stmt = con.prepareStatement("UPDATE favorite_directories SET"
                    + " favorite_name = ?, directory_name = ?, favorite_index = ?"
                    + " WHERE id = ?");
            stmt.setString(1, favorite.getName());
            stmt.setString(2, favorite.getDirectory().getAbsolutePath());
            stmt.setInt(3, favorite.getIndex());
            stmt.setLong(4, favorite.getId());
            LOGGER.log(Level.FINER, stmt.toString());
            int count = stmt.executeUpdate();
            con.commit();
            updated = count > 0;
            if (updated) {
                notifyUpdated(oldFavorite, favorite);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
            rollback(con);
        } finally {
            close(stmt);
            free(con);
        }
        return updated;
    }

    List<Favorite> getAllFavorites() {
        List<Favorite> favorites = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            String sql = "SELECT id, favorite_name, directory_name, favorite_index"
                    + " FROM favorite_directories ORDER BY favorite_index ASC";
            LOGGER.log(Level.FINEST, sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Favorite favorite = new Favorite();
                favorite.setId(rs.getLong(1));
                favorite.setName(rs.getString(2));
                favorite.setDirectory(new File(rs.getString(3)));
                favorite.setIndex(rs.getInt(4));
                favorites.add(favorite);
            }
        } catch (Throwable t) {
            favorites.clear();
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return favorites;
    }

    private Favorite find(Connection con, Long id) {
        Favorite favorite = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT id, favorite_name, directory_name, favorite_index"
                    + " FROM favorite_directories WHERE id = ?");
            stmt.setLong(1, id);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                favorite = new Favorite();
                favorite.setId(rs.getLong(1));
                favorite.setName(rs.getString(2));
                favorite.setDirectory(new File(rs.getString(3)));
                favorite.setIndex(rs.getInt(4));
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
        }
        return favorite;
    }

    private Favorite find(String favoriteName) {
        Favorite favorite = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement("SELECT id, favorite_name, directory_name, favorite_index"
                    + " FROM favorite_directories WHERE favorite_name = ?");
            stmt.setString(1, favoriteName);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                favorite = new Favorite();
                favorite.setId(rs.getLong(1));
                favorite.setName(rs.getString(2));
                favorite.setDirectory(new File(rs.getString(3)));
                favorite.setIndex(rs.getInt(4));
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return favorite;
    }

    private Long findIdByFavoriteName(String favoriteName) {
        Long id = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement("SELECT id FROM favorite_directories WHERE favorite_name = ?");
            stmt.setString(1, favoriteName);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return id;
    }

    boolean existsFavorite(String favoriteName) {
        if (favoriteName == null) {
            throw new NullPointerException("favoriteName == null");
        }
        boolean exists = false;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement("SELECT COUNT(*) FROM favorite_directories WHERE favorite_name = ?");
            stmt.setString(1, favoriteName);
            LOGGER.log(Level.FINEST, stmt.toString());
            rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            exists = count > 0;
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        } finally {
            close(rs, stmt);
            free(con);
        }
        return exists;
    }

    private void notifyInserted(Favorite favorite) {
        EventBus.publish(new FavoriteInsertedEvent(this, favorite));
    }

    private void notifyDeleted(Favorite favorite) {
        EventBus.publish(new FavoriteDeletedEvent(this, favorite));
    }

    private void notifyUpdated(Favorite oldFavorite, Favorite updatedFavorite) {
        EventBus.publish(new FavoriteUpdatedEvent(this, oldFavorite, updatedFavorite));
    }
}

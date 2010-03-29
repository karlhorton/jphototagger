/*
 * @(#)DatabaseMaintainance.java    Created on 2008-10-21
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.database;

import org.jphototagger.program.app.AppLogger;
import org.jphototagger.program.app.MessageDisplayer;

import java.sql.Connection;
import java.sql.Statement;

/**
 *
 *
 * @author  Elmar Baumann
 */
public final class DatabaseMaintainance extends Database {
    public static final DatabaseMaintainance INSTANCE =
        new DatabaseMaintainance();

    private DatabaseMaintainance() {}

    /**
     * Shuts down the database.
     */
    public void shutdown() {
        Connection con  = null;
        Statement  stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(true);
            stmt = con.createStatement();
            AppLogger.logInfo(DatabaseMaintainance.class,
                              "DatabaseMaintainance.Info.Shutdown");
            stmt.executeUpdate("SHUTDOWN");
        } catch (Exception ex) {
            AppLogger.logSevere(Database.class, ex);
            MessageDisplayer.error(null, "DatabaseMaintainance.Error.Shutdown");
        } finally {
            close(stmt);
        }
    }

    /**
     * Komprimiert die Datenbank.
     *
     * @return true, wenn die Datenbank erfolgreich komprimiert wurde
     */
    public boolean compressDatabase() {
        boolean    success = false;
        Connection con     = null;
        Statement  stmt    = null;

        try {
            con = getConnection();
            con.setAutoCommit(true);
            stmt = con.createStatement();
            stmt.executeUpdate("CHECKPOINT DEFRAG");
            success = true;
        } catch (Exception ex) {
            AppLogger.logSevere(DatabaseMaintainance.class, ex);
        } finally {
            close(stmt);
            free(con);
        }

        return success;
    }
}
/*
 * @(#)Operator.java    Created on 2008-08-28
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

package org.jphototagger.program.database.metadata;

import org.jphototagger.program.resource.JptBundle;

/**
 * Operator einer Spaltenverknüpfung.
 *
 * @author  Elmar Baumann
 */
public enum Operator {

    /** Verknüpfung zweier Spalten mit AND */
    AND(0, "AND", JptBundle.INSTANCE.getString("Operator.And")),

    /** Verknüpfung zweier Spalten mit OR */
    OR(1, "OR", JptBundle.INSTANCE.getString("Operator.Or")),;

    private final int    id;
    private final String sqlString;
    private final String localizedString;

    private Operator(int id, String sqlString, String localizedString) {
        this.id              = id;
        this.sqlString       = sqlString;
        this.localizedString = localizedString;
    }

    /**
     * Liefert die ID des Operators.
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Liefert einen Operator mit bestimmter ID.
     *
     * @param  id ID
     * @return Operator oder null bei ungültiger ID
     */
    public static Operator get(int id) {
        for (Operator operator : Operator.values()) {
            if (operator.id == id) {
                return operator;
            }
        }

        return null;
    }

    /**
     * Liefert die Verknüpfung in der Landessprache.
     *
     * @return Lokalisierter String.
     */
    public String toLocalizedString() {
        return localizedString;
    }

    /**
     * Liefert den String für ein SQL-Statement.
     *
     * @return SQL-String
     */
    public String toSqlString() {
        return sqlString;
    }

    @Override
    public String toString() {
        return localizedString;
    }
}
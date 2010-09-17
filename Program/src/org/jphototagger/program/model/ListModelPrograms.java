/*
 * @(#)ListModelPrograms.java    Created on 2008-10-16
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.model;

import org.jphototagger.program.data.Program;
import org.jphototagger.program.database.ConnectionPool;
import org.jphototagger.program.database.DatabasePrograms;
import org.jphototagger.program.database.DatabasePrograms.Type;
import org.jphototagger.program.event.listener.DatabaseProgramsListener;

import java.awt.EventQueue;

import java.util.List;

import javax.swing.DefaultListModel;

/**
 * Contains {@link Program}s retrieved through
 * {@link DatabasePrograms#getAll(DatabasePrograms.Type)}.
 *
 * All programs in this model are actions, where
 * {@link org.jphototagger.program.data.Program#isAction()} is true, <em>or</em>
 * programs, where that method returns <code>false</code>.
 *
 * @author Elmar Baumann
 */
public final class ListModelPrograms extends DefaultListModel
        implements DatabaseProgramsListener {
    private static final long serialVersionUID = 1107244876982338977L;
    private Type              type;

    public ListModelPrograms(Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }

        this.type = type;
        addElements();
        DatabasePrograms.INSTANCE.addListener(this);
    }

    private void addElements() {
        if (!ConnectionPool.INSTANCE.isInit()) {
            return;
        }

        List<Program> programs = DatabasePrograms.INSTANCE.getAll(type);

        for (Program program : programs) {
            addElement(program);
        }
    }

    private boolean isAppropriateProgramType(Program program) {
        return (program.isAction() && type.equals(Type.ACTION))
               || (!program.isAction() && type.equals(Type.PROGRAM));
    }

    private void updateProgram(Program program)
            throws IllegalArgumentException {
        int index = indexOf(program);

        if (index >= 0) {
            int     sequenceNumber = program.getSequenceNumber();
            boolean validSeqNumber = (sequenceNumber >= 0)
                                     && (sequenceNumber <= getSize());

            if (!validSeqNumber) {
                throw new IllegalArgumentException(
                    "Invalid sequence number. Size: " + getSize()
                    + ". Sequence number: " + sequenceNumber);
        }

            if (index == sequenceNumber) {
                set(index, program);
            } else if (validSeqNumber) {
                remove(index);
                insertElementAt(program, sequenceNumber);
            }
        }
    }

    @Override
    public void programDeleted(final Program program) {
        if (isAppropriateProgramType(program)) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeElement(program);
                }
            });
        }
    }

    @Override
    public void programInserted(final Program program) {
        if (isAppropriateProgramType(program)) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    addElement(program);
                }
            });
        }
    }

    @Override
    public void programUpdated(final Program program) {
        if (isAppropriateProgramType(program)) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateProgram(program);
                        }
            });
        }
    }
}

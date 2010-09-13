/*
 * @(#)ListenerSupport.java    Created on 2010-01-12
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

package org.jphototagger.program.event.listener.impl;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;

/**
 *
 *
 * @param <T> listener type
 * @author  Elmar Baumann
 */
public class ListenerSupport<T> {
    protected final Set<T> listeners = new CopyOnWriteArraySet<T>();

    public void add(T listener) {
        if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        listeners.add(listener);
    }

    public void remove(T listener) {
        if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        listeners.remove(listener);
    }

    /**
     * Returns added listeners.
     * 
     * @return thread save set (that does not reflect added or removed
     *         listeners after calling this mehtod)
     */
    public Set<T> get() {
        return Collections.unmodifiableSet(listeners);
    }
}

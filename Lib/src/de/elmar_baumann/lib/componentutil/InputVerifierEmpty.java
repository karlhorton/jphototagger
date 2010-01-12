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
package de.elmar_baumann.lib.componentutil;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * A valid input of a <code>JTextComponent</code> has to be empty.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2010-01-06
 */
public final class InputVerifierEmpty extends InputVerifier {

    private final boolean trim;

    /**
     * Constructor setting whether the text shall be trimmed.
     *
     * @param trim true if the text shall be trimmed before verifying.
     *             Default: false.
     */
    public InputVerifierEmpty(boolean trim) {
        this.trim = trim;
    }

    public InputVerifierEmpty() {
        trim = false;
    }

    @Override
    public boolean verify(JComponent input) {
        assert input instanceof JTextComponent;
        if (input instanceof JTextComponent) {
            String text = ((JTextComponent) input).getText();
            return trim ? text.trim().isEmpty() : text.isEmpty();
        }
        return false;
    }
}
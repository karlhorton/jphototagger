package org.jphototagger.lib.swing.inputverifier;

import java.io.Serializable;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * A valid input of a <code>JTextComponent</code> has to be empty.
 *
 * @author Elmar Baumann
 */
public final class EmptyInputVerifier extends InputVerifier implements Serializable {

    private static final long serialVersionUID = 1L;
    private final boolean trim;

    /**
     * Constructor setting whether the text shall be trimmed.
     *
     * @param trim true if the text shall be trimmed before verifying.
     *             Default: false.
     */
    public EmptyInputVerifier(boolean trim) {
        this.trim = trim;
    }

    public EmptyInputVerifier() {
        trim = false;
    }

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextComponent) {
            String text = ((JTextComponent) input).getText();

            return trim
                    ? text.trim().isEmpty()
                    : text.isEmpty();
        }

        return false;
    }
}

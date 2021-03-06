package org.jphototagger.lib.util.logging;

/**
 * (Stack-) Frame in einer Logdatei von <code>java.util.logging.Logger</code>.
 * Ein Frame beschreibt eine Zeile im Backtrace eines Throwable-Objekts.
 *
 * @author Elmar Baumann
 */
public final class FrameLogfileRecord {

    private String className;
    private String methodName;
    private String line;

    /**
     * Liefert den Namen der Klasse, die den Logeintrag veranlasste.
     *
     * @return className Klassenname
     */
    public String getClassName() {
        return className;
    }

    /**
     * Setzt den vollständig qualifizierten Namen der Klasse, die den Logeintrag
     * veranlasste, z.B. <code>javax.marsupial.Wombat</code>.
     *
     * @param className Klassenname
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Liefert die Zeilennummer innerhalb der Quellcodedatei der Klasse.
     *
     * @return Zeilennummer (Integer) oder null, falls nicht existent
     */
    public String getLine() {
        return line;
    }

    /**
     * Liefert ob die Zeilennummer innerhalb der Quellcodedatei der Klasse
     * existiert.
     *
     * @return true, falls existent
     */
    public boolean hasLine() {
        return line != null;
    }

    /**
     * Setzt die Zeilennummer innerhalb der Quellcodedatei der Klasse.
     *
     * @param line Zeilennummer (Integer)
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Liefert den Namen der Methode, die den Logeintrag veranlasste. Dies kann
     * ein unqualifizierte Methodenname sein wie z.B. <code>fred</code> oder
     * Informationen über die Parametertypen in Klammern enthalten, z.B.
     * <code>fred(int,String)</code>.
     *
     * @return Methodenname
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Setzt den Namen der Methode, die den Logeintrag veranlasste. Dies kann
     * ein unqualifizierte Methodenname sein wie z.B. <code>fred</code> oder
     * Informationen über die Parametertypen in Klammern enthalten, z.B.
     * <code>fred(int,String)</code>.
     *
     * @param methodname Methodenname
     */
    public void setMethodName(String methodname) {
        this.methodName = methodname;
    }

    /**
     * Liefert, ob ein Teilstring in irgendeinem der Inhalte vorkommt.
     *
     * @param substring Teilstring
     * @return  true, wenn der Teilstring in irgendeinem der Inhalte vorkommt
     * @throws  NullPointerException if substring is null
     */
    boolean contains(String substring) {
        if (substring == null) {
            throw new NullPointerException("substring == null");
        }

        return containsSubstring(getLine(), substring) || containsSubstring(getClassName(), substring)
                || containsSubstring(getMethodName(), substring);
    }

    private boolean containsSubstring(String string, String substring) {
        if (string == null) {
            return false;
        }

        return string.toLowerCase().contains(substring.toLowerCase());
    }
}

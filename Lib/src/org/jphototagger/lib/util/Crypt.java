package org.jphototagger.lib.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.jphototagger.lib.thirdparty.Base64;

//Code based on: http://www.exampledepot.com/egs/javax.crypto/desstring.html
/**
 * En- and decrypts strings with <code>DESede</code> and <code>Base64</code> so
 * that the encrypted string can be stored into a plain text file, e.g. a
 * {@code java.util.Properties} file.
 * <p>
 * Usage example:
 * <pre>
 * String key         = "d?=30sd#fgvjie}[]§$aslkg";
 * byte[] secKeyBytes = new byte[24];
 *
 * for (int i = 0; i &lt; secKeyBytes.length; i++) {
 *     secKeyBytes[i] = (byte) key.charAt(i);
 * }
 * SecretKey secKey = new SecretKeySpec(secKeyBytes, "DESede");
 *
 * // Encrypt a string
 * try {
 *     Crypt encrypter = new Crypt(secKey);
 *     encrypter.encrypt(string);
 * } catch (Throwable t) {
 *     Logger.getLogger(MyClass.class.getName()).log(Level.SEVERE, null, t);
 * }
 *
 * // Decrypt an encrypted string
 * try {
 *    Crypt decrypter = new Crypt(secKey);
 *    decrypter.decrypt(encryptedString);
 * } catch (Throwable t) {
 *     Logger.getLogger(MyClass.class.getName()).log(Level.SEVERE, null, t);
 * }
 * </pre>
 *
 * You can generate a temporary key:
 * {@code SecretKey secKey = KeyGenerator.getInstance("DESede").generateKey();}.
 *
 * @author Elmar Baumann
 * @see     Base64
 */
public final class Crypt {

    private Cipher ecipher;
    private Cipher dcipher;

    public Crypt(SecretKey key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        try {
            ecipher = Cipher.getInstance("DESede");
            dcipher = Cipher.getInstance("DESede");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Throwable t) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    /**
     * Encrypts a string.
     *
     * @param  str string to encrypt
     * @return     encrypted string <code>Base64</code> encoded or null on errors
     */
    public String encrypt(String str) {
        if (str == null) {
            throw new NullPointerException("str == null");
        }

        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            return Base64.encodeBytes(enc);
        } catch (Throwable t) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, t);
        }

        return null;
    }

    /**
     * Decrypts a string.
     *
     * @param  str <code>Base64</code> encoded string to dencrypt
     * @return     decrpyted string or null on errors
     */
    public String decrypt(String str) {
        if (str == null) {
            throw new NullPointerException("str == null");
        }

        try {
            byte[] dec = Base64.decode(str);
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        } catch (Throwable t) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, t);
        }

        return null;
    }
}

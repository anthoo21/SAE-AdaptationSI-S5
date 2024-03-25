/**
 * Package de la SAE
 */
package com.example.saedolistocks5.outilapi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Classe qui crypte et decrypte le token.
 */
public class EncryptAndDecrypteToken {

    /**
     * Constructeur
     */
    private EncryptAndDecrypteToken() {
    }

    /**
     * La clé a utilisé pour décrypter le token pour se connecter à l'API
     */
    static Key keyToUse;

    /**
     * Initialise la clef.
     * @throws NoSuchAlgorithmException si l'algo pour crypter n'existe pas.
     */
    public static void initializeKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56); // 56 bits pour DES
        keyToUse = keyGen.generateKey();
    }

    /**
     * Méthode pour crypter.
     * @param plainText la chaine à crypté.
     * @return un byte crypté.
     * @throws NoSuchAlgorithmException si l'algo n'est pas reconnu.
     * @throws NoSuchPaddingException pas de padding.
     * @throws IllegalBlockSizeException taille du block illegal.
     * @throws BadPaddingException padding pas bon.
     * @throws InvalidKeyException key pas bonne.
     */
    public static byte[] encrypt(String plainText) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyToUse);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Méthode pour décrypter.
     * @param cipherText le texte cryté.
     * @param key la clef.
     * @return un String.
     * @throws NoSuchPaddingException pas de padding.
     * @throws NoSuchAlgorithmException si l'algo n'est pas reconnue.
     * @throws InvalidKeyException la clef est mauvaise.
     * @throws IllegalBlockSizeException taille du block illegal.
     * @throws BadPaddingException padding mauvais.
     */
    public static String decrypt(byte[] cipherText, Key key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] newPlainText = cipher.doFinal(cipherText);
        return new String(newPlainText, StandardCharsets.UTF_8);
    }

    /**
     * Getter pour obtenir la clef.
     * @return la clef a utiliser.
     */
    public static Key getKey() {
        return keyToUse;
    }

    /**
     * Convertit la clé en tableau de bytes.
     * @param key la clef.
     * @return encode ce tableau en String base64.
     */
    public static String keyToString(Key key) {
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }

    /**
     * Reconstruction de la clef.
     * @param keyStr la clef en String.
     * @param algorithm l'algo.
     * @return la clef reconstruit.
     */
    public static Key stringToKey(String keyStr, String algorithm) {
        // Décode la String base64 en tableau de bytes
        byte[] decodedKey = Base64.decode(keyStr, Base64.DEFAULT);

        // Reconstruit la clé en utilisant le tableau de bytes et l'algorithme
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);

    }
}

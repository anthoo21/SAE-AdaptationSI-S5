package com.example.saedolistocks5.outilapi;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;


import java.security.*;

public class EncryptAndDecrypteToken {

    static Key keyToUse;

    public static void InitializeKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56); // 56 bits pour DES
        keyToUse = keyGen.generateKey();
    }

    public static byte[] encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyToUse);
        return cipher.doFinal(plainText.getBytes("UTF8"));
    }

    // Méthode de décryptage
    public static String decrypt(byte[] cipherText, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] newPlainText = cipher.doFinal(cipherText);
        return new String(newPlainText, "UTF8");
    }

    public static Key getKey() {
        return keyToUse;
    }

    public static String keyToString(Key key) {
        // Convertit la clé en tableau de bytes, puis encode ce tableau en String base64
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }

    public static Key stringToKey(String keyStr, String algorithm) {
        // Décode la String base64 en tableau de bytes
        byte[] decodedKey = Base64.decode(keyStr, Base64.DEFAULT);

        // Reconstruit la clé en utilisant le tableau de bytes et l'algorithme
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);

    }
}

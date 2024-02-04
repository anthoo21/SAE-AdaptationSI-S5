package com.example.saedolistocks5.outilapi;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

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
}

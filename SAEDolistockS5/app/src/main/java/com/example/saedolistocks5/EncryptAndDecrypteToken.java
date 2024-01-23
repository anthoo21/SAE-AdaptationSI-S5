package com.example.saedolistocks5;

import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.security.*;
import javax.crypto.*;


import androidx.annotation.RequiresApi;

public class EncryptAndDecrypteToken {


    public static byte[] encrypt(String plainText, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plainText.getBytes("UTF8"));
    }

    // Méthode de décryptage
    public static String decrypt(byte[] cipherText, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] newPlainText = cipher.doFinal(cipherText);
        return new String(newPlainText, "UTF8");

    }
}

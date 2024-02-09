package com.example.saedolistocks5.outilsdivers;


import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Classe regroupant divers outil
 * @author BONNET, FROMENT et ENJALBERT
 * @version 1.0
 */
public class OutilDivers {

    public static Quartet<String, String, String, String> getInfosUserAndApi(InputStreamReader infosUser) throws IOException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {

        String[] valeurInfoUser;
        BufferedReader infosUserTxt = new BufferedReader(infosUser);
        valeurInfoUser = infosUserTxt.readLine().split(";;;;");

        String tokenCrypte = valeurInfoUser[0];

        // Supprimez les crochets et les espaces
        tokenCrypte = tokenCrypte.replaceAll("[\\[\\]\\s]", "");

        // Séparez les valeurs en utilisant la virgule comme délimiteur
        String[] valeurs = tokenCrypte.split(",");

        // Créez un tableau de bytes et remplissez-le avec les valeurs
        byte[] tableauDeBytes = new byte[valeurs.length];
        for (int i = 0; i < valeurs.length; i++) {
            tableauDeBytes[i] = Byte.parseByte(valeurs[i].trim());
        }
        // On récupère la clé pour décrypter le token
        Key key = EncryptAndDecrypteToken.stringToKey(valeurInfoUser[3], "DES");

        // On décrypte le token
        String token = EncryptAndDecrypteToken.decrypt(tableauDeBytes, key);

        // On récupère l'URL de l'API
        String URLApi = valeurInfoUser[2];

        // On récupère l'utilisateur courant
        String utilisateurCourant = valeurInfoUser[1];

        // Il n'y a pas de quatrième élément
        return new Quartet<>(token, URLApi, utilisateurCourant, null);
    }
}

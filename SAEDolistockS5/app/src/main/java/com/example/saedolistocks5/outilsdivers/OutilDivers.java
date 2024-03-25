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
 * Classe regroupant divers outils
 * @author BONNET, ENJALBERT et FROMENT
 * @version 1.1
 */
public class OutilDivers {

    /**
     * Permet de récupérer les infos de l'utilisateur, de l'API et du mode de connexion
     * @param infosUser quartet regroupant quatre informations :
     *                  - Le token de connexion à l'API
     *                  - L'URL de l'API
     *                  - Le pseudo de l'utilisateur courant
     *                  - Le mode de connexion (connecté ou déconnecté)
     * @param modeTxt Le fichier contenant le choix du mode
     * @return un quartet avec les informations de l'utilisateur, de l'API et du mode de connexion
     */
    public static Quartet<String, String, String, String> getInfosUserAndApi(InputStreamReader infosUser,
                                                                             InputStreamReader modeTxt) throws IOException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {

        String[] valeurInfoUser;

        // On récupère le fichier des informations de l'utilisateur et de l'API
        BufferedReader infosUserTxt = new BufferedReader(infosUser);
        // On récupère le fichier du choix du mode
        BufferedReader bufferModeTxt = new BufferedReader(modeTxt);

        valeurInfoUser = infosUserTxt.readLine().split(";;;;");

        // On récupère le token crypté
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
        String urlApi = valeurInfoUser[2];

        // On récupère l'utilisateur courant
        String utilisateurCourant = valeurInfoUser[1];

        // On récupère le choix du mode
        String mode = bufferModeTxt.readLine();

        // Il n'y a pas de quatrième élément
        return new Quartet<>(token, urlApi, utilisateurCourant, mode);
    }
}

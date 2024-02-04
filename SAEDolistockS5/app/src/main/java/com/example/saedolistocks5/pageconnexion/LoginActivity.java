package com.example.saedolistocks5.pageconnexion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.saedolistocks5.pageliste.ListeActivity;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.OutilAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.KeyGenerator;


public class LoginActivity extends AppCompatActivity {

    // TextView du nom d'utilisateur
    private TextView userView;
    // TextView du mot de passe
    private TextView passwordView;
    // TextView de l'URL de l'API
    private TextView urlApiView;
    // TextView permettant d'afficher les différentes erreurs
    private TextView texteErreurView;

    // Nom d'utilisateur
    private String user;
    // Mot de passe de l'utilisateur
    private String password;
    // URL de l'API de l'utilisateur
    private String urlApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userView = findViewById(R.id.champsLogin);
        passwordView = findViewById(R.id.champsMdp);
        urlApiView = findViewById(R.id.champsURL);
        texteErreurView = findViewById(R.id.texteErreur);
    }

    /**
     * Méthode appelé lors d'un click sur le bouton "Se connecter"
     * @param view
     * @throws JSONException
     */
    public void seConnecter(View view) {
        user = userView.getText().toString();
        password = passwordView.getText().toString();
        urlApi = urlApiView.getText().toString();

        // Si jamais tous les champs ne sont pas renseigné, alors on affiche une erreur
        if(user.equals("") || password.equals("") || urlApi.equals("")) {
            texteErreurView.setText("Erreur : tous les champs de sont pas renseignés");
        } else {

            // On construit l'URL de l'API entière
            String urlApiEntiere = String.format("http://%s/htdocs/api/index.php/login?login=%s&password=%s",
                    urlApi, user, password);

            // On appelle la méthode getApiRetour pour appeler l'API
            OutilAPI.getApiRetour(LoginActivity.this ,urlApiEntiere, new OutilAPI.ApiCallback() {

                /**
                 * Méthode appelée lorsque l'appel à l'API se passe correctement
                 * @param result object JSON contenant la réponse de l'API
                 */
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(JSONObject result)  {
                    JSONObject success = null;
                    try {
                        // On récupère l'object "success" dans la réponse
                        success = result.getJSONObject("success");

                        // Puis on récupère le paramètre "token" dans l'object success
                        String token = success.getString("token");

                        // On initialise la clé pour crypter le token
                        EncryptAndDecrypteToken.InitializeKey();

                        byte[] tokenEncrypt = EncryptAndDecrypteToken.encrypt(token);

                        // Une fois le token crypté, on écrit sa valeur dans un fichier
                        EcrireInfosFichier(tokenEncrypt);

                        // Puis on lance l'activité des listes
                        Intent intention = new Intent(LoginActivity.this,
                                ListeActivity.class);
                        startActivity(intention);

                    } catch (JSONException e) {
                        texteErreurView.setText("Erreur : récupération du JSON incorrect");
                    } catch (NoConnectionError e) {
                        texteErreurView.setText("Erreur : URL incorrect");
                    } catch (Exception e) {
                        texteErreurView.setText("Erreur : " + e.getMessage());
                    }
                }

                @Override
                public void onSuccess(JSONArray result) {
                    // Null ici
                }

                /**
                 * Méthode appelée lorsque l'appel à l'API rencontre un problème
                 * @param error Erreur de type VolleyError
                 */
                @Override
                public void onError(VolleyError error) {
                  
                    // On récupère l'état de la Wifi de l'appareil courant
                    ConnectivityManager connManager =
                            (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());

                    // Si l'erreur correspond à une erreur de type NoConnectionError
                    if(error.getClass() == NoConnectionError.class) {
                        // Et si l'appareil n'a pas de connexion Wifi (mode avion par exemple)
                        if(mWifi == null) {
                            texteErreurView.setText("Erreur : vous n'avez pas de connexion à Internet.");
                        // Sinon
                        } else {
                            texteErreurView.setText("Erreur : l'URL " + urlApi + " est incorrect.");
                        }
                    } else if (error.networkResponse != null) {
                        // Si le code de retour de l'erreur est de type "403",
                        // Cela veut dire qu'on a eu un message de type "Forbidden"
                        if(error.networkResponse.statusCode == 403) {
                            texteErreurView.setText("Erreur : login ou mot de passe incorrect.");
                        } else {
                            texteErreurView.setText("");
                        }
                        // Si la connexion à Timeout, cela veut dire que la connexion avec
                        // Dolibarr est impossible
                    } else if (error.getClass() == TimeoutError.class) {
                        texteErreurView.setText("Erreur : accès impossible à Dolibarr");
                    }
                }
            });
        }       
    }

    /**
     * Méthode permettant d'écrire les différentes informations dans un fichier stocké
     * dans la mémoire de l'application, tels que le token crypté, le nom d'utilisateur
     * et l'URL de l'API
     * @param tokenEncrypt le token crypté
     */
    public void EcrireInfosFichier(byte[] tokenEncrypt) {
        String separator = ";;;;"; // Votre chaîne séparatrice
        FileOutputStream fichier = null;
        try {
            fichier = openFileOutput("infouser.txt", Context.MODE_PRIVATE);
            // Concatène le token, le user et l'urlApi avec le séparateur
            String toWrite = Arrays.toString(tokenEncrypt) + separator + user + separator + urlApi;
            fichier.write(toWrite.getBytes());
        } catch (Exception e) {
            // Gestion des exceptions
        } finally {
            if (fichier != null) {
                try {
                    fichier.close();
                } catch (IOException e) {
                    // Gestion des exceptions
                }
            }
        }
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de retour vers l'activité principale
     * @param view  source du clic
     */
    public void onClickRetour(View view) {

        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }

}


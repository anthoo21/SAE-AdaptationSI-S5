package com.example.saedolistocks5;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.icu.util.Output;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class LoginActivity extends AppCompatActivity {

    private String token;

    private TextView userView;
    private TextView passwordView;
    private TextView urlApiView;
    private TextView texteErreurView;

    private String user;
    private String password;
    private String urlApi;

    private Context context;

    SharedPreferences mesPreferences;

    SharedPreferences.Editor editeur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userView = findViewById(R.id.champsLogin);
        passwordView = findViewById(R.id.champsMdp);
        urlApiView = findViewById(R.id.champsURL);
        texteErreurView = findViewById(R.id.texteErreur);

        mesPreferences = getSharedPreferences("monFichierPref.xml", Activity.MODE_PRIVATE);
        editeur = mesPreferences.edit();

        context = this;
    }

    public void seConnecter(View view) throws JSONException {
        user = userView.getText().toString();
        password = passwordView.getText().toString();
        urlApi = urlApiView.getText().toString();

        if(user.equals("") || password.equals("") || urlApi.equals("")) {
            texteErreurView.setText("Erreur : tous les champs de sont pas renseignés");
        } else {

            String urlApiEntiere = String.format("http://%s/htdocs/api/index.php/login?login=%s&password=%s",
                    urlApi, user, password);
            OutilAPI.getApiRetour(LoginActivity.this ,urlApiEntiere, new OutilAPI.ApiCallback() {

                //@RequiresApi(api = Build.VERSION_CODES.O)
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(JSONObject result)  {
                    JSONObject success = null;
                    try {
                        success = result.getJSONObject("success");
                        String token = success.getString("token");

                        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
                        keyGen.init(56); // 56 bits pour DES
                        Key key = keyGen.generateKey();
                        // Clé secrète permettant d'encrypter et de décrypter un message
                        byte[] tokenEncrypt = EncryptAndDecrypteToken.encrypt(token, key);

                        EcrireInfosFichier(tokenEncrypt);

                        Intent intention = new Intent(LoginActivity.this,
                                ListeActivity.class);
                        startActivity(intention);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (NoConnectionError e) {
                        texteErreurView.setText("Erreur : URL incorrect");
                    } catch (Exception e) {
                        texteErreurView.setText("Erreur : " + e.getMessage());
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ConnectivityManager connManager =
                            (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());

                    if(error.getClass() == NoConnectionError.class) {
                        if(mWifi == null) {
                            texteErreurView.setText("Erreur : vous n'avez pas de connexion à Internet.");
                        } else {
                            texteErreurView.setText("Erreur : l'URL " + urlApi + " est incorrect.");
                        }
                    } else if (error.networkResponse != null) {
                        if(error.networkResponse.statusCode == 403) {
                            texteErreurView.setText("Erreur : login ou mot de passe incorrect.");
                        } else {
                            texteErreurView.setText("");
                        }
                    } else if (error.getClass() == TimeoutError.class) {
                        texteErreurView.setText("Erreur : accès impossible à Dolibarr");
                    }
                }
            });
        }       
    }


    public void EcrireInfosFichier(byte[] tokenEncrypt) {
        try {
            FileOutputStream fichier = openFileOutput("infouser.txt", Context.MODE_PRIVATE);
            fichier.write(tokenEncrypt);
            fichier.write("\n".getBytes());
            fichier.write(user.getBytes());
            fichier.write("\n".getBytes());
            fichier.write(urlApi.getBytes());
        } catch(IOException ex) {
            texteErreurView.setText(ex.toString());
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


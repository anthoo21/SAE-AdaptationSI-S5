package com.example.saedolistocks5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userView = findViewById(R.id.nomUser);
        passwordView = findViewById(R.id.mdpUser);
        urlApiView = findViewById(R.id.urlApi);
        texteErreurView = findViewById(R.id.textErreur);

        context = this;
    }

    public void seConnecter(View view) throws JSONException {
        user = userView.getText().toString();
        password = passwordView.getText().toString();
        urlApi = urlApiView.getText().toString();

        if(user.equals("") || password.equals("") || urlApi.equals("")) {
            texteErreurView.setText("Erreur : tous les champs de sont pas renseignés");
        } else {
            String urlApiEntiere = String.format("%slogin?login=%s&password=%s", urlApi, user, password);
            OutilAPI.getApiRetour(getApplicationContext(),urlApiEntiere, new OutilAPI.ApiCallback() {
                //@RequiresApi(api = Build.VERSION_CODES.O)
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(JSONObject result)  {
                    JSONObject success = null;
                    try {
                        success = result.getJSONObject("success");
                        String token = success.getString("token");

                        // Clé secrète permettant d'encrypter et de décrypter un message
                        SecretKey secretKey = EncryptAndDecrypteToken.generateSecretKey();
                        String tokenEncrypt = EncryptAndDecrypteToken.encrypt(token, secretKey);

                        texteErreurView.setText(token);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    texteErreurView.setText(error.toString());
                }
            });
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

    @Override
    /**
     * Méthode invoquée automatiquement lors d'un clic sur le bouton
     * de retour du téléphone
     * @param view  source du clic
     * @deprecated
     */
    public void onBackPressed() {

        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }
}


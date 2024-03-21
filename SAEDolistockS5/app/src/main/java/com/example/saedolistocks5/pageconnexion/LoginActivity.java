/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageconnexion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.saedolistocks5.pageliste.ListeActivity;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.OutilAPI;
import com.example.saedolistocks5.pagemain.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Arrays;

/**
 * Classe de login pour se connecter
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * TextView du nom d'utilisateur.
     */
    private TextView userView;
    /**
     * TextView du mot de passe.
     */
    private TextView passwordView;
    /**
     * TextView de l'URL de l'API.
     */
    private TextView urlApiView;
    /**
     * TextView permettant d'afficher les différentes erreurs.
     */
    private TextView texteErreurView;
    /**
     * Nom d'utilisateur.
     */
    private String user;
    /**
     * URL de l'API de l'utilisateur.
     */
    private String urlApi;
    /**
     * EditText du champ mot de passe
     */
    private EditText passwordEditText;

    /**
     * Méthode onCreate
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                          <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userView = findViewById(R.id.champsLogin);
        passwordView = findViewById(R.id.champsMdp);
        urlApiView = findViewById(R.id.champsURL);
        texteErreurView = findViewById(R.id.texteErreur);
        passwordEditText = findViewById(R.id.champsMdp);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            /**
             * On touch
             * @param v The view the touch event has been dispatched to.
             * @param event The MotionEvent object containing full information about
             *        the event.
             * @return un boolean
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP && (event.getRawX()
                        >= (passwordEditText.getRight() - passwordEditText
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                        if(passwordEditText.getInputType()
                                == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEditText
                                    .setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                            R.drawable.ic_eye_closed, 0); // Icône œil fermé
                        } else {
                            passwordEditText.setInputType
                                    (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                    R.drawable.ic_eye, 0); // Icône œil ouvert
                        }
                        return true;

                }
                return false;
            }
        });
    }

    /**
     * Méthode appelé lors d'un click sur le bouton "Se connecter".
     * @param view la vue.
     */
    public void seConnecter(View view) {
        String password;
        user = userView.getText().toString();
        password = passwordView.getText().toString();
        urlApi = urlApiView.getText().toString();

        // Si jamais tous les champs ne sont pas renseigné, alors on affiche une erreur
        if(user.equals("") || password.equals("") || urlApi.equals("")) {
            texteErreurView.setText(R.string.ErreurSaisie);
        } else {

            // On construit l'URL de l'API entière
            String urlApiEntiere = String.
                    format("http://%s/htdocs/api/index.php/login?login=%s&password=%s",
                            urlApi, user, password);

            // On appelle la méthode getApiRetour pour appeler l'API
            OutilAPI.GetApiJsonObject(LoginActivity.this ,urlApiEntiere, new OutilAPI.ApiCallback() {
                /**
                 * Méthode appelée lorsque l'appel à l'API se passe correctement.
                 * @param result object JSON contenant la réponse de l'API.
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
                        EncryptAndDecrypteToken.initializeKey();

                        byte[] tokenEncrypt = EncryptAndDecrypteToken.encrypt(token);

                        // Une fois le token crypté, on écrit sa valeur dans un fichier
                        ecrireInfosFichier(tokenEncrypt);

                        // Puis on lance l'activité des listes
                        Intent intention = new Intent(LoginActivity.this,
                                ListeActivity.class);
                        intention.putExtra("MODE", "connecte");
                        intention.putExtra("PAGE", "Login");
                        startActivity(intention);
                        finish(); // destruction de l'activité courante
                    } catch (JSONException e) {
                        texteErreurView.setText(R.string.ErreurJSON);
                    } catch (Exception e) {
                        texteErreurView.setText(R.string.Erreur + e.getMessage());
                    }
                }

                /**
                 * Méthode si succès.
                 * @param result un resulat sous forme de JSONArray.
                 */
                @Override
                public void onSuccess(JSONArray result) {
                    // Null ici car on souhaite l'objet
                }

                /**
                 * Méthode appelée lorsque l'appel à l'API rencontre un problème.
                 * @param error Erreur de type VolleyError.
                 */
                @Override
                public void onError(VolleyError error) {

                    // On récupère l'état de la Wifi de l'appareil courant
                    ConnectivityManager connManager =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());

                    // Si l'erreur correspond à une erreur de type NoConnectionError
                    if(error.getClass() == NoConnectionError.class) {
                        // Et si l'appareil n'a pas de connexion Wifi (mode avion par exemple)
                        if(mWifi == null) {
                            texteErreurView.setText(R.string.ErreurConnexionInternet);
                        } else {
                            texteErreurView.setText("Erreur : l'URL " + urlApi + " est incorrect.");
                        }
                    } else if (error.networkResponse != null) {
                        // Cela veut dire qu'on a eu un message de type "Forbidden"
                        if(error.networkResponse.statusCode == 403) {
                            texteErreurView.setText(R.string.ErreurLoginMdp);
                        } else {
                            texteErreurView.setText("");
                        }
                        // Si la connexion à Timeout, cela veut dire que la connexion avec
                        // Dolibarr est impossible
                    } else if (error.getClass() == TimeoutError.class) {
                        texteErreurView.setText(R.string.ErreurDolibarr);
                    }
                }
            });
        }
    }

    /**
     * Méthode permettant d'écrire les différentes informations dans un fichier stocké
     * dans la mémoire de l'application, tels que le token crypté, le nom d'utilisateur
     * et l'URL de l'API.
     * @param tokenEncrypt le token crypté.
     */
    public void ecrireInfosFichier(byte[] tokenEncrypt) {
        String separator = ";;;;"; // Votre chaîne séparatrice
        try (FileOutputStream fichier = openFileOutput("infouser.txt", Context.MODE_PRIVATE)) {
            // Concatène le token, le user et l'urlApi avec le séparateur
            Key key = EncryptAndDecrypteToken.getKey();
            String toWrite = Arrays.toString(tokenEncrypt) + separator + user
                    + separator + urlApi + separator + EncryptAndDecrypteToken.keyToString(key);
            fichier.write(toWrite.getBytes());
        } catch (Exception e) {
            // Gestion des exceptions
        }
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton.
     * de retour vers l'activité principale.
     * @param view  source du clic.
     */
    public void onClickRetour(View view) {
        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }

}


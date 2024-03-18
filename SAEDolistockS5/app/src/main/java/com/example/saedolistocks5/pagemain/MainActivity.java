/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pagemain;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.saedolistocks5.pageliste.ListeActivity;
import com.example.saedolistocks5.pageconnexion.LoginActivity;
import com.example.saedolistocks5.R;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class de la mainActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Méthode OnCreate.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView logo;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);

        // Masquer l'image au début
        logo.setAlpha(0f);

        // Créer une animation d'opacité pour faire apparaître l'image progressivement
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(2000); // 2 secondes

        // Créer une animation de rotation
        ObjectAnimator rotate = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        rotate.setDuration(2000); // 2 secondes

        // Créer un ensemble d'animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, rotate);

        animatorSet.start();

    }

    /**
     * Méthode pour lire le fichier.
     * @return un boolean pour savoir si le fichier est lisible
     */
    public boolean readFichier() {
        boolean ok;
        int compteur = 0;
        InputStreamReader fichier;
        try {
            fichier = new InputStreamReader(openFileInput("infouser.txt"));
            BufferedReader fichiertexte = new BufferedReader(fichier);
            while(fichiertexte.readLine() != null) {
                compteur += 1;
            }
            if (compteur == 1) {
                ok = true;
            } else {
                ok = false;
            }
            fichier.close();
        } catch (FileNotFoundException e) {
            ok = false;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return ok;
    }

    /**
     * Méthode pour cliquer et passer en mode connexion.
     * @param button le boutton.
     */
    public void onClickModeCo(View button){
        if (readFichier()) {
            if (estConnecteAInternet(this)) {
                Intent intention =
                        new Intent(MainActivity.this, ListeActivity.class);
                startActivity(intention);
            } else {
                Toast.makeText(this, R.string.messageModeConnecte, Toast.LENGTH_LONG).show();

            }
        } else {
            Intent intention = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intention);
        }
    }

    /**
     * Méthode pour passer en mode deconnecte.
     * @param button le bouton.
     */
    public void onClickModeDeco(View button){
        if (readFichier()) {
            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(intention);
        } else {
            Intent intention = new Intent(MainActivity.this, LoginActivity.class);
            intention.putExtra("MODE", "deconnecte");
            // Toast d'information, pas de mode déconnecté si ReadFichier() renvoie faux
            Toast.makeText(this,R.string.messageRedirection,Toast.LENGTH_LONG).show();
            startActivity(intention);
        }

    }

    public boolean estConnecteAInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return false;
    }

}
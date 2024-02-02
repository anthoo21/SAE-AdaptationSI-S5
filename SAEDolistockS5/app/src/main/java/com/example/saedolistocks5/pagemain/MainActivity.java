package com.example.saedolistocks5.pagemain;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
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

public class MainActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public boolean ReadFichier() {
        boolean ok;
        int compteur = 0;
        try {
            InputStreamReader fichier = new InputStreamReader(openFileInput("infouser.txt"));
            BufferedReader fichiertexte = new BufferedReader(fichier);
            while(fichiertexte.readLine() != null) {
                compteur += 1;
            }
            if (compteur == 3) {
                ok = true;
            } else {
                ok = false;
            }
            fichier.close();
        } catch (FileNotFoundException e) {
            ok = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ok;
    }
    public boolean connexion() {
        boolean estConnecter = false;
        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());
        if (mWifi != null && ReadFichier() && mWifi.isConnectedOrConnecting()) {
            estConnecter = true;
        }
        return estConnecter;
    }

    public int speed() {
        int kbs = 0;
        if (connexion()) {
            ConnectivityManager connManager =
                    (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
            kbs = nc.getLinkDownstreamBandwidthKbps();
        }
        return kbs;
    }

    public void onClickModeCo(View Button){
        // co renvoie flase why ? passe pas dans le switch ?

        /* v1
                if(connexion() && ReadFichier()) {
            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(intention);
            return;
        } else {
            Toast.makeText(this,R.string.messageModeConnecte,Toast.LENGTH_LONG).show();
        }
        if(!ReadFichier()) {
            Intent intention = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intention);
        }
         */
        if (connexion() && ReadFichier() && speed() > 0) {
            PhoneStateListener ecouterConnexion = new PhoneStateListener() {
                @Override
                public void onDataConnectionStateChanged(int etat) {
                    switch (etat) {
                        case TelephonyManager.DATA_CONNECTED:
                        case TelephonyManager.DATA_CONNECTING:
                            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
                            startActivity(intention);
                            break;
                        case TelephonyManager.DATA_DISCONNECTED:
                        case TelephonyManager.DATA_DISCONNECTING:
                        case TelephonyManager.DATA_SUSPENDED:
                            //Toast.makeText(this, R.string.messageModeConnecte, Toast.LENGTH_LONG).show();
                            Log.e(null,"Pas de connexion");
                            break;
                    }
                    super.onDataConnectionStateChanged(etat);

                }
            };
            TelephonyManager gestionnaire = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            gestionnaire.listen(ecouterConnexion, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        }
        if (!connexion() || speed() > 0){
            Toast.makeText(this, R.string.messageModeConnecte, Toast.LENGTH_LONG).show();
            Log.i(null, String.valueOf(speed()));
        }
        if(!ReadFichier()) {
            Intent intention = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intention);
        }
    }

    public void onClickModeDeco(View Button){
        if (ReadFichier()) {
            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(intention);
        } else {
            Intent intention = new Intent(MainActivity.this, LoginActivity.class);
            // Toat d'information, pas de mode déconnecté si ReadFichier() renvoie faux
            Toast.makeText(this,R.string.messageRedirection,Toast.LENGTH_LONG).show();
            startActivity(intention);
        }

    }
}
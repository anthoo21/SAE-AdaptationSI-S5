package com.example.saedolistocks5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public void onClickModeCo(View Button){

        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());

        if(mWifi != null && ReadFichier() && mWifi.isAvailable()) {
            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(intention);
            return;
        } else if(mWifi == null && ReadFichier()) {
            Toast.makeText(this,R.string.messageModeConnecte,Toast.LENGTH_LONG).show();
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
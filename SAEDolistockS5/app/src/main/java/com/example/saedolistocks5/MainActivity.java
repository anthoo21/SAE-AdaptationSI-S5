package com.example.saedolistocks5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public boolean ReadFichier() {
        boolean vide;
        Resources resources = getApplicationContext().getResources();
        try {
            InputStream inputStream = resources.openRawResource(R.raw.infouser);
            long fileSize = inputStream.available();
            if (fileSize > 0.0) {
                vide = true;
            } else {
                vide = false;
            }
            inputStream.close();
        } catch (IOException e) {
            vide = false;
            throw new RuntimeException(e);
        }
        return vide;
    }
    public void onClickModeCo(View Button){
        if (ReadFichier()) {
            Intent intention = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(intention);
        } else {
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
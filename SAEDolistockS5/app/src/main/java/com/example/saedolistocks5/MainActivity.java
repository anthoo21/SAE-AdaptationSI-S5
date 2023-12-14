package com.example.saedolistocks5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Méthode appelée automatiquement quand l'utilisateur clique sur le bouton.
     *
     * @param bouton initialisé automatiquement avec le bouton à l'origine du clic (ici le
     * bouton mode connexion).
     */
    public void onClickModeDeco(View Button){
        Intent intention = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intention);
    }
      
    /**
     * Méthode appelée automatiquement quand l'utilisateur clique sur le bouton.
     *
     * @param bouton initialisé automatiquement avec le bouton à l'origine du clic (ici le
     * bouton mode connexion).
     */
    public void onClickModeCo(View Button){
        Intent intention = new Intent(MainActivity.this, ListeActivity.class);
        startActivity(intention);

    }
}
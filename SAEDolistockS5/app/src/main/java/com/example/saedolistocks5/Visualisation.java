package com.example.saedolistocks5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Visualisation extends AppCompatActivity {

    private ArrayList<ItemVisualisation> listeArticles;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualisation_liste_activity);

        recyclerView = findViewById(R.id.informationListe);
        intialiserItem();

        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gestionnaireLineaire);

        ItemVisualisationAdpater adpater = new ItemVisualisationAdpater(listeArticles);
        recyclerView.setAdapter(adpater);
    }

    private void intialiserItem() {
        listeArticles = new ArrayList<>();
        // bouchon
        listeArticles.add(new ItemVisualisation("Ajout","Peluche 33 pouces en forme d'ours polaire","50"));
        listeArticles.add(new ItemVisualisation("test2","test2","25000"));
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de retour vers l'activité principale
     * @param view  source du clic
     */

    // TODO manque la pop-up entre liste activity et visu pour l'intention
    public void onClickRetour(View view) {

        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }
}

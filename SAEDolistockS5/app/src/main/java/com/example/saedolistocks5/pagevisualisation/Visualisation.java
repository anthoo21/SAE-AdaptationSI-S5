package com.example.saedolistocks5.pagevisualisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageliste.ListeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Visualisation extends AppCompatActivity {

    private ArrayList<ItemVisualisation> listeArticles;
    private RecyclerView recyclerView;

    /**
     * Récupère la position de l'item
     */
    private int positionItem;

    /**
     * Liste des listes de l'utilisateur courant
     */
    private ArrayList<String> fichierUser;

    private TextView libelleListe;

    private TextView libelleDateHeure;

    private TextView libelleEntrepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualisation_liste_activity);

        libelleListe = findViewById(R.id.libelleListe);
        libelleDateHeure = findViewById(R.id.libelleDateCrea);
        libelleEntrepot = findViewById(R.id.libelleEntrepot);

        recyclerView = findViewById(R.id.informationListe);

        Intent intent = getIntent();
        positionItem = intent.getIntExtra("positionItem", 0);

        fichierUser = ListeActivity.getListeFichierUser();
        intialiserItem();

        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gestionnaireLineaire);

        ItemVisualisationAdpater adpater = new ItemVisualisationAdpater(listeArticles);
        recyclerView.setAdapter(adpater);



    }

    private void intialiserItem() {
        listeArticles = new ArrayList<>();
        try {
            String nomFichier = fichierUser.get(positionItem);

            InputStreamReader fichier = new InputStreamReader(openFileInput(nomFichier));
            BufferedReader fichiertxt = new BufferedReader(fichier);
            String ligne;
            String nomListe = "";
            String date = "";
            String heure = "";
            String entrepot = "";
            String[] elementListe;
            while ((ligne = fichiertxt.readLine()) != null) {
                elementListe = ligne.split(";");
                nomListe = elementListe[1];
                date = elementListe[9];
                heure = elementListe[10];
                entrepot = elementListe[4];
                listeArticles.add(new ItemVisualisation(elementListe[6], elementListe[3]
                        + " (" + elementListe[2] + ")", elementListe[7]));
            }

            libelleListe.setText(nomListe);
            libelleDateHeure.setText(String.format("Créée le %s à %s", date, heure));
            libelleEntrepot.setText("Entrepôt : " + entrepot);
        } catch (FileNotFoundException e) {
           // throw new RuntimeException(e); peu importe
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

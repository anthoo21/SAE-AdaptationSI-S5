package com.example.saedolistocks5.pagevisualisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.saedolistocks5.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        try {
            // TODO Faire autre méthode pour récup info fichier via intention ?
            FileOutputStream fichierInser = openFileOutput("Visu.txt",Context.MODE_PRIVATE);
            String Chaine1 = "Ajout;La liste;10";
            String Chaine2 = "Modification;Une liste;442";
            fichierInser.write(Chaine1.getBytes());
            fichierInser.write("\n".getBytes());
            fichierInser.write(Chaine2.getBytes());


            InputStreamReader fichier = new InputStreamReader(openFileInput("Visu.txt"));
            BufferedReader fichiertxt = new BufferedReader(fichier);
            // TODO for i nombre d'articles à recup de la liste via intention ?
            for (int i = 0; i<2; i++) {
                String txt = fichiertxt.readLine();
                String[] ligne = txt.split(";");
                // Si integrétion du user + 1 à tous les index de ligne
                listeArticles.add(new ItemVisualisation(ligne[0], ligne[1],ligne[2]));
            }
        } catch (FileNotFoundException e) {
           // throw new RuntimeException(e); peu importe
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* bouchon
        listeArticles.add(new ItemVisualisation("Ajout","Peluche 33 pouces en forme d'ours polaire","50"));
        listeArticles.add(new ItemVisualisation("Ajout","test2","25000"));
         */
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

/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pagevisualisation;

import android.app.Activity;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class Visualisation.
 */
public class Visualisation extends AppCompatActivity {

    /**
     * ArrayList des items pour visualiser.
     */
    private ArrayList<ItemVisualisation> listeArticles;
    /**
     * Récupère la position de l'item.
     */
    private int positionItem;
    /**
     * Liste des listes de l'utilisateur courant.
     */
    private ArrayList<String> fichierUser;
    /**
     * TextView libelleListe.
     */
    private TextView libelleListe;
    /**
     * TextView libelleDate.
     */
    private TextView libelleDateHeure;
    /**
     * TextView du libelleEntrepot
     */
    private TextView libelleEntrepot;

    /**
     * Méthode OnCreate.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                          <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualisation_liste_activity);

        libelleListe = findViewById(R.id.libelleListe);
        libelleDateHeure = findViewById(R.id.libelleDateCrea);
        libelleEntrepot = findViewById(R.id.libelleEntrepot);

        recyclerView = findViewById(R.id.informationListe);

        Intent intent = getIntent();
        positionItem = intent.getIntExtra("positionItem", 0);

        fichierUser = (ArrayList<String>) ListeActivity.getListeFichierUser();
        intialiserItem();

        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gestionnaireLineaire);

        ItemVisualisationAdpater adpater = new ItemVisualisationAdpater(listeArticles);
        recyclerView.setAdapter(adpater);
    }

    /**
     * Méthode inialiser les items.
     */
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
                        + " (" + elementListe[2] + ")", elementListe[8]));
            }
            libelleListe.setText(nomListe);
            libelleDateHeure.setText(String.format("Créée le %s à %s", date, heure));
            libelleEntrepot.setText(getString(R.string.Entrepot) + " " + entrepot);
        } catch (FileNotFoundException e) {
           // throw new RuntimeException(e); peu importe
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // TODO manque la pop-up entre liste activity et visu pour l'intention
    /**
     * Méthode quand on clique sur le boton retour.
     * @param view la vue
     */
    public void onClickRetour(View view) {

        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }
}

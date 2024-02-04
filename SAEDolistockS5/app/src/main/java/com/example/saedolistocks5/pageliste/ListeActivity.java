package com.example.saedolistocks5.pageliste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageconnexion.LoginActivity;
import com.example.saedolistocks5.pagevisualisation.Visualisation;
import com.example.saedolistocks5.pageajoutliste.AjoutListeActivity;
import com.example.saedolistocks5.pagemain.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListeActivity extends AppCompatActivity {

    /**
     * Liste source des données à afficher :
     * chaque élément contient une instance de PhotoParis (une photo
     * et son libellé)
     */
    private ArrayList<ListeAccueil> listeAccueil;

    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView listeAccueilRecyclerView;

    /**
     * Utilisateur courant sur l'application
     */
    private String utilisateurCourant;

    /**
     * Position de l'item liste
     */
    private int positionItemListe;

    /**
     * Liste des listes de l'utilisateur courant
     */
    static ArrayList<String> listeFichierUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_activity);

        listeAccueilRecyclerView = findViewById(R.id.liste_listes_accueil);
        try {
            listeFichierUser = new ArrayList<>();
            initialiseListeAccueil();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*
         * On crée un gestionnaire de layout linéaire, et on l'associe à la
         * liste de type RecyclerView
         */
        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        listeAccueilRecyclerView.setLayoutManager(gestionnaireLineaire);

        /*
         * On crée un adaptateur personnalisé et permettant de gérer spécifiquement
         * l'affichage des instances de type listeAccueil en tant que item de la liste.
         * Cet adapatateur est associé au RecyclerView
         */
        ListeAccueilAdapter adaptateur = new ListeAccueilAdapter(listeAccueil);
        listeAccueilRecyclerView.setAdapter(adaptateur);

        // on précise qu'un menu contextuel est associé à la liste
        registerForContextMenu(listeAccueilRecyclerView);

    }
    /**
     * Méthode pour initialiser la liste des photos et des textes
     */
    private void initialiseListeAccueil() throws IOException {
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));
        BufferedReader infosUserTxt = new BufferedReader(infosUser);
        String[] valeurInfoUser = infosUserTxt.readLine().split(";;;;");

        utilisateurCourant = valeurInfoUser[1];

        listeAccueil = new ArrayList<>();

        File filesDir = getFilesDir(); // Récupère le répertoire "files" de votre application
        File[] files = filesDir.listFiles(); // Obtient la liste des fichiers dans le répertoire

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(utilisateurCourant)) {
                    try {
                        listeFichierUser.add(file.getName());
                        InputStreamReader liste = new InputStreamReader(openFileInput(file.getName()));
                        BufferedReader listeText = new BufferedReader(liste);

                        String[] ligneListe = listeText.readLine().split(";");

                        String nomListe = ligneListe[1];
                        String dateCreation = "Date de création : " + ligneListe[9];
                        String heureCreation = ligneListe[10];

                        listeText.close();

                        listeAccueil.add(new ListeAccueil(nomListe, dateCreation, heureCreation));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /* ===============================================================================
     *                       méthodes pour gérer le menu CONTEXTUEL
     * ===============================================================================
     */

    public void onClickMenu(View view) {
        // Affiche le menu contextuel
        positionItemListe = (int) view.getTag();
        view.showContextMenu();
    }

    /**
     * Méthode invoquée automatiquement lorsque l'utiisateur active le menu contextuel
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        /*
         *  on désérialise le fichier XML décriant le menu et on l'associe
         *  au menu argument (celui qui a été activé)
         */
        new MenuInflater(this).inflate(R.menu.menu_liste_accueil, menu);
    }

    /**
     * Méthode invoquée automatiquement lorsque l'utilisateur choisit une option
     * dans un menu contextuel
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        /*
         *  on accède à des informations supplémentaires sur la vue associée
         *  au menu activé. L'information qui nous intéresse est la position
         *  de l'élément de la liste sur lequel l'utilisateur a cliqué pour
         *  activer le menu.
         */
        AdapterView.AdapterContextMenuInfo information =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // selon l'option sélectionnée dans le menu, on réalise le traitement adéquat
        if (item.getItemId() == R.id.optionVisualisation) { // visualisation d'une liste
            Intent intention = new Intent(ListeActivity.this, Visualisation.class);
            intention.putExtra("positionItem", positionItemListe);
            startActivity(intention);
        } else { // modification d'une liste

        }
        return (super.onContextItemSelected(item));
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de retour vers l'activité principale
     * @param view  source du clic
     */
    public void onClickRetour(View view) {

        Intent intention = new Intent(ListeActivity.this, MainActivity.class);
        startActivity(intention);
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * d'ajout
     * @param bouton  source du clic
     */
    public void onClickAjouter(View bouton) {
        Intent intention = new Intent(ListeActivity.this, AjoutListeActivity.class);
        startActivity(intention);
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de deconnexion
     * @param view  source du clic
     */
    public void onClickDeco(View view) {
        try {
            InputStreamReader fichier = new InputStreamReader(openFileInput("infouser.txt"));
            BufferedReader fichiertexte = new BufferedReader(fichier);
            Intent intention = new Intent(ListeActivity.this, LoginActivity.class);
            deleteFile("infouser.txt");
            startActivity(intention);
        } catch (FileNotFoundException e) {
            Toast.makeText(this,R.string.messageModeConnecte,Toast.LENGTH_LONG).show();
        }

    }

    public static ArrayList<String> getListeFichierUser() {
        return listeFichierUser;
    }
}

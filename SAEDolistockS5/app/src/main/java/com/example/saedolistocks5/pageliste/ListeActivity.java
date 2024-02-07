/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    /**
     * Liste source des données à afficher :
     * chaque élément contient une instance de PhotoParis (une photo
     * et son libellé)
     */
    private ArrayList<ListeAccueil> listeAccueil;
    /**
     * Position de l'item liste
     */
    private int positionItemListe;

    /**
     * Liste des listes de l'utilisateur courant
     */
    static ArrayList<String> listeFichierUser = new ArrayList<>();

    /**
     * Méthode onCreate.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView listeAccueilRecyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_activity);
        listeAccueilRecyclerView = findViewById(R.id.liste_listes_accueil);
        try {
            initialiseListeAccueil();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
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
     * Méthode pour initialiser la liste des photos et des textes.
     * @throws IOException exception.
     */
    private void initialiseListeAccueil() throws IOException {
        String utilisateurCourant;
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));
        String[] valeurInfoUser;
        BufferedReader infosUserTxt = new BufferedReader(infosUser);
        valeurInfoUser = infosUserTxt.readLine().split(";;;;");


        utilisateurCourant = valeurInfoUser[1];

        listeAccueil = new ArrayList<>();

        File filesDir = getFilesDir(); // Récupère le répertoire "files" de votre application
        File[] files = filesDir.listFiles(); // Obtient la liste des fichiers dans le répertoire

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(utilisateurCourant)) {
                    InputStreamReader liste = new InputStreamReader(openFileInput(file.getName()));
                    try (BufferedReader listeText = new BufferedReader(liste)) {
                        listeFichierUser.add(file.getName());
                        String[] ligneListe = listeText.readLine().split(";");

                        String nomListe = ligneListe[1];
                        String dateCreation = "Date de création : " + ligneListe[9];
                        String heureCreation = ligneListe[10];
                        listeAccueil.add(new ListeAccueil(nomListe, dateCreation, heureCreation));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    /**
     * Méthod de click sur le menu.
     * @param view la vue.
     */
    public void onClickMenu(View view) {
        // Affiche le menu contextuel
        positionItemListe = (int) view.getTag();
        view.showContextMenu();
    }

    /**
     * Constructeur.
     * @param menu     The context menu that is being built.
     * @param v        The view for which the context menu is being built.
     * @param menuInfo Extra information about the item for which the
     *                 context menu should be shown. This information will vary
     *                 depending on the class of v.
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
     * Méthode pour l'item choisie.
     * @param item The context menu item that was selected.
     * @return l'item.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // selon l'option sélectionnée dans le menu, on réalise le traitement adéquat
        if (item.getItemId() == R.id.optionVisualisation) { // visualisation d'une liste
            Intent intention = new Intent(ListeActivity.this, Visualisation.class);
            intention.putExtra("positionItem", positionItemListe);
            startActivity(intention);
        } else {
            // modification d'une liste
        }
        return (super.onContextItemSelected(item));
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic retour.
     * de retour vers l'activité principale.
     * @param view  source du clic.
     */
    public void onClickRetour(View view) {
        Intent intention = new Intent(ListeActivity.this, MainActivity.class);
        startActivity(intention);
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur ajouter.
     * @param bouton  source du clic.
     */
    public void onClickAjouter(View bouton) {
        Intent intention = new Intent(ListeActivity.this, AjoutListeActivity.class);
        startActivity(intention);
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de deconnexion
     * @param view  source du clic.
     */
    public void onClickDeco(View view) {
            Intent intention = new Intent(ListeActivity.this, LoginActivity.class);
            deleteFile("infouser.txt");
            startActivity(intention);

    }

    /**
     * getter de la liste des fichier users.
     * @return la liste des fichiers Users.
     */
    public static List<String> getListeFichierUser() {
        return listeFichierUser;
    }
}

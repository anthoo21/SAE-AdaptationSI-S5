package com.example.saedolistocks5.pageliste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saedolistocks5.R;

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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_activity);

        listeAccueilRecyclerView = findViewById(R.id.liste_listes_accueil);
        initialiseListeAccueil();

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
    }
    /**
     * Méthode pour initialiser la liste des photos et des textes
     */
    private void initialiseListeAccueil() {
        listeAccueil = new ArrayList<>();
        listeAccueil.add(new ListeAccueil("Liste N°1", "Date de création : 01/01/01", "9:30"));
        listeAccueil.add(new ListeAccueil("Liste N°2", "Date de création : 12/02/01", "10:30"));
        listeAccueil.add(new ListeAccueil("Liste N°3", "Date de création : 16/05/01", "22:10"));
    }

    /* ===============================================================================
     *                       méthodes pour gérer le menu CONTEXTUEL
     * ===============================================================================
     */

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

        } else { // modification d'une liste

        }
        return (super.onContextItemSelected(item));
    }
}

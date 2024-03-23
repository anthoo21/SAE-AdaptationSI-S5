/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.OutilAPI;
import com.example.saedolistocks5.outilapi.RequetesApi;
import com.example.saedolistocks5.outilsdivers.OutilDivers;
import com.example.saedolistocks5.outilsdivers.Quartet;
import com.example.saedolistocks5.pageconnexion.LoginActivity;
import com.example.saedolistocks5.pagemodifliste.ModifListeActivity;
import com.example.saedolistocks5.pagevisualisation.Visualisation;
import com.example.saedolistocks5.pageajoutliste.AjoutListeActivity;
import com.example.saedolistocks5.pagemain.MainActivity;
import com.example.saedolistocks5.popup.CustomPopupAfficherMenu;
import com.example.saedolistocks5.popup.CustomPopupConfirmationSupp;
import com.example.saedolistocks5.popup.CustomPopupEnvoyer;
import com.example.saedolistocks5.popup.CustomPopupSuppression;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Activité gérant la page d'accueil qui va afficher les listes de l'utilisateur courant
 * et le permettre d'effectuer diverses actions dessus (Visualiser, Modifier, Supprimer, ...)
 *
 * @author BONNET, FROMENT et ENJALBERT
 * @version 3.0
 */
public class ListeActivity extends AppCompatActivity {

    /**
     * Liste source des données à afficher :
     * chaque élément contient une instance de PhotoParis (une photo
     * et son libellé)
     */
    public static ArrayList<ListeAccueil> listeAccueil;

    /**
     * Liste pour récupérer les codes articles
     */
    public static ArrayList<String> listeCodeArticleVerif;

    /**
     * Liste de code barre
     */
    public static ArrayList<String> listeCodeBarreVerif;

    /**
     * Liste des ID des articles
     */
    public static ArrayList<String> idArticleVerif;

    /**
     * Liste des libellé des articles
     */
    public static ArrayList<String> libelleArticleVerif;


    /**
     * Liste pour récupérer les entrepôts
     */
    public static ArrayList<String> listeEntrepotsVerif;

    /**
     * Liste des IDs des entrepôts
     */
    public static ArrayList<String> idEntrepotVerif;

    /**
     * Adatpateur de liste accueil
     */
    public static ListeAccueilAdapter adaptateur;

    /**
     * Utilisateur courant sur l'application
     */
    public static String utilisateurCourant,
    /**
     * Le token pour utiliser l'API
     */
    token, /**
     * L'URL de l'API
     */
    URLApi;

    /**
     * Mode de connexion (connecté ou déconnecté)
     */
    public static String mode;


    /**
     * Id de l'article
     */
    public static String idArticle;

    /**
     * Id de l'entrepôt
     */
    public static String idEntrepot;

    /**
     * Libelle de l'article
     */
    public static String libelleArticle;



    /**
     * Position de l'item liste
     */
    public static int positionItemListe;

    /**
     * Liste des listes de l'utilisateur courant
     */
    public static ArrayList<String> listeFichierUser;

    /**
     * Nombre de lignes du fichier à envoyer sur Dolibarr.
     */
    public static int nombreLignes;

    /**
     * Index de parcours de ligne
     */
    public static int indexParcoursListe;

    /**
     * Permet de savoir si on vient de la page connexion ou non
     */
    private boolean venuConnexion;


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
        String modeFirstIntention = "";
        String pageVenue = "";
        try {
            Intent intentionParent = getIntent();
            modeFirstIntention = intentionParent.getStringExtra("MODE");
            pageVenue = intentionParent.getStringExtra("PAGE");
            // Permet d'écrire le choix du mode de l'uril
            if(modeFirstIntention != null) {
                ecritureModeFichier(modeFirstIntention);
            } else {
                ecritureModeFichier(mode);
            }
            if(pageVenue != null) {
                venuConnexion = pageVenue.equals("Login") || pageVenue.equals("Ajout")
                        || pageVenue.equals("Modif");
            }

            // Permet d'afficher les listes de l'utilisateur sur la vue
            initialiseListeAccueil();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        idArticleVerif = new ArrayList<>();
        idEntrepotVerif = new ArrayList<>();
        listeCodeArticleVerif = new ArrayList<>();
        listeCodeBarreVerif = new ArrayList<>();
        listeEntrepotsVerif = new ArrayList<>();
        libelleArticleVerif = new ArrayList<>();

        // Permet d'alimenter la liste des entrepôts et des codes articles via l'API
        RequetesApi.getListeEntrepot(URLApi, token, getApplicationContext(), "Liste");
        RequetesApi.getArticles(URLApi, token, getApplicationContext(), "Liste", null);

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
        adaptateur = new ListeAccueilAdapter(listeAccueil);
        listeAccueilRecyclerView.setAdapter(adaptateur);

        // on précise qu'un menu contextuel est associé à la liste
        registerForContextMenu(listeAccueilRecyclerView);

    }

    private void ecritureModeFichier(String mode) throws IOException {
        FileOutputStream fichier = openFileOutput("mode.txt", Context.MODE_PRIVATE);
        fichier.write(mode.getBytes());
    }

    /**
     * Méthode pour initialiser la ou les listes de l'utilisateur courant
     * pour les aficher sur la vue
     *
     * @throws IOException               exception.
     * @throws NoSuchPaddingException    the no such padding exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws NoSuchAlgorithmException  the no such algorithm exception
     * @throws BadPaddingException       the bad padding exception
     * @throws InvalidKeyException       the invalid key exception
     */
    private void initialiseListeAccueil() throws IOException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {
        // On ouvre le fichier infouser pour récupérer les infos à propos de l'utilisateur
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));
        InputStreamReader modeTxt = new InputStreamReader(openFileInput("mode.txt"));

        // On appelle une méthode pour récupérer les infos de l'utilisateur
        Quartet<String, String, String, String> infos = OutilDivers.getInfosUserAndApi(infosUser, modeTxt);

        // On récupère le token
        token = infos.first();

        // On récupère l'URL de l'API
        URLApi = infos.second();

        // On récupère l'utilisateur courant
        utilisateurCourant = infos.third();

        // Choix du mode
        mode = infos.fourth();

        listeAccueil = new ArrayList<>();

        listeFichierUser = new ArrayList<>();

        File filesDir = getFilesDir(); // Récupère le répertoire "files" de votre application
        File[] files = filesDir.listFiles(); // Obtient la liste des fichiers dans le répertoire

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(utilisateurCourant)) {
                    InputStreamReader liste = new InputStreamReader(openFileInput(file.getName()));
                    try (BufferedReader listeText = new BufferedReader(liste)) {
                        String ligne = listeText.readLine();
                        if(ligne != null) {
                            listeFichierUser.add(file.getName());
                            String[] ligneListeSplit = ligne.split(";");
                            String nomListe = ligneListeSplit[1];
                            String dateCreation = "Date de création : " + ligneListeSplit[7];
                            String heureCreation = ligneListeSplit[8];
                            listeAccueil.add(new ListeAccueil(nomListe, dateCreation, heureCreation));
                        }
                        } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * Méthod de click sur le menu.
     *
     * @param view la vue.
     */
    public void onClickMenu(View view) {
        // Affiche le menu contextuel
        positionItemListe = (int) view.getTag();

        CustomPopupAfficherMenu dialog = CustomPopupAfficherMenu.createDialog(this, this);
        dialog.show();
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic retour.
     * de retour vers l'activité principale.
     *
     * @param view source du clic.
     */
    public void onClickRetour(View view) {
        if(venuConnexion) {
            Intent intention = new Intent(ListeActivity.this, MainActivity.class);
            startActivity(intention);
        } else {
            // création d'une intention pour informer l'activté parente
            Intent intentionRetour = new Intent();
            // retour à l'activité parente et destruction de l'activité fille
            setResult(Activity.RESULT_OK, intentionRetour);
            finish(); // destruction de l'activité courante
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(venuConnexion) {
            Intent intention = new Intent(ListeActivity.this, MainActivity.class);
            startActivity(intention);
        } else {
            // création d'une intention pour informer l'activté parente
            Intent intentionRetour = new Intent();

            // retour à l'activité parente et destruction de l'activité fille
            setResult(Activity.RESULT_OK, intentionRetour);
            finish(); // destruction de l'activité courante
        }

    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur ajouter.
     *
     * @param bouton source du clic.
     */
    public void onClickAjouter(View bouton) {
        Intent intention = new Intent(ListeActivity.this, AjoutListeActivity.class);
        startActivity(intention);
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de deconnexion
     *
     * @param view source du clic.
     */
    public void onClickDeco(View view) {
        Intent intention = new Intent(ListeActivity.this, LoginActivity.class);
        deleteFile("infouser.txt");
        deleteFile("mode.txt");
        startActivity(intention);
        finish();
    }

    /**
     * getter de la liste des fichier users.
     *
     * @return la liste des fichiers Users.
     */
    public static List<String> getListeFichierUser() {
        return listeFichierUser;
    }

    /**
     * Supprimer la liste.
     */
    public void supprimerListe() {
        String nomFichier = listeFichierUser.get(positionItemListe);

        listeFichierUser.remove(positionItemListe);

        deleteFile(nomFichier);

        if(!listeAccueil.isEmpty()) {
            listeAccueil.remove(positionItemListe);
            adaptateur.notifyItemRemoved(positionItemListe);
            this.recreate();
        }
    }
}

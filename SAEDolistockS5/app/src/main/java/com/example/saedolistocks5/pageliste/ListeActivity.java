/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;


import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
    private ArrayList<ListeAccueil> listeAccueil;

    /**
     * Liste pour récupérer les codes articles
     */
    public static ArrayList<String> listeCodeArticleVerif;

    /**
     * Liste pour récupérer les entrepôts
     */
    public static ArrayList<String> listeEntrepotsVerif;

    /**
     * Adatpateur de liste accueil
     */
    ListeAccueilAdapter adaptateur;

    /**
     * Utilisateur courant sur l'application
     */
    private String utilisateurCourant,
    /**
     * Le token pour utiliser l'API
     */
    token, /**
     * L'URL de l'API
     */
    URLApi;

    /**
     * Position de l'item liste
     */
    private int positionItemListe;

    /**
     * Liste des listes de l'utilisateur courant
     */
    static ArrayList<String> listeFichierUser;

    /**
     * Nombre de lignes du fichier à envoyer sur Dolibarr.
     */
    private int nombreLignes;

    /**
     * Index de parcours de ligne
     */
    private int indexParcoursListe;

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
            // Permet d'afficher les listes de l'utilisateur sur la vue
            initialiseListeAccueil();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        listeCodeArticleVerif = new ArrayList<>();
        listeEntrepotsVerif = new ArrayList<>();

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

        // On appelle une méthode pour récupérer les infos de l'utilisateur
        Quartet<String, String, String, String> infos = OutilDivers.getInfosUserAndApi(infosUser);

        // On récupère le token
        token = infos.first();

        // On récupère l'URL de l'API
        URLApi = infos.second();

        // On récupère l'utilisateur courant
        utilisateurCourant = infos.third();

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
        } else if (item.getItemId() == R.id.optionModification) { // modification d'une liste
            Intent intention = new Intent(ListeActivity.this, ModifListeActivity.class);
            intention.putExtra("positionItem", positionItemListe);
            startActivity(intention);
        } else if (item.getItemId() == R.id.optionEnvoyer) {
            EnvoyerListe();
        } else {
            // modification d'une liste
        }
        return (super.onContextItemSelected(item));
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic retour.
     * de retour vers l'activité principale.
     *
     * @param view source du clic.
     */
    public void onClickRetour(View view) {
        Intent intention = new Intent(ListeActivity.this, MainActivity.class);
        startActivity(intention);
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
            startActivity(intention);

    }

    /**
     * Envoyer liste.
     */
    public void EnvoyerListe() {

        String nomFichier = listeFichierUser.get(positionItemListe);        try {
        InputStreamReader liste = new InputStreamReader(openFileInput(nomFichier));
        BufferedReader listeText = new BufferedReader(liste);

        indexParcoursListe = 0;
        nombreLignes = 0;

        // Compter le nombre de lignes
        while (listeText.readLine() != null) {
            nombreLignes++;
        }

        // Réinitialiser la position de lecture du flux
        listeText.close();
        liste = new InputStreamReader(openFileInput(nomFichier));
        listeText = new BufferedReader(liste);

        String[] elementListe;
        String ligne;
        // Index pour savoir si on est sur le dernier traitement du fichier ou nom

        while ((ligne = listeText.readLine()) != null) {
            elementListe = ligne.split(";");
            String[] finalElementListe = elementListe;
            RequetesApi.GetArticlesByEntrepot(URLApi, token, getApplicationContext(),
                    "Liste", elementListe[9], elementListe[10], null, new RequetesApi.QuantiteCallback() {
                        @Override
                        public void onQuantiteRecuperee(int quantiteAvantEnvoieListe)
                                throws FileNotFoundException, JSONException {
                            JSONObject bodyJSON = new JSONObject();
                            indexParcoursListe++;
                            int quantiteApres;
                            int quantiteSaisie;
                            int maj = 0;

                            quantiteSaisie = Integer.parseInt(finalElementListe[5]);

                            if(finalElementListe[6].equals("Ajout")) {
                                finalElementListe[6] = "0";
                                quantiteApres = quantiteAvantEnvoieListe + quantiteSaisie;
                            } else {
                                finalElementListe[6] = "1";
                                quantiteApres = Integer.parseInt(finalElementListe[5]);
                                quantiteSaisie = quantiteApres - quantiteAvantEnvoieListe;
                            }
                            maj = VerifArticlesEtEntrepots(finalElementListe[2], finalElementListe[4]);
                            bodyJSON.put("ref", "");
                            bodyJSON.put("status", 1);
                            bodyJSON.put("Libelle", finalElementListe[1]);
                            bodyJSON.put("CodeArticle", finalElementListe[2]);
                            bodyJSON.put("CodeBarre", "");
                            bodyJSON.put("Designation", finalElementListe[3]);
                            bodyJSON.put("Entrepot", finalElementListe[4]);
                            bodyJSON.put("Quantite", quantiteSaisie);
                            bodyJSON.put("Mode", Integer.parseInt(finalElementListe[6]));
                            bodyJSON.put("Maj", maj);
                            bodyJSON.put("StockAvant", quantiteAvantEnvoieListe);
                            bodyJSON.put("StockApres", quantiteApres);
                            bodyJSON.put("date", finalElementListe[7]);
                            bodyJSON.put("Utilisateur", utilisateurCourant);

                            EnvoyerListeSurDolibarr(nomFichier, bodyJSON, indexParcoursListe);
                            // Si la mise à jour est "Oui", donc que tout s'est bien passé, on modifie le stock
                            if(maj == 0) {
                                JSONObject bodyJSONMvtStock = new JSONObject();
                                bodyJSONMvtStock.put("product_id", Integer.parseInt(finalElementListe[9]));
                                bodyJSONMvtStock.put("warehouse_id", Integer.parseInt(finalElementListe[10]));
                                bodyJSONMvtStock.put("qty", quantiteSaisie);
                                bodyJSONMvtStock.put("datem", finalElementListe[7]);
                                bodyJSONMvtStock.put("movementlabel",
                                        String.format("%s : %s", utilisateurCourant, finalElementListe[1]));
                                ModifierMouvementStock(bodyJSONMvtStock);
                            }
                        }
                    });

        }

        listeText.close();

        listeFichierUser.remove(positionItemListe);

    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    /**
     * Verif articles et entrepots int.
     *
     * @param codeArticle the code article
     * @param nomEntrepot the nom entrepot
     * @return the int
     */
    public int VerifArticlesEtEntrepots(String codeArticle, String nomEntrepot) {
        boolean verifArticle = false;
        boolean verifEntrepot = false;

        for(String codeArticleVerif : listeCodeArticleVerif) {
            if(codeArticle.equals(codeArticleVerif)) {
                verifArticle = true;
            }
        }

        for(String nomEntrepotVerif : listeEntrepotsVerif) {
            if(nomEntrepot.equals(nomEntrepotVerif)) {
                verifEntrepot = true;
            }
        }

        if(verifArticle && verifEntrepot) {
            return 0;
        }

        return 1;
    }


    /**
     * Envoyer liste sur dolibarr.
     *
     * @param nomFichier the nom fichier
     * @param bodyJson   the body json
     * @throws JSONException         the json exception
     * @throws FileNotFoundException the file not found exception
     */
    public void EnvoyerListeSurDolibarr(String nomFichier, JSONObject bodyJson,
                                        int index)
            throws JSONException, FileNotFoundException {
        String urlAPI = String.format("http://%s/htdocs/api/index.php/dolistockapi/listess?api_key=%s",
                URLApi, token);

        OutilAPI.PostApiJson(getApplicationContext(), urlAPI, bodyJson, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // BLC
            }

            @Override
            public void onSuccess(JSONArray result) {
                if(nombreLignes == index) {
                    traiterResultatOK(nomFichier);
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse != null ) {
                    if(error.networkResponse.statusCode != 503) {
                        if(Objects.requireNonNull(error.getMessage()).contains("JSONException")) {
                            // Si on traite la dernière ligne du fichier
                            if(nombreLignes == index) {
                                traiterResultatOK(nomFichier);
                            }
                        } else {
                            Toast.makeText(ListeActivity.this, "Problème d'insertion",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if(nombreLignes == index) {
                        traiterResultatOK(nomFichier);
                    }
                }
            }
        });

    }

    /**
     * Modifier mouvement stock.
     *
     * @param bodyJson the body json
     */
    public void ModifierMouvementStock(JSONObject bodyJson) {
        String urlAPI = String.format("http://%s/htdocs/api/index.php/stockmovements?api_key=%s",
                URLApi, token);

        OutilAPI.PostApiJson(getApplicationContext(), urlAPI, bodyJson, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getApplicationContext(), "Insertion OK",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(JSONArray result) {
                Toast.makeText(getApplicationContext(), "Insertion OK",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VolleyError error) {
                if(error.getMessage() != null) {
                    if(!error.getMessage().contains("JSONException")) {
                        Toast.makeText(getApplicationContext(), "Erreur : " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Traiter resultat ok.
     *
     * @param nomFichier the nom fichier
     */
    public void traiterResultatOK(String nomFichier) {
        Toast.makeText(ListeActivity.this, "Insertion OK",
                Toast.LENGTH_LONG).show();
        deleteFile(nomFichier);
        if(listeAccueil.size() != 0) {
            listeAccueil.remove(positionItemListe);
            adaptateur.notifyItemRemoved(positionItemListe);
        } else {
            // Recrée la vue pour remettre à jour le positionItemListe
            this.recreate();
        }
    }

    /**
     * getter de la liste des fichier users.
     *
     * @return la liste des fichiers Users.
     */
    public static List<String> getListeFichierUser() {
        return listeFichierUser;
    }
}

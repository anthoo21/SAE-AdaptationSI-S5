/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;


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
import com.example.saedolistocks5.pageconnexion.LoginActivity;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class ListeActivity extends AppCompatActivity {

    /**
     * Liste source des données à afficher :
     * chaque élément contient une instance de PhotoParis (une photo
     * et son libellé)
     */
    private ArrayList<ListeAccueil> listeAccueil;

    public static ArrayList<String> listeCodeArticleVerif;

    public static ArrayList<String> listeEntrepotsVerif;

    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView listeAccueilRecyclerView;

    /**
     * Adatpateur de liste accueil
     */
    ListeAccueilAdapter adaptateur;

    /**
     * Utilisateur courant sur l'application
     */
    private String utilisateurCourant, token, URLApi;

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
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        listeCodeArticleVerif = new ArrayList<>();
        listeEntrepotsVerif = new ArrayList<>();

        // Permet d'alimenter la liste des entrepôts et des codes articles via l'API
        RequetesApi.getListeEntrepot(URLApi, token, getApplicationContext(), "Liste");
        RequetesApi.getArticles(URLApi, token, getApplicationContext(), "Liste");

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
     * Méthode pour initialiser la liste des photos et des textes.
     * @throws IOException exception.
     */
    private void initialiseListeAccueil() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String utilisateurCourant;
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));
        String[] valeurInfoUser;
        BufferedReader infosUserTxt = new BufferedReader(infosUser);
        valeurInfoUser = infosUserTxt.readLine().split(";;;;");


        String tokenCrypte = valeurInfoUser[0];

        // Supprimez les crochets et les espaces
        tokenCrypte = tokenCrypte.replaceAll("[\\[\\]\\s]", "");

        // Séparez les valeurs en utilisant la virgule comme délimiteur
        String[] valeurs = tokenCrypte.split(",");

        // Créez un tableau de bytes et remplissez-le avec les valeurs
        byte[] tableauDeBytes = new byte[valeurs.length];
        for (int i = 0; i < valeurs.length; i++) {
            tableauDeBytes[i] = Byte.parseByte(valeurs[i].trim());
        }
        Key key = EncryptAndDecrypteToken.stringToKey(valeurInfoUser[3], "DES");

        token = EncryptAndDecrypteToken.decrypt(tableauDeBytes, key);

        URLApi = valeurInfoUser[2];

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
        } else if (item.getItemId() == R.id.optionModification) { // modification d'une liste

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

    public void EnvoyerListe() {

        String nomFichier = listeFichierUser.get(positionItemListe);        try {
        InputStreamReader liste = new InputStreamReader(openFileInput(nomFichier));
        BufferedReader listeText = new BufferedReader(liste);

        String ligne;




        String utilisateur = utilisateurCourant;
        int maj = 0;
        String[] elementListe;
        while ((ligne = listeText.readLine()) != null) {
            JSONObject bodyJSON = new JSONObject();

            elementListe = ligne.split(";");
            if(elementListe[6].equals("Ajout")) {
                elementListe[6] = "0";
            } else {
                elementListe[6] = "1";
            }
            // Méthode permettant de vérifier l'article et l'entrepôt
            maj = VerifArticlesEtEntrepots(elementListe[2], elementListe[4]);

            bodyJSON.put("ref", "");
            bodyJSON.put("status", 1);
            bodyJSON.put("Libelle", elementListe[1]);
            bodyJSON.put("CodeArticle", elementListe[2]);
            bodyJSON.put("CodeBarre", "");
            bodyJSON.put("Designation", elementListe[3]);
            bodyJSON.put("Entrepot", elementListe[4]);
            bodyJSON.put("Quantite",Integer.parseInt(elementListe[5]));
            bodyJSON.put("Mode", Integer.parseInt(elementListe[6]));
            bodyJSON.put("Maj", maj);
            bodyJSON.put("StockAvant", Integer.parseInt(elementListe[7]));
            bodyJSON.put("StockApres", Integer.parseInt(elementListe[8]));
            bodyJSON.put("date", elementListe[9]);
            bodyJSON.put("Utilisateur", utilisateur);
            EnvoyerListeSurDolibarr(nomFichier, bodyJSON);
        }



        listeText.close();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }

    }

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

    public void EnvoyerListeSurDolibarr(String nomFichier, JSONObject bodyJson) throws JSONException, FileNotFoundException {
        String urlAPI = String.format("http://%s/htdocs/api/index.php/dolistockapi/listess?api_key=%s",
                URLApi, token);

        OutilAPI.PostApiJson(getApplicationContext(), urlAPI, bodyJson, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // BLC
            }

            @Override
            public void onSuccess(JSONArray result) {
                traiterResultatOK(nomFichier);
            }

            @Override
            public void onError(VolleyError error) {
                if(Objects.requireNonNull(error.getMessage()).contains("JSONException")) {
                    traiterResultatOK(nomFichier);
                } else {
                    Toast.makeText(ListeActivity.this, "Problème d'insertion",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //TODO faire méthode envoyer liste et regarder mouvement de stock etc

    }

    public void traiterResultatOK(String nomFichier) {
        Toast.makeText(ListeActivity.this, "Insertion OK",
                Toast.LENGTH_LONG).show();
        deleteFile(nomFichier);
        listeAccueil.remove(positionItemListe);
        adaptateur.notifyItemRemoved(positionItemListe);
    }

    /**
     * getter de la liste des fichier users.
     * @return la liste des fichiers Users.
     */
    public static List<String> getListeFichierUser() {
        return listeFichierUser;
    }
}

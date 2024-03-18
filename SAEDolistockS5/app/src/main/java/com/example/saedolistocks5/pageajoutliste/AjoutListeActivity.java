/**
 * Package de la SAE
 */
package com.example.saedolistocks5.pageajoutliste;

import static com.example.saedolistocks5.outilapi.RequetesApi.getArticles;
import static com.example.saedolistocks5.outilapi.RequetesApi.getListeEntrepot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.RequetesApi;
import com.example.saedolistocks5.outilsdivers.OutilDivers;
import com.example.saedolistocks5.pageliste.ListeActivity;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.example.saedolistocks5.outilsdivers.Quartet;
import com.example.saedolistocks5.popup.CustomPopupAjouter;
import com.example.saedolistocks5.popup.CustomPopupModifier;

/**
 * Classe AjoutListeActivity qui permet d'ajouter une liste sur l'application Dolistock
 * en fonction du nom de la liste, du nom de l'entrepôt et d'ajouter des lignes à la liste
 * en fonction du code de l'article et de la quantité à ajouter ou à modifier
 * @author BONNET, FROMENT et ENJALBERT
 * @version 2.0
 */
public class AjoutListeActivity extends AppCompatActivity {

    /**
     * Editext pour la saisie du Nom de la liste.
     */
    private EditText saisieNomListe;
    /**
     * Editext pour la saisie du nom de l'entrepot.
     */
    private AutoCompleteTextView saisieNomEntrepot;
    /**
     * Editext pour saisir la quantite.
     */
    private EditText saisieQuantite;
    /**
     * AutoCompleteTextView pour la saisie du code article
     */
    private AutoCompleteTextView saisieCodeArticle;
    /**
     * Textview du libelle du stock.
     */
    private TextView libelleStock;
    /**
     * Textview du texte d'erreur
     */
    private TextView texteErreur;
    /**
     * textView des erreurs de saisies.
     */
    private TextView erreurSaisies;
    /**
     * Spinner du choix du mode.
     */
    private Spinner choixMode;
    /**
     * RecyclerView d'ajout article.
     */
    private RecyclerView ajoutArticleRecyclerView;

    /**
     * Bouton pour ajouter un article.
     */
    private Button ajouterArticle;

    /**
     * Bouton pour bloquer l'entête de la liste.
     */
    private Button bloquerEntete;

    /**
     * Bouton image pour rechercher un article par son libellé
     */
    private ImageButton rechercherArticle;

    /**
     * Liste des entrepots.
     */
    public static ArrayList<String> listeEntrepots;
    /**
     * Listes des articles.
     */
    public static ArrayList<String> listeArticles;
    /**
     * Listes des stocks.
     */
    public static ArrayList<String> listeStock;
    /**
     * Liste du choix du mode.
     */
    private ArrayList<String> listeChoixMode;
    /**
     * Liste pour la réference.
     */
    public static ArrayList<String> listeRef;

    /**
     * Liste pour la quantite saisie.
     */
    private ArrayList<Integer> listeQuantiteSaisie;

    /**
     * Liste des ids des articles
     */
    private ArrayList<String> listeIdArticle;

    /**
     * Liste de paire contenant les articles et leurs id associés
     */
    public static ArrayList<Pair<String, String>> listeArticlesIdEtNom;

    /**
     * Liste de quartet (quatres valeurs) contenant l'id, le libellé, sa référence et son stock
     */
    public static ArrayList<Quartet<String, String, String, String>> listeInfosArticle;

    /**
     * Liste de paire contenant les entrepôts et leurs id associés
     */
    public static ArrayList<Pair<String, String>> listeEntrepotIdEtNom;

    /**
     * Liste des articles à ajouter.
     */
    private ArrayList<AjoutListe> articlesAAjouter;

    /**
     * Adapter pour l'ajout d'une liste.
     */
    private AjoutListeAdapter adaptateurAjoutListe;

    /**
     * Adpater pour la liste du choix du mode.
     */
    private ArrayAdapter<String> adaptateurListeChoixMode;

    /**
     * Suggestion à afficher lors du saisie du libellé d'un article
     */
    private ArrayList<String> suggetsionSaisieLibelleArticle;

    /**
     * Permet de récupérer l'ID associé à la suggestion de l'article
     */
    private ArrayList<String> suggestionRecupRefArticle;

    /**
     * Adaptateur pour la suggestion du libellé d'un article
     */
    public static ArrayAdapter<String> adapterSuggestionArticle;

    /**
     * String por le token de l'api.
     */
    private String token;
    /**
     * String pour le user de l'api.
     */
    private String user;
    /**
     * String pour l'URL de l'API.
     */
    private String urlApi;
    /**
     * Pour controler si l'entrepot existe.
     */
    private boolean entrepotOk;

    /**
     * Méthode on create
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_liste_activity);

        // Initialse les composants de base
        initialiserComposants();

        try {
            // Permet d'initialiser les variables à utiliser pour contacter l'API
            initialiserVariableAPI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Configure les différents listener
        configurerListeners();

        // Initialise tous les adaptateurs
        initialiserAdapteurs();

        // Charge les données initiales de la vue
        chargerDonneesInitiales();

        bloquerSaisieArticle();
    }

    /**
     * Initialisation des composants de l'interface utilisateur.
     */
    private void initialiserComposants() {
        saisieNomListe = findViewById(R.id.saisieNomListe);
        saisieNomEntrepot = findViewById(R.id.saisieNomEntrepot);
        saisieCodeArticle = findViewById(R.id.saisieCodeArticle);
        libelleStock = findViewById(R.id.libelleStock);
        saisieQuantite = findViewById(R.id.saisieQuantite);
        texteErreur = findViewById(R.id.erreurSaisieEntrepot);
        erreurSaisies = findViewById(R.id.erreurSaisies);
        choixMode = findViewById(R.id.ddlModeMaj);
        ajoutArticleRecyclerView = findViewById(R.id.ajout_article_recycler);
        ajouterArticle = findViewById(R.id.btnAjouter);
        bloquerEntete = findViewById(R.id.btnValiderEntete);
        rechercherArticle = findViewById(R.id.rechercheArticle);

        listeEntrepots = new ArrayList<>();
        listeArticles = new ArrayList<>();
        listeStock = new ArrayList<>();
        listeChoixMode = new ArrayList<>();
        listeRef = new ArrayList<>();
        articlesAAjouter = new ArrayList<>();
        listeQuantiteSaisie = new ArrayList<>();
        listeArticlesIdEtNom = new ArrayList<>();
        listeEntrepotIdEtNom = new ArrayList<>();
        listeInfosArticle = new ArrayList<>();
        listeIdArticle = new ArrayList<>();
        suggetsionSaisieLibelleArticle = new ArrayList<>();
        suggestionRecupRefArticle = new ArrayList<>();

        adapterSuggestionArticle = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listeArticles);

        entrepotOk = false;
    }

    /**
     * Méthode pour initialiser l'API.
     * @throws Exception exception.
     */
    private void initialiserVariableAPI() throws Exception {
        // On ouvre le fichier infouser pour récupérer les infos à propos de l'utilisateur
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));

        // On appelle une méthode pour récupérer les infos de l'utilisateur
        Quartet<String, String, String, String> infos = OutilDivers.getInfosUserAndApi(infosUser);

        // Récupère le token
        token = infos.first();

        // Récupère l'URL de l'API
        urlApi = infos.second();

        // Récupère le nom de l'utilisateur
        user = infos.third();
    }

    /**
     * Configuration des listeners pour les composants interactifs.
     */
    private void configurerListeners() {
        saisieNomEntrepot.addTextChangedListener(new VerificationEntrepotTextWatcher());
        saisieCodeArticle.addTextChangedListener(new FiltreArticleTextWatcher());
    }

    /**
     * Initialisation et configuration des adapteurs.
     */
    private void initialiserAdapteurs() {
        // On ajoute "Ajout" et "Modification à la liste permettant de choisir le mode
        listeChoixMode.add(getString(R.string.Ajout));
        listeChoixMode.add(getString(R.string.Modification));

        adaptateurListeChoixMode = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, listeChoixMode);
        choixMode.setAdapter(adaptateurListeChoixMode);

        adaptateurAjoutListe = new AjoutListeAdapter(articlesAAjouter);
        ajoutArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ajoutArticleRecyclerView.setAdapter(adaptateurAjoutListe);
    }

    /**
     * Chargement des données initiales pour les entrepôts et les articles.
     */
    private void chargerDonneesInitiales() {
        // Récupère la liste des entrepôts
        getListeEntrepot(urlApi, token, getApplicationContext(), "AjoutListe");

        // Récupère la liste des articles
        getArticles(urlApi, token, getApplicationContext(), "AjoutListe", null);
    }

    private void bloquerSaisieArticle() {
        rechercherArticle.setEnabled(false);
        saisieCodeArticle.setEnabled(false);
        saisieQuantite.setEnabled(false);
        ajouterArticle.setEnabled(false);
    }

    /**
     * Lors d'un click pour ajouter un article à la liste
     * @param view la vue
     */
    public void clickAjouter(View view) {
        // On vérifie si les champs saisies sont valides
        Pair<Boolean, String> verif = verificationChamp(false);
        // Si ce n'est pas valide, alors on affiche une ereur
        if(Boolean.FALSE.equals(verif.first)) {
            erreurSaisies.setText(String.format("%s : %s", getString(R.string.ErreurSaisie),
                    verif.second));
        // Sinon, on traite normalement l'article
        } else {
            erreurSaisies.setText("");

            String libelleArticle = "";
            String idArticle = "";
            // On récupère le code article saisis
            String valeurSaisieCodeArticle = saisieCodeArticle.getText().toString();
            // On récupère la quantité saisie
            String quantiteSaisie = saisieQuantite.getText().toString();

            // On boucle sur les infos de tous article
            for(Quartet<String, String, String, String> quartet : listeInfosArticle) {
                if(valeurSaisieCodeArticle.equals(quartet.third())) {
                    // On récupère le libellé de l'article
                    libelleArticle = quartet.second();
                    // On récupère l'ID de l'article
                    idArticle = quartet.first();
                }
            }

            // On récupère la quantité saisie pour la mettre dans une liste
            listeQuantiteSaisie.add(Integer.parseInt(quantiteSaisie));
            // On récupère l'ID de l'article pour le mettre dans une liste
            listeIdArticle.add(idArticle);
            // On ajoute au recycler view l'article
            articlesAAjouter.add(new AjoutListe(libelleArticle,  valeurSaisieCodeArticle, quantiteSaisie));
            // On met à jour l'adaptateur pour qu'il mette à jour la vue
            adaptateurAjoutListe.notifyDataSetChanged(); // Mise à jour de l'adaptateur après l'ajout
        }
    }

    /**
     *  Permet de vérifier si l'entrepôt, l'article ou autre sont correct
     * @param verifValiderFichier un boolean verifiant le fichier
     * @return une pair
     */
    public Pair<Boolean, String> verificationChamp(boolean verifValiderFichier) {
        // On récupère le code article saisie
        String valeurSaisieArticle = saisieCodeArticle.getText().toString();
        // On récupère la quantité saisie
        String valeurSaisieQuantite = saisieQuantite.getText().toString();

        // Si on fait la vérif au moment de valider la liste
        if (verifValiderFichier) {
            String valeurSaisieNomListe = saisieNomListe.getText().toString().trim();
            if (valeurSaisieNomListe.isEmpty()) {
                return new Pair<>(false, getString(R.string.veuillez_saisir_nom_liste));
            }

            if (!entrepotOk) {
                return new Pair<>(false, getString(R.string.entrepot_incorrect));
            }
            return new Pair<>(true, "");
        }

        // Si l'utilisateur n'a pas saisi d'article
        if (valeurSaisieArticle.isEmpty()) {
            return new Pair<>(false, getString(R.string.veuillez_saisir_code_article));
        }

        // Si l'utilisateur n'a pas saisi de quantité
        if(valeurSaisieQuantite.isEmpty()) {
            return new Pair<>(false, getString(R.string.veuillez_saisir_quantite));
        }

        // Si l'article est incorrect
        if (!listeRef.contains(valeurSaisieArticle)) {
            return new Pair<>(false, getString(R.string.code_article_incorrect));
        }

        if(choixMode.getSelectedItem().toString().equals(getString(R.string.Modification))) {
            for (AjoutListe ajoutListe : articlesAAjouter) {
                if (ajoutListe.getCodeArticle().equals(valeurSaisieArticle)) {
                    return new Pair<>(false, getString(R.string.article_deja_present));
                }
            }
        }

        // Si il n'y a aucun problème, on renvoie true
        return new Pair<>(true, "");
    }

    /**
     * Suppression de l'article
     * @param view la vue
     */
    public void supprimerArticle(View view) {
        // On récupère la position de la ligne à supprimer
        int position = (int) view.getTag();
        // Si ce n'est pas le dernier article de la liste
        if (position >= 0 && position < articlesAAjouter.size()) {
            listeQuantiteSaisie.remove(position);
            listeIdArticle.remove(position);
            articlesAAjouter.remove(position);
            adaptateurAjoutListe.notifyItemRemoved(position);
        }
    }

    /**
     * Réinitialisation des champs
     * @param view la vue
     */
    public void reinitialiserChamp(View view) {
        saisieNomListe.setText("");
        saisieNomEntrepot.setText("");
        saisieCodeArticle.setText("");
        saisieQuantite.setText("");
        libelleStock.setText("");
        texteErreur.setText("");
        erreurSaisies.setText("");
        saisieNomEntrepot.setEnabled(true);
        choixMode.setEnabled(true);
        saisieCodeArticle.setEnabled(false);
        saisieQuantite.setEnabled(false);
        ajouterArticle.setEnabled(false);
        bloquerEntete.setText(getString(R.string.libelle_bloquer));
        adaptateurListeChoixMode.notifyDataSetChanged();
        articlesAAjouter.clear();
        listeQuantiteSaisie.clear();
        listeIdArticle.clear();
        adaptateurAjoutListe.notifyDataSetChanged();
    }

    /**
     * Validation de la liste
     * @param view la vue
     * @throws IOException l'exception
     */
    public void validerListe(View view) throws IOException {
        // On vérifie si les champs saisies sont valides
        Pair<Boolean, String> verif = verificationChamp(true);

        // Si ce n'est pas valide, alors on retourne une erreur
        if(Boolean.FALSE.equals(verif.first)) {
            erreurSaisies.setText(String.format("%s %s", getString(R.string.ErreurSaisie), verif.second));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

            // On met à jour le format pour la date
            simpleDateFormat.applyPattern("yyyy-MM-dd");
            String date = simpleDateFormat.format(Calendar.getInstance().getTime());
            // On met à jour le format pour l'heure
            simpleDateFormat.applyPattern("HH:mm");
            String heure = simpleDateFormat.format(Calendar.getInstance().getTime());
            // On récupère le nom de la liste, de l'entrepôt et le choix du mode
            String nomListe = saisieNomListe.getText().toString();
            String nomEntrepot = saisieNomEntrepot.getText().toString();
            String modeMaj = choixMode.getSelectedItem().toString();

            String refArticle;
            String libelleArticle;
            String quantiteSaisie;
            String idArticle;
            String ligneFichier;

            // On récupère l'ID de l'entrepôt
            String idEntrepot = "";
            for(Pair<String, String> pair : listeEntrepotIdEtNom) {
                if(pair.second.equals(nomEntrepot)) {
                    idEntrepot = pair.first;
                }
            }

            erreurSaisies.setText("");

            // On construit le nom du fichier
            String nomFichier = user + nomListe.toLowerCase().replace(" ", "")
                    + date + heure;

            // On crée un nouveau fichier pour écrire la liste
            FileOutputStream fichier = openFileOutput(nomFichier + ".txt",
                    Context.MODE_PRIVATE);
            // On boucle sur l'ensemble des articles de la liste
            for(int i = 0; i < articlesAAjouter.size(); i++) {
                refArticle = articlesAAjouter.get(i).getCodeArticle();
                libelleArticle = articlesAAjouter.get(i).getLibelleArticle();
                quantiteSaisie = listeQuantiteSaisie.get(i).toString();
                idArticle = listeIdArticle.get(i);
                // On constitue la ligne du fichier
                ligneFichier = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s%s",
                        user, nomListe, refArticle, libelleArticle, nomEntrepot, quantiteSaisie,
                        modeMaj, date, heure, idArticle, idEntrepot, "\n");
                // On écrit la ligne dans le fichier
                fichier.write(ligneFichier.getBytes());
            }

            CustomPopupAjouter dialog = CustomPopupAjouter.createDialog(this);
            dialog.show();

        }
    }

    /**
     * Méthode exécuté pour valider et bloquer l'entête
     * @param view
     */
    public void clickValiderEntete(View view) {
        if(entrepotOk) {
            if(bloquerEntete.getText().toString().equals(getString(R.string.libelle_bloquer))) {
                bloquerEntete.setText(getString(R.string.libelle_debloquer));
                // On désactive la saisie d'un entrepot car il peut y en avoir seulement un par liste
                saisieNomEntrepot.setEnabled(false);

                // On désactive le choix du mode
                choixMode.setEnabled(false);

                // Maintenant, il faut activer la saisie d'un article
                rechercherArticle.setEnabled(true);
                saisieCodeArticle.setEnabled(true);
                saisieQuantite.setEnabled(true);
                ajouterArticle.setEnabled(true);
            } else {
                bloquerEntete.setText(getString(R.string.libelle_bloquer));
                saisieNomEntrepot.setEnabled(true);
                choixMode.setEnabled(true);

                saisieCodeArticle.setEnabled(false);
                saisieCodeArticle.setText("");
                saisieQuantite.setEnabled(false);
                saisieQuantite.setText("");
                ajouterArticle.setEnabled(false);
                libelleStock.setText("");
                rechercherArticle.setEnabled(false);
                articlesAAjouter.clear();
                adaptateurAjoutListe.notifyDataSetChanged();
                listeQuantiteSaisie.clear();
                listeIdArticle.clear();
            }
        } else {
            texteErreur.setText(getString(R.string.entrepot_incorrect));
        }
    }

    /**
     * Permet de vérifier si un article se trouve dans un entrepot
     * @param idEntrepot id de l'entrepôt
     */
    public void VerifArticles(String idEntrepot) {
        for(Quartet<String, String, String, String> quartet : listeInfosArticle) {
            RequetesApi.GetArticlesByEntrepot(urlApi, token, getApplicationContext(),
                    "AjoutListe", quartet.first(), idEntrepot,quartet,
                    null);
        }
    }

    /**
     * Permet d'afficher une popup qui va permettre de rechercher un article
     * par son libellé
     * @param view
     */
    public void afficherPopup(View view) {
        // Création de la pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(AjoutListeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_recherche_article, null);
        builder.setView(dialogView);


        // Initialiser le EditText et le ListView
        final EditText editText = dialogView.findViewById(R.id.saisieLibelleArticle);
        final ListView listView = dialogView.findViewById(R.id.libellesArticles);

        // Exemple pour ajouter des éléments à la liste en fonction de la saisie
        adapterSuggestionArticle = new ArrayAdapter<>(AjoutListeActivity.this,
                android.R.layout.simple_list_item_1, listeArticles);
        listView.setAdapter(adapterSuggestionArticle);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String saisie = s.toString();
                suggetsionSaisieLibelleArticle = new ArrayList<>();
                suggestionRecupRefArticle = new ArrayList<>();

                for(int i = 0; i < listeArticles.size(); i++) {
                    if(listeArticles.get(i).toLowerCase()
                            .startsWith(saisie.toLowerCase())) {
                        suggestionRecupRefArticle.add(listeRef.get(i));
                        suggetsionSaisieLibelleArticle.add(listeArticles.get(i));
                    }
                }


                adapterSuggestionArticle = new ArrayAdapter<>(AjoutListeActivity.this,
                        android.R.layout.simple_list_item_1, suggetsionSaisieLibelleArticle);
                listView.setAdapter(adapterSuggestionArticle);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        // Afficher la pop-up
        AlertDialog dialog = builder.create();
        dialog.show();

        // Gérer le clic sur un élément de la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String refAInserer;
                if(suggestionRecupRefArticle.isEmpty()) {
                    refAInserer = listeRef.get(position);
                } else {
                    refAInserer = suggestionRecupRefArticle.get(position); // Récupère l'élément sélectionné

                }
                saisieCodeArticle.setText(refAInserer);
                dialog.dismiss();
                suggetsionSaisieLibelleArticle.clear();
                suggestionRecupRefArticle.clear();
            }
        });

    }

    /**
     * Verification de l'entrepot.
     */
    private class VerificationEntrepotTextWatcher implements TextWatcher {
        /**
         * Avant la modification du texte.
         * @param s charSequence.
         * @param start int.
         * @param count int.
         * @param after int.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Avant la modification du texte, pas d'action nécessaire
        }

        /**
         * Lorsque que le texte change.
         * @param s charSequence.
         * @param start int.
         * @param before int.
         * @param count int.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // A chaque modification du texte
            String saisie = s.toString();
            Pair<String, String> idEtNomEntrepotActuel = new Pair<>("", "");
            ArrayList<String> suggestions = new ArrayList<>();

            listeEntrepots = new ArrayList<>();

            for(Pair<String, String> pair : listeEntrepotIdEtNom)
            {
                listeEntrepots.add(pair.second);
                // On vérifie si le nom de l'entrepôt est celui saisi par l'utilisateur
                if(pair.second.contains(saisie)) {
                    idEtNomEntrepotActuel = new Pair<>(pair.first, pair.second);
                    suggestions.add(pair.second);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(AjoutListeActivity.this,
                    android.R.layout.simple_dropdown_item_1line, suggestions);
            saisieNomEntrepot.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            // Si l'entrepôt existe
            if (listeEntrepots.contains(saisie)) {
                // On indique à l'utilisateur que l'entrepôt est correct
                texteErreur.setText(getString(R.string.EntrepotOk)); // Pas d'erreur
                texteErreur.setTextColor(getResources().getColor(R.color.green, getTheme()));
                entrepotOk = true;
                // On appel verifArticle pour mettre à jour la liste des articles
                // en fonction de l'entrepôt
                listeArticles = new ArrayList<>();
                VerifArticles(idEtNomEntrepotActuel.first);
            } else {
                // On vide la liste des noms et codes articles et de leur stock
                listeArticles.clear();
                listeRef.clear();
                listeStock.clear();
                libelleStock.setText("");
                // On indique à l'utilisateur que l'entrepôt est incorrect
                texteErreur.setText(getString(R.string.EntrepotIncorrect));
                texteErreur.setTextColor(getResources().getColor(R.color.red, getTheme())); // Utilisez une couleur appropriée
                entrepotOk = false;
            }
        }

        /**
         * Pas d'action.
         * @param s un editable.
         */
        @Override
        public void afterTextChanged(Editable s) {
            // Après la modification du texte, pas d'action nécessaire
        }
    }

    /**
     * Vérifie à chaque saisie de l'utilisateur sur le champ saisie article
     */
    private class FiltreArticleTextWatcher implements TextWatcher {
        /**
         * Avant qu'un texte change.
         * @param s charsequence.
         * @param start int.
         * @param count int.
         * @param after int.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Avant la modification du texte, pas d'action nécessaire
        }

        /**
         * Quand l'utilisateur saisie du texte sur le champ saisieArticle
         * @param s charsequence.
         * @param start int.
         * @param before int.
         * @param count int.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // A chaque modification du texte
            String saisie = s.toString();
            // Lorsque l'utilisateur saisi 3 caractères ou plus
            if (saisie.length() >= 3) {
                // ArrayList pour proposer à l'utilisateur des suggestions de saisies en fonction
                // du code article
                ArrayList<String> suggestions = new ArrayList<>();
                for (String ref : listeRef) {
                    if (ref.toLowerCase().contains(saisie.toLowerCase())) {
                        suggestions.add(ref);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AjoutListeActivity.this,
                        android.R.layout.simple_dropdown_item_1line, suggestions);
                saisieCodeArticle.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            // Mettre à jour le stock disponible si l'article est dans la liste
            if (listeRef.contains(saisie)) {
                int index = listeRef.indexOf(saisie);
                libelleStock.setText(String.format("%s en stock", listeStock.get(index)));
            } else {
                libelleStock.setText("");
            }
        }

        /**
         * Apres que le texte change
         * @param s editable
         */
        @Override
        public void afterTextChanged(Editable s) {
            // Après la modification du texte, pas d'action nécessaire
        }
    }

    /**
     * Méthode invoquée automatiquement lors d'un clic sur l'image bouton
     * de retour vers l'activité principale
     * @param view  source du clic
     */
    public void onClickRetour(View view) {

        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }

}

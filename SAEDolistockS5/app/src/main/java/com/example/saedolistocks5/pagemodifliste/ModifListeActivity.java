/**
 * Package de la SAE
 */
package com.example.saedolistocks5.pagemodifliste;

import static com.example.saedolistocks5.outilapi.RequetesApi.getArticles;
import static com.example.saedolistocks5.outilapi.RequetesApi.getListeEntrepot;
// On importe toutes les listes de AjoutListe pour ne pas avoir à les recréer
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeCodeBarre;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeInfosArticle;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticles;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeRef;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeStock;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeEntrepots;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticlesIdEtNom;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeEntrepotIdEtNom;
import static com.example.saedolistocks5.pageliste.ListeActivity.mode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.BarcodeScannerActivity;
import com.example.saedolistocks5.outilapi.RequetesApi;
import com.example.saedolistocks5.outilsdivers.OutilDivers;
import com.example.saedolistocks5.outilsdivers.Quintet;
import com.example.saedolistocks5.pageajoutliste.AjoutListe;
import com.example.saedolistocks5.pageajoutliste.AjoutListeActivity;
import com.example.saedolistocks5.pageconnexion.LoginActivity;
import com.example.saedolistocks5.pageliste.ListeActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.saedolistocks5.outilsdivers.Quartet;
import com.example.saedolistocks5.pagemain.MainActivity;
import com.example.saedolistocks5.popup.CustomPopupEnvoyer;
import com.example.saedolistocks5.popup.CustomPopupModifier;

/**
 * Classe ModifListeActivity qui permet de modifier une liste sur l'application Dolistock
 * en fonction du nom de la liste, du nom de l'entrepôt et de modifier des lignes à la liste
 * en fonction du code de l'article et de la quantité à ajouter ou à modifier
 * @author BONNET, FROMENT et ENJALBERT
 * @version 2.0
 */
public class ModifListeActivity extends AppCompatActivity {

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
     * RecyclerView modif article.
     */
    private RecyclerView modifArticleRecyclerView;

    /**
     * LayoutManager du RecyclerView
     */
    private RecyclerView.LayoutManager layoutManager;


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
     * Bouton image pour scanner un code barre
     */
    private ImageButton scanCodeBarre;

    /**
     * Liste du choix du mode.
     */
    private ArrayList<String> listeChoixMode;

    /**
     * Liste avant les stocks.
     */
    private ArrayList<Integer> listeStockAvant;

    /**
     * Liste pour la quantite saisie.
     */
    private ArrayList<Integer> listeQuantiteSaisie;

    /**
     * Liste des ids des articles
     */
    private ArrayList<String> listeIdArticle;

    /**
     * Fichiers de l'utilisateurs (listes)
     */
    private ArrayList<String> fichierUser;

    /**
     * Liste des articles à modifier.
     */
    private ArrayList<ModifListe> articlesAModifier;

    /**
     * Adapter pour la modif d'une liste.
     */
    private ModifListeAdapter adaptateurModifListe;

    /**
     * Adpater pour la liste du choix du mode.
     */
    private ArrayAdapter<String> adaptateurListeChoixMode;

    /**
     * Adaptateur pour les suggestions
     */
    public static ArrayAdapter<String> adapterSuggestionArticleModif;

    /**
     * Liste de code barre pour la modification
     */
    public static ArrayList<String> codeBarreModif;

    /**
     * Suggestion de l'utilisateur pour saisir le libellé d'un article
     */
    private ArrayList<String> suggestionSaisieLibelleArticleModif;

    /**
     * Suggestion qui récupère la référence de l'article selectionné par l'utilisateur
     */
    private ArrayList<String> suggestionRecupRefArticleModif;

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
     * Position de l'item sélectionné pour modification
     */
    private int positionItem;

    /**
     * Date de la liste lors de sa création
     */
    private String dateFichierRecup;

    /**
     * Heure de la liste lors de sa création
     */
    private String heureFichierRecup;

    /**
     * Savoir si l'utilisateur est en train de modifier un article ou non
     */
    private boolean modifOk;

    /**
     * Libellé de l'article en modification
     */
    private String articleEnModif;

    /**
     * Permet d'empêcher la saisie du caractère ";".
     */
    InputFilter filter;

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
        setContentView(R.layout.modif_liste_activity);

        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (source.charAt(i) == ';') {
                        return ""; // Bloque le caractère ';'
                    }
                }
                return null; // Laisse passer les autres caractères
            }
        };

        // Initialse les composants de base
        initialiserComposants();

        // Initialise tous les adaptateurs
        initialiserAdapteurs();

        try {
            // Permet d'initialiser les variables à utiliser pour contacter l'API
            initialiserVariableAPI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(mode.equals("connecte")) {
            // Charge les données initiales de la vue
            chargerDonneesInitiales();
        } else {
            recupListePourModif();
        }

        Intent intent = getIntent();
        positionItem = intent.getIntExtra("positionItem", 0);

        // Configure les différents listener
        if(mode.equals("connecte")) {
            configurerListeners();
        }

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
        modifArticleRecyclerView = findViewById(R.id.ajout_article_recycler);
        ajouterArticle = findViewById(R.id.btnAjouter);
        bloquerEntete = findViewById(R.id.btnValiderEntete);
        rechercherArticle = findViewById(R.id.rechercheArticle);
        scanCodeBarre = findViewById(R.id.scanCodeBarre);
        layoutManager = modifArticleRecyclerView.getLayoutManager();

        saisieNomListe.setFilters(new InputFilter[]{filter});
        saisieCodeArticle.setFilters(new InputFilter[]{filter});
        saisieNomListe.setFilters(new InputFilter[]{filter});
        saisieQuantite.setFilters(new InputFilter[]{filter});

        listeEntrepots = new ArrayList<>();
        listeArticles = new ArrayList<>();
        listeStock = new ArrayList<>();
        listeCodeBarre = new ArrayList<>();
        codeBarreModif = new ArrayList<>();
        listeChoixMode = new ArrayList<>();
        listeRef = new ArrayList<>();
        articlesAModifier = new ArrayList<>();
        listeStockAvant = new ArrayList<>();
        listeQuantiteSaisie = new ArrayList<>();
        listeArticlesIdEtNom = new ArrayList<>();
        listeEntrepotIdEtNom = new ArrayList<>();
        listeInfosArticle = new ArrayList<>();
        listeIdArticle = new ArrayList<>();
        fichierUser = new ArrayList<>();
        suggestionRecupRefArticleModif = new ArrayList<>();
        suggestionSaisieLibelleArticleModif = new ArrayList<>();

        adapterSuggestionArticleModif = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        entrepotOk = false;
        modifOk = false;
    }

    public void ScanCodeBarre(View view) {
        // Lance l'activité de scan
        startActivityForResult(new Intent(ModifListeActivity.this,
                BarcodeScannerActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra("SCAN_RESULT");
            saisieCodeArticle.setText(scanResult);
        }
    }

    private void recupListePourModif() {
        try {
            fichierUser = (ArrayList<String>) ListeActivity.getListeFichierUser();
            String nomFichier = fichierUser.get(positionItem);
            InputStreamReader fichier = new InputStreamReader(openFileInput(nomFichier));
            BufferedReader fichiertxt = new BufferedReader(fichier);
            String ligne;
            String nomListe = "";
            String entrepot = "";
            String modeMaj = "";
            String nomArticle;
            String codeArticle;
            String quantite;
            String idArticle;
            String[] elementListe;
            while ((ligne = fichiertxt.readLine()) != null) {
                elementListe = ligne.split(";");
                nomListe = elementListe[1];
                entrepot = elementListe[4];
                nomArticle = elementListe[3];
                codeArticle = elementListe[2];
                quantite = elementListe[5];
                modeMaj = elementListe[6];
                dateFichierRecup = elementListe[7];
                heureFichierRecup = elementListe[8];
                idArticle = elementListe[9];
                listeIdArticle.add(idArticle);
                articlesAModifier.add(new ModifListe(nomArticle, codeArticle,
                        quantite));
                adaptateurModifListe.notifyDataSetChanged();
                listeQuantiteSaisie.add(Integer.parseInt(quantite));
            }
            saisieNomListe.setText(nomListe);
            saisieNomEntrepot.setText(entrepot);
            choixMode.setEnabled(false);
            saisieNomEntrepot.setEnabled(false);

        } catch (FileNotFoundException e) {
            // throw new RuntimeException(e); peu importe
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Méthode pour initialiser l'API.
     * @throws Exception exception.
     */
    private void initialiserVariableAPI() throws Exception {
        // On ouvre le fichier infouser pour récupérer les infos à propos de l'utilisateur
        InputStreamReader infosUser = new InputStreamReader(openFileInput("infouser.txt"));
        InputStreamReader modeTxt = new InputStreamReader(openFileInput("mode.txt"));

        // On appelle une méthode pour récupérer les infos de l'utilisateur
        Quartet<String, String, String, String> infos = OutilDivers.getInfosUserAndApi(infosUser, modeTxt);

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

        adaptateurModifListe = new ModifListeAdapter(articlesAModifier);
        modifArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        modifArticleRecyclerView.setAdapter(adaptateurModifListe);
    }

    /**
     * Chargement des données initiales pour les entrepôts et les articles.
     */
    private void chargerDonneesInitiales() {
        // Récupère la liste des entrepôts
        getListeEntrepot(urlApi, token, getApplicationContext(), "ModifListe");

        // Récupère la liste des articles
        getArticles(urlApi, token, getApplicationContext(), "ModifListe", new RequetesApi.InfosArticlesCallback() {
            @Override
            public void onInfosArticlesRecup() {
                    recupListePourModif();
            }
        });
    }

    /**
     * Lors d'un click pour ajouter un article à la liste
     * @param view la vue
     */
    public void clickAjouter(View view) {

        modifOk = false;

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
            for(Quintet<String, String, String, String, String> quintet : listeInfosArticle) {
                if(valeurSaisieCodeArticle.equals(quintet.third())
                        || valeurSaisieCodeArticle.equals(quintet.fifth())) {
                    // On récupère le libellé de l'article
                    libelleArticle = quintet.second();
                    // On récupère l'ID de l'article
                    idArticle = quintet.first();
                }
            }

            // On récupère la quantité saisie pour la mettre dans une liste
            listeQuantiteSaisie.add(Integer.parseInt(quantiteSaisie));
            // On récupère l'ID de l'article pour le mettre dans une liste
            listeIdArticle.add(idArticle);
            // On ajoute au recycler view l'article
            articlesAModifier.add(new ModifListe(libelleArticle,  valeurSaisieCodeArticle, quantiteSaisie));
            // On met à jour l'adaptateur pour qu'il mette à jour la vue
            adaptateurModifListe.notifyDataSetChanged(); // Mise à jour de l'adaptateur après l'ajout
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
        Pair<Boolean, String> pair = new Pair<>(true, "");


        // Si on fait la vérif au moment de valider la liste
        if (verifValiderFichier) {
            String valeurSaisieNomListe = saisieNomListe.getText().toString().trim();
            if (valeurSaisieNomListe.isEmpty()) {
                return new Pair<>(false, getString(R.string.veuillez_saisir_nom_liste));
            }

            if (!entrepotOk && mode.equals("connecte")) {
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
        if (listeCodeBarre.contains(valeurSaisieArticle) && mode.equals("connecte")) {
            return new Pair<>(true, "");
        }

        // Si l'article est incorrect
        if (!listeRef.contains(valeurSaisieArticle) && mode.equals("connecte")) {
            pair =  new Pair<>(false, getString(R.string.code_article_incorrect));
        }


        if(choixMode.getSelectedItem().toString().equals(getString(R.string.Modification))) {
            for (ModifListe modifListe : articlesAModifier) {
                if (modifListe.getCodeArticle().equals(valeurSaisieArticle)) {
                    return new Pair<>(false, getString(R.string.article_deja_present));
                }
            }
        }

        // Si il n'y a aucun problème, on renvoie true
        return pair;
    }

    /**
     * Suppression de l'article
     * @param view la vue
     */
    public void supprimerArticle(View view) {
        // On récupère la position de la ligne à supprimer
        int position = (int) view.getTag();
        modifOk = false;
        // Si ce n'est pas le dernier article de la liste
        if (position >= 0 && position < articlesAModifier.size()) {
            listeQuantiteSaisie.remove(position);
            articlesAModifier.remove(position);
            listeIdArticle.remove(position);
            adaptateurModifListe.notifyItemRemoved(position);
        }
    }

    public void modifierArticle(View view) {
        // On récupère la position de la ligne à modifier
        int position = (int) view.getTag();

        ModifListe modif = articlesAModifier.get(position);

        if (!articlesAModifier.isEmpty()) {
            listeQuantiteSaisie.remove(position);
            articlesAModifier.remove(position);
            listeIdArticle.remove(position);
            adaptateurModifListe.notifyItemRemoved(position);
            adaptateurModifListe.notifyItemRangeChanged(position, articlesAModifier.size());
            // Mise à jour des tags pour chaque vue restante dans l'adaptateur
           //for (int i = 0; i < articlesAModifier.size(); i++) {
           //    View itemView = layoutManager.findViewByPosition(i);
           //    if (itemView != null) {
           //        itemView.setTag(i); // Mettre à jour le tag avec la nouvelle position
           //    }
           //}

        }

        if(modifOk) {
            // On récupère le code article saisis
            String valeurSaisieCodeArticle = saisieCodeArticle.getText().toString();
            // On récupère la quantité saisie
            String quantiteSaisie = saisieQuantite.getText().toString();

            String idArticle = "";

            // On boucle sur les infos de tous article
            for(Quintet<String, String, String, String, String> quintet : listeInfosArticle) {
                if(valeurSaisieCodeArticle.equals(quintet.third())
                        || valeurSaisieCodeArticle.equals(quintet.fifth())) {
                    // On récupère l'ID de l'article
                    idArticle = quintet.first();
                }
            }
            articlesAModifier.add(new ModifListe(articleEnModif,
                    saisieCodeArticle.getText().toString(), saisieQuantite.getText().toString()));
            listeQuantiteSaisie.add(Integer.parseInt(quantiteSaisie));
            listeIdArticle.add(idArticle);
        }
        saisieCodeArticle.setText(modif.getCodeArticle());
        saisieQuantite.setText(modif.getQuantite());
        articleEnModif = modif.getLibelleArticle();

        modifOk = true;
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
        choixMode.setEnabled(true);
        saisieNomEntrepot.setEnabled(true);
        saisieCodeArticle.setEnabled(false);
        saisieQuantite.setEnabled(false);
        ajouterArticle.setEnabled(false);
        bloquerEntete.setText(getString(R.string.libelle_bloquer));
        adaptateurListeChoixMode.notifyDataSetChanged();
        articlesAModifier.clear();
        listeQuantiteSaisie.clear();
        listeIdArticle.clear();
        adaptateurModifListe.notifyDataSetChanged();
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
            String nomFichier = fichierUser.get(positionItem);
            deleteFile(nomFichier);

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

            // On crée un nouveau fichier pour écrire la liste
            FileOutputStream fichier = openFileOutput(nomFichier,
                    Context.MODE_PRIVATE);
            // On boucle sur l'ensemble des articles de la liste
            for(int i = 0; i < articlesAModifier.size(); i++) {
                refArticle = articlesAModifier.get(i).getCodeArticle();
                libelleArticle = articlesAModifier.get(i).getLibelleArticle();
                quantiteSaisie = listeQuantiteSaisie.get(i).toString();
                idArticle = listeIdArticle.get(i);
                if(mode.equals("deconnecte")) {
                    idEntrepot = "0";
                    idArticle = "0";
                }
                // On constitue la ligne du fichier
                ligneFichier = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s%s",
                        user, nomListe, refArticle, libelleArticle, nomEntrepot, quantiteSaisie,
                        modeMaj, dateFichierRecup, heureFichierRecup, idArticle, idEntrepot, "\n");
                // On écrit la ligne dans le fichier
                fichier.write(ligneFichier.getBytes());
            }

            CustomPopupModifier dialog = CustomPopupModifier.createDialog(this);
            dialog.show();

        }
    }

    /**
     * Méthode exécuté pour valider et bloquer l'entête
     * @param view
     */
    public void clickValiderEntete(View view) {
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
            articlesAModifier.clear();
            adaptateurModifListe.notifyDataSetChanged();
            listeQuantiteSaisie.clear();
            listeIdArticle.clear();

        }
    }

    /**
     * Permet d'afficher une popup qui va permettre de rechercher un article
     * par son libellé
     * @param view
     */
    public void afficherPopup(View view) {
        // Création de la pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifListeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_recherche_article, null);
        builder.setView(dialogView);


        // Initialiser le EditText et le ListView
        final EditText editText = dialogView.findViewById(R.id.saisieLibelleArticle);
        final ListView listView = dialogView.findViewById(R.id.libellesArticles);

        // Exemple pour ajouter des éléments à la liste en fonction de la saisie
        adapterSuggestionArticleModif = new ArrayAdapter<>(ModifListeActivity.this,
                android.R.layout.simple_list_item_1, listeArticles);
        listView.setAdapter(adapterSuggestionArticleModif);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String saisie = s.toString();
                suggestionSaisieLibelleArticleModif = new ArrayList<>();
                suggestionRecupRefArticleModif = new ArrayList<>();

                for(int i = 0; i < listeArticles.size(); i++) {
                    if(listeArticles.get(i).toLowerCase()
                            .startsWith(saisie.toLowerCase())) {
                        suggestionRecupRefArticleModif.add(listeRef.get(i));
                        suggestionSaisieLibelleArticleModif.add(listeArticles.get(i));
                    }
                }


                adapterSuggestionArticleModif = new ArrayAdapter<>(ModifListeActivity.this,
                        android.R.layout.simple_list_item_1, suggestionSaisieLibelleArticleModif);
                listView.setAdapter(adapterSuggestionArticleModif);
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
                if(suggestionRecupRefArticleModif.isEmpty()) {
                    refAInserer = listeRef.get(position);
                } else {
                    refAInserer = suggestionRecupRefArticleModif.get(position); // Récupère l'élément sélectionné

                }
                saisieCodeArticle.setText(refAInserer);
                dialog.dismiss();
                suggestionSaisieLibelleArticleModif.clear();
                suggestionRecupRefArticleModif.clear();
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
                if(pair.second.toLowerCase().contains(saisie.toLowerCase())) {
                    idEtNomEntrepotActuel = new Pair<>(pair.first, pair.second);
                    suggestions.add(pair.second);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(ModifListeActivity.this,
                    android.R.layout.simple_dropdown_item_1line, suggestions);
            saisieNomEntrepot.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            // Si l'entrepôt existe
            if (listeEntrepots.contains(saisie)) {
                // On indique à l'utilisateur que l'entrepôt est correct
                texteErreur.setText(R.string.EntrepotOk); // Pas d'erreur
                texteErreur.setTextColor(getResources().getColor(R.color.green, getTheme()));
                entrepotOk = true;
                // On appel verifArticle pour mettre à jour la liste des articles
                // en fonction de l'entrepôt
                VerifArticles(idEtNomEntrepotActuel.first);
            } else {
                // On vide la liste des noms et codes articles et de leur stock
                listeArticles.clear();
                listeRef.clear();
                listeStock.clear();
                // On indique à l'utilisateur que l'entrepôt est incorrect
                texteErreur.setText(R.string.EntrepotIncorrect);
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
     * Permet de vérifier si un article se trouve dans un entrepot
     * @param idEntrepot id de l'entrepôt
     */
    public void VerifArticles(String idEntrepot) {

        for(Quintet<String, String, String, String, String> quintet : listeInfosArticle) {
            RequetesApi.GetArticlesByEntrepot(urlApi, token, getApplicationContext(),
                    "ModifListe", quintet.first(), idEntrepot,quintet, null);
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

                for (String codeBarre : listeCodeBarre) {
                    if (codeBarre.toLowerCase().contains(saisie.toLowerCase())) {
                        suggestions.add(codeBarre);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ModifListeActivity.this,
                        android.R.layout.simple_dropdown_item_1line, suggestions);
                saisieCodeArticle.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            // Mettre à jour le stock disponible si l'article est dans la liste
            int index = 0;
            if (listeRef.contains(saisie)) {
                index = listeRef.indexOf(saisie);
                libelleStock.setText(String.format("%s en stock", listeStock.get(index)));
            } else if (listeCodeBarre.contains(saisie)) {
                index = listeCodeBarre.indexOf(saisie);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // création d'une intention pour informer l'activté parente
        Intent intentionRetour = new Intent();

        // retour à l'activité parente et destruction de l'activité fille
        setResult(Activity.RESULT_OK, intentionRetour);
        finish(); // destruction de l'activité courante
    }

}

/**
 * Package de la SAE
 */
package com.example.saedolistocks5.pageajoutliste;

import static com.example.saedolistocks5.outilapi.RequetesApi.getArticles;
import static com.example.saedolistocks5.outilapi.RequetesApi.getListeEntrepot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.OutilAPI;
import com.example.saedolistocks5.outilapi.RequetesApi;
import com.example.saedolistocks5.pageliste.ListeActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

/**
 * Classe AjoutListeActivity
 */
public class AjoutListeActivity extends AppCompatActivity {

    /**
     * Editext pour la saisie du Nom de la liste.
     */
    private EditText saisieNomListe;
    /**
     * Editext pour la saisie du nom de l'entrepot.
     */
    private EditText saisieNomEntrepot;
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
     * Liste avant les stocks.
     */
    private ArrayList<Integer> listeStockAvant;
    /**
     * Liste des stocks apres modification ou ajout.
     */
    private ArrayList<Integer> listeStockApres;
    /**
     * Liste pour la quantite saisie.
     */
    private ArrayList<Integer> listeQuantiteSaisie;

    public static ArrayList<Pair<String, String>> listeArticlesIdEtNom;

    public static ArrayList<Quartet<String, String, String, String>> listeInfosArticle;

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
     * Pour controler sir l'entrepot existe.
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

        initialiserComposants();
        try {
            initialiserVariableAPI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        configurerListeners();
        initialiserAdapteurs();
        chargerDonneesInitiales();
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

        listeEntrepots = new ArrayList<>();
        listeArticles = new ArrayList<>();
        listeStock = new ArrayList<>();
        listeChoixMode = new ArrayList<>();
        listeRef = new ArrayList<>();
        articlesAAjouter = new ArrayList<>();
        listeStockAvant = new ArrayList<>();
        listeStockApres = new ArrayList<>();
        listeQuantiteSaisie = new ArrayList<>();
        listeArticlesIdEtNom = new ArrayList<>();
        listeEntrepotIdEtNom = new ArrayList<>();
        listeInfosArticle = new ArrayList<>();

        entrepotOk = false;
    }

    /**
     * Méthode pour initialiser l'API.
     * @throws Exception exception.
     */
    private void initialiserVariableAPI() throws Exception {
        InputStreamReader fichier = new InputStreamReader(openFileInput("infouser.txt"));
        BufferedReader fichiertexte = new BufferedReader(fichier);

        String ligneFichier = fichiertexte.readLine();
        String[] valeurFichierInfos = ligneFichier.split(";;;;");
        String tokenCrypte = valeurFichierInfos[0];

        // Supprimez les crochets et les espaces
        tokenCrypte = tokenCrypte.replaceAll("[\\[\\]\\s]", "");

        // Séparez les valeurs en utilisant la virgule comme délimiteur
        String[] valeurs = tokenCrypte.split(",");

        // Créez un tableau de bytes et remplissez-le avec les valeurs
        byte[] tableauDeBytes = new byte[valeurs.length];
        for (int i = 0; i < valeurs.length; i++) {
            tableauDeBytes[i] = Byte.parseByte(valeurs[i].trim());
        }
        Key key = EncryptAndDecrypteToken.stringToKey(valeurFichierInfos[3], "DES");

        token = EncryptAndDecrypteToken.decrypt(tableauDeBytes, key);

        user = valeurFichierInfos[1];

        urlApi = valeurFichierInfos[2];
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
        listeChoixMode.add(String.valueOf(R.string.Ajout));
        listeChoixMode.add(String.valueOf(R.string.Modification));

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
        getListeEntrepot(urlApi, token, getApplicationContext(), "AjoutListe");
        getArticles(urlApi, token, getApplicationContext(), "AjoutListe");
    }

    /**
     * Click pour ajouter
     * @param view la vue
     */
    public void clickAjouter(View view) {
        Pair<Boolean, String> verif = verificationChamp(false);
        if(Boolean.FALSE.equals(verif.first)) {
            erreurSaisies.setText(R.string.ErreurSaisie + "" +  verif.second);
            return;
        } else {
            erreurSaisies.setText("");
        }

        String libelleArticle = "";
        int quantite = 0;
        int iteration = 0;
        String valeurSaisieCodeArticle = saisieCodeArticle.getText().toString();
        int quantiteSaisie = Integer.parseInt(saisieQuantite.getText().toString());
        for(String valLabel : listeRef) {
            if(valeurSaisieCodeArticle.equals(valLabel)) {
                libelleArticle = listeArticles.get(iteration);
                quantite = Integer.parseInt(listeStock.get(iteration));
                listeStockAvant.add(Integer.valueOf(listeStock.get(iteration)));
            }
            iteration++;
        }

        // On désactive la saisie d'un entrepot car il peut y en avoir seulement un par liste
        saisieNomEntrepot.setEnabled(false);

        if(choixMode.getSelectedItem().toString().equals(String.valueOf(R.string.Ajout))) {

            quantite += quantiteSaisie;
            if(listeChoixMode.size() > 1) {
                listeChoixMode.remove(1);
                adaptateurListeChoixMode.notifyDataSetChanged();
            }
        } else {
            quantite = quantiteSaisie;
            if(listeChoixMode.size() > 1) {
                listeChoixMode.remove(0);
                adaptateurListeChoixMode.notifyDataSetChanged();
            }
        }
        listeQuantiteSaisie.add(quantiteSaisie);
        listeStockApres.add(quantite);
        articlesAAjouter.add(new AjoutListe(libelleArticle,  valeurSaisieCodeArticle, quantite + ""));
        adaptateurAjoutListe.notifyDataSetChanged(); // Mise à jour de l'adaptateur après l'ajout
    }

    /**
     *
     * @param verifValiderFichier un boolean verifiant le fichier
     * @return une pair
     */
    public Pair<Boolean, String> verificationChamp(boolean verifValiderFichier) {
        String valeurSaisieArticle = saisieCodeArticle.getText().toString();
        String valeurSaisieQuantite = saisieQuantite.getText().toString();

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

        if (valeurSaisieArticle.isEmpty()) {
            return new Pair<>(false, getString(R.string.veuillez_saisir_code_article));
        }

        if(valeurSaisieQuantite.isEmpty()) {
            return new Pair<>(false, getString(R.string.veuillez_saisir_quantite));
        }

        if (!listeRef.contains(valeurSaisieArticle)) {
            return new Pair<>(false, getString(R.string.code_article_incorrect));
        }

        for (AjoutListe ajoutListe : articlesAAjouter) {
            if (ajoutListe.getCodeArticle().equals(valeurSaisieArticle)) {
                return new Pair<>(false, getString(R.string.article_deja_present));
            }
        }

        return new Pair<>(true, "");
    }

    /**
     * Suppresion de l'article
     * @param view la vue
     */
    public void supprimerArticle(View view) {
        int position = (int) view.getTag();
        if (position >= 0 && position < articlesAAjouter.size()) {
            articlesAAjouter.remove(position);
            adaptateurAjoutListe.notifyItemRemoved(position);
        }
        // Cas où il n'y a plus d'article ajouter,
        // On remet à jour le choix du mode
        if(articlesAAjouter.isEmpty()) {
            listeChoixMode.clear();
            listeChoixMode.add("Ajout");
            listeChoixMode.add("Modification");
            saisieNomEntrepot.setEnabled(true);
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
        listeChoixMode.clear();
        listeChoixMode.add("Ajout");
        listeChoixMode.add("Modification");
        saisieNomEntrepot.setEnabled(true);
        adaptateurListeChoixMode.notifyDataSetChanged();
        articlesAAjouter.clear();
        adaptateurAjoutListe.notifyDataSetChanged();
    }

    /**
     * Validation de la liste
     * @param view la vue
     * @throws IOException l'exception
     */
    public void validerListe(View view) throws IOException {
        Pair<Boolean, String> verif = verificationChamp(true);

        if(Boolean.FALSE.equals(verif.first)) {
            erreurSaisies.setText(R.string.ErreurSaisie + " " + verif.second);
        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
            simpleDateFormat.applyPattern("yyyy-dd-MM");
            String nomListe = saisieNomListe.getText().toString();
            String nomEntrepot = saisieNomEntrepot.getText().toString();
            String modeMaj = choixMode.getSelectedItem().toString();
            String date = simpleDateFormat.format(Calendar.getInstance().getTime());
            simpleDateFormat.applyPattern("HH:mm");
            String heure = simpleDateFormat.format(Calendar.getInstance().getTime());


            String refArticle;
            String libelleArticle;
            String quantiteSaisie;
            String stockAvant;
            String stockApres;

            String ligneFichier;

            erreurSaisies.setText("");
            String nomFichier = user + nomListe.toLowerCase().replace(" ", "")
                    + date + heure;

            FileOutputStream fichier = openFileOutput(nomFichier + ".txt",
                    Context.MODE_PRIVATE);
            for(int i = 0; i < articlesAAjouter.size(); i++) {
                refArticle = listeRef.get(i);
                libelleArticle = listeArticles.get(i);
                quantiteSaisie = listeQuantiteSaisie.get(i).toString();
                stockAvant = listeStockAvant.get(i).toString();
                stockApres = articlesAAjouter.get(i).getQuantite();

                ligneFichier = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s%s",
                        user, nomListe, refArticle, libelleArticle, nomEntrepot, quantiteSaisie,
                        modeMaj, stockAvant, stockApres, date, heure, "\n");
                fichier.write(ligneFichier.getBytes());
            }

            Intent intention = new Intent(AjoutListeActivity.this, ListeActivity.class);
            startActivity(intention);

        }
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
            listeEntrepots = new ArrayList<>();
            for(Pair<String, String> pair : listeEntrepotIdEtNom)
            {
                listeEntrepots.add(pair.second);
                if(pair.second.contains(saisie)) {
                    idEtNomEntrepotActuel = new Pair<>(pair.first, pair.second);
                }
            }
            if (listeEntrepots.contains(saisie)) {
                texteErreur.setText(R.string.EntrepotOk); // Pas d'erreur
                texteErreur.setTextColor(getResources().getColor(R.color.green, getTheme()));
                entrepotOk = true;
                VerifArticles(idEtNomEntrepotActuel.first);
            } else {
                listeArticles.clear();
                listeRef.clear();
                listeStock.clear();
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

    public void VerifArticles(String idEntrepot) {
        for(Quartet<String, String, String, String> quartet : listeInfosArticle) {
            RequetesApi.GetArticlesByEntrepot(urlApi, token, getApplicationContext(),
                    "AjoutListe", quartet.first(), idEntrepot,quartet);
        }
    }


    /**
     * Filtre de l'article.
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
         * Quand le texte change..
         * @param s charsequence.
         * @param start int.
         * @param before int.
         * @param count int.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // A chaque modification du texte
            String saisie = s.toString();
            if (saisie.length() >= 3) { // Par exemple, filtrer après 3 caractères saisis
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
                libelleStock.setText(listeStock.get(index) + " en stock");
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

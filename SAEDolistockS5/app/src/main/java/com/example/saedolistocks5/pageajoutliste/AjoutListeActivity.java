package com.example.saedolistocks5.pageajoutliste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.EncryptAndDecrypteToken;
import com.example.saedolistocks5.outilapi.OutilAPI;
import com.example.saedolistocks5.pageliste.ListeActivity;
import com.example.saedolistocks5.pagemain.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AjoutListeActivity extends AppCompatActivity {

    // Déclaration des composants de l'interface utilisateur
    private EditText saisieNomListe, saisieNomEntrepot, saisieQuantite;
    private AutoCompleteTextView saisieCodeArticle;
    private TextView libelleStock, texteErreur, erreurSaisies;
    private Spinner choixMode;
    private RecyclerView ajoutArticleRecyclerView;

    // Listes pour stocker les données
    private ArrayList<String> listeEntrepots, listeArticles, listeStock, listeChoixMode, listeRef;
    private ArrayList<Integer> listeStockAvant, listeStockApres, listeQuantiteSaisie;
    private ArrayList<AjoutListe> articlesAAjouter;

    // Adapteurs pour les vues
    private AjoutListeAdapter adaptateurAjoutListe;
    private ArrayAdapter<String> adaptateurListeChoixMode;

    // Token, utilisateur et URL de dolibarr à utiliser pour contacter l'API
    private String token, user, URLApi;

    // Variables de contrôle
    private boolean entrepotOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_liste_activity);

        initialiserComposants();
        try {
            initialiserVariableAPI();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        entrepotOk = false;
    }

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
        Key key = EncryptAndDecrypteToken.getKey();

        token = EncryptAndDecrypteToken.decrypt(tableauDeBytes, key);

        user = valeurFichierInfos[1];

        URLApi = valeurFichierInfos[2];
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
        listeChoixMode.add("Ajout");
        listeChoixMode.add("Modification");

        adaptateurListeChoixMode = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listeChoixMode);
        choixMode.setAdapter(adaptateurListeChoixMode);

        adaptateurAjoutListe = new AjoutListeAdapter(articlesAAjouter);
        ajoutArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ajoutArticleRecyclerView.setAdapter(adaptateurAjoutListe);
    }

    /**
     * Chargement des données initiales pour les entrepôts et les articles.
     */
    private void chargerDonneesInitiales() {
        getListeEntrepot();
        getArticles();
    }

    public void getListeEntrepot() {

        String urlApi = String.format("http://%s/htdocs/api/index.php/warehouses?api_key=%s", URLApi, token);

        OutilAPI.getApiRetour(getApplicationContext(), urlApi, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String ref = item.getString("ref");
                        listeEntrepots.add(ref);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void getArticles() {

        String urlApi = String.format("http://%s/htdocs/api/index.php/products?api_key=%s", URLApi, token);

        OutilAPI.getApiRetour(getApplicationContext(), urlApi, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String label = item.getString("label");
                        String stockReel = item.getString("stock_reel");
                        String refArticle = item.getString("ref");
                        listeArticles.add(label);
                        listeStock.add(stockReel.equals("null") ? "0" : stockReel);
                        listeRef.add(refArticle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    public void clickAjouter(View view) {
        Pair<Boolean, String> verif = verificationChamp(false);
        if(!verif.first) {
            erreurSaisies.setText("Erreur de saisie : " + verif.second);
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

        if(choixMode.getSelectedItem().toString() == "Ajout") {
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

    public Pair<Boolean, String> verificationChamp(boolean verifValiderFichier) {
        String valeurSaisieArticle = saisieCodeArticle.getText().toString();
        String valeurSaisieQuantite = saisieQuantite.getText().toString();

        if (verifValiderFichier) {
            String valeurSaisieNomListe = saisieNomListe.getText().toString().trim();
            if (valeurSaisieNomListe.isEmpty()) {
                return new Pair<>(false, getString(R.string.veuillez_saisir_nom_liste));
            }

            String valeurSaisieEntrepot = saisieNomEntrepot.getText().toString().trim();
            if (!listeEntrepots.contains(valeurSaisieEntrepot)) {
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


    public void supprimerArticle(View view) {
        int position = (int) view.getTag();
        if (position >= 0 && position < articlesAAjouter.size()) {
            articlesAAjouter.remove(position);
            adaptateurAjoutListe.notifyItemRemoved(position);
        }
        // Cas où il n'y a plus d'article ajouter,
        // On remet à jour le choix du mode
        if(articlesAAjouter.size() == 0) {
            listeChoixMode.clear();
            listeChoixMode.add("Ajout");
            listeChoixMode.add("Modification");
        }
    }

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
        adaptateurListeChoixMode.notifyDataSetChanged();
        articlesAAjouter.clear();
        adaptateurAjoutListe.notifyDataSetChanged();
    }

    public void validerListe(View view) throws IOException {
        Pair<Boolean, String> verif = verificationChamp(true);

        if(!verif.first) {
            erreurSaisies.setText("Erreur saisie : " + verif.second);
        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
            simpleDateFormat.applyPattern("yyyyMMdd");
            String nomListe = saisieNomListe.getText().toString();
            String nomEntrepot = saisieNomEntrepot.getText().toString();
            String modeMaj = choixMode.getSelectedItem().toString();
            String date = simpleDateFormat.format(Calendar.getInstance().getTime());
            simpleDateFormat.applyPattern("HHmm");
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

            FileOutputStream fichier = openFileOutput(nomFichier + ".txt", Context.MODE_PRIVATE);
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

    private class VerificationEntrepotTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Avant la modification du texte, pas d'action nécessaire
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // A chaque modification du texte
            String saisie = s.toString();
            if (listeEntrepots.contains(saisie)) {
                texteErreur.setText("Entrepot OK"); // Pas d'erreur
                texteErreur.setTextColor(getResources().getColor(R.color.green, getTheme()));
                entrepotOk = true;
            } else {
                texteErreur.setText("Entrepôt incorrect !");
                texteErreur.setTextColor(getResources().getColor(R.color.red, getTheme())); // Utilisez une couleur appropriée
                entrepotOk = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Après la modification du texte, pas d'action nécessaire
        }
    }

    private class FiltreArticleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Avant la modification du texte, pas d'action nécessaire
        }

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

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AjoutListeActivity.this, android.R.layout.simple_dropdown_item_1line, suggestions);
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

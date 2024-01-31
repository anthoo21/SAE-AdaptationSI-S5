package com.example.saedolistocks5.pageajoutliste;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.R;
import com.example.saedolistocks5.outilapi.OutilAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AjoutLiseActivity extends AppCompatActivity {

    EditText saisieNomListe;

    EditText saisieNomEntrepot;

    AutoCompleteTextView saisieCodeArticle;

    TextView libelleStock;

    EditText saisieQuantite;

    TextView texteErreur;

    Spinner choixMode;

    ArrayList<String> listeEntrepots;

    ArrayList<String> listeArticles;

    ArrayList<String> listeStock;

    ArrayList<String> listeChoixMode;

    ArrayList<AjoutListe> articlesAAjouter;

    ArrayList<String> listeRef;

    private RecyclerView ajoutArticleRecyclerView;

    boolean entrepotOk;

    AjoutListeAdapter adaptateurAjoutListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_liste_activity);
        saisieNomListe = findViewById(R.id.saisieNomListe);
        saisieNomEntrepot = findViewById(R.id.saisieNomEntrepot);
        saisieCodeArticle = findViewById(R.id.saisieCodeArticle);
        libelleStock = findViewById(R.id.libelleStock);
        saisieQuantite = findViewById(R.id.saisieQuantite);
        texteErreur = findViewById(R.id.erreurSaisieEntrepot);
        choixMode = findViewById(R.id.ddlModeMaj);

        listeEntrepots = new ArrayList<>();
        listeArticles = new ArrayList<>();
        listeStock = new ArrayList<>();
        listeChoixMode = new ArrayList<>();
        listeRef = new ArrayList<>();
        articlesAAjouter = new ArrayList<>();

        entrepotOk = false;

        listeChoixMode.add("Ajout");
        listeChoixMode.add("Modification");

        ArrayAdapter<String> adaptateur =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        listeChoixMode);
        adaptateurAjoutListe = new AjoutListeAdapter(articlesAAjouter);

        choixMode.setAdapter(adaptateur);

        getListeEntrepot();
        getArticles();

        ajoutArticleRecyclerView = findViewById(R.id.ajout_article_recycler);

        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        ajoutArticleRecyclerView.setLayoutManager(gestionnaireLineaire);

        ajoutArticleRecyclerView.setAdapter(adaptateurAjoutListe);

        saisieNomEntrepot.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for(String nomEntrepot : listeEntrepots) {
                    if(!saisieNomEntrepot.getText().toString().equals(nomEntrepot)) {
                        texteErreur.setText("Entrepot incorrect !");
                        entrepotOk = false;
                    } else {
                        texteErreur.setText("Entrepot OK");
                        entrepotOk = true;
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        saisieCodeArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String valeurSaisieArticle = saisieCodeArticle.getText().toString();
                int longueurArticle = valeurSaisieArticle.length();
                ArrayList<String> listeArticlesFiltres = new ArrayList<>();
                ArrayAdapter adapter;

                if(longueurArticle >= 3) {
                    for(String valeur : listeArticles) {
                        if(valeur.toLowerCase().contains(valeurSaisieArticle.toLowerCase())) {
                            listeArticlesFiltres.add(valeur);
                        }
                    }
                    adapter = new ArrayAdapter(AjoutLiseActivity.this,
                            android.R.layout.simple_list_item_1,
                            listeArticlesFiltres);
                    saisieCodeArticle.setAdapter(adapter);
                } else {
                    listeArticlesFiltres.clear();
                    saisieCodeArticle.setAdapter(null);
                }

                int iteration = 0;
                for(String valeur : listeArticles) {
                    if(valeurSaisieArticle.equals(valeur)) {
                        libelleStock.setText(listeStock.get(iteration) + " en stock");
                        return;
                    } else {
                        libelleStock.setText("");
                    }
                    iteration++;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getListeEntrepot() {

        String urlApi = "http://dolistock.go.yo.fr/htdocs/api/index.php/warehouses?api_key=M1K576oo4nZP3DVAW2Qhva1eoN9tak4P";

        OutilAPI.getApiRetour(AjoutLiseActivity.this, urlApi, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String ref = item.getString("ref");
                        // Vous pouvez maintenant utiliser la valeur "ref" comme vous le souhaitez.
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

        String urlApi = "http://dolistock.go.yo.fr/htdocs/api/index.php/products?api_key=M1K576oo4nZP3DVAW2Qhva1eoN9tak4P";

        OutilAPI.getApiRetour(AjoutLiseActivity.this, urlApi, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String label = item.getString("label");
                        String stockReel = item.getString("stock_reel");
                        String refArticle = item.getString("ref");
                        if(stockReel == "null") {
                            stockReel = "0";
                        }
                        // Vous pouvez maintenant utiliser la valeur "ref" comme vous le souhaitez.
                        listeArticles.add(label);
                        listeStock.add(stockReel);
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

        String codeArticle = "";
        int quantite = 0;
        String valeurSaisieArticle = saisieCodeArticle.getText().toString();
        int quantiteSaisie = Integer.parseInt(saisieQuantite.getText().toString()) ;
        int iteration = 0;
        for(String valLabel : listeArticles) {
            if(valeurSaisieArticle.equals(valLabel)) {
                codeArticle = listeRef.get(iteration);
                quantite = Integer.parseInt(listeStock.get(iteration));
            }
            iteration++;
        }

        quantite += quantiteSaisie;

        articlesAAjouter.add(new AjoutListe(valeurSaisieArticle + " (" + codeArticle + ")", quantite + ""));
        adaptateurAjoutListe.notifyDataSetChanged(); // Mise à jour de l'adaptateur après l'ajout
        //refreshListeArticles();
    }

    public void refreshListeArticles() {

    }

    public void supprimerArticle(View view) {
        int position = (int) view.getTag();
        if (position >= 0 && position < articlesAAjouter.size()) {
            articlesAAjouter.remove(position);
            adaptateurAjoutListe.notifyItemRemoved(position);
        }
    }

}

package com.example.saedolistocks5.outilapi;

import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticles;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticlesIdEtNom;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeEntrepotIdEtNom;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeEntrepots;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeInfosArticle;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeRef;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeStock;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeCodeArticleVerif;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeEntrepotsVerif;

import android.content.Context;
import android.util.Pair;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.outilsdivers.Quartet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequetesApi {

    public static void getListeEntrepot(String URLApi, String token, Context context,
                                        String classeAppelante) {

        String urlApi = String.format("http://%s/htdocs/api/index.php/warehouses?api_key=%s", URLApi, token);

        OutilAPI.getApiRetour(context, urlApi,  new OutilAPI.ApiCallback() {

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
                        String id = item.getString("id");
                        if(classeAppelante.equals("AjoutListe")) {
                            listeEntrepotIdEtNom.add(new Pair<>(id, ref));
                        } else if (classeAppelante.equals("Liste")) {
                            listeEntrepotsVerif.add(ref);
                        }
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

    public static void getArticles(String URLApi, String token, Context context,
                                   String classeAppelante) {

        String urlApi = String.format("http://%s/htdocs/api/index.php/products?api_key=%s", URLApi, token);

        OutilAPI.getApiRetour(context, urlApi,  new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String idArticle = item.getString("id");
                        String label = item.getString("label");
                        String refArticle = item.getString("ref");
                        String stockReel = item.getString("stock_reel");
                        // Gère les listes dans AjoutListeActivity
                        if(classeAppelante.equals("AjoutListe")) {
                            listeInfosArticle.add(new Quartet<>(idArticle, label, refArticle,
                                    stockReel.equals("null") ? "0" : stockReel));
                            listeArticlesIdEtNom.add(new Pair<>(idArticle, label));
                        } else if (classeAppelante.equals("Liste")) {
                            listeCodeArticleVerif.add(refArticle);
                        }

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

    public static void GetArticlesByEntrepot(String URLApi, String token, Context context,
                                             String classeAppelante, String idProduit, String idEntrepot,
                                                Quartet<String, String, String, String> quartet) {
        String urlApi = String.format("http://%s/htdocs/api/index.php/products/%s/stock?selected_warehouse_id=%s&api_key=%s",
                URLApi, idProduit, idEntrepot, token);

        OutilAPI.GetApiJsonObject(context, urlApi, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject stockWarehouses = result.getJSONObject("stock_warehouses");
                JSONObject idStock = stockWarehouses.getJSONObject(idEntrepot);
                String stock = idStock.getString("real");
                listeArticles.add(quartet.second());
                listeRef.add(quartet.third());
                listeStock.add(stock);
            }

            @Override
            public void onSuccess(JSONArray result) {
                // Non important pour cette requête
            }

            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse.statusCode == 404) {

                } else {
                    error.printStackTrace();
                }
            }
        });

    }
}

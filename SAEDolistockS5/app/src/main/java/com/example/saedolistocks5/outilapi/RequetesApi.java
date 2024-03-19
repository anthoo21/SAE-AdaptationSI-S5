package com.example.saedolistocks5.outilapi;

import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.adapterSuggestionArticle;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticles;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeArticlesIdEtNom;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeEntrepotIdEtNom;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeInfosArticle;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeRef;
import static com.example.saedolistocks5.pageajoutliste.AjoutListeActivity.listeStock;
import static com.example.saedolistocks5.pageliste.ListeActivity.idArticleVerif;
import static com.example.saedolistocks5.pageliste.ListeActivity.idEntrepotVerif;
import static com.example.saedolistocks5.pageliste.ListeActivity.libelleArticleVerif;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeCodeArticleVerif;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeEntrepotsVerif;
import static com.example.saedolistocks5.pagemodifliste.ModifListeActivity.adapterSuggestionArticleModif;

import android.content.Context;
import android.util.Pair;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.outilsdivers.Quartet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Classe regroupant différentes requêtes vers l'API de dolibarr
 *
 * @author BONNET, FROMENT et ENJALBERT
 * @version 1.0
 */
public class RequetesApi {


    public interface QuantiteCallback {
        void onQuantiteRecuperee(int quantite) throws FileNotFoundException, JSONException;
    }

    public interface InfosArticlesCallback {
        void onInfosArticlesRecup();
    }


    /**
     * Méthode requêtant l'ensemble des entrepôts de dolibarr
     *
     * @param URLApi          URL de l'api
     * @param token           API Key
     * @param context         Context de l'application appelant l'API
     * @param classeAppelante Classe appelant la méthode
     */
    public static void getListeEntrepot(String URLApi, String token, Context context,
                                        String classeAppelante) {

        String urlApi = String.format("http://%s/htdocs/api/index.php/warehouses?api_key=%s", URLApi, token);

        OutilAPI.GetApiJsonArray(context, urlApi,  new OutilAPI.ApiCallback() {

            /**
             * Inutile ici car la méthode ne renvoie jamais un JSONObject
             * @param result le résultat sous forme de JSONObject.
             */
            @Override
            public void onSuccess(JSONObject result) {
                // Rien est renvoyé ici
            }

            /**
             * Résultat correspondant à l'ensemble des entrepôts
             * @param result un resulat sous forme de JSONArray.
             */
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String ref = item.getString("ref");
                        String id = item.getString("id");
                        // Si la classe appelante est "AjoutListe", alors on récupère l'ID et
                        // le nom de l'entrepôt
                        if(classeAppelante.equals("AjoutListe") ||
                                classeAppelante.equals("ModifListe")) {
                            listeEntrepotIdEtNom.add(new Pair<>(id, ref));

                            // Et si la classe appelante est "Liste",
                            // Alors on récupère le nom de l'entrepôt
                        } else if (classeAppelante.equals("Liste")) {
                            listeEntrepotsVerif.add(ref);
                            idEntrepotVerif.add(id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Gère les erreurs de type Volley
             * @param error retourne une erreur de type Volley.
             */
            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    /**
     * Récupère tous les articles
     *
     * @param URLApi          l'url api
     * @param token           le token
     * @param context         le context de l'application appelante
     * @param classeAppelante la classe appelante
     */
    public static void getArticles(String URLApi, String token, Context context,
                                   String classeAppelante,
                                   InfosArticlesCallback callback) {

        String urlApi = String.format("http://%s/htdocs/api/index.php/products?api_key=%s", URLApi, token);

        OutilAPI.GetApiJsonArray(context, urlApi,  new OutilAPI.ApiCallback() {

            /**
             * Inutile ici car la méthode ne renvoie jamais un JSONObject
             * @param result le résultat sous forme de JSONObject.
             */
            @Override
            public void onSuccess(JSONObject result) {
                // Null ici
            }

            /**
             * Résultat correspond à l'ensemble des articles
             * @param result un resulat sous forme de JSONArray.
             */
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String idArticle = item.getString("id");
                        String label = item.getString("label");
                        String refArticle = item.getString("ref");
                        String stockReel = item.getString("stock_reel");
                        // Si la classe appelante est "AjoutListe", alors on récupère toutes les
                        // Infos de tous les articles
                        if(classeAppelante.equals("AjoutListe") ||
                        classeAppelante.equals("ModifListe")) {
                            Quartet<String, String, String, String> infosArticles =
                                    new Quartet<>(idArticle, label, refArticle,
                                            stockReel.equals("null") ? "0" : stockReel);
                            listeInfosArticle.add(infosArticles);


                            listeArticlesIdEtNom.add(new Pair<>(idArticle, label));
                            // Et si la classe appelante est "Liste", alors on récupère
                            // le code de tous les articles
                        } else if (classeAppelante.equals("Liste")) {
                            listeCodeArticleVerif.add(refArticle);
                            idArticleVerif.add(idArticle);
                            libelleArticleVerif.add(label);
                        }

                    }
                    if(callback != null) {
                        callback.onInfosArticlesRecup();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Gère les erreurs de type Volley
             * @param error retourne une erreur de type Volley.
             */
            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Vérifie si un article se trouve dans un entrepôt.
     *
     * @param URLApi          l'url api
     * @param token           le token
     * @param context         le context de l'application appelante
     * @param classeAppelante la classe appelante
     * @param idProduit       l'id du produit
     * @param idEntrepot      l'id de entrepot
     * @param quartet         le quartet
     */
    public static void GetArticlesByEntrepot(String URLApi, String token, Context context,
                                             String classeAppelante, String idProduit, String idEntrepot,
                                             Quartet<String, String, String, String> quartet,
                                             QuantiteCallback callback) {
        String urlApi = String.format("http://%s/htdocs/api/index.php/products/%s/stock?selected_warehouse_id=%s&api_key=%s",
                URLApi, idProduit, idEntrepot, token);

        OutilAPI.GetApiJsonObject(context, urlApi, new OutilAPI.ApiCallback() {

            /**
             * Résultat correspondant à un article d'un entrepot
             * @param result le résultat sous forme de JSONObject.
             * @throws JSONException exception JSON
             */
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject stockWarehouses = result.getJSONObject("stock_warehouses");
                JSONObject idStock = stockWarehouses.getJSONObject(idEntrepot);
                String stock = idStock.getString("real");

                if(classeAppelante.equals("Liste")) {
                    try {
                        callback.onQuantiteRecuperee(Integer.parseInt(stock));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listeArticles.add(quartet.second());
                    listeRef.add(quartet.third());
                    listeStock.add(stock);
                    if(classeAppelante.equals("AjoutListe")) {
                        adapterSuggestionArticle.notifyDataSetChanged();
                    }
                    if(classeAppelante.equals("ModifListe")) {
                        adapterSuggestionArticleModif.notifyDataSetChanged();
                    }
                }
            }

            /**
             * Inutile ici car la méthode ne renvoie jamais un JSONArray
             * @param result le résultat sous forme de JSONArray.
             */
            @Override
            public void onSuccess(JSONArray result) {
                // Non important pour cette requête
            }

            /**
             * Gère les erreurs de type Volley
             * @param error retourne une erreur de type Volley.
             */
            @Override
            public void onError(VolleyError error) {
                // Si le code d'erreur n'est pas 404, cela veut dire que une véritable erreur s'est
                // produite (car le code 404 est renvoyé si aucun article n'appartient à l'entrepôt
                if(error.networkResponse.statusCode != 404) {
                    error.printStackTrace();
                }
            }
        });

    }
}

/**
 * Package de la SAE
 */
package com.example.saedolistocks5.outilapi;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe pour facilitetr l'utilisation de l'API.
 */
public class OutilAPI {

    /**
     * Temps de timeout.
     */
    public static final int TIMEOUT_MS = 5000;
    /**
     * La file d'attente pour les requetes.
     */
    private static RequestQueue fileRequete;

    /**
     * Interface pour appel API.
     */
    public interface ApiCallback {
        /**
         * Méthode en cas de succès.
         * @param result le résultat sous forme de JSONObject.
         */
        void onSuccess(JSONObject result) throws JSONException;

        /**
         * Méthode en cas de succès.
         * @param result un resulat sous forme de JSONArray.
         */
        void onSuccess(JSONArray result);

        /**
         * Méthode en cas d'erreur.
         * @param error retourne une erreur de type Volley.
         */
        void onError(VolleyError error);
    }
    /**
     * Renvoie la file d'attente pour les requêtes Web :
     * - si la file n'existe pas encore : elle est créée puis renvoyée.
     * - si une file d'attente existe déjà : elle est renvoyée.
     * On assure ainsi l'unicité de la file d'attente.
     * @return RequestQueue une file d'attente pour les requêtes Volley.
     */
    private static RequestQueue getFileRequete(Context context) {
        if (fileRequete == null) {
            fileRequete = Volley.newRequestQueue(context);
        }
        // sinon
        return fileRequete;
    }

    /**
     * Fais une requête GET avec un retour de type JSONObject
     * @param context contexte de l'application appelant la méthode
     * @param url URL de l'api à contacter
     * @param callback retour de l'API
     */
    public static void GetApiJsonObject(Context context, String url,
                                        final ApiCallback callback) {
        // Créer une nouvelle requête JSONObject
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * Si l'API renvoie une réponse correct
                     * @param response réponse de l'API sous forme de JSONObject
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Si l'API renvoie une erreur volley
                     * @param error erreur sous forme de VolleyError
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
        //getFileRequete(context).add(requete);
    }

    /**
     * Fais une requête GET avec un retour de type JSONArray
     * @param context le contexte de l'app.
     * @param url l'url de l'API
     * @param callback le callback de l'API.
     */
    public static void GetApiJsonArray(Context context, String url, final ApiCallback callback) {
        // Créer une nouvelle file de requêtes Volley
        // Cas pour les arrays.
        JsonArrayRequest arrayRequete = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    /**
                     * Methode si succès.
                     * @param response renvoie un JSONObject.
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Méthode si erreur
                     * @param error renvoie une erreur volley
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
        getFileRequete(context).add(arrayRequete);

    }

    /**
     * Fais une requête POST avec un retour de type JSONObject
     * @param context context de l'application
     * @param url URL de l'application
     * @param jsonBody body json à envoyer
     * @param callback le callback de l'API.
     */
    public static void PostApiJson(Context context, String url,
                                   JSONObject jsonBody, final ApiCallback callback) {
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    /**
                     * En cas de succès
                     * @param response réponse sous forme de JSONObject
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    /**
                     * En cas d'erreur
                     * @param error réponse sous forme de VolleyError
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérez les erreurs ici
                        callback.onError(error);
                    }
                }
        );
        getFileRequete(context).add(postRequest);
    }
}

package com.example.saedolistocks5.outilapi;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class OutilAPI {

    public interface ApiCallback {
        void onSuccess(JSONObject result);
        void onSuccess(JSONArray result) throws IOException;
        void onError(VolleyError error) throws IOException;
    }

    private static RequestQueue fileRequete;

    /**
     * Renvoie la file d'attente pour les requêtes Web :
     * - si la file n'existe pas encore : elle est créée puis renvoyée
     * - si une file d'attente existe déjà : elle est renvoyée
     * On assure ainsi l'unicité de la file d'attente
     * @return RequestQueue une file d'attente pour les requêtes Volley
     */
    private static RequestQueue getFileRequete(Context context) {
        if (fileRequete == null) {
            fileRequete = Volley.newRequestQueue(context);
        }
        // sinon
        return fileRequete;
    }

    public static int TIMEOUT_MS = 5000;

    public static void getApiRetour(Context context, String url,
                                    JSONArray jsonBody, final ApiCallback callback) {
        // Créer une nouvelle file de requêtes Volley
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            callback.onError(error);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        JsonArrayRequest arrayRequete = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            callback.onSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            callback.onError(error);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        requete.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        getFileRequete(context).add(requete);
        getFileRequete(context).add(arrayRequete);

    }

    public static void GetApiJsonObject(Context context, String url,
                                        final ApiCallback callback) {
        // Créer une nouvelle file de requêtes Volley
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            callback.onError(error);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        getFileRequete(context).add(requete);
    }

    public static void PostApiJson(Context context, String url,
                            JSONObject jsonBody, final ApiCallback callback) {
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérez les erreurs ici
                        try {
                            callback.onError(error);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        getFileRequete(context).add(postRequest);
    }
}


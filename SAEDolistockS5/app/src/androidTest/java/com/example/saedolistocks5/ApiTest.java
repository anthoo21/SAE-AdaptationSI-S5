package com.example.saedolistocks5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.VolleyError;
import com.example.saedolistocks5.outilapi.OutilAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ApiTest {
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private String urlApi = "dolistocktest.go.yo.fr";
    private String user = "u.test";
    private String password = "userDeTestAPI";

    private String apikey = "6yLt95c9HFa96dzeRj5Uwx399ISXDAUe";
  
    @Test
    public void TestLogin(){
        // TODO url ok mais renvoie pas dans on success
        String urlApiEntiere = String.
                format("http://%s/dolibarr-17.0.3/htdocs/api/index.php/login?login=%s&password=%s",
                        urlApi, user, password);
        final boolean[] ok = {false};
        OutilAPI.getApiRetour(appContext, urlApiEntiere, new OutilAPI.ApiCallback() {
            @Test
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject success = null;
                try {
                    success = result.getJSONObject("success");
                    String token = success.getString("token");
                    assertNotNull(success);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onSuccess(JSONArray result) {
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    @Test
    public void GetAPI(){
        // expected récuperation de l'entrepot existant
        String urlApiEntiere = String.
                format("http://%s/dolibarr-17.0.3/htdocs/api/index.php/warehouses?api_key=%s"
                        ,urlApi,apikey);
        OutilAPI.getApiRetour(appContext, urlApiEntiere, new OutilAPI.ApiCallback() {

            @Override
            public void onSuccess(JSONObject result) {
            }
            // TODO je crois on passe pas dedans, peut car l'url pris est celle de l'environnement
            // de base à voir & rien dans data pour faire la méthode
            // CallBack null voir debugger
            // Pb suite à l'installation de Dolibarr donc attention au lien
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        String ref = item.getString("ref");
                        Log.d("TestDebug", "Valeur de ref : " + ref);
                        assertEquals("je suis faux mais ca marche pk", ref);
                       // assertEquals("Entrepot Paris",ref);
                    }
                } catch (JSONException e) {
                    Log.e("TestDebug", "Erreur lors de la récupération " +
                            "de la valeur ref : " + e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

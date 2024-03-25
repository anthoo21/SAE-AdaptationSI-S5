package com.example.saedolistocks5.outilapi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Activité permettant d'utiliser la bibliothèque zxing pour scanner
 * un code barre
 * @author BONNET, ENJALBERT et FROMENT
 */
public class BarcodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise le scanneur de code barre
        new IntentIntegrator(this).initiateScan();
    }

    /**
     * Lorsque le code barre a été scanné, cette classe est appelée
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            // Si le résultat obtenu n'est pas null
            if(result.getContents() != null) {
                // Renvoie le résultat à l'activité précédente
                Intent intent = new Intent();
                // On renvoie le résultat du scan de code barre
                intent.putExtra("SCAN_RESULT", result.getContents());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                // Renvoie à l'activité précédente sans résultat
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

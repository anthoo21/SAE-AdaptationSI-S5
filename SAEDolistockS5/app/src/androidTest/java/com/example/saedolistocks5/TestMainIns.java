package com.example.saedolistocks5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestMainIns {

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void readFichierTestFichierInconnu() throws FileNotFoundException {
        // expected : une exception est lévée si le fichier n'existe pas
        InputStreamReader fichier = null;
        try {
            fichier = new InputStreamReader(appContext.openFileInput("truc.txt"));
            BufferedReader fichiertexte = new BufferedReader(fichier);
        }catch (FileNotFoundException e) { // Si null pas de fichier donc FileNotFound
            assertThrows(NullPointerException.class, (ThrowingRunnable) fichier);
        }
    }
    @Test
    public void readFichierPresent() {
        // Given un fichier
        InputStreamReader fichier;
        /*
        String filePath = "/sdcard/Android/data/data/com.example.saedolistock5/files/infouser.txt";
        // Vérifie si le fichier existe
        File file = new File(filePath);
        assertTrue(file.exists()); */
        // TODO fichier tape sur data/0/user = pas bien

        try {
            File truc = appContext.getFilesDir();
            Log.i("Truc", String.valueOf(truc));
            String autre = appContext.getApplicationInfo().dataDir;
            Log.i("Autre",autre);
            fichier = new InputStreamReader(appContext.openFileInput("infouser.txt"));
            BufferedReader fichiertexte = new BufferedReader(fichier);
            // expected : On peut lire le fichier.
            assertNotNull(fichiertexte.readLine());
        } catch (FileNotFoundException e) {
            // pas important
        } catch (IOException e) {
            //
        }

    }

    /**
     * Test pour savoir si la connexion est présente
     */
    @Test
    public void TestConnexion() {
        // Given une connexion
        ConnectivityManager connManager =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());
        // expected la connexion est presente
        assertTrue(mWifi != null);
    }
}

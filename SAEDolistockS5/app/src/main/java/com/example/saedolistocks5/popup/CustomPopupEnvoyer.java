package com.example.saedolistocks5.popup;

import static com.example.saedolistocks5.pageliste.ListeActivity.adaptateur;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeAccueil;
import static com.example.saedolistocks5.pageliste.ListeActivity.positionItemListe;
import static com.example.saedolistocks5.popup.CustomPopupAfficherMenu.nomFichier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageliste.ListeActivity;

public class CustomPopupEnvoyer extends AlertDialog {

    private final Activity activity;

    protected CustomPopupEnvoyer(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dialog content view
        setContentView(R.layout.popup_validation_envoyer);

        // Get the buttons and set their click listeners
        Button menuButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(this::OnClickEnvoyer);

    }
    public static CustomPopupEnvoyer createDialog(Context context, Activity activity) {
        CustomPopupEnvoyer dialog = new CustomPopupEnvoyer(context, activity);
        return dialog;
    }

    public void OnClickEnvoyer(View view) {
        activity.deleteFile(nomFichier);
        if(!listeAccueil.isEmpty()) {
            listeAccueil.remove(positionItemListe);
            adaptateur.notifyItemRemoved(positionItemListe);
            activity.recreate();
        }
        dismiss(); // Dismiss the dialog
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity.deleteFile(nomFichier);
        if(!listeAccueil.isEmpty()) {
            listeAccueil.remove(positionItemListe);
            adaptateur.notifyItemRemoved(positionItemListe);
            activity.recreate();
        }
        dismiss(); // Dismiss the dialog
    }
}

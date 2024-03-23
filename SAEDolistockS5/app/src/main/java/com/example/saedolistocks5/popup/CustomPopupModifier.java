package com.example.saedolistocks5.popup;

import static com.example.saedolistocks5.pageliste.ListeActivity.adaptateur;
import static com.example.saedolistocks5.pageliste.ListeActivity.listeAccueil;
import static com.example.saedolistocks5.pageliste.ListeActivity.positionItemListe;
import static com.example.saedolistocks5.popup.CustomPopupAfficherMenu.nomFichier;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageliste.ListeActivity;
import com.example.saedolistocks5.pagemodifliste.ModifListeActivity;

public class CustomPopupModifier extends AlertDialog {

    protected CustomPopupModifier(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dialog content view
        setContentView(R.layout.popup_validation_modification);

        // Get the buttons and set their click listeners
        Button menuButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog

                // On retourne sur la page de visualisation de toutes les listes
                Intent intention = new Intent(getContext(), ListeActivity.class);
                intention.putExtra("PAGE", "Modif");
                getContext().startActivity(intention);
            }
        });
    }
    public static CustomPopupModifier createDialog(Context context) {
        CustomPopupModifier dialog = new CustomPopupModifier(context);
        return dialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss(); // Dismiss the dialog

        // On retourne sur la page de visualisation de toutes les listes
        Intent intention = new Intent(getContext(), ListeActivity.class);
        intention.putExtra("PAGE", "Modif");
        getContext().startActivity(intention);
    }
}

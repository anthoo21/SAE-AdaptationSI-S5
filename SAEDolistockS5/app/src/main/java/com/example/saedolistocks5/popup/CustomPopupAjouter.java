package com.example.saedolistocks5.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageajoutliste.AjoutListeActivity;
import com.example.saedolistocks5.pageliste.ListeActivity;

public class CustomPopupAjouter extends AlertDialog {


    protected CustomPopupAjouter(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dialog content view
        setContentView(R.layout.popup_validation_ajouter);

        // Get the buttons and set their click listeners
        Button menuButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog

                // On retourne sur la page de visualisation de toutes les listes
                Intent intention = new Intent(getContext(), ListeActivity.class);
                intention.putExtra("PAGE", "Ajout");
                getContext().startActivity(intention);
            }
        });
    }
    public static CustomPopupAjouter createDialog(Context context) {
        CustomPopupAjouter dialog = new CustomPopupAjouter(context);
        return dialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss(); // Dismiss the dialog

        // On retourne sur la page de visualisation de toutes les listes
        Intent intention = new Intent(getContext(), ListeActivity.class);
        intention.putExtra("PAGE", "Ajout");
        getContext().startActivity(intention);
    }
}

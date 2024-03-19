package com.example.saedolistocks5.popup;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.deleteIfExists;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageliste.ListeActivity;

import java.util.ArrayList;

public class CustomPopupSuppression extends AlertDialog {

    /**
     * Liste des listes de l'utilisateur courant
     */
    static ArrayList<String> listeFichierUser;

    /**
     * Position de l'item liste
     */
    private int positionItemListe;

    protected CustomPopupSuppression(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dialog content view
        setContentView(R.layout.popup_validation_supp);

        // Get the buttons and set their click listeners
        Button menuButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog
                //ListeActivity.supprimerListe();
            }
        });
    }
    public static CustomPopupSuppression createDialog(Context context) {
        CustomPopupSuppression dialog = new CustomPopupSuppression(context);
        return dialog;
    }
}

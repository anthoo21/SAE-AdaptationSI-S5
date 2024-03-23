package com.example.saedolistocks5.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;
import com.example.saedolistocks5.pageliste.ListeActivity;

public class CustomPopupConfirmationSupp extends AlertDialog {

    private final ListeActivity listeActivity;

    protected CustomPopupConfirmationSupp(Context context, ListeActivity listeActivity) {
        super(context);
        this.listeActivity = listeActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dialog content view
        setContentView(R.layout.popup_confirmation_supp);

        // Get the buttons and set their click listeners
        Button cancelButton = findViewById(R.id.cancelButton);
        Button validateButton = findViewById(R.id.validateButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog
                CustomPopupSuppression dialog = CustomPopupSuppression.createDialog(getContext(), listeActivity);
                dialog.show();
            }
        });
    }

    public static CustomPopupConfirmationSupp createDialog(Context context, ListeActivity listeActivity) {
        CustomPopupConfirmationSupp dialog = new CustomPopupConfirmationSupp(context, listeActivity);
        return dialog;
    }

}
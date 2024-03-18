package com.example.saedolistocks5.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saedolistocks5.R;

public class CustomPopupConfirmationSupp extends AlertDialog {

    protected CustomPopupConfirmationSupp(Context context) {
        super(context);
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
                CustomPopupSuppression dialog = CustomPopupSuppression.createDialog(getContext());
                // Utilisation du contexte de l'activité
                dialog.show();
            }
        });
    }

    public static CustomPopupConfirmationSupp createDialog(Context context) {
        CustomPopupConfirmationSupp dialog = new CustomPopupConfirmationSupp(context);
        return dialog;
    }
}

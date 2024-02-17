package com.project.thelibrarians_lso2324.activities;

import static com.project.thelibrarians_lso2324.utils.Utils.showAlert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.thelibrarians_lso2324.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ImageButton backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        Button confermaButton = findViewById(R.id.button);

        confermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText currentPasswordEditText = findViewById(R.id.current_password_edittext);
                EditText newPasswordEditText = findViewById(R.id.new_password_edittext);
                EditText confirmNewPasswordEditText = findViewById(R.id.confirm_new_password_edittext);

                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString().trim();
                String currentPassword = currentPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword) || TextUtils.isEmpty(currentPassword) ) {
                    // Almeno uno dei campi è vuoto
                    showErrorDialog();

                } else if (newPassword.equals(confirmNewPassword)) {
                    // Le due password corrispondono

                } else {
                    // Le due password non corrispondono
                    // Mostra una finestra di dialogo di avviso
                    showPasswordMismatchDialog();
                }
            }
        });


    }

    private void showConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conferma");
        builder.setMessage("Sei sicuro di voler tornare indietro? Perderai i dati inseriti.");

        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se l'utente conferma, vai alla schermata precedente
                Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se l'utente annulla, chiudi semplicemente il dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPasswordMismatchDialog() {
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Errore");
        builder.setMessage("Le password non corrispondono. Per favore, riprova.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chiudi semplicemente la finestra di dialogo
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();*/
        showAlert(this, "Errore", "Le password non corrispondono. Per favore, riprova.");
    }

    private void showErrorDialog() {
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Errore");
        builder.setMessage("Uno dei  campi è vuoto.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chiudi semplicemente la finestra di dialogo
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show(); */

        showAlert(this , "Errore", "Uno dei  campi è vuoto.");
    }
}

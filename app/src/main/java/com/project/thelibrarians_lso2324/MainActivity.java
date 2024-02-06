package com.project.thelibrarians_lso2324;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.project.thelibrarians_lso2324.RestClient.RestClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.login_button);

        btnLogin.setOnClickListener( v -> setContentView(R.layout.activity_main));

        // Effettua la richiesta a /api/login
        String endpoint = "/api/";
        String method = "GET";
        String bearerToken = "il_tuo_bearer_token"; // Imposta il bearer token corretto
        String body = null; // Non c'è bisogno di un body per una richiesta GET

        try {
            String response = RestClient.getInstance().sendRequest(method, endpoint, bearerToken, body);
            // Stampa la risposta in un Toast
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            // Stampa l'errore in un Toast
            Toast.makeText(this, "Errore nella richiesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
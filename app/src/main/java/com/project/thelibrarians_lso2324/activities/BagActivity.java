package com.project.thelibrarians_lso2324.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BagActivity extends AppCompatActivity {

    private static final int K = 5; // Sostituisci con il valore desiderato di k

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Tasto Torna Indietro
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Chiude l'activity e torna indietro
            }
        });

        // Contenitore per gli elementi nel carrello
        LinearLayout carrelloContainer = findViewById(R.id.carrelloContainer);

        // Simulazione di dati del carrello (da 0 a k libri)
        for (int i = 0; i < K; i++) {
            // Layout orizzontale per ogni elemento nel carrello
            LinearLayout carrelloItemLayout = new LinearLayout(this);
            carrelloItemLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Immagine del libro
            ImageView bookImageView = new ImageView(this);
            // Imposta l'immagine del libro tramite un metodo o un'API appropriata
            // bookImageView.setImageResource(R.drawable.ic_book_placeholder); // Sostituisci con l'immagine reale

            // Titolo del libro
            TextView bookTitleTextView = new TextView(this);
            bookTitleTextView.setText("Titolo del Libro " + (i + 1));

            // Aggiungi immagine e titolo al layout
            carrelloItemLayout.addView(bookImageView);
            carrelloItemLayout.addView(bookTitleTextView);

            // Aggiungi il layout dell'elemento al contenitore del carrello
            carrelloContainer.addView(carrelloItemLayout);
        }

        // Tasto Checkout
        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (K > 0) {
                    // Implementa l'azione di checkout solo se ci sono libri nel carrello
                    // Altrimenti, mostra un messaggio di errore
                    // Esempio: startActivity(new Intent(CheckoutActivity.this, PagamentoActivity.class));
                } else {
                    // Mostra un messaggio di errore se non ci sono libri nel carrello
                    TextView errorTextView = new TextView(BagActivity.this);
                    errorTextView.setText("Errore: Nessun libro nel carrello.");
                    carrelloContainer.addView(errorTextView);
                }
            }
        });
    }
}

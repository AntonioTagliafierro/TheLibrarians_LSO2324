package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.thelibrarians_lso2324.R;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
    }
    // Tasto Torna Indietro
    ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();  // Chiude l'activity e torna indietro
        }
    });

    // Immagine del Libro
    ImageView bookImageView = findViewById(R.id.bookImageView);
    // Imposta l'immagine del libro tramite un metodo o un'API appropriata

    // Informazioni del Libro
    TextView titleTextView = findViewById(R.id.titleTextView);
    TextView authorTextView = findViewById(R.id.authorTextView);
    TextView genreTextView = findViewById(R.id.genreTextView);
    TextView descriptionTextView = findViewById(R.id.descriptionTextView);
    TextView isbnTextView = findViewById(R.id.isbnTextView);
    TextView copiesTextView = findViewById(R.id.copiesTextView);

    // Simulazione di dati del libro (sostituisci con i dati reali)
        titleTextView.setText("Titolo del Libro");
        authorTextView.setText("Nome dell'Autore");
        genreTextView.setText("Genere del Libro");
        descriptionTextView.setText("Breve descrizione del libro...");
        isbnTextView.setText("ISBN: 123456789");
        copiesTextView.setText("Copie disponibili: 50");

    // Tasto Aggiungi alla Bag
    Button addToBagButton = findViewById(R.id.addToBagButton);
        addToBagButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Implementa l'aggiunta del libro alla "bag" o al carrello
            // Puoi implementare questa funzionalità come desideri
        }
    });
}
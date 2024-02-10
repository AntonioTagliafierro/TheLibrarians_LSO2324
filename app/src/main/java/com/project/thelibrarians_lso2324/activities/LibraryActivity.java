package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.utils.BookAdapter;
public class LibraryActivity extends AppCompatActivity {

    private GridView gridView;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private GridLayout bookGridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // Ottenere il riferimento al GridLayout
        bookGridLayout = findViewById(R.id.bookGridLayout);

        // Esempio di caricamento dinamico di miniature
        bookList = generateSampleBookList(); // Sostituisci questa chiamata con la logica per ottenere la lista dei libri

        // Creazione della griglia
        populateGridLayout();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_list, menu);

        // Configura la barra di ricerca
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filtra la lista in base alla query di ricerca
                bookAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra la lista mentre l'utente digita
                bookAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                // Logica per gestire il click sul tasto di ordinamento
                Collections.sort(bookList, new Comparator<Book>() {
                    @Override
                    public int compare(Book book1, Book book2) {
                        // Sostituisci questa logica con il tuo criterio di ordinamento desiderato
                        return book1.getTitle().compareToIgnoreCase(book2.getTitle());
                    }
                });
                bookAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_filter:
                // Logica per gestire il click sul tasto di filtro per genere
                // Implementa un dialog o un altro metodo per selezionare il genere desiderato
                // e filtra la lista di conseguenza
                // ...

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
        private void populateGridLayout() {
            // Pulire la griglia prima di ricrearla
            bookGridLayout.removeAllViews();

            // Creazione della griglia con due colonne
            for (int i = 0; i < bookList.size(); i++) {
                // Creare una View per ogni elemento della griglia
                View bookItemView = getLayoutInflater().inflate(R.layout.book_item, null);

                // Ottenere riferimenti agli elementi della vista
                ImageView bookImageView = bookItemView.findViewById(R.id.bookImageView);
                TextView bookTitleTextView = bookItemView.findViewById(R.id.bookTitleTextView);
                Button addToCartButton = bookItemView.findViewById(R.id.addToCartButton);

                // Ottieni il libro corrente
                final Book currentBook = bookList.get(i);

                // Imposta i dati del libro nella vista
                bookImageView.setImageResource(currentBook.getImageResourceId());
                bookTitleTextView.setText(currentBook.getTitle());

                // Configurare il pulsante Aggiungi al carrello
                addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Logica per aggiungere il libro al carrello
                        // Esempio: mostra un messaggio di avviso
                        showToast("Libro aggiunto al carrello: " + currentBook.getTitle());
                    }
                });

                // Aggiungere la vista alla griglia
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(i % 2 == 0 ? 0 : 1); // 2 colonne, alternando
                params.setGravity(Gravity.CENTER);

                bookItemView.setLayoutParams(params);
                bookGridLayout.addView(bookItemView);
            }
        }

        // Metodo di esempio per generare una lista di libri di prova
        private ArrayList<Book> generateSampleBookList() {
            ArrayList<Book> books = new ArrayList<>();
            // Aggiungi libri di esempio
            // ...

            return books;
        }

        // Metodo di utilità per mostrare un toast
        private void showToast(String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}

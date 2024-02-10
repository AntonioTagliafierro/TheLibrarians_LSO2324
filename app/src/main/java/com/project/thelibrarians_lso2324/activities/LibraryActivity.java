package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        gridView = findViewById(R.id.bookGridLayout); // Ottenere il riferimento al GridLayout
        bookList = generateSampleBookList(); // Sostituisci questa chiamata con la logica per ottenere la lista dei libri

        // Inizializza l'adattatore
        bookAdapter = new BookAdapter(this, bookList);
        gridView.setAdapter(bookAdapter);

        // Esempio di caricamento dinamico di miniature nel GridLayout
        for (int i = 0; i < bookList.size(); i++) {
            // Creare una ImageView per ogni miniatura del libro
            ImageView imageView = new ImageView(this);
            // Sostituisci con la tua logica per ottenere la risorsa dell'immagine del libro
            imageView.setImageResource(bookList.get(i).getImageResourceId());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Aggiungere ImageView al GridLayout
            gridView.addView(imageView);
        }
    }

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

    // Metodo di esempio per generare una lista di libri di prova
    private ArrayList<Book> generateSampleBookList() {
        // Implementa la tua logica per ottenere la lista dei libri
        // ...
    }
}

package com.project.thelibrarians_lso2324.activities;

import static com.project.thelibrarians_lso2324.R.id.action_filter;
import static com.project.thelibrarians_lso2324.R.id.action_sort;

import com.project.thelibrarians_lso2324.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.events.BookFetchEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.model.BookGenre;
import com.project.thelibrarians_lso2324.utils.BookAdapter;
import com.project.thelibrarians_lso2324.viewmodel.LibraryViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LibraryActivity extends AppCompatActivity {

    private GridView gridView;
    private BookAdapter bookAdapter;
    private List<Book> books;
    private List<Book> filteredBooks;
    private LibraryViewModel model;
    private BookGenre selectedGenre = BookGenre.SCONOSCIUTO;

    private Spinner genreSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        gridView = findViewById(R.id.bookGridLayout);
        genreSpinner = findViewById(R.id.genreSpinner); // Inizializza il genreSpinner

        model = new ViewModelProvider(this).get(LibraryViewModel.class);

        books = new ArrayList<>(Objects.requireNonNull(model.getBooks().getValue()));
        filteredBooks = new ArrayList<>(books);

        bookAdapter = new BookAdapter(this, filteredBooks);

        // Inizializza l'adattatore con la griglia
        gridView.setAdapter(bookAdapter);

        // Gestione del clic sugli elementi della griglia
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = (Book) parent.getItemAtPosition(position);
            showToast(selectedBook.getTitle());

            // Esegui le azioni desiderate con l'ISBN del libro selezionato
            String selectedIsbn = selectedBook.getISBN();
            // Ad esempio, puoi passare l'ISBN all'attività di dettaglio del libro
            Intent intent = new Intent(LibraryActivity.this, BookDetailsActivity.class);
            intent.putExtra("ISBN", selectedIsbn);
            startActivity(intent);
        });

        populateGridLayout();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void populateGridLayout() {
        bookAdapter.notifyDataSetChanged(); // Aggiorna la griglia con i dati aggiornati
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (bookAdapter != null) {
                    bookAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (bookAdapter != null) {
                    bookAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == action_sort) {
            sortBooksByAvailability();
            return true;
        } else if (itemId == action_filter) {
            applyGenreFilter();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void sortBooksByAvailability() {
        Collections.sort(filteredBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                return Integer.compare(book1.getAvailableCopies(), book2.getAvailableCopies());
            }
        });
        populateGridLayout();
    }

    private void applyGenreFilter() {

        selectedGenre = BookGenre.valueOf(genreSpinner.getSelectedItem().toString());
        populateFilteredBooks();
        populateGridLayout();
    }

    private void populateFilteredBooks() {
        filteredBooks.clear();

        for (Book book : books) {
            if (book.getGenre() == selectedGenre && book.getAvailableCopies() > 0) {
                filteredBooks.add(book);
            }
        }
    }
    @Subscribe // potrebbe essere inutile andare a prendere solo quelli col genere in quanto già la funzione applyGenreFilter
    // dovrebbe popolare con i soli libri che hanno il genere scelto
    public void onDrinksFetched(BookFetchEvent event) {
        if (selectedGenre != null) {
            event.getBooks().removeIf(book -> !book.getGenre().equals(selectedGenre));
        }

        model.getBooks().setValue(event.getBooks());
        model.setDefaultBooks(event.getBooks());
        gridView.setAdapter(new BookAdapter(this, model.getBooks().getValue()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}

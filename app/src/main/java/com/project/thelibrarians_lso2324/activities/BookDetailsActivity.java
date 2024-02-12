package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.controller.Controller;
import com.project.thelibrarians_lso2324.events.BookImageEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.utils.BookGenreUtils;
import com.project.thelibrarians_lso2324.viewmodel.BookDetailsActivityViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {

    private int availableCopies;

    private ImageView bookImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        BookDetailsActivityViewModel model = new ViewModelProvider(this).get(BookDetailsActivityViewModel.class);

        // Tasto Torna Indietro
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                finish();  // Chiude l'activity e torna indietro
            }
        });


        Controller controller = Controller.getInstance();


        // Informazioni del Libro
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView authorTextView = findViewById(R.id.authorTextView);
        TextView genreTextView = findViewById(R.id.genreTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView isbnTextView = findViewById(R.id.isbnTextView);
        TextView copiesTextView = findViewById(R.id.copiesTextView);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_detail);
        FloatingActionButton addBag = findViewById(R.id.addToBagButton);

        Book chosenBook = (Book) Objects.requireNonNull(getIntent().getExtras().getSerializable("book"));

        if(chosenBook == null){
            throw new RuntimeException("Book not found");
        }

        controller.getBookDao().getBookImage(chosenBook.getISBN(), this);

        // Immagine del Libro
        bookImageView = findViewById(R.id.bookImageView);
        // Imposta l'immagine del libro tramite un metodo o un'API appropriata

        availableCopies = chosenBook.getTotalCopies() - chosenBook.getCopiesOnLendLease();

        titleTextView.setText(chosenBook.getTitle());
        authorTextView.setText(chosenBook.getAuthors());
        genreTextView.setText(BookGenreUtils.getDisplayName(chosenBook.getGenre()));
        descriptionTextView.setText(chosenBook.getDescription());
        isbnTextView.setText(chosenBook.getISBN());
        copiesTextView.setText(availableCopies);



        addBag.setOnClickListener(v -> {
            if (availableCopies > 0) {
                model.addBookInBag(chosenBook);
                Snackbar.make(coordinatorLayout, String.format("%s aggiunto al carrello", chosenBook.getTitle()), Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(coordinatorLayout, "Copie terminate", Snackbar.LENGTH_SHORT).show();
            }
        });




    }

    @Subscribe
    public void onDrinkImageEvent(BookImageEvent bookImageEvent) {
        Bitmap image = bookImageEvent.image;
        bookImageView.setImageBitmap(image);

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
package com.project.thelibrarians_lso2324.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.model.Book; // Assicurati di avere l'import corretto
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookBagAdapter extends RecyclerView.Adapter<BookBagAdapter.BookViewHolder> {

    private List<Book> books;
    private Context context;

    public BookBagAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if (books.isEmpty()) {
            // Se la lista è vuota, mostra un messaggio di errore
            holder.titleTextView.setText("Nessun libro nel carrello.");
            // Aggiungi altre impostazioni dell'interfaccia utente in base alle tue esigenze
        } else {
            // Se la lista contiene elementi, mostra i dettagli del libro
            Book book = books.get(position);
            holder.titleTextView.setText(book.getTitle());
            Picasso.get().load(book.getCoverUrl()).placeholder(R.drawable.placeholder_book_image).into(holder.bookImageView);
            // Aggiungi altre impostazioni dell'interfaccia utente in base alle tue esigenze
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView;


        public BookViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.bookTitleTextView);
            // Inizializza altre viste dell'elemento qui in base alle tue esigenze
        }
    }
}

package com.project.thelibrarians_lso2324.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.model.Book;
import java.util.ArrayList;
import java.util.Locale;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> bookList;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }


    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        }

        // Ottieni riferimenti agli elementi della vista
        ImageView bookImageView = convertView.findViewById(R.id.bookImageView);
        TextView bookTitleTextView = convertView.findViewById(R.id.bookTitleTextView);

        // Ottieni il libro corrente
        Book currentBook = (Book) getItem(position);

        // Imposta i dati del libro nella vista
        bookImageView.setImageResource(currentBook.getImageResourceId());
        bookTitleTextView.setText(currentBook.getTitle());

        return convertView;
    }

    public Locale getFilter() {
        return null;
    }
}

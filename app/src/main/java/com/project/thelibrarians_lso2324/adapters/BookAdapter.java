package com.project.thelibrarians_lso2324.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Book> originalBooks; // Libri originali senza filtro
    private List<Book> filteredBooks; // Libri attualmente visualizzati dopo il filtro
    private LayoutInflater inflater;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.originalBooks = books;
        this.filteredBooks = new ArrayList<>(books);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.book_item, parent, false);
        }

        ImageView bookImageView = view.findViewById(R.id.bookImageView);
        TextView bookTitleTextView = view.findViewById(R.id.bookTitleTextView);

        Book book = (Book) getItem(position);

        bookImageView.setImageResource(book.getImageResourceId());
        bookTitleTextView.setText(book.getTitle());

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Book> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalBooks);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Book book : originalBooks) {
                        if (book.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(book);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBooks.clear();
                filteredBooks.addAll((List<Book>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}

package com.project.thelibrarians_lso2324.daos;

import android.content.Context;

import com.android.volley.Response;
import com.project.thelibrarians_lso2324.model.Book;

import org.json.JSONObject;

import java.util.List;



public interface AdminBookInterface {

    // This method is used to get all the books from the database
    public List<Book> getBooks(Context context);

    // This method is used to get all the new releases book from the database
    public List<Book> getNewReleases(Context context);

    //This method is used to get all most popular books from the database
    public List<Book> getMostPopular(Context context);

    // This method is used to get a specific book from the database
    public Book getBook(String isbn, Context context, Response.Listener<JSONObject> listener);

    // This method is used to get the image of a specific book from the database
     public void getBookImage(String isbn, Context context);

        // This method is used to add a book to the database
    public void addBook(Book book, Context context);

    // This method is used to update a book in the database
    public void updateBook(Book book, Context context);

    // This method is used to delete a book from the database
    public void deleteBook(String isbn, Context context);
}

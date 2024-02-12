package com.project.thelibrarians_lso2324.daos;

import android.content.Context;

import com.android.volley.Response;
import com.project.thelibrarians_lso2324.model.Book;

import org.json.JSONObject;

import java.util.List;

public interface BookDaoInterface {
    public List<Book> getBooks(Context context);

    public Book getBook(String isbn, Context context, Response.Listener<JSONObject> listener);

    public void getBookImage(String isbn, Context context);
}

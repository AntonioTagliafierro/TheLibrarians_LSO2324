package com.project.thelibrarians_lso2324.daos;

import static com.project.thelibrarians_lso2324.utils.Utils.API_BASE_URL;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.project.thelibrarians_lso2324.events.BookFetchEvent;
import com.project.thelibrarians_lso2324.events.BookImageEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.utils.LoginManager;
import com.project.thelibrarians_lso2324.utils.RequestSender;
import com.project.thelibrarians_lso2324.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

public class BookDao implements BookDaoInterface{

    public List<Book> getBooks(Context context) {

        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "books", Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;
    }

    @Override
    public Book getBook(String isbn, Context context, Response.Listener<JSONObject> listener) {
        String token = LoginManager.getInstance().getTokenPayload().rawToken;
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "drinks" + isbn, Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;    }

    @Override
    public void getBookImage(String isbn, Context context) {

        RequestSender.sendRequestForImage(context, API_BASE_URL + "book/image/" + isbn, Request.Method.GET, null, new Pair<>(1000,300),new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookImageEvent(response, isbn));
        }, System.out::println));

    }
}

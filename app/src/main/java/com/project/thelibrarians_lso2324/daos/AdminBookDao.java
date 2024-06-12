package com.project.thelibrarians_lso2324.daos;

import static com.project.thelibrarians_lso2324.utils.Utils.API_BASE_URL;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.project.thelibrarians_lso2324.events.BookAddEvent;
import com.project.thelibrarians_lso2324.events.BookFetchEvent;
import com.project.thelibrarians_lso2324.events.BookImageEvent;
import com.project.thelibrarians_lso2324.events.DeleteBook;
import com.project.thelibrarians_lso2324.events.EditBookEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.utils.LoginManager;
import com.project.thelibrarians_lso2324.utils.RequestSender;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;





public class AdminBookDao implements AdminBookInterface {


    // This method is used to get all the books from the database
    public List<Book> getBooks(Context context) {

        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "books", Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;
    }
    // This method is used to get all the new releases book from the database
    public List<Book> getNewReleases(Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "books/new", Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;
    }
    //This method is used to get all most popular books from the database
    public List<Book> getMostPopular(Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "books/popular", Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;
    }


    // This method is used to get a specific book from the database
    @Override
    public Book getBook(String isbn, Context context, Response.Listener<JSONObject> listener) {
        String token = LoginManager.getInstance().getTokenPayload().rawToken;
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "books" + isbn, Request.Method.GET, null,new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));

        return null;    }


    // This method is used to get the image of a specific book from the database
    @Override
    public void getBookImage(String isbn, Context context) {

        RequestSender.sendRequestForImage(context, API_BASE_URL + "book/image/" + isbn, Request.Method.GET, null, new Pair<>(1000,300),new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookImageEvent(response, isbn));
        }, System.out::println));

    }

    // This method is used to add a book to the database
    public void addBook(Book book, Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "book", Request.Method.POST, null , new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new BookAddEvent(book));
        }, System.out::println));
    }
// This method is used to edit a book in the database
    public void updateBook(Book book, Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "book", Request.Method.PUT, null , new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new EditBookEvent(book));
        }, System.out::println));
    }

    // This method is used to delete a book from the database
    public void deleteBook(String isbn, Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "book/" + isbn, Request.Method.DELETE, null , new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new DeleteBook(isbn));
        }, System.out::println));
    }


}

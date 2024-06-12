package com.project.thelibrarians_lso2324.daos;



import static com.project.thelibrarians_lso2324.utils.Utils.API_BASE_URL;

import android.content.Context;

import com.android.volley.Request;
import com.project.thelibrarians_lso2324.events.FantasyBooksFetchEvent;
import com.project.thelibrarians_lso2324.events.HorrorBooksFetchEvent;
import com.project.thelibrarians_lso2324.events.RomanticBooksFetchEvent;
import com.project.thelibrarians_lso2324.events.SciFiBooksFetchEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.utils.RequestSender;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MostPopularCategoriesDao implements MostPopularCategoriesInterface {

    // This method is used to get romantic books
    public List<Book> getRomanticBooks(Context context) {

        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "romanticbooks", Request.Method.GET, null, new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new RomanticBooksFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));
        return null;
    }

    // This method is used to get horror books
    public List<Book> getHorrorBooks(Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "horrorbooks", Request.Method.GET, null, new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new HorrorBooksFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));
        return null;
    }

    // This method is used to get fantasy books
    public List<Book> getFantasyBooks(Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "fantasybooks", Request.Method.GET, null, new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new FantasyBooksFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));
        return null;
    }

    // This method is used to get science fiction books
    public List<Book> getScienceFictionBooks(Context context) {
        RequestSender.sendRequestForJsonArray(context, API_BASE_URL + "sciencefictionbooks", Request.Method.GET, null, new RequestSender.RequestListeners<>(response -> {
            EventBus.getDefault().post(new SciFiBooksFetchEvent(Book.fromJsonArray(response)));
        }, System.out::println));
        return null;

    }
}

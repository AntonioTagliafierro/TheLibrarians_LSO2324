package com.project.thelibrarians_lso2324.daos;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.project.thelibrarians_lso2324.events.BookBagPushErrorEvent;
import com.project.thelibrarians_lso2324.events.BookBagPushedEvent;
import com.project.thelibrarians_lso2324.events.FetchLendLeaseFromServerEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.model.BookBag;
import com.project.thelibrarians_lso2324.utils.LoginManager;
import com.project.thelibrarians_lso2324.utils.RequestSender;
import com.project.thelibrarians_lso2324.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LendLeaseDao implements LendLeaseDaoInterface{


    public void pushBookBagToServer(BookBag bookBag, Context context) {
        if (bookBag.getBooksMap().isEmpty())
            return;



        String token = LoginManager.getInstance().getTokenPayload().rawToken;
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "Bearer " + token);

            bookBag.getBooksMap().forEach((book, amount) -> {
            JSONObject body = new JSONObject();

            try {
                body.put("isbn_book", book.getISBN());
                body.put("quantity", amount);

                RequestSender.sendRequestForString(context, Utils.API_BASE_URL + "lendlease/book", Request.Method.POST, body, headers, new RequestSender.RequestListeners<>(
                        response -> {
                            EventBus.getDefault().post(new BookBagPushedEvent());
                        },
                        error -> {
                            throw new RuntimeException(error.getMessage());
                        }
                ));

            } catch (JSONException e) {
                EventBus.getDefault().post(new BookBagPushErrorEvent("Error while parsing JSON"));
            } catch (RuntimeException e) {
                EventBus.getDefault().post(new BookBagPushErrorEvent(e.getMessage()));
            }


        });

    }

    @Override
    public void fetchLendLeaseFromServer(Context context) {
        String token = LoginManager.getInstance().getTokenPayload().rawToken;
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "Bearer " + token);

        RequestSender.sendRequestForJsonObject(context, Utils.API_BASE_URL + "lendlease/last", Request.Method.GET, null, headers, new RequestSender.RequestListeners<>(

                response -> {
                    List<Book> booksList = new ArrayList<>();

                    JSONArray books = response.optJSONArray("books");

                    if (books == null) {
                        EventBus.getDefault().post(new FetchLendLeaseFromServerEvent(booksList));
                        return;
                    }

                    Stream.iterate(0, i -> i + 1).limit(books.length()).forEach(i -> {
                        JSONObject book = books.optJSONObject(i);
                        booksList.add(Book.fromJSON(book, "isbn"));
                    });

                    EventBus.getDefault().post(new FetchLendLeaseFromServerEvent(booksList));
                },
                error -> {
                    EventBus.getDefault().post(new FetchLendLeaseFromServerEvent(new ArrayList<>()));
                }
        ));
    }
}



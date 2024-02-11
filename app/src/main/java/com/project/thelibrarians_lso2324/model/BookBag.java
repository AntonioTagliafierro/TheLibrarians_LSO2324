package com.project.thelibrarians_lso2324.model;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.project.thelibrarians_lso2324.events.BagUpdateEvent;

public class BookBag {

    private Map<Book, Integer> booksInBag;


    public BookBag() {
        booksInBag = new HashMap<>();
    }
    public int getSize() {
        return booksInBag.size();
    }

    public int getTotalSize() {
        return booksInBag.values().stream()
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .sum();
    }


    public Map<Book, Integer> getBooksMap() {
        return booksInBag;
    }

    public List<Book> getBooksList() {
        return new ArrayList<>(booksInBag.keySet());
    }

    public void clear() {
        booksInBag.clear();
    }

    public void addBook(Book book) {
        booksInBag.put(book, booksInBag.getOrDefault(book, 0) + 1);
        EventBus.getDefault().post(new BagUpdateEvent(getTotalSize()));
    }

    public void removeAmountOfBook(Book book, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }

        if (booksInBag.containsKey(book)) {
            int newAmount = booksInBag.get(book) - amount;
            if (newAmount <= 0) {
                booksInBag.remove(book);
            } else {
                booksInBag.put(book, newAmount);
            }
        }

        EventBus.getDefault().post(new BagUpdateEvent(getTotalSize()));
    }
}

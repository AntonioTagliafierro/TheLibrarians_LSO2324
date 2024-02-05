package com.example.thelibrarians_lso2324.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bag {

    Map<Book, Integer> booksInBag;

    public Bag(){
        booksInBag = new HashMap<>();
    }

    public int getSize() {
        return booksInBag.size();
    }

    public Map<Book, Integer> getBooksMap() {
        return booksInBag;
    }

    public List<Book> getBooksList() {
        return (List<Book>) booksInBag.keySet();
    }

    public void clear() {
        booksInBag.clear();
    }
}

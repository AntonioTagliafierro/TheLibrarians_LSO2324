package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;



public class SciFiBooksFetchEvent {
    List<Book> sciFiBooks;
    public SciFiBooksFetchEvent(List<Book> sciFiBooks) {
        this.sciFiBooks = sciFiBooks;
    }
    public List<Book> getSciFiBooks() {
        return sciFiBooks;
    }



}

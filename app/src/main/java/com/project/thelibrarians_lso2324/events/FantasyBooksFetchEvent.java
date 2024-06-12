package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;



public class FantasyBooksFetchEvent {
    List<Book> fantasyBooks;

    public FantasyBooksFetchEvent(List<Book> fantasyBooks) {
        this.fantasyBooks = fantasyBooks;
    }

    public List<Book> getFantasyBooks() {
        return fantasyBooks;

    }
}

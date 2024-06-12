package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;



public class RomanticBooksFetchEvent {
    List<Book> romanticBooks;

    public RomanticBooksFetchEvent(List<Book> romanticBooks) {
        this.romanticBooks = romanticBooks;
    }
    public List<Book> getRomanticBooks() {
        return romanticBooks;
    }

}

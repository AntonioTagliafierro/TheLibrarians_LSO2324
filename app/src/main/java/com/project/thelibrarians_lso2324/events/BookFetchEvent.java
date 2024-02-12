package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;

public class BookFetchEvent {

    List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public BookFetchEvent(List<Book> books) {
        this.books = books;
    }
}

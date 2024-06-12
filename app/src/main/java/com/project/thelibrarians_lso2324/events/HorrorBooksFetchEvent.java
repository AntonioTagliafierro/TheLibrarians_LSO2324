package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;



public class HorrorBooksFetchEvent {
    List<Book> horrorBooks;

    public HorrorBooksFetchEvent(List<Book> horrorBooks) {
        this.horrorBooks = horrorBooks;
    }
    public List<Book> getHorrorBooks() {
        return horrorBooks;
    }
}

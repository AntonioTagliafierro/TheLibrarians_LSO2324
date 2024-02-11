package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;

public class FetchLendLeaseFromServerEvent {

    List<Book> booksFetched;

    public FetchLendLeaseFromServerEvent(List<Book> booksFetched) {
        this.booksFetched = booksFetched;
    }

    public List<Book> getDrinksFetched() {
        return booksFetched;
    }
}

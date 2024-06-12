package com.project.thelibrarians_lso2324.events;


import com.project.thelibrarians_lso2324.model.Book;

public class EditBookEvent {
    //This is the event that will be used to edit a book
    private Book book;
    public EditBookEvent(Book book) {
        this.book = book;
    }
    public Book getBook() {
        return book;
    }

}

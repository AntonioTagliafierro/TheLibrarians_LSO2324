package com.project.thelibrarians_lso2324.events;


import com.project.thelibrarians_lso2324.model.Book;

public class BookAddEvent {
    private Book addedbook;
    public BookAddEvent(Book addedbook){
        this.addedbook = addedbook;
    }
    public Book getAddedbook() {
        return addedbook;
    }





}

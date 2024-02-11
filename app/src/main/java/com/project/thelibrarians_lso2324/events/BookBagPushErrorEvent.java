package com.project.thelibrarians_lso2324.events;

public class BookBagPushErrorEvent {

    private String message;

    public BookBagPushErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

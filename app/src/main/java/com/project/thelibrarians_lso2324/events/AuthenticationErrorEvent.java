package com.project.thelibrarians_lso2324.events;

public class AuthenticationErrorEvent {
    private String message;

    private final String DEFAULT_MESSAGE = "An error occurred";

    public AuthenticationErrorEvent(String message) {
        if (message == null || message.isEmpty())
            this.message = DEFAULT_MESSAGE;
        else
            this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

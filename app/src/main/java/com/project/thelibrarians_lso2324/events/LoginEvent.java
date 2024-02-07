package com.project.thelibrarians_lso2324.events;

import com.project.thelibrarians_lso2324.daos.TokenPayload;

public class LoginEvent {
    public TokenPayload tokenPayload;

    public LoginEvent(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
    }
}

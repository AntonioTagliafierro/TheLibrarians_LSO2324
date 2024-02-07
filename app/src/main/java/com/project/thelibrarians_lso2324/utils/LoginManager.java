package com.project.thelibrarians_lso2324.utils;

import com.project.thelibrarians_lso2324.model.User;
import com.project.thelibrarians_lso2324.daos.TokenPayload;
public class LoginManager {

    private static LoginManager _instance;

    User user;
    TokenPayload tokenPayload;

    private LoginManager() {
        user = null;
        tokenPayload = null;

    }

    public static LoginManager getInstance() {
        if (_instance == null) {
            _instance = new LoginManager();
        }
        return _instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTokenPayload(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
        this.user = Utils.extractUserFromTokenPayload(tokenPayload);
    }

    public User getUser() {
        return user;
    }

    public TokenPayload getTokenPayload() {
        return tokenPayload;
    }
}

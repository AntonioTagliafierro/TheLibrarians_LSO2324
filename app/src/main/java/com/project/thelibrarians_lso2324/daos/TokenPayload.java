package com.project.thelibrarians_lso2324.daos;

import com.auth0.android.jwt.JWT;
import com.project.thelibrarians_lso2324.exceptions.TokenPayloadExceptions;

public class TokenPayload {
    public String email;
    public String rawToken;

    public int id;
    public String expire;
    public TokenPayload(String token) throws TokenPayloadExceptions {

        // Set the raw token
        this.rawToken = token;

        JWT jwt = new JWT(token);

        String id = jwt.getClaim("id").asString();
        String email = jwt.getClaim("email").asString();
        String expire = jwt.getClaim("expire").asString();

        if(id == null || email == null || expire == null)
            throw new TokenPayloadExceptions("Error");

        this.id = Integer.parseInt(id);
        this.email = email;
        this.expire = expire;
    }

    public String getRawToken() {
        return rawToken;
    }
}

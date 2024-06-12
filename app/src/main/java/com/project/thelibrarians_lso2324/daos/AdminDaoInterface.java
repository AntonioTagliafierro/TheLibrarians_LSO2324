package com.project.thelibrarians_lso2324.daos;

import android.content.Context;

import com.project.thelibrarians_lso2324.exceptions.AdminExceptions;


public interface AdminDaoInterface {
    public TokenPayload login(String email, String password, Context context) throws AdminExceptions;
}

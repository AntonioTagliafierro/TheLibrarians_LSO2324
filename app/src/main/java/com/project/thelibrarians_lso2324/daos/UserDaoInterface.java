package com.project.thelibrarians_lso2324.daos;

import android.content.Context;
import com.project.thelibrarians_lso2324.exceptions.UserExceptions;
import com.project.thelibrarians_lso2324.model.User;


public interface UserDaoInterface {
    public TokenPayload login(String email, String password, Context context) throws UserExceptions;
    public void register(String name, String surname, String email, String password, Context context) throws UserExceptions;
}

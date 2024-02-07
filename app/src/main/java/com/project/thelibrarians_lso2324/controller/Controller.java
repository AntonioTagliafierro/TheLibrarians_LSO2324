package com.project.thelibrarians_lso2324.controller;

import com.project.thelibrarians_lso2324.daos.UserDao;

public class Controller {

    private static Controller instance;

    UserDao userDao;

    private Controller(){

        userDao = new UserDao();
    }
    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}

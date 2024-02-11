package com.project.thelibrarians_lso2324.controller;

import com.project.thelibrarians_lso2324.daos.LendLeaseDao;
import com.project.thelibrarians_lso2324.daos.UserDao;
import com.project.thelibrarians_lso2324.model.BookBag;

public class Controller {

    private static Controller instance;

    UserDao userDao;
    BookBag bookBag;
    LendLeaseDao lendLeaseDao;
    private Controller(){

        userDao = new UserDao();
    }
    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public BookBag getBookBag() {
        return bookBag;
    }
    public UserDao getUserDao() {
        return userDao;
    }

    public LendLeaseDao getLendLeaseDao() {
        return lendLeaseDao;
    }
}
package com.project.thelibrarians_lso2324.controller;

import com.project.thelibrarians_lso2324.daos.BookDao;
import com.project.thelibrarians_lso2324.daos.BookDaoInterface;
import com.project.thelibrarians_lso2324.daos.LendLeaseDao;
import com.project.thelibrarians_lso2324.daos.UserDao;
import com.project.thelibrarians_lso2324.events.BagUpdateEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.model.BookBag;

import org.greenrobot.eventbus.EventBus;

public class Controller {

    private static Controller instance;

    UserDao userDao;
    BookBag bookBag;

    BookDao bookDao;
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

    public BookDao getBookDao(){
        return bookDao;
    }

    public LendLeaseDao getLendLeaseDao() {
        return lendLeaseDao;
    }


    public void addBookInBag(Book book){
        this.getBookBag().addBook(book);
        EventBus.getDefault().post(new BagUpdateEvent(this.getBookBag().getTotalSize()));
    }
}
package com.project.thelibrarians_lso2324.viewmodel;

import androidx.lifecycle.ViewModel;

import com.project.thelibrarians_lso2324.controller.Controller;
import com.project.thelibrarians_lso2324.model.Book;

public class BookDetailsActivityViewModel extends ViewModel {

    public void addBookInBag(Book book){
        Controller.getInstance().addBookInBag(book);
    }
}

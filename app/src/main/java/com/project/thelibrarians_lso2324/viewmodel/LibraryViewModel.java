package com.project.thelibrarians_lso2324.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.thelibrarians_lso2324.controller.Controller;
import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;

public class LibraryViewModel extends ViewModel {

    public MutableLiveData<List<Book>> books;
    public  MutableLiveData<String> searchQuery;

    private List<Book> defaultBooks;

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LibraryViewModel() {
        searchQuery = new MutableLiveData<>();
        books = new MutableLiveData<>();
    }

    public MutableLiveData<List<Book>> getBooks() {
        return books;
    }

    public void setDefaultBooks(List<Book> defaultBooks) {
        this.defaultBooks = defaultBooks;
    }

    public List<Book> getDefaultBooks() {
        return defaultBooks;
    }
    public void addBookInBag(Book book){
        Controller.getInstance().addBookInBag(book);
    }
}

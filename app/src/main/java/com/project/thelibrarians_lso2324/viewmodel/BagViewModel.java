package com.project.thelibrarians_lso2324.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.thelibrarians_lso2324.controller.Controller;
import com.project.thelibrarians_lso2324.model.Book;

import java.util.Map;

public class BagViewModel  extends ViewModel {

    private MutableLiveData<Map<Book, Integer>> books;

    public BagViewModel() {
        Controller controller = Controller.getInstance();
        books = new MutableLiveData<>(controller.getBookBag().getBooksMap());
    }

    public MutableLiveData<Map<Book, Integer>> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        Controller.getInstance().getBookBag().addBook(book);
        books.postValue(Controller.getInstance().getBookBag().getBooksMap());
    }

    public void sendOrder(Context context) {
        Controller.getInstance().getLendLeaseDao().pushBookBagToServer(Controller.getInstance().getBookBag(), context);
    }

    public void clearCart() {
        Controller.getInstance().getBookBag().clear();
        books.postValue(Controller.getInstance().getBookBag().getBooksMap());
    }

    public void removeDrink(Book drink) {
        Controller.getInstance().getBookBag().removeAmountOfBook(drink, 1);
        books.postValue(Controller.getInstance().getBookBag().getBooksMap());
    }
}

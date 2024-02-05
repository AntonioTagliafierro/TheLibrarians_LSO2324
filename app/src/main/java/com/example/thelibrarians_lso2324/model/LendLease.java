package com.example.thelibrarians_lso2324.model;

import com.example.thelibrarians_lso2324.model.Book;

import java.util.Date;
import java.util.List;

public class LendLease {

    private List<Book> books;
    private Date startDate;
    private Date duedate;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public LendLease(List<Book> books, Date startDate, Date duedate) {
        this.books = books;
        this.startDate = startDate;
        this.duedate = duedate;
    }
}

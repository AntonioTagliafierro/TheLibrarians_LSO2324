package com.project.thelibrarians_lso2324.model;

import java.util.Date;
import java.util.List;

public class LendLease {

    private User user;
    private List<Book> books;
    private Date startDate;
    private Date dueDate;
    private String status;




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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date duedate) {
        this.dueDate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public LendLease(List<Book> books, Date startDate, Date duedate) {
//        this.books = books;
//        this.startDate = startDate;
//        this.dueDate = dueDate;
//    }

    public LendLease(User user, List<Book> books, Date startDate, Date dueDate, String status) {
        this.user = user;
        this.books = books;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public LendLease() {

    }
}

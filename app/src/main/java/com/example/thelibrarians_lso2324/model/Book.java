package com.example.thelibrarians_lso2324.model;

import android.media.Image;

import java.io.Serializable;

public class Book implements Serializable {

    public String title;
    public String authors;
    public String ISBN;
    public String description;
    public Bookgenre genre;
    public Image cover;
    public int totalItems;
    public int notAvaiableItems;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bookgenre getGenre() {
        return genre;
    }

    public void setGenre(Bookgenre genre) {
        this.genre = genre;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getNotAvaiableItems() {
        return notAvaiableItems;
    }

    public void setNotAvaiableItems(int notAvaiableItems) {
        this.notAvaiableItems = notAvaiableItems;
    }


    public Book(String title, String authors, String ISBN, String description, Bookgenre genre, Image cover, int totalItems, int notAvaiableItems) {
        this.title = title;
        this.authors = authors;
        this.ISBN = ISBN;
        this.description = description;
        this.genre = genre;
        this.cover = cover;
        this.totalItems = totalItems;
        this.notAvaiableItems = notAvaiableItems;
    }
}

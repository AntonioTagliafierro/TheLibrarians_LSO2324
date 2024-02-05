package com.example.thelibrarians_lso2324.model;

import android.media.Image;

import java.io.Serializable;

public class Book implements Serializable {

    private String title;
    private String authors;
    private String ISBN;
    private String description;
    private Bookgenre genre;
    private Image cover;
    private int totalCopies;
    private int copiesOnLendLease; //copie attualmente in prestito
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

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getCopiesOnLendLease() {
        return copiesOnLendLease;
    }

    public void setCopiesOnLendLease(int copiesOnLendLease) {
        this.copiesOnLendLease = copiesOnLendLease;
    }


    public Book(String title, String authors, String ISBN, String description, Bookgenre genre, Image cover, int totalCopies, int copiesOnLendLease) {
        this.title = title;
        this.authors = authors;
        this.ISBN = ISBN;
        this.description = description;
        this.genre = genre;
        this.cover = cover;
        this.totalCopies = totalCopies;
        this.copiesOnLendLease = copiesOnLendLease;
    }
}

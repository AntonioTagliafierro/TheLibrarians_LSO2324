package com.project.thelibrarians_lso2324.model;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

    private String title;
    private String authors;
    private String ISBN;
    private String description;
    private BookGenre genre;
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

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
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

    public static Book fromJSON(JSONObject object) {
        return fromJSON(object, "ISBN");
    }
    public static BookGenre getBookGenreFromString(String genreString) {
        switch (genreString.toUpperCase()) {
            case "FANTASCIENZA":
                return BookGenre.FANTASCIENZA;
            case "FANTASY":
                return BookGenre.FANTASY;
            case "ROMANZO_STORICO":
                return BookGenre.ROMANZO_STORICO;
            case "THRILLER":
                return BookGenre.THRILLER;
            case "HORROR":
                return BookGenre.HORROR;
            case "ROMANTICO":
                return BookGenre.ROMANTICO;
            case "GIALLO":
                return BookGenre.GIALLO;
            case "AVVENTURA":
                return BookGenre.AVVENTURA;
            case "FANTASY_URBANO":
                return BookGenre.FANTASY_URBANO;
            case "FANTASY_EPICO":
                return BookGenre.FANTASY_EPICO;
            case "FAVOLA":
                return BookGenre.FAVOLA;
            case "FANTASY_SCIENTIFICO":
                return BookGenre.FANTASY_SCIENTIFICO;
            case "DRAMMATICO":
                return BookGenre.DRAMMATICO;
            case "BIOGRAFICO":
                return BookGenre.BIOGRAFICO;
            case "SAGGIO":
                return BookGenre.SAGGIO;
            case "SAGGIO_FILOSOFICO":
                return BookGenre.SAGGIO_FILOSOFICO;
            case "POESIA":
                return BookGenre.POESIA;
            case "COMMEDIA":
                return BookGenre.COMMEDIA;
            case "SATIRA":
                return BookGenre.SATIRA;
            case "AZIONE":
                return BookGenre.AZIONE;
            default:
                return BookGenre.SCONOSCIUTO;
        }
    }

    public static Book fromJSON(JSONObject object, String overrideIdName) {
        try {
            String genreString = object.getString("genre");

            return new Book(
                    object.getString("title"),
                    object.getString("authors"),
                    object.getString("isbn"),
                    object.getString("description"),
                    getBookGenreFromString(genreString),
                    /*gestire l'oggetto Image qui. */
                    null,
                    object.getInt("totalcopies"),
                    object.getInt("copiesonlendlease")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Book> fromJsonArray(JSONArray array) {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                bookList.add(fromJSON(object));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return bookList;
    }


    public Book(String title, String authors, String ISBN, String description, BookGenre genre, Image cover, int totalCopies, int copiesOnLendLease) {
        this.title = title;
        this.authors = authors;
        this.ISBN = ISBN;
        this.description = description;
        this.genre = genre;
        this.cover = cover;
        this.totalCopies = totalCopies;
        this.copiesOnLendLease = copiesOnLendLease;
    }

    public int getImageResourceId() {
        return 0;
    }

    public File getCoverUrl() {
        return null;
    }
}

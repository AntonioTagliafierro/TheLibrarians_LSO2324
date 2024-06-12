package com.project.thelibrarians_lso2324.daos;

import android.content.Context;

import com.project.thelibrarians_lso2324.model.Book;

import java.util.List;



public interface MostPopularCategoriesInterface {

    // This method is used to get romantic books
    public List<Book> getRomanticBooks(Context context);

    // This method is used to get horror books
    public List<Book> getHorrorBooks(Context context);

    // This method is used to get fantasy books
    public List<Book> getFantasyBooks(Context context);

    // This method is used to get science fiction books
    public List<Book> getScienceFictionBooks(Context context);
}


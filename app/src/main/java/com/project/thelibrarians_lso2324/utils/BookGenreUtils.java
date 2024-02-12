package com.project.thelibrarians_lso2324.utils;

import com.project.thelibrarians_lso2324.model.BookGenre;

public class BookGenreUtils {

    public static String getDisplayName(BookGenre genre) {

        return genre.name().toLowerCase().replace("_", " ");
    }
}

package com.project.thelibrarians_lso2324.events;

import android.graphics.Bitmap;

public class BookImageEvent {

    public Bitmap image;
    public String isbn;

    public BookImageEvent(Bitmap image, String isbn){
        this.image = image;
        this.isbn = isbn;

    }
}

package com.project.thelibrarians_lso2324.events;

public class DeleteBook {
private String isbn;
public DeleteBook(String isbn){
    this.isbn = isbn;
}
public String getIsbn() {
    return isbn;
}

}

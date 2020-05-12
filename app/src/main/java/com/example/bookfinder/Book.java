package com.example.bookfinder;

public class Book {

    //name of the book
    private String mName;
    //name of author
    private String mAuthor;

    public Book(String name, String author)
    {
        mName = name;
        mAuthor = author;
    }

    public String getBookName(){
        return mName;
    }
    public String getAuthorName(){
        return mAuthor;
    }

}

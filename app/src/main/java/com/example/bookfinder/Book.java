package com.example.bookfinder;

import java.io.Serializable;

public class Book implements Serializable {

    //name of the book
    private String mName;

    //name of author
    private String mAuthor;

    //Image of Book
    private String mImgUrl;

    //Retail Price
    private double mPrice;

    //book rating
    private double mRating;

    //No. of Pages
    private int mPages;

    //Book category
    private String mCategory;

    //Book publisher
    private String mPublisher;

    //Book description
    private String mDescription;

    //Book preview link
    private String mPreview;

    //constructor to load the members
    public Book(String name, String author, String Image, double price, double rating,
                    int pages, String category, String preview,String publisher,String description)
    {
        mName = name;
        mAuthor = author;
        mImgUrl = Image;
        mPrice = price;
        mRating = rating;
        mPages = pages;
        mCategory = category;
        mPreview = preview;
        mPublisher = publisher;
        mDescription = description;
    }

    // method to get book name
    public String getBookName(){
        return mName;
    }

    // method to get book author
    public String getAuthorName(){
        return mAuthor;
    }

    // method to get book image url
    public String getBookImg(){
        return mImgUrl;
    }

    // method to get book price
    public double getBookPrice(){
        return mPrice;
    }

    // method to get book average rating
    public double getBookRating(){
        return mRating;
    }

    // method to get book pages
    public int getBookPages(){
        return mPages;
    }

    // method to get book category
    public String getBookCategory(){
        return mCategory;
    }

    // method to get book publisher
    public String getBookPublisher(){
        return mPublisher;
    }

    // method to get book description
    public String getBookDescription(){
        return mDescription;
    }

    // method to get book preview
    public String getpreviewUrl(){
        return mPreview;
    }
    

}

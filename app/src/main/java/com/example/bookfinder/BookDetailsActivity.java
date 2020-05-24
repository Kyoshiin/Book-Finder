package com.example.bookfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = BookDetailsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        Book selectd_book = (Book) getIntent().getSerializableExtra("Book");

        ImageView bookimage = findViewById(R.id.BookImg);
        //setting visibility if info not available
        if (TextUtils.isEmpty(selectd_book.getBookImg()))
            bookimage.setVisibility(View.GONE);
        else {
            //displaying image from url using picasso
            String imageURL = selectd_book.getBookImg();
            //making http -> https as picasso not http
            //Log.v(LOG_TAG,"Image URL"+imageURL);
            if (imageURL.charAt(4)!= 's')
                imageURL = imageURL.substring(0,4)+'s'+imageURL.substring(4);
            Picasso.get().load(imageURL).into(bookimage);
        }


        TextView bookname =(TextView) findViewById(R.id.Book_name);
        bookname.setText(selectd_book.getBookName());

        TextView authorname = findViewById(R.id.BookAuthorname);
        authorname.setText("by "+selectd_book.getBookAuthorName());

        TextView bookpublisher = findViewById(R.id.Bookpublisher);
        //setting visibility if info not available
        if (TextUtils.isEmpty(selectd_book.getBookPublisher()))
            bookpublisher.setVisibility(View.GONE);
        else
            bookpublisher.setText("Publishers: "+selectd_book.getBookPublisher());

        TextView bookprice = findViewById(R.id.Bookprice);
        //setting visibility if info not available
        if (selectd_book.getBookPrice() == 0.0)
            bookprice.setVisibility(View.GONE);
        else
        bookprice.setText("Price: Rs. " +Double.toString(selectd_book.getBookPrice()));

        RatingBar bookrating = findViewById(R.id.Bookrating);
        //setting visibility if info not available
        if (selectd_book.getBookRating() == 0.0)
            bookrating.setVisibility(View.GONE);
        else
            bookrating.setRating((float)selectd_book.getBookRating());

        TextView bookpages = findViewById(R.id.Bookpages);
        //setting visibility if info not available
        if (selectd_book.getBookPages() == 0)
            bookpages.setVisibility(View.GONE);
        else
            bookpages.setText("Pages: "+Integer.toString(selectd_book.getBookPages()));

        TextView bookdesc = findViewById(R.id.Bookdescription);
        //setting visibility if info not available
        if (TextUtils.isEmpty(selectd_book.getBookDescription())){
            bookdesc.setVisibility(View.GONE);
            findViewById(R.id.about).setVisibility(View.GONE);}
        else
            bookdesc.setText(selectd_book.getBookDescription());

        TextView bookcategory = findViewById(R.id.Bookcategories);
        //setting visibility if info not available
        if (TextUtils.isEmpty(selectd_book.getBookCategory()))
            bookcategory.setVisibility(View.GONE);
        else
            bookcategory.setText("Genre: "+selectd_book.getBookCategory());

        TextView bookpreview = findViewById(R.id.Bookpreview);
        //setting visibility if info not available
        if (TextUtils.isEmpty(selectd_book.getBookpreviewUrl()))
            bookpreview.setVisibility(View.GONE);
        else {
            //Log.v(LOG_TAG,"preview URL: "+selectd_book.getBookpreviewUrl());
            bookpreview.setText(selectd_book.getBookpreviewUrl()+"\n"+"Click here to preview");
        }

        //FOR FEEDBACK
        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri feedbackUri = Uri.parse("https://forms.gle/Y6bk2BqMEpZVwjC46");

                // Create a new intent to view the earthquake URI
                Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, feedbackUri);

                // Send the intent to launch a new activity
                startActivity(feedbackIntent);
            }
        });
    }
}

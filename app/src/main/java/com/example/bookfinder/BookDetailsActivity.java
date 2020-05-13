package com.example.bookfinder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        Book selectd_book = (Book)getIntent().getSerializableExtra("Book");

        TextView txt =  findViewById(R.id.text_view);
        txt.setText(""+selectd_book.getBookName());
    }
}

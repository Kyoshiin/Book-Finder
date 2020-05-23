package com.example.bookfinder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0,books);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item_view, parent, false);
        }

        Book currentBook = getItem(position);

        //adding book names to the list
        TextView bookname = (TextView) listItemView.findViewById(R.id.Bookname);
        bookname.setText(""+currentBook.getBookName());

        //adding author name
        TextView authorname = (TextView) listItemView.findViewById(R.id.Authorname);
        authorname.setText(""+currentBook.getBookAuthorName());

        //adding picture in the listview
        ImageView bookimage_list = listItemView.findViewById(R.id.BookImage);
        if (!TextUtils.isEmpty(currentBook.getBookImg())) {
            String imageURL = currentBook.getBookImg();

            //making http -> https as picasso not http
            if (imageURL.charAt(4) != 's')
                imageURL = imageURL.substring(0, 4) + 's' + imageURL.substring(4);
            Picasso.get().load(imageURL).into(bookimage_list);
        }
        return listItemView;
    }
}

package com.example.bookfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BookFinderActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookFinderActivity.class.getName();

    //URL for req book
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=love&maxResults=40";

    //object for bookadater
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_books);

        //custom adapter taking empty set on books
        adapter = new BookAdapter(this,new ArrayList<Book>());

        //List view to display result
        ListView search_result = (ListView)findViewById(R.id.search_result);
        search_result.setAdapter(adapter);

        //Toast.makeText(this,"Fetching books..",Toast.LENGTH_SHORT).show();
        //creating connection on background thread
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in any int ID (since using only 1),pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter
        loaderManager.initLoader(1, null, this);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        //Toast.makeText(this,"Fetching books",Toast.LENGTH_SHORT).show();
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

        @Override
        public void onLoaderReset (Loader < List < Book >> loader) {
            // Loader reset, so we can clear out our existing data.
            adapter.clear();

        }
    }
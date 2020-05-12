package com.example.bookfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BookFinderActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookFinderActivity.class.getName();

    //Temp URL for req book
    private String temp_url = "https://www.googleapis.com/books/v1/volumes?q=";
    //Final URL
    private String REQUEST_URL;

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

        //EditText to input search item
        final EditText search_key = (EditText)findViewById(R.id.search_key);

        //creating connection on background thread
        final LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in any int ID (since using only 1),pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter
        loaderManager.initLoader(1, null, BookFinderActivity.this);

        //Button for start searching
        findViewById(R.id.searchbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_input = search_key.getText().toString();
                //functio to form the final search input URL
                formatInputSearch(search_input);
                search_key.setText(search_input); // to display the input text


                Toast.makeText(getApplicationContext(),"Fetching books..", Toast.LENGTH_SHORT).show();
                //for restarting loader when new search item is given
                loaderManager.restartLoader(1,null,BookFinderActivity.this);

            }
        });
    }

    private void formatInputSearch(String search_input) {

        //removing additional trailing spaces
        search_input = search_input.trim();
        //replacing space with '+'
        search_input = search_input.replaceAll("\\s+","+");

        //storing the final url
        REQUEST_URL = temp_url+search_input+"&maxResults=25&orderBy=newest";

        Log.v(LOG_TAG, "Input URL "+REQUEST_URL);
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
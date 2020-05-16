package com.example.bookfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BookFinderActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookFinderActivity.class.getName();

    private EditText search_key;

    private LoaderManager loaderManager;

    //TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

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

        //EditText to input search item
        search_key = (EditText) findViewById(R.id.search_key);

        //creating connection on background thread
        loaderManager = getLoaderManager();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String searched_book  = intent.getStringExtra("searched_books");
        //Log.v(LOG_TAG,"intent seaarch: "+searched_book);
        createSearch(searched_book);

        //custom adapter taking empty set on books
        adapter = new BookAdapter(this, new ArrayList<Book>());

        //List view to display result
        ListView search_result = (ListView) findViewById(R.id.search_result);

        search_result.setAdapter(adapter);

        search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //storing the selected book
                Book currentBook = adapter.getItem(position);

                //creating intent
                Intent details_intent = new Intent(getApplicationContext(),BookDetailsActivity.class);
                //sending current selected book to details activity
                details_intent.putExtra("Book",currentBook);
                startActivity(details_intent);
            }
        });


        // Initialize the loader. Pass in any int ID (since using only 1),pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter
        loaderManager.initLoader(1, null, BookFinderActivity.this);

        //start searching when search key is pressed on keyboard
        search_key.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //remove focus from editText (cursor)
                    search_key.clearFocus();

                    //to remove keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    String search_input = search_key.getText().toString();

                    //starting search for another book
                    createSearch(search_input);
                    return true;
                }
                return false;
            }
        });
    }

    //method to start the searching process
    private void createSearch(String searched_book) {

        //function to form the final search input URL
        formatInputSearch(searched_book);
        search_key.setText(searched_book); // to display the input text

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //for restarting loader when new search item is given
            loaderManager.restartLoader(1, null, BookFinderActivity.this);
        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    //method for formatting input to URL
    private void formatInputSearch(String search_input) {

        //removing additional trailing spaces
        search_input = search_input.trim();
        //replacing space with '+'
        search_input = search_input.replaceAll("\\s+", "+");

        //storing the final url
        REQUEST_URL = temp_url + search_input + "&maxResults=40&filter=ebooks&langRestrict=en&printType=books";

        //Log.v(LOG_TAG, "Input URL " + REQUEST_URL);
    }


    //LOADER  METHODS
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }
}
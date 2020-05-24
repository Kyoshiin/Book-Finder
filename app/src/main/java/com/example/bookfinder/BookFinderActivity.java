package com.example.bookfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class BookFinderActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>>, SearchPreferenceDialog.PreferenceDialogListner {

    private static final String LOG_TAG = BookFinderActivity.class.getName();

    private EditText search_key;

    private LoaderManager loaderManager;

    private View loadingIndicator;

    //TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    //current Searched
    private String currentSearch;
    //Final URL
    private String REQUEST_URL;

    // to set preference to search by author or title
    private String mPreference = "";

    // to set max results
    private String mMaxResults = "";

    //object for bookadater
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_books);

        /**variables initialization */
        loadingIndicator = findViewById(R.id.loading_indicator);

        //EditText to input search item
        search_key = (EditText) findViewById(R.id.search_key);

        //creating connection on background thread
        loaderManager = getLoaderManager();

        //creating List view to display result
        ListView search_result = (ListView) findViewById(R.id.search_result);

        //creating textview to display error msg
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        search_result.setEmptyView(mEmptyStateTextView);

        //custom adapter taking empty set of books
        adapter = new BookAdapter(this, new ArrayList<Book>());

        /** STARTING ACTIONS  */
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        currentSearch = intent.getStringExtra("searched_books");

        //creating a search for req from MainActivity
        CreateSearch();

        //setting adapter on the search result listview
        search_result.setAdapter(adapter);

        //setting on itemClick of search_result listview
        search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //storing the selected book
                Book currentBook = adapter.getItem(position);

                //creating intent
                Intent details_intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
                //sending current selected book to details activity
                details_intent.putExtra("Book", currentBook);
                startActivity(details_intent);
            }
        });

        // Initialize the loader. Pass in any int ID (since using only 1),pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter
        if (CheckNetwork()) // checking if network is available
            loaderManager.initLoader(1, null, BookFinderActivity.this);


        //start searching when search key is pressed on keyboard
        search_key.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //remove focus from editText (cursor)
                    search_key.clearFocus();

                    //to remove keyboard after pressing search icon in keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    //updating the current search query
                    currentSearch = search_key.getText().toString();
                    //clearing adapter for next searches only via edit text
                    //adapter.clear();

                    loadingIndicator.setVisibility(VISIBLE);
                    //starting search for another book
                    CreateSearch();
                    return true;
                }
                return false;
            }
        });

        //preference icon onclick listner
        findViewById(R.id.preferbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening preference dialog box

                SearchPreferenceDialog dialog = new SearchPreferenceDialog();
                dialog.show(getSupportFragmentManager(), "preferencedialog");
            }
        });
    }

    private boolean CheckNetwork() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection return true
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //method to start the searching process
    private void CreateSearch() {

        //checking if adapter is not empty
        if (adapter.getCount()!=0)
            adapter.clear();

        //enabling loading indicator for next search
        loadingIndicator.setVisibility(VISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);

        //function to form the final search input URL
        formatInputSearch(currentSearch);
        search_key.setText(currentSearch); // to display the input text


        // If there is a network connection, fetch data
        if (CheckNetwork()) {
            //for restarting loader when new search item is given
            loaderManager.restartLoader(1, null, BookFinderActivity.this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.INVISIBLE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setVisibility(VISIBLE);
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
        REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + mPreference + search_input +
                "&langRestrict=en&printType=books" + mMaxResults;

        Log.v(LOG_TAG, "Input URL " + REQUEST_URL);
    }

    /**
     * LOADER  METHODS
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.INVISIBLE);

        //to clear adapter after coming back from detailsActivity
        adapter.clear();

        // If there is a valid list of books, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        } else {
            // Set empty state text to display "No books found."
            mEmptyStateTextView.setVisibility(VISIBLE);
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }


    /**
     * implementiong preference interface method of searchpreferenceDialog
     *
     * @param Preference
     * @param maxresults
     */
    @Override
    public void setPreference(String Preference, String maxresults) {
        //editing according to url
        if (TextUtils.isEmpty(Preference))
            mPreference = "";
        else {
            mPreference = "in" + Preference.toLowerCase() + ":";
            Toast.makeText(getApplicationContext(),"Search Filter: "+Preference,Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(maxresults))
            mMaxResults = "";
        else
            mMaxResults = "&maxResults=" + maxresults;

        //creating a search for exiting search query with updated preference
        CreateSearch();
    }
}
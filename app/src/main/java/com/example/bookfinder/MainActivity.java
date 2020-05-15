package com.example.bookfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText to input search item
        final EditText search = (EditText) findViewById(R.id.starting_search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //remove focus from editText (cursor)
                    search.clearFocus();

                    //to remove keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    String input = search.getText().toString();

                    //creating intent to call bookfinder activity
                    Intent search_intent = new Intent(getApplicationContext(),BookFinderActivity.class);
                    //sending current selected book to details activity
                    search_intent.putExtra("searched_books",input);
                    startActivity(search_intent);

                    return true;
                }
                return false;
            }
        });
    }
}

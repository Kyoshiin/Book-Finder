package com.example.bookfinder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class SearchPreferenceDialog extends DialogFragment {

    //Variables for Shared Preferences
    public static final String SHARED_PREFS = "sharedPreference";
    public static final String SEARCHBY = "searchtext";
    public static final String RESULTS = "results";

    //String array resources
    private String searchby[] = {"Any","Title","Author","Publisher"};
    private String resultsNum[] ={"10","5","15","20","30","40"};
    private Spinner spinnerMaxResults;
    private Spinner spinnerPreference;
    private PreferenceDialogListner listener;

    //variable to store data
    private String mPreference;
    private int mPreferenceID;
    private String mMaxResult;
    private int mMaxResultID;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //creating layout inflator object
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflating the custom layout
        View view = inflater.inflate(R.layout.search_preference, null);

        loaddata(); // method to load previously inputted value

        /** ACCESSING PREFERENCE FIELDS **/

        //spinner for Title|Author|Publisher
        spinnerPreference = (Spinner) view.findViewById(R.id.prefer_spinner);
        ArrayAdapter<String> prefAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_layout,searchby);
        spinnerPreference.setAdapter(prefAdapter);
        spinnerPreference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //storing the preference of search
//                if (position!=0)
//                    mPreference = parent.getItemAtPosition(position).toString().toLowerCase();
//
//                else
//                    mPreference="";

                mPreferenceID = position; //storing pos of selected data
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mPreference="";

            }
        });

        //spinner for MaxResults
        spinnerMaxResults = (Spinner) view.findViewById(R.id.maxResult_spinner);
        ArrayAdapter<String> resultsAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_layout,resultsNum);
        spinnerMaxResults.setAdapter(resultsAdapter);
        spinnerMaxResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //storing the preference of search
                //mMaxResult = parent.getItemAtPosition(position).toString();
                mMaxResultID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mMaxResult="";
            }
        });

        updateUI(); // to update spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //customising the alert dialog
        builder.setView(view)
                .setTitle("Search Preference")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //listener.setPreference(mPreference, mMaxResult);
                        listener.setPreference(searchby[mPreferenceID],resultsNum[mMaxResultID]);
                        savedata(); // to save users data
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PreferenceDialogListner) context;
    }

    //interface which calling activity must implement
    public interface PreferenceDialogListner {
        void setPreference(String Preference, String maxresults);
    }

    /** MEDTHODS FOR SHARED PREFERENCES **/

    public void savedata()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SEARCHBY,mPreferenceID);
        editor.putInt(RESULTS,mMaxResultID);
        editor.apply();
    }

    public void loaddata()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        //loading saved data
        mPreferenceID = sharedPreferences.getInt(SEARCHBY,0);
        mMaxResultID = sharedPreferences.getInt(RESULTS,0);
    }

    public void updateUI()
    {
        spinnerPreference.setSelection(mPreferenceID);
        spinnerMaxResults.setSelection(mMaxResultID);
    }

}

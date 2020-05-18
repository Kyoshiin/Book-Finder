package com.example.bookfinder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class SearchPreferenceDialog extends DialogFragment {

    private EditText editTextMaxResults;
    private EditText editTextPreference;
    private PreferenceDialogListner listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //creating layout inflator object
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflating the custom layout
        View view = inflater.inflate(R.layout.search_preference, null);

        //accessing the custom layout fields
        editTextPreference = view.findViewById(R.id.prefer);
        editTextMaxResults = view.findViewById(R.id.maxResults);


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

                        String prefer = editTextPreference.getText().toString().toLowerCase();
                        String maxresults = editTextMaxResults.getText().toString();
                        listener.setPreference(prefer, maxresults);
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
}

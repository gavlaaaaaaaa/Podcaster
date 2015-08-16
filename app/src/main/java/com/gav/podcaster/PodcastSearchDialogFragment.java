package com.gav.podcaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Gav on 16/08/15.
 */
public class PodcastSearchDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInsanceState){
        //initialise Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogFragView = inflater.inflate(R.layout.fragment_search_dialog, null);
        builder.setView(dialogFragView);
        //set title
        builder.setMessage("Search for Podcasts...")
                //Set positive button text and on click listener
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //set negative button text and on click listener
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        //initialise spinner and array adapter for SEARCH SOURCE
        Spinner searchSourceSpinner = (Spinner) dialogFragView.findViewById(R.id.SRCHFRAG_SPIN_SOURCE);
        ArrayAdapter<CharSequence> srcArrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.search_source_array, android.R.layout.simple_spinner_item);
        srcArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSourceSpinner.setAdapter(srcArrayAdapter);

        //initialise spinner and array adapter for SORT BY
        Spinner sortSpinner = (Spinner) dialogFragView.findViewById(R.id.SRCHFRAG_SPIN_SORT);
        ArrayAdapter<CharSequence> sortArrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_by_array, android.R.layout.simple_spinner_item);
        sortArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortArrayAdapter);

        Dialog dialog = builder.create();
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return dialog;
    }
}

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Gav on 16/08/15.
 */
public class PodcastSearchDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInsanceState){
        //initialise Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogFragView = inflater.inflate(R.layout.fragment_search_dialog, null);
        builder.setView(dialogFragView);

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



        //set title
        builder.setMessage("Search for Podcasts...")
                //Set positive button text and on click listener
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //obtain search string
                        String searchString = ((EditText)dialogFragView.findViewById(R.id.SRCHFRAG_ET_SEARCH))
                                .getText().toString().replace(" ","%20"); //replace spaces with %20 for url
                        //TODO: obtain sort by value
                        //TODO: obtain search source value
                        //TODO: obtain number of results value
                        //TODO: obtain filter preferences
                        //TODO: use above to construct api parameter string
                        Toast.makeText(getActivity(),searchString,Toast.LENGTH_SHORT).show();
                    }
                })
                //set negative button text and on click listener
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });




        Dialog dialog = builder.create();
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return dialog;
    }
}

package com.gav.podcaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Gav on 16/08/15.
 */
public class PodcastSearchDialogFragment extends DialogFragment {

    private final String[] sortByArray = {"alpha", "hits", "subs", "new", "rel", "rating"};
    private final String[] sourceArray = {"all", "title"};

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
                                .getText().toString().replace(" ","%20").toLowerCase(); //replace spaces with %20 for url

                        //get position of spinner and use as index for sortByArray
                        String sortString = sortByArray[((Spinner)dialogFragView.findViewById(R.id.SRCHFRAG_SPIN_SORT)).getSelectedItemPosition()];

                        //get position of spinner and use as index for sourceArray
                        String sourceString = sourceArray[((Spinner)dialogFragView.findViewById(R.id.SRCHFRAG_SPIN_SOURCE)).getSelectedItemPosition()];

                        //get number of results
                        String numResultsString = ((EditText)dialogFragView.findViewById(R.id.SRCHFRAG_ET_NUM_RESULTS)).getText().toString();

                        //obtain filter preferences
                        Boolean noAdult = ((CheckBox)dialogFragView.findViewById(R.id.SRCHFRAG_CHK_ADULT)).isEnabled();
                        Boolean noExplicit = ((CheckBox)dialogFragView.findViewById(R.id.SRCHFRAG_CHK_EXPLICIT)).isEnabled();
                        Boolean cleanOnly = ((CheckBox)dialogFragView.findViewById(R.id.SRCHFRAG_CHK_CLEAN)).isEnabled();

                        //construct filter string
                        String contentFilter="";
                        if(noAdult) contentFilter = contentFilter + "&contentfilter=noadult";
                        if(noExplicit) contentFilter = contentFilter + "&contentfilter=noexplicit";
                        if(cleanOnly) contentFilter = contentFilter + "&contentfilter=clean";

                        //use above to construct api parameter string
                        String apiSearchString = "keywords="+searchString+"&format=opml&sort="+sortString+"&searchsource="+sourceString+contentFilter+"&start=0&results="+numResultsString;

                        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                        intent.putExtra("apiSearchString", apiSearchString);

                        startActivity(intent);
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

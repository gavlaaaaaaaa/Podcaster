package com.gav.podcaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

/**
 * Created by Gav on 16/08/15.
 */
public class PodcastSearchDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInsanceState){
        //initialise Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_search_dialog, null));
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


        Dialog dialog = builder.create();
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return dialog;
    }
}

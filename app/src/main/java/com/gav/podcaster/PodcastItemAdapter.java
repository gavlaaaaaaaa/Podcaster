package com.gav.podcaster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gav.podcaster.com.gav.rss.model.RssFeed;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gav on 02/08/15.
 */
public class PodcastItemAdapter extends BaseAdapter  {

    private Context mContext;
    private ArrayList<RssFeed> podcasts;

    public PodcastItemAdapter(Context c, List<RssFeed> podcasts){
        mContext = c;
        this.podcasts = (ArrayList) podcasts;
    }

    public int getCount(){
        return podcasts.size();
    }

    //return RssFeed (podcast) item
    public Object getItem(int position){
        return podcasts.get(position);
    }

    //return id of item in db
    public long getItemId(int position){
        return podcasts.get(position).getId();
    }

    public View getView(int position, View podcastButtonView, ViewGroup parent){
        // get current podcast
        final RssFeed currPodcast = podcasts.get(position);

        if(podcastButtonView == null){
            //if not recycled, initialize attributes and inflate podcastButtonView
            LayoutInflater lInflater = (LayoutInflater)mContext.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            podcastButtonView = (RelativeLayout) lInflater.inflate(R.layout.podcast_button, null);

        }
        else {
            //recylced so just use the view
            podcastButtonView = (RelativeLayout) podcastButtonView;
        }

        //TODO: grab imageview object and set image based on currPodcast image

        // grab the text view object and set its text
        TextView tv = (TextView) podcastButtonView.findViewById(R.id.PODCAST_BTN_TEXT);
        tv.setText(currPodcast.getTitle());

        return podcastButtonView;
    }




}

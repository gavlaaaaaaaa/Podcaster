package com.gav.podcaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
public class PodcastItemAdapter extends BaseAdapter {

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

    public View getView(int position, View convertView, ViewGroup parent){
        //change this if needed
        ImageView imageView;

        RssFeed currPodcast = podcasts.get(position);

        if(convertView == null){
            //if not recycled, initialize attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85,85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }
        else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.gallery_thumb));

        return imageView;
    }



}

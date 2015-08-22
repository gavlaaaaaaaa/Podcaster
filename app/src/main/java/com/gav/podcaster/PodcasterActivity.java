package com.gav.podcaster;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.gav.podcaster.com.gav.rss.model.RssFeed;
import com.gav.podcaster.com.gav.rss.model.RssFeedParser;
import com.gav.podcaster.com.gav.rss.model.RssMessage;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class PodcasterActivity extends AppCompatActivity  {

    private ArrayList<RssFeed> pods = new ArrayList<RssFeed>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcaster);

        GridView gridview = (GridView) findViewById(R.id.GRID_ID);

        gridview.setAdapter(new PodcastItemAdapter(this, pods));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pods != null) {
                    Toast.makeText(parent.getContext(), pods.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_podcaster, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.PODCAST_SEARCH));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addPodcast(View v){
        // write your code here ..
        Toast.makeText(v.getContext(), "Add a podcast", Toast.LENGTH_SHORT).show();
        DialogFragment searchDialog = new PodcastSearchDialogFragment();
        searchDialog.show(getFragmentManager(), "SearchFragment");

    }

}

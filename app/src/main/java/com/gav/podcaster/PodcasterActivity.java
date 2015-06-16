package com.gav.podcaster;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gav.podcaster.com.gav.rss.model.RssFeed;
import com.gav.podcaster.com.gav.rss.model.RssFeedParser;
import com.gav.podcaster.com.gav.rss.model.RssMessage;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;


public class PodcasterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcaster);

        InputStream stream = null;
        RssFeedParser parser = new RssFeedParser(this);
        RssFeed feed = null;
        try {
            parser.execute("http://api.digitalpodcast.com/v2r/search/?appid=b5be1f305cecaf9fe99d60dd5c29946b&keywords=karl&format=opml&sort=rating&searchsource=title&contentfilter=noadult&start=0&results=10", "false");

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_podcaster, menu);
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
}

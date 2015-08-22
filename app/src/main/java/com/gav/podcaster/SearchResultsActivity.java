package com.gav.podcaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gav.podcaster.com.gav.rss.model.RssFeed;
import com.gav.podcaster.com.gav.rss.model.RssFeedParser;
import com.gav.podcaster.com.gav.rss.model.RssMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchResultsActivity extends AppCompatActivity {

    private String apiSearchString;
    private ArrayList<RssFeed> pods = new ArrayList<RssFeed>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        apiSearchString = "";

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            apiSearchString = extras.getString("apiSearchString");
        }

        TextView tv = (TextView) findViewById(R.id.dummy_tv);
        tv.setText("RESULTS:\n");

        InputStream stream = null;
        RssFeedParser parser = new RssFeedParser(this);
        RssFeed feed = null;
        try {
            feed = parser.execute("http://api.digitalpodcast.com/v2r/search/?appid=b5be1f305cecaf9fe99d60dd5c29946b&"+apiSearchString, "false").get();
            pods.add(feed);
            for(RssMessage message : feed.getMessages())
            {
                tv.append(message.toString()+"\n\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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

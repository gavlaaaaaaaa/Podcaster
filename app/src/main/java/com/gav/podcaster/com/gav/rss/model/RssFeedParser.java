package com.gav.podcaster.com.gav.rss.model;

/**
 * Created by Gav on 11/06/15.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.gav.podcaster.PodcastItemAdapter;
import com.gav.podcaster.PodcasterActivity;
import com.gav.podcaster.R;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RssFeedParser extends AsyncTask<String, Void, RssFeed>{
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    String ns = "";
    private Activity activity;

    public RssFeedParser(Activity activity){
        this.activity = activity;
    }

    public InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    public RssFeed parse(InputStream in, boolean feed) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            if(feed)
                return readFeed(parser);
            else
                return readResults(parser);
        } finally {
            in.close();
        }
    }

    //parse search results when searching for podcasts through the api
    private RssFeed readResults(XmlPullParser parser) throws XmlPullParserException, IOException {
        RssFeed feed = new RssFeed("Search results", null, null, null, null, null,null);

        RssMessage result = null;
        parser.require(XmlPullParser.START_TAG, ns, "opml");
        String name = null;
        //continue to get the rest of the items
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();
            if (name.equals("body")) {
                parser.require(XmlPullParser.START_TAG, ns, "body");
                parser.next();
                while(parser.getName() == null || !parser.getName().equals("body")) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        parser.next();
                        continue;
                    }
                    String resultName = parser.getAttributeValue(ns, "text");
                    String resultUrl = parser.getAttributeValue(ns, "url");
                    RssFeedParser getChild = new RssFeedParser(activity);
                    try {
                        RssFeed podcast = getChild.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, resultUrl, "true").get();
                        result = new RssMessage(resultName, null, resultUrl, null, null,podcast.getImage());
                        feed.getMessages().add(result);
                        parser.next();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
            else{
                skip(parser);
            }

        }
        return feed;
    }

    //parse a feed for a -specific- podcast obtaining all of its available content
    private RssFeed readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        RssFeed feed = null;
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        feed = readHeader(parser);
        // get the first item after the header
        String name = parser.getName();
        //System.out.println("\n\n\n\n\n" + name);
        // Starts by looking for the entry tag
        if (name.equals("item")) {
            feed.getMessages().add(readEntry(parser));
        }

        //continue to get the rest of the items
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();
            //System.out.println("\n\n\n\n\n" + name);
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                feed.getMessages().add(readEntry(parser));
            }

        }
        return feed;
    }

    private RssFeed readHeader(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        String title = null;
        String description = null;
        String link = null;
        String language = null;
        String copyright = null;
        String pubdate = null;
        String image = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("image")) {
                image = readImage(parser);
            } else if (name.equals("item")){
                break;
            } else{
                skip(parser);
            }
        }
        return new RssFeed(title, link, description, language, copyright, pubdate, image);
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private RssMessage readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        String guid = null;
        String enclosure = null;
        String image = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("itunes:summary")) { //description is sometimes replaced with itunes:summary
                description = readSummary(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("guid")) {
                guid = readGuid(parser);
            } else if (name.equals("enclosure")) {
                enclosure = readEnclosure(parser);
            } else {
                skip(parser);
            }

        }
        return new RssMessage(title, description, link, guid, enclosure, image);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes discription tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "itunes:summary");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "itunes:summary");
        return description;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        return "";

    }

    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        String name = "";
        while( !name.equals("url") ) {
            parser.next();
            name = parser.getName();
            if (name == null) name = "";
        }
        String image = readText(parser);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "image");
        return image;
    }

    private String readGuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "guid");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "guid");
        return guid;
    }

    private String readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
        String enclosure = parser.getAttributeValue(ns, "url");
        parser.next();
        return enclosure;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        String name = parser.getName();
        if(name.equals("channel")){
            return;
        }
        else if(name.equals("head")){
            return;
        }
        else if(name.equals("body")){
            return;
        }
        else{
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

    }


    @Override
    protected RssFeed doInBackground(String... params) {
        InputStream stream = null;
        boolean feed = false;
        if(params.length > 1){
            if(params[1] == "true"){
                feed = true;
            }
        }

        try {
            stream = this.downloadUrl(params[0]);
            return this.parse(stream, feed);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(RssFeed result){
        /*final RssFeed resultFeed = result;
        final ArrayList<RssFeed> pods = new ArrayList<RssFeed>();
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        pods.add(resultFeed);
        activity.runOnUiThread(new Runnable() {
            public void run() {

                final TextView tv1 = (TextView) activity.findViewById(R.id.texview1);
                tv1.setText(resultFeed.toString() + "\n\n");
                for(RssMessage message : resultFeed.getMessages()) {
                    tv1.append(message.toString()+ "\n\n");
                }



            }
        });*/
    }
}

package com.gav.podcaster.com.gav.rss.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gav on 11/06/15.
 */
public class RssFeed {

    int id;
    final String title;
    final String link;
    final String description;
    final String language;
    final String copyright;
    final String pubDate;
    final String image;

    final List<RssMessage> entries;

    public RssFeed(){
        this.title = "";
        this.link = "";
        this.description = "";
        this.language = "";
        this.copyright = "";
        this.pubDate = "";
        this.image = "";
        this.id = -1;
        entries = new ArrayList<RssMessage>();
    }

    public RssFeed(String title, String link, String description, String language,
                String copyright, String pubDate, String image) {
        this.id = -1;
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
        this.image = image;
        entries = new ArrayList<RssMessage>();
    }

    public List<RssMessage> getMessages() {
        return entries;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description
                + ", language=" + language + ", link=" + link + ", pubDate="
                + pubDate + ", title=" + title + "]";
    }
}

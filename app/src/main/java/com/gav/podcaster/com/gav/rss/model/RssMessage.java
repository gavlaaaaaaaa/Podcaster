package com.gav.podcaster.com.gav.rss.model;

/**
 * Created by Gav on 11/06/15.
 */
public class RssMessage {

    String title;
    String description;
    String link;
    String guid;
    String enclosure;

    public RssMessage(String title, String description, String link,
                      String guid, String enclosure){
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.enclosure = enclosure;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public String getGuid() {
        return guid;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", guid=" + guid
                + ", enclosure=" + enclosure + "]";
    }



}

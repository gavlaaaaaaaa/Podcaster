package com.gav.podcaster.com.gav.rss.model;

/**
 * Created by Gav on 11/06/15.
 */
public class RssMessage {

    int id;
    int podcast_id;
    String title;
    String description;
    String link;
    String guid;
    String enclosure;
    String image;

    public RssMessage(String title, String description, String link,
                      String guid, String enclosure, String image){
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.enclosure = enclosure;
        this.image = image;
        id = -1;
        podcast_id=-1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPodcast_id() {
        return podcast_id;
    }

    public void setPodcast_id(int podcast_id) {
        this.podcast_id = podcast_id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", guid=" + guid
                + ", enclosure=" + enclosure + ", image=" + image +"]";
    }



}

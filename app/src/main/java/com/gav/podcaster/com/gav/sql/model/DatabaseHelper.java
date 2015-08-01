package com.gav.podcaster.com.gav.sql.model;

/**
 * Created by Gav on 01/08/15.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gav.podcaster.com.gav.rss.model.RssFeed;
import com.gav.podcaster.com.gav.rss.model.RssMessage;

public class DatabaseHelper extends SQLiteOpenHelper{

    // DB Name and Version
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "podcaster";

    //DB Table names
    private static final String TABLE_PODCAST = "podcast";
    private static final String TABLE_MESSAGE = "message";

    //Common column names
    private static final String KEY_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_LINK = "link";
    private static final String COL_DESC = "description";
    private static final String COL_IMAGE = "image";


    //PODCAST Table column names
    private static final String COL_LANGUAGE = "language";
    private static final String COL_COPYRIGHT = "copyright";
    private static final String COL_PUBDATE = "pubdate";

    //MESSAGE table column names
    private static final String COL_GUID = "guid";
    private static final String COL_ENCLOSURE = "enclosure";
    private static final String FKEY_PODCAST_ID = "podcast_id";

    //Table Create Statements
    //PODCAST Table
    private static final String CREATE_TABLE_PODCAST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PODCAST + "(" + KEY_ID + " INTEGER PRIMARY KEY," + COL_TITLE
            + " TEXT," + COL_LINK
            + " TEXT," + COL_DESC
            + " TEXT," + COL_IMAGE
            + " TEXT," + COL_LANGUAGE
            + " TEXT," + COL_COPYRIGHT
            + " TEXT," + COL_PUBDATE
            + " TEXT," +")";

    //MESSAGE Table
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE "
            + TABLE_MESSAGE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + FKEY_PODCAST_ID
            + " INTEGER," + COL_TITLE
            + " TEXT," + COL_LINK
            + " TEXT, " + COL_DESC
            + " TEXT, " + COL_GUID
            + " TEXT, " + COL_ENCLOSURE
            + " TEXT, " + COL_IMAGE
            + " TEXT" + ")";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(CREATE_TABLE_PODCAST);
        db.execSQL(CREATE_TABLE_MESSAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PODCAST);

        //recreate db
        onCreate(db);
    }

    //close db
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //dump all data from database and start a fresh
    public void restartDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        // drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PODCAST);

        //recreate db
        onCreate(db);
    }

    //create podcast in db
    public long createPodcast(RssFeed podcast){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, podcast.getTitle());
        values.put(COL_LINK, podcast.getLink());
        values.put(COL_DESC, podcast.getDescription());
        values.put(COL_IMAGE, podcast.getImage());
        values.put(COL_LANGUAGE, podcast.getLanguage());
        values.put(COL_COPYRIGHT, podcast.getCopyright());
        values.put(COL_PUBDATE, podcast.getPubDate());

        //insert feed row into db
        long feed_id = db.insert(TABLE_PODCAST, null, values);

        return feed_id;
    }

    //get podcast from db
    public RssFeed getPodcast(long feed_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PODCAST + " WHERE "
                + KEY_ID + " = " + feed_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        RssFeed podcast = new RssFeed(
                c.getString(c.getColumnIndex(COL_TITLE)),
                c.getString(c.getColumnIndex(COL_LINK)),
                c.getString(c.getColumnIndex(COL_DESC)),
                c.getString(c.getColumnIndex(COL_LANGUAGE)),
                c.getString(c.getColumnIndex(COL_COPYRIGHT)),
                c.getString(c.getColumnIndex(COL_PUBDATE)),
                c.getString(c.getColumnIndex(COL_IMAGE)));
        podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        c.close();

        //add all entries for the podcast
        podcast.getMessages().addAll(this.getMessagesForPodcast(podcast.getId()));

        return podcast;
    }

    //get all podcasts from db
    public List<RssFeed> getAllPodcasts() {
        List<RssFeed> podcasts = new ArrayList<RssFeed>();
        String selectQuery = "SELECT * FROM " + TABLE_PODCAST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //iterate through days and add them to days list
        if(c.moveToFirst()){
            do {
                RssFeed podcast = new RssFeed(
                        c.getString(c.getColumnIndex(COL_TITLE)),
                        c.getString(c.getColumnIndex(COL_LINK)),
                        c.getString(c.getColumnIndex(COL_DESC)),
                        c.getString(c.getColumnIndex(COL_LANGUAGE)),
                        c.getString(c.getColumnIndex(COL_COPYRIGHT)),
                        c.getString(c.getColumnIndex(COL_PUBDATE)),
                        c.getString(c.getColumnIndex(COL_IMAGE)));
                podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));


                podcasts.add(podcast);
            } while (c.moveToNext());
        }
        c.close();
        return podcasts;
    }

    //update a podcast
    public int updatePodcast(RssFeed podcast){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, podcast.getTitle());
        values.put(COL_LINK, podcast.getLink());
        values.put(COL_DESC, podcast.getDescription());
        values.put(COL_IMAGE, podcast.getImage());
        values.put(COL_LANGUAGE, podcast.getLanguage());
        values.put(COL_COPYRIGHT, podcast.getCopyright());
        values.put(COL_PUBDATE, podcast.getPubDate());

        return db.update(TABLE_PODCAST, values, KEY_ID + " = ?", new String[] { String.valueOf(podcast.getId()) } );
    }

    //delete a podcast
    public void deletePodcast(long podcast_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PODCAST, KEY_ID + " = ?", new String[] { String.valueOf(podcast_id)});
    }



    //create message
    public long createMessage(RssMessage message, long podcast_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FKEY_PODCAST_ID, podcast_id);
        values.put(COL_TITLE, message.getTitle());
        values.put(COL_LINK, message.getLink());
        values.put(COL_DESC, message.getDescription());
        values.put(COL_IMAGE, message.getImage());
        values.put(COL_GUID, message.getGuid());
        values.put(COL_ENCLOSURE, message.getEnclosure());

        //insert message row into db
        long msg_id = db.insert(TABLE_MESSAGE, null, values);

        return msg_id;
    }

    //get Message
    public RssMessage getMessage(long msg_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE + " WHERE "
                + KEY_ID + " = " + msg_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        RssMessage msg = new RssMessage(
                c.getString(c.getColumnIndex(COL_TITLE)),
                c.getString(c.getColumnIndex(COL_DESC)),
                c.getString(c.getColumnIndex(COL_LINK)),
                c.getString(c.getColumnIndex(COL_GUID)),
                c.getString(c.getColumnIndex(COL_ENCLOSURE)),
                c.getString(c.getColumnIndex(COL_IMAGE))
        );

        msg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        msg.setPodcast_id(c.getInt(c.getColumnIndex(FKEY_PODCAST_ID)));
        c.close();

        return msg;
    }

    //get messages by podcast
    public List<RssMessage> getMessagesForPodcast(long podcast_id){
        List<RssMessage> messages = new ArrayList<RssMessage>();

        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " + FKEY_PODCAST_ID + " = '" + podcast_id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do {
                RssMessage msg = getMessage(c.getInt(c.getColumnIndex(KEY_ID)));
                messages.add(msg);
            } while (c.moveToNext());
        }
        c.close();
        return messages;
    }

    //get all messages
    public List<RssMessage> getAllMessages() {
        List<RssMessage> messages = new ArrayList<RssMessage>();
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //iterate through days and add them to days list
        if(c.moveToFirst()){
            do {
                RssMessage msg = new RssMessage(
                        c.getString(c.getColumnIndex(COL_TITLE)),
                        c.getString(c.getColumnIndex(COL_DESC)),
                        c.getString(c.getColumnIndex(COL_LINK)),
                        c.getString(c.getColumnIndex(COL_GUID)),
                        c.getString(c.getColumnIndex(COL_ENCLOSURE)),
                        c.getString(c.getColumnIndex(COL_IMAGE))
                );

                msg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                msg.setPodcast_id(c.getInt(c.getColumnIndex(FKEY_PODCAST_ID)));
                messages.add(msg);
            } while (c.moveToNext());
        }
        c.close();
        return messages;
    }


    //update message
    public int updateMessage(RssMessage message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FKEY_PODCAST_ID, message.getPodcast_id());
        values.put(COL_TITLE, message.getTitle());
        values.put(COL_LINK, message.getLink());
        values.put(COL_DESC, message.getDescription());
        values.put(COL_IMAGE, message.getImage());
        values.put(COL_GUID, message.getGuid());
        values.put(COL_ENCLOSURE, message.getEnclosure());

        return db.update(TABLE_MESSAGE, values, KEY_ID + " = ?", new String[] { String.valueOf(message.getId()) } );
    }

    //delete message
    public void deleteMessage(long msg_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGE, KEY_ID + " = ?", new String[] { String.valueOf(msg_id)});
    }



}

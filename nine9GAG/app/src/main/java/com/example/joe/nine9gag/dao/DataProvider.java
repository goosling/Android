package com.example.joe.nine9gag.dao;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by joe on 2016/4/19.
 */
public class DataProvider extends ContentProvider {

    private static final String TAG = DataProvider.class.getSimpleName();

    static final Object DBLock = new Object();

    public static final String AUTHORITY = "com.storm.9gag.provider";

    public static final String SCHEME = "content://";

    public static final String PATH_FEEDS = "/feeds";

    public static final Uri FEEDS_CONTENT_URI = Uri.parse(AUTHORITY + SCHEME + PATH_FEEDS);

    private static final int FEEDS = 0;

    public static final String FEED_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.storm.9gag.feed";

    private static final UriMatcher mUriMather;

    static{
        mUriMather = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMather.addURI(AUTHORITY, "feeds", FEEDS);
    }


}

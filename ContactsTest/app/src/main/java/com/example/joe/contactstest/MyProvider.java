package com.example.joe.contactstest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by JOE on 2016/2/16.
 */
public class MyProvider extends ContentProvider {

    public static final int TABLE1_DIR = 0;
    public static final int TABLE1_ITEM = 1;
    public static final int TABLE2_DIR = 2;
    public static final int TABLE2_ITEM = 3;

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.joe.contactstest", "table1", TABLE1_DIR);
        uriMatcher.addURI("com.example.joe.contactstest", "table1/#", TABLE1_ITEM);
        uriMatcher.addURI("com.example.joe.contactstest", "table2", TABLE2_DIR);
        uriMatcher.addURI("com.example.joe.contactstest", "table2/#", TABLE2_ITEM);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case TABLE1_DIR:
                //查询表1中所有数据
                break;
            case TABLE1_ITEM:
                //查询表1中单条数据
                break;
            case TABLE2_DIR:
                break;
            case TABLE2_ITEM:
                break;
            default:
                break;
        }

        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.joe.contactstest.table1";
            //...
            default:
                break;
        }
        return null;
    }
}

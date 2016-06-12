package com.example.joe.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.joe.zhihudaily.bean.DailyNews;
import com.example.joe.zhihudaily.support.Constants;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by JOE on 2016/6/12.
 */
public class DailyNewsDataSource {

    private SQLiteDatabase database;
    private DBHelper mHelper;
    private String allColumns[]  = {
        DBHelper.COLUMN_ID, DBHelper.COLUMN_DATE, DBHelper.COLUMN_CONTENT
    };

    public DailyNewsDataSource(Context context) {
        mHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = mHelper.getWritableDatabase();
    }

    public List<DailyNews> insertDailyNewsList(String date, String content) {
        ContentValues mValues = new ContentValues();
        mValues.put(DBHelper.COLUMN_DATE, date);
        mValues.put(DBHelper.COLUMN_CONTENT, content);

        long insertId = database.insert(DBHelper.TABLE_NAME, null, mValues);
        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                allColumns, DBHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        List<DailyNews> newsList = cursorToNewsList(cursor);
        cursor.close();
        return newsList;
    }

    private List<DailyNews> cursorToNewsList(Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0) {
            return new GsonBuilder().create().fromJson(cursor.getString(2), Constants.Types.newsListType);
        }else {
            return null;
        }
    }

    public void updateNewsList(String date, String content) {
        ContentValues mValues = new ContentValues();
        mValues.put(DBHelper.COLUMN_DATE, date);
        mValues.put(DBHelper.COLUMN_CONTENT, content);

        database.update(DBHelper.TABLE_NAME, mValues, DBHelper.COLUMN_DATE + "=" + date, null);
    }

    public void insertOrUpdateNewsList(String date, String content) {
        if(newsOfTheDay(date) != null) {
            updateNewsList(date, content);
        }else {
            insertDailyNewsList(date, content);
        }
    }

    public List<DailyNews> newsOfTheDay(String date) {
        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                allColumns, DBHelper.COLUMN_DATE + " = " + date, null, null, null, null);

        cursor.moveToFirst();
        List<DailyNews> newsList = cursorToNewsList(cursor);
        cursor.close();
        return newsList;
    }
}

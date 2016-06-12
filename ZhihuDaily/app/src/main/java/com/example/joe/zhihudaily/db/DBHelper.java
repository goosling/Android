package com.example.joe.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JOE on 2016/6/12.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "daily_news_lists";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CONTENT = "content";

    public static final String DATABASE_NAME = "daily_news.db";
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE_NAME
            + "(" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " CHAR(8) UNIQUE, "
            + COLUMN_CONTENT + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

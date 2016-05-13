package com.joehu.csdnapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joe on 2016/4/12.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "csdn_app_demo";

    private static final String SQL = "create table tb_newsItem( _id integer primary key autoincrement , " +
            "title text , link text , date text , imgLink text , content text , newstype integer  )";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

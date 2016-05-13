package com.example.joe.nine9gag.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joe on 2016/4/19.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "9gag.db";

    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FeedsDataHelper.FeedsDBInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

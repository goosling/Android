package com.deseweather.modules.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JOE on 2016/3/11.
 */
public class DBManager {
    private static String TAG = DBManager.class.getSimpleName();

    private final int BUFFER_SIZE = 400000;

    public static final String DB_NAME = "china_city.db";

    public static final String PACKAGE_NAME = "com.deseweather";

    public static final String DB_PATH = "/data"+ Environment.getDataDirectory()
            .getAbsolutePath()+"/"+PACKAGE_NAME;

    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return sqLiteDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        sqLiteDatabase = database;
    }

    public void openDatabase() {
        Log.e(TAG, DB_PATH + "/" + DB_NAME);
        this.sqLiteDatabase = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDataBase(String dbfile) {
        try{
            File file = new File(dbfile);
            if(!file.exists()){
                InputStream is = this.context.getResources().openRawResource(R.raw.china_city);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while((count = is.read()) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.sqLiteDatabase.close();
    }
}

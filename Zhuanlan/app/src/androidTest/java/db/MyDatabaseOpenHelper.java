package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joe on 2016/4/9.
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_NEWS = "create table news(" +
            "id integer primary keys autoincrement," +
            "title text," +
            "content text," +
            "publishdate integer," +
            "commentcount integer)";


    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

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

    public static final String CREATE_COMMENT = "create table comment ("
            + "id integer primary key autoincrement, "
            + "content text)";


    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        db.execSQL(CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * 旧的写法，并不好，直接删除数据库
         * db.execSQL("drop table if exists news");
        onCreate(db);*/

        /** 新的版本在表创建的时候需要写版本号*/
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_COMMENT);
                break;
            case 2:
                db.execSQL("alter table comment add column publishdate integer");
                break;
            default:
        }
    }
}

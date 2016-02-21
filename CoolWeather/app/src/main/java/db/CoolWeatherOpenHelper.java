package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JOE on 2016/2/20.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    //province建表语句
    public static final String CREATE_PROVINCE = "create table Province(" +
            "id integer primary key autoincrement," +
            "province_name text," +
            "province_code text)";

    //city建表语句
    public static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "city_code text," +
            "province_id integer)";

    //county建表语句

    public static final String CREATE_COUNTY = "create table County(" +
            "id integer primary key autoincrement," +
            "county_name text," +
            "county_code text," +
            "city_id text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

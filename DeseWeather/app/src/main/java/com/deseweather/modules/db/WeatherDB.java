package com.deseweather.modules.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deseweather.modules.domain.City;
import com.deseweather.modules.domain.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/3/11.
 */
public class WeatherDB {

    private Context context;

    public WeatherDB(Context context) {
        this.context = context;
    }

    public List<Province> loadProvince(SQLiteDatabase db) {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProSort(cursor.getInt(cursor.getColumnIndex("ProSort")));
                province.setProName(cursor.getString(cursor.getColumnIndex("ProName")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<City> loadCity(SQLiteDatabase db, int proID) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("T_City", null, "provinceID = ?", new String[] { String.valueOf(proID) }, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("CityName")));
                city.setProvinceID(proID);
                list.add(city);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}

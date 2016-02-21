package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;

/**
 * Created by JOE on 2016/2/20.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    //版本，数据库
    public static final int DB_VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    //将构造方法私有化
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(
                context, DB_NAME, null, DB_VERSION
        );
        db = coolWeatherOpenHelper.getWritableDatabase();
    }

    //获取CoolWeatherDB的实例
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if(coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //将province存储到数据库
    public void saveProvince(Province province) {
        if(province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("Province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    //从全国读取所有的省份信息
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(
                        cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(
                        cursor.getColumnIndex("province_code")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        return list;
    }

    //将city储存到数据库
    public void saveCity(City city) {
        if(city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id=?",
                new String[] {String.valueOf(provinceId)}, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void saveCounty(County county) {
        if(county != null) {
            ContentValues values = new ContentValues();
            values.put("county_id", county.getId());
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCityCode());
            values.put("city_id", county.getCityCode());
            db.insert("County", null, values);
        }
    }

    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id=?",
                new String[] {String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityCode(cityId);
                list.add(county);
            }while(cursor.moveToNext());
        }
        return list;
    }
}

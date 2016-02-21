package util;

import android.text.TextUtils;

import db.CoolWeatherDB;
import model.City;
import model.County;
import model.Province;

/**
 * Created by JOE on 2016/2/20.
 */
public class Utility {
    //解析和处理服务器返回的省级数据‘’
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if(!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length>0) {
                for(String p: allProvinces) {
                    String array[] = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存放到province中
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
                                               String response, int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length>0) {
                for(String c:allCities) {
                    String array[] = c.split("\\|");
                    City city = new City();
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
                                                 String response, int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if(allCounties!=null && allCounties.length>0) {
                for(String c:allCounties) {
                    String array[] = c.split("\\|");
                    County county = new County();
                    county.setCountyName(array[1]);
                    county.setCityCode(cityId);
                    county.setCountyCode(array[0]);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }

        return false;
    }
}

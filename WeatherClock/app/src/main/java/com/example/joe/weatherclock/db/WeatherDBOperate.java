package com.example.joe.weatherclock.db;

import com.example.joe.weatherclock.bean.CityManager;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by JOE on 2016/6/27.
 */
public class WeatherDBOperate {

    private static WeatherDBOperate mWeatherDBOperate;

    private WeatherDBOperate() {
        Connector.getDatabase();
    }

    public synchronized static WeatherDBOperate getInstance() {
        if(mWeatherDBOperate == null) {
            synchronized (WeatherDBOperate.class)  {
                if(mWeatherDBOperate == null) {
                    mWeatherDBOperate = new WeatherDBOperate();
                }
            }
        }
        return mWeatherDBOperate;
    }

    /**
     * 将城市管理实例存储到数据库
     *
     * @return 是否存储成功
     */
    public boolean saveCityManager(CityManager cityManager) {
        return cityManager != null && cityManager.saveFast();
    }

    public void updateCityManager(CityManager cityManager) {
        if(cityManager != null) {
            cityManager.update(cityManager.getId());
        }
    }

    public void updateCityManagr(CityManager cityManager, String cityName) {
        if(cityManager != null) {
            cityManager.updateAll("cityName = ?", cityName);
        }
    }

    //从数据库读取城市管理信息
    public List<CityManager> loadCityManagers() {
        List<CityManager> cityManagerList;
        cityManagerList = DataSupport.findAll(CityManager.class);
        return cityManagerList;
    }

    public void deleteCityManager(CityManager cityManager) {
        if(cityManager != null) {
            cityManager.delete();
        }
    }

    public int queryCityManager(String cityName) {
        return DataSupport.where("cityName = ?", cityName).count(CityManager.class);
    }

    public int queryCityManager() {
        return DataSupport.count(CityManager.class);
    }

    /**
     * 查询定位城市是否已存在城市管理列表
     *
     * @param locationCity 定位城市名
     * @return 件数
     */
    public int queryCityManageLocationCity(String locationCity) {
        return DataSupport.where("locationCity = ?", locationCity).count(CityManager.class);
    }

}

package com.example.joe.weatherclock.db;

import android.content.ContentValues;

import com.example.joe.weatherclock.bean.AlarmClock;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by JOE on 2016/7/1.
 */
public class AlarmClockOperate {
    private static AlarmClockOperate mAlarmClockOperate;

    private AlarmClockOperate() {
        Connector.getDatabase();
    }

    public static AlarmClockOperate getInstance() {
        if(mAlarmClockOperate == null) {
            synchronized (AlarmClockOperate.class) {
                if(mAlarmClockOperate == null) {
                    mAlarmClockOperate = new AlarmClockOperate();
                }
            }
        }
        return mAlarmClockOperate;
    }

    public boolean saveAlarmClock(AlarmClock alarmClock) {
        return alarmClock != null && alarmClock.saveFast();
    }

    public void updateAlarmClock(AlarmClock alarmClock) {
        if(alarmClock != null) {
            ContentValues values = setContentValues(alarmClock);
            DataSupport.update(AlarmClock.class, values, alarmClock.getId());
        }
    }

    public void updateAlarmClock(boolean onOff, int id) {
        ContentValues values = new ContentValues();
        values.put(WeacDBMetaDataLitePal.AC_ON_OFF, onOff);
        DataSupport.update(AlarmClock.class, values, id);
    }

    public List<AlarmClock> loadAlarmClock() {
        List<AlarmClock> alarmClockList;
        alarmClockList = DataSupport.order("hour, minute asc").find(AlarmClock.class);
        return alarmClockList;
    }

    public void deleteAlarmClock(AlarmClock alarmClock) {
        if(alarmClock != null) {
            alarmClock.delete();
        }
    }



    /**
     * 设置列全部更新的ContentValues
     *
     * @param ac AlarmClock实例
     * @return ContentValues
     */
    private ContentValues setContentValues(AlarmClock ac) {
        ContentValues cv = new ContentValues();
        cv.put(WeacDBMetaDataLitePal.AC_HOUR, ac.getHour());
        cv.put(WeacDBMetaDataLitePal.AC_MINUTE, ac.getMinute());
        cv.put(WeacDBMetaDataLitePal.AC_REPEAT, ac.getRepeat());
        cv.put(WeacDBMetaDataLitePal.AC_WEEKS, ac.getWeeks());
        cv.put(WeacDBMetaDataLitePal.AC_TAG, ac.getTag());
        cv.put(WeacDBMetaDataLitePal.AC_RING_NAME, ac.getRingName());
        cv.put(WeacDBMetaDataLitePal.AC_RING_URL, ac.getRingUrl());
        cv.put(WeacDBMetaDataLitePal.AC_RING_PAGER, ac.getRingPager());
        cv.put(WeacDBMetaDataLitePal.AC_VOLUME, ac.getVolume());
        cv.put(WeacDBMetaDataLitePal.AC_VIBRATE, ac.isVibrate());
        cv.put(WeacDBMetaDataLitePal.AC_NAP, ac.isNap());
        cv.put(WeacDBMetaDataLitePal.AC_NAP_INTERVAL, ac.getNapInterval());
        cv.put(WeacDBMetaDataLitePal.AC_NAP_TIMES, ac.getNapTimes());
        cv.put(WeacDBMetaDataLitePal.AC_WEA_PROMPT, ac.isWeaPrompt());
        cv.put(WeacDBMetaDataLitePal.AC_ON_OFF, ac.isOnOff());
        return cv;
    }
}

package com.example.joe.weatherclock.db;

import com.example.joe.weatherclock.bean.AlarmClock;

import org.litepal.tablemanager.Connector;

/**
 * Created by JOE on 2016/7/1.
 */
public class AlarmClockOperate {
    private static AlarmClock mAlarmClock;

    private AlarmClockOperate() {
        Connector.getDatabase();
    }

    
}

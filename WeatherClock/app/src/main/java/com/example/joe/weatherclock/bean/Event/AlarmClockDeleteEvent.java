package com.example.joe.weatherclock.bean.Event;

import com.example.joe.weatherclock.bean.AlarmClock;

/**
 * Created by JOE on 2016/6/22.
 */
public class AlarmClockDeleteEvent {

    private int mPosition;
    private AlarmClock mAlarmClock;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public AlarmClockDeleteEvent(int position, AlarmClock alarmClock) {
        this.mPosition = position;
        mAlarmClock = alarmClock;
    }

    public AlarmClock getAlarmClock() {
        return mAlarmClock;
    }
}

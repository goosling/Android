package com.example.joe.weatherclock.common;

/**
 * Created by JOE on 2016/6/22.
 *
 * 闹钟状态类
 */
public class WeacStatus {

    /**
     * 启动的AlarmClockOnTimeActivity个数
     */
    public static int sActivityNumber = 0;

    /**
     * 上一次闹钟响起时间
     */
    public static long sLastStartTime = 0;

    /**
     * 上一次响起级别（1：闹钟，2：小睡，0：无）
     */
    public static int sStrikerLevel = 0;
}

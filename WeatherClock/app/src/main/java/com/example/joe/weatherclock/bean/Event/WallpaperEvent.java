package com.example.joe.weatherclock.bean.Event;

/**
 * Created by JOE on 2016/6/22.
 *
 * 壁纸更新event
 */
public class WallpaperEvent {

    private boolean isAppWallpaper = false;

    public WallpaperEvent() {}

    public WallpaperEvent(boolean isAppWallPaper) {
        this.isAppWallpaper = isAppWallPaper;
    }

    public boolean isAppWallpaper() {
        return isAppWallpaper;
    }
}

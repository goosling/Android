package com.example.joe.weatherclock.Listener;

import com.example.joe.weatherclock.bean.RecordDeleteItem;

/**
 * Created by JOE on 2016/6/22.
 *
 * 批量录音删除选中回调接口
 */
public interface RecordCheckChangeListener {

    void onChecked(RecordDeleteItem recordDeleteItem);

    void unChecked(RecordDeleteItem recordDeleteItem);
}

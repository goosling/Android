package com.example.joe.weatherclock.bean;

/**
 * Created by JOE on 2016/6/22.
 *
 * 共通铃声选择项目
 */
public class RingSelectItem {

    private static RingSelectItem ringSelectItem;

    /**
     * 铃声名
     */
    private String mName;

    /**
     * 铃声地址
     */
    private String mUrl;

    /**
     * 铃声界面
     */
    private int mRingPager;

    public int getRingPager() {
        return mRingPager;
    }

    public void setRingPager(int ringPager) {
        mRingPager = ringPager;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    /**
     * 取得共通铃声选择项目实例
     *
     * @return 共通铃声选择项目实例
     */
    public static RingSelectItem getInstance() {
        if(ringSelectItem == null) {
            synchronized (RingSelectItem.class) {
                if(ringSelectItem == null) {
                    ringSelectItem = new RingSelectItem();
                }
            }
        }
        return ringSelectItem;
    }

}

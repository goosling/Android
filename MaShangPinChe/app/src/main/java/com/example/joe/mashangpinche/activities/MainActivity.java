package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.example.joe.mashangpinche.db.ActionResult;
import com.example.joe.mashangpinche.db.Member;
import com.example.joe.mashangpinche.db.PubWithMem;
import com.example.joe.mashangpinche.db.Publication;
import com.example.joe.mashangpinche.views.MyHorizontalPicker;
import com.example.joe.mashangpinche.views.SeekBarWithText;

import java.util.ArrayList;

/**
 * Created by joe on 2016/4/30.
 */
public class MainActivity extends Activity {

    // 页面布局
    private MyHorizontalPicker taPicker;
    private View hpicker_subview_me;
    private MapView mapView;
    private AMap aMap;
    private SeekBarWithText seekBarSpace;
    private SeekBarWithText seekBarTime;
    private Button bt_xunta;
    private Button bt_start;
    private RelativeLayout rl_guide;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    // data
    private Publication myPub;
    private Member member;
    private PubWithMem currentTa;
    private boolean isPhoneCallMadeToCurrentTa = false;

    private ArrayList<View> hPickerSubViewList;

    private ArrayList<PubWithMem> taList;



    // 已经联系过，但不合适的pubwithmem
    private ArrayList<PubWithMem> inproperTaList;

    // 所有的marker，包含me和ta
    private ArrayList<Marker> markerList;
    private int taCount = 0;
    private int taIndex = 0;
    private int currentZoom = MAP_ZOOM_DEFAULT;

    private IwantUApp app;

    private ActionResult actionResult;
    private LocationManagerProxy mAMapLocManager;
    private PublishAndSearchTask publishAndSearchTask;

    // 常亮
    // HSV界面
    public static final int HPICKER_ITEM_CNT = 5;
    public static final int HPICKER_DUMMY_ITEM_CNT = 4;
    public static final int HPICKER_ITEM_GAP = 2;

    public static final float MAP_TILT = 15.0f;
    public static final int MAP_ZOOM_20m = 20;
    public static final int MAP_ZOOM_200m = 17;
    public static final int MAP_ZOOM_500m = 15;
    public static final int MAP_ZOOM_2km = 14;
    public static final int MAP_ZOOM_10km = 12;
    public static final int MAP_ZOOM_city = 11;
    public static final int MAP_ZOOM_region = 8;
    public static final int MAP_ZOOM_country = 4;


    public static final int MAP_ZOOM_LVL0 = MAP_ZOOM_city;
    public static final int MAP_ZOOM_LVL1 = MAP_ZOOM_10km;
    public static final int MAP_ZOOM_LVL2 = MAP_ZOOM_2km;
    public static final int MAP_ZOOM_DEFAULT = MAP_ZOOM_LVL1;

    public static final int SEEKBAR_PROGRESS_TIME_DEFAULT = 1;
    public static final int SEEKBAR_PROGRESS_SPACE_DEFAULT = 1;
    public static final int SEEKBAR_PROGRESS_TIME_MAX = 2;
    public static final int SEEKBAR_PROGRESS_SPACE_MAX = 2;


    public static String SEEKBAR_SPACE_200M_STR = "200米";
    public static String SEEKBAR_SPACE_500M_STR = "500米";
    public static String SEEKBAR_SPACE_2KM_STR = "2公里";
    public static String SEEKBAR_SPACE_10KM_STR = "10公里";
    public static String SEEKBAR_SPACE_CITY_STR = "30公里";

    public static String SEEKBAR_SPACE_LVL0_STR = SEEKBAR_SPACE_CITY_STR;;
    public static String SEEKBAR_SPACE_LVL1_STR = SEEKBAR_SPACE_10KM_STR;;
    public static String SEEKBAR_SPACE_LVL2_STR = SEEKBAR_SPACE_2KM_STR;
    public static String SEEKBAR_SPACE_DEFAULT_STR = SEEKBAR_SPACE_LVL1_STR;

    // unit meter
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_20m = 20;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_200m = 200;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_500m = 500;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_2km = 2000;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_10km = 10000;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_city = 30000;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_region = 100000;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_country = 1565430;

    public static float SEARCH_CRITERIA_SPACE_DISTANCE_LVL0 = SEARCH_CRITERIA_SPACE_DISTANCE_city;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_LVL1 = SEARCH_CRITERIA_SPACE_DISTANCE_10km;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_LVL2 = SEARCH_CRITERIA_SPACE_DISTANCE_2km;
    public static float SEARCH_CRITERIA_SPACE_DISTANCE_DEFAULT = SEARCH_CRITERIA_SPACE_DISTANCE_LVL1;

    public static String SEEKBAR_TIME_5MIN_STR = "5分钟";
    public static String SEEKBAR_TIME_10MIN_STR = "10分钟";
    public static String SEEKBAR_TIME_30MIN_STR = "30分钟";
    public static String SEEKBAR_TIME_1HOUR_STR = "1小时";
    public static String SEEKBAR_TIME_6HOUR_STR = "6小时";
    public static String SEEKBAR_TIME_12HOUR_STR = "12小时";
    public static String SEEKBAR_TIME_24HOUR_STR = "24小时";

    public static String SEARCH_CRITERIA_TIME_DISTANCE_LVL0_STR = SEEKBAR_TIME_24HOUR_STR;
    public static String SEARCH_CRITERIA_TIME_DISTANCE_LVL1_STR = SEEKBAR_TIME_12HOUR_STR;
    public static String SEARCH_CRITERIA_TIME_DISTANCE_LVL2_STR = SEEKBAR_TIME_6HOUR_STR;
    public static String SEEKBAR_TIME_DEFAULT_STR = SEARCH_CRITERIA_TIME_DISTANCE_LVL1_STR;

    // unit minute;
    // 一年的时间范围。
    public static long SEARCH_CRITERIA_TIME_DISTANCE_1min = 1;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_5min = 5;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_10min = 10;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_30min = 30;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_1hour = 60;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_6hour = 360;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_12hour = 720;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_24hour = 1440;

    public static long SEARCH_CRITERIA_TIME_DISTANCE_LVL0 = SEARCH_CRITERIA_TIME_DISTANCE_24hour;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_LVL1 = SEARCH_CRITERIA_TIME_DISTANCE_12hour;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_LVL2 = SEARCH_CRITERIA_TIME_DISTANCE_6hour;
    public static long SEARCH_CRITERIA_TIME_DISTANCE_DEFAULT = SEARCH_CRITERIA_TIME_DISTANCE_LVL1;

    // //位置更新的时间门槛，单位是毫秒，在requestLocationUpdates方法中使用。
    public static final int CONS_LOC_UPDATE_TIME_THRESHOLD = 30000;
    // 位置更新的距离门槛，单位是米，在requestLocationUpdates方法中使用。
    private static final int CONS_LOC_UPDATE_SPACE_THRESHOLD = 200;

    private static final int REQUEST_CODE_MEMBERINFO = 0x01;
    private static final int REQUEST_CODE_FEEDBACK = 0x02;
    private static final int REQUEST_CODE_ABOUTUS = 0x03;
    private static final int REQUEST_CODE_IWANT = 0x04;
    private static final int REQUEST_CODE_CALL = 0x05;
    private static final int REQUEST_CODE_SMS = 0x06;

    private static final float METERS_PER_LATITUDE = 110000;

    public void handleMsg(Message msg) {

    }
}

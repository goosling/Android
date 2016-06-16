package com.example.joe.mashangpinche.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.activities.IwantUApp;
import com.example.joe.mashangpinche.db.PubWithMem;
import com.example.joe.mashangpinche.db.Publication;
import com.example.joe.mashangpinche.utils.AppUtil;

/**
 * Created by JOE on 2016/6/15.
 */
public class MainInfoWindowView extends RelativeLayout {

    private ImageView  iv;
    private TextView tv_gender_and_age;
    private TextView tv_space_time_dest;
    private Button bt_call;
    private Button bt_sms;
    private Activity activity;

    public MainInfoWindowView(Context context) {
        super(context);
        if(context instanceof Activity) {
            this.activity = (Activity)context;
        }

        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_info_window, this, true);

        init();
    }

    public MainInfoWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(context instanceof Activity) {
            this.activity = (Activity)context;
        }

        init();
    }

    public void init() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        this.setBackgroundResource(R.mipmap.infowindow_bg_9);
        iv = (ImageView)this.findViewById(R.id.main_infowindow_iv_portrait);
        tv_gender_and_age = (TextView)this
                .findViewById(R.id.main_infoWindow_tv_gender_and_age);
        tv_space_time_dest = (TextView)this
                .findViewById(R.id.main_infoWindow_tv_space_and_time_and_dest);
        bt_call = (Button)this.findViewById(R.id.main_infoWindow_bt_call);
        bt_sms = (Button)this.findViewById(R.id.main_infoWindow_bt_sms);
        this.invalidate();
    }

    public void createViewContent(Publication myPub, PubWithMem tabPubWithMem) {
        int width = AppUtil.dipRes2Px(activity,
                R.dimen.main_infowindow_portrait_width);
        int height = AppUtil.dipRes2Px(activity,
                R.dimen.main_infowindow_portrait_height);
        IwantUApp app = (IwantUApp)activity.getApplication();
        Drawable drawable = app.getDrawableFromFileName(tabPubWithMem.getPortraitFileName(),
                width, height);

        if(null == drawable) {
            drawable = getResources().getDrawable(R.mipmap.portrait_default_ta);
        }

        iv.setImageDrawable(drawable);

        String gender;
        int age;
        if(tabPubWithMem.getGender() != null &&
                tabPubWithMem.getGender().equals(IwantUApp.CONS_GENDER_FEMALE)) {
            gender = getResources().getString(R.string.main_infowindow_ta_female);
        }else {
            gender = getResources().getString(R.string.main_infowindow_ta_male);
        }

        age = tabPubWithMem.getAge();
        String response = getResources().getString(R.string.main_infowindow_gender_and_age);
        tv_gender_and_age.setText(String.format(response, gender, age));

        String spaceAndTimeInfo = getSpaceAndTimeInfo(myPub, tabPubWithMem.getPublication());
        String destInfo = String.format(getResources().getString(R.string.main_infowindow_ta_destination),
                tabPubWithMem.getDestName());
        tv_space_time_dest.setText(spaceAndTimeInfo + destInfo);
    }

    public void setOnClickListener_bt_call(OnClickListener listener){
        bt_call.setOnClickListener(listener);
    }
    public void setOnClickListener_bt_sms(OnClickListener listener){
        bt_sms.setOnClickListener(listener);
    }

    private String getSpaceAndTimeInfo(Publication myPub, Publication taPub) {
        long timeDistance = (myPub.getDatetime() - taPub.getDatetime()) / 60000;
        Location myLoc = new Location("GPS");
        myLoc.setLatitude(myPub.getLatitude());
        myLoc.setLongitude(myPub.getLongitude());
        Location taLoc = new Location("GPS");
        taLoc.setLatitude(taPub.getLatitude());
        taLoc.setLongitude(taPub.getLongitude());
        int spaceDistanceMeter = (int)myLoc.distanceTo(taLoc);
        int spaceDistanceKilo = (int)(spaceDistanceMeter / 1000);
        String spaceInfo;
        if(spaceDistanceKilo >= 1) {
            spaceInfo = spaceDistanceKilo + "公里";
        }else {
            spaceInfo = spaceDistanceMeter + "米";
        }
        return String.format(getResources().getString(
                R.string.main_infowindow_space_and_time), spaceInfo, timeDistance);
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        Bundle b = new Bundle();
        b.putInt("infowindowheight", h);
        Message msg = new Message();
        msg.what = IwantUApp.MSG_TO_MAIN_INFOWINDOW_DRAWN;
        IwantUApp.msgHandler.sendMessage(msg);
    }
}

package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Member;
import com.example.joe.mashangpinche.views.MyHorizontalPicker;

import java.io.File;

/**
 * Created by joe on 2016/4/30.
 */
public class MemberInfoActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private static final int AGEPICKER_ITEM_CNT = 5;
    private static final int AGEPICKET_DUMMY_ITEM_CNT = 2;

    private Member member;
    private IwantUApp app;

    private int currentAgeIndex;
    private Button bt_confirm;
    private ImageView iv_portrait;
    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private MyHorizontalPicker agePicker;

    private File portraitFile;
    //拍照方式，临时储存的图片
    private File tempPortraitFile;

    private long lastPressedTime;

    private static final int REQUEST_CODE_GALLERY = 0x01;
    private static final int REQUEST_CODE_TAKE_PICTURE = 0x02;
    private static final int REQUEST_CODE_CROP_IMAGE = 0x11;

    private static final long DOUBLE_CLICK_EXIT_INTERVAL = 2000;

    private static final String CONS_TEMP_PORTRAIT_FILE_NAME = "mspc_temp_portrait.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IwantUApp)this.getApplication();
        app.addActivity(this);

        setContentView(R.layout.memberinfo);

        initViews();

        iv_portrait.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
        rg_gender.setOnCheckedChangeListener(this);

        if(Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            tempPortraitFile = new File(Environment.getExternalStorageDirectory(),
                    CONS_TEMP_PORTRAIT_FILE_NAME);
        }else {
            tempPortraitFile = new File(getFilesDir(),
                    CONS_TEMP_PORTRAIT_FILE_NAME);
        }

        initAgePicker();

        // member信息来自于savedInstanceState,即系统从内存回收中恢复过来
        if (savedInstanceState != null) {
            member = savedInstanceState.getParcelable("member");
            Log.d("memberinfo oncreate", "member initialized from savedInstanceState");
        } else {
            // memmber信息来自于rstep2或者main
            Intent i = getIntent();
            member = (Member) i.getParcelableExtra(IwantUApp.ONTOLOGY_MEMBER);
            Log.d("memberinfo oncreate", "member initialized from intent");
        }

        //设置头像
        if(null != member && null != member.getPortraitFileName()
                && !member.getPortraitFileName().equals(IwantUApp.CONS_PORTRAIT_DEFAULT_NAME)) {
            Log.d("memberinfo oncreate", "PortraitFileName is:" + member.getPortraitFileName());
            portraitFile = new File(app.getPortraitFilesDir() + File.separator +
                            member.getPortraitFileName());
            Drawable drawable = Drawable.createFromPath(portraitFile.getAbsolutePath());
            iv_portrait.setImageDrawable(drawable);
        }

        currentAgeIndex = member.getAge() - IwantUApp.CONS_AGE_MIN + AGEPICKET_DUMMY_ITEM_CNT;
        agePicker.moveToSubView(currentAgeIndex);

    }

    private void initViews() {
        agePicker = (MyHorizontalPicker)findViewById(R.id.memberinfo_agepicker);
        bt_confirm = (Button)findViewById(R.id.memberinfo_bt_confirm);
        rg_gender = (RadioGroup)findViewById(R.id.memberinfo_rg_gender);
        rb_male = (RadioButton)findViewById(R.id.memberinfo_rb_male);
        rb_female = (RadioButton)findViewById(R.id.memberinfo_rb_female);
        iv_portrait = (ImageView)findViewById(R.id.memberinfo_iv_image);
    }

    private void checkFileState() {
        if(Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            tempPortraitFile = new File(Environment.getExternalStorageDirectory(),
                    CONS_TEMP_PORTRAIT_FILE_NAME);
        }else {
            tempPortraitFile = new File(getFilesDir(),
                    CONS_TEMP_PORTRAIT_FILE_NAME);
        }
    }

    public void handleMsg(Message msg) {

    }
}

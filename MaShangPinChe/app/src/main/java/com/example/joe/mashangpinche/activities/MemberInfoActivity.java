package com.example.joe.mashangpinche.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Member;
import com.example.joe.mashangpinche.utils.AppUtil;
import com.example.joe.mashangpinche.views.MyHorizontalPicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by joe on 2016/4/30.
 */
public class MemberInfoActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private static final int AGEPICKER_ITEM_CNT = 5;
    private static final int AGEPICKER_DUMMY_ITEM_CNT = 2;

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

        currentAgeIndex = member.getAge() - IwantUApp.CONS_AGE_MIN + AGEPICKER_DUMMY_ITEM_CNT;
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

    public void onStart() {
        super.onStart();
        Log.d("memberinfo", "onStart is invoked");
    }

    public void onResume() {
        super.onResume();
        Log.d("memberinfo", "onResume is invoked");
    }

    public void onPause() {
        super.onPause();
        Log.d("memberinfo", "onPause is invoked");
    }

    public void onStop() {
        super.onStop();
        Log.d("memberinfo", "onStop is invoked");
    }

    public void onRestart() {
        super.onRestart();
        Log.d("memberinfo", "onRestart is invoked");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("member", member);
        Log.d("memberinfo", "onSaveInstanceState is invoked");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("memberinfo onRestoreInstanceState", "age is:" + member.getAge());


        Log.d("memberinfo", "onRestoreInstanceState is invoked");
    }

    /**
     * 初始化age piker。显示每屏显示3个数字，左右各有一个dummy view，默认显示岁数是25.
     */
    private void initAgePicker() {
        int agePickerWidth = (int)getResources().getDimension(R.dimen.memberinfo_hpicker_width);
        int margin = (int)getResources().getDimension(R.dimen.main_hpicker_subview_margin);

        int height = (int)(agePickerWidth / AGEPICKER_ITEM_CNT) - 2*margin;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)agePicker
                .getLayoutParams();
        params.height = height;
        params.width = agePickerWidth;
        agePicker.setLayoutParams(params);

        agePicker.setDummySubViewCnt(AGEPICKER_DUMMY_ITEM_CNT);
        agePicker.init(AGEPICKER_ITEM_CNT, R.drawable.memberinfo_iv_border,
                RelativeLayout.CENTER_HORIZONTAL, 0, margin, IwantUApp.msgHandler,
                IwantUApp.MSG_TO_MEMBERINFO_HPICKER_CHANGED);
        agePicker.setDummySubViewCnt(AGEPICKER_DUMMY_ITEM_CNT);

        for(int i=0; i< AGEPICKER_DUMMY_ITEM_CNT; i++) {
            agePicker.addSubView(new TextView(agePicker.getContext()),
                    agePicker.getItemCnt());
        }

        for(int i = IwantUApp.CONS_AGE_MIN; i < IwantUApp.CONS_AGE_MAX; i++) {
            TextView tv = new TextView(agePicker.getContext());
            tv.setText(Integer.toString(i));
            tv.setGravity(Gravity.CENTER);
            agePicker.addSubView(tv, agePicker.getItemCnt());
        }

        for (int i = 0; i < AGEPICKER_DUMMY_ITEM_CNT; i++) {
            agePicker.addSubView(new TextView(agePicker.getContext()),
                    agePicker.getItemCnt());
        }
    }

    /**
     * 获取输出文件。文件名为memberid+时间戳的16进制表示，格式为png，存储目录为/portraits.
     *
     * @return
     */
    private File getOutputImageFile() {
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)){
            return null;
        }
        File theImageFile = new File(app.getPortraitFilesDir().getPath()
                + File.separator + member.getId()
                + String.format("%012x", System.currentTimeMillis())
                +IwantUApp.CONS_PORTRAIT_FILENAME_SUR);

        return theImageFile;
     }

    /**
     * 用戶是重新拍照或者在已有相片中选择。
     *
     */
    @SuppressLint("NewApi")
    private void startSelectOpenImageActivity() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= 11) {
            builder = new AlertDialog.Builder(this,
                    R.style.progressdialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(R.string.memberinfo_open_image_title)
                .setCancelable(true)
                .setItems(R.array.memberinfo_open_image_array,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent photoPickerIntent = new Intent(
                                                Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent,
                                                REQUEST_CODE_GALLERY);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        Uri mImageCaptureUri = null;

                                        Log.d("memberinfo startSelectOpenImageActivity",
                                                "tempPortraitFile is:"
                                                        + tempPortraitFile
                                                        .toString());
                                        mImageCaptureUri = Uri
                                                .fromFile(tempPortraitFile);
                                        intent.putExtra(
                                                android.provider.MediaStore.EXTRA_OUTPUT,
                                                mImageCaptureUri);
                                        intent.putExtra("return-data", true);
                                        startActivityForResult(intent,
                                                REQUEST_CODE_TAKE_PICTURE);
                                        break;
                                    default:
                                }
                            }
                        });
        AlertDialog selectOpenImageDialog = builder.create();
        selectOpenImageDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        InputStream inputStream;
        FileOutputStream fileOutputStream;
        switch(requestCode) {
            case REQUEST_CODE_GALLERY:
                portraitFile = getOutputImageFile();
                try{
                    inputStream = getContentResolver().openInputStream(data.getData());
                    fileOutputStream = new FileOutputStream(portraitFile);
                    AppUtil.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropFile();
                }catch(Exception e) {
                    Toast.makeText(this, "文件打开失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:
                portraitFile = getOutputImageFile();
                try {
                    Log.d("memberinfo REQUEST_CODE_TAKE_PICTURE", "tempFile is:"
                            + tempPortraitFile.toString());
                    inputStream = new FileInputStream(tempPortraitFile);
                    fileOutputStream = new FileOutputStream(portraitFile);
                    AppUtil.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();
                } catch (Exception e) {
                    Toast.makeText(this, "文件打开失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_CROP_IMAGE:
                String pathName = data.getStringExtra(CropImage.IMAGE_PATH);
                if (tempPortraitFile != null && tempPortraitFile.exists()) {
                    tempPortraitFile.delete();
                }
                if (pathName == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(pathName);
                iv_portrait.setImageBitmap(bm);

                //设置member的头像文件
                if ( portraitFile != null && portraitFile.exists()) {
                    member.setPortraitFileName(portraitFile.getName());
                }
                Log.d("memberinfo REQUEST_CODE_CROP_IMAGE", "portraitFile is:" + portraitFile.getAbsolutePath());
                break;
        }
    }

    public void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);

    }

    public void handleMsg(Message msg) {

    }
}

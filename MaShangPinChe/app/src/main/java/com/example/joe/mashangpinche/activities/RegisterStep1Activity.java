package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Register;
import com.example.joe.mashangpinche.utils.AppUtil;

/**
 * Created by sodiaw on 2016/5/11.
 */
public class RegisterStep1Activity extends Activity{
    private Register register;

    private Button bt_next;
    private EditText et_phoneNum;
    private IwantUApp app;

    private long lastPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IwantUApp)this.getApplication();
        app.addActivity(this);

        setContentView(R.layout.register_step1);
        bt_next = (Button)findViewById(R.id.rstep1_bt_next);
        et_phoneNum = (EditText)findViewById(R.id.rstep1_et);
        bt_next.setOnClickListener(new OnClickListener_next());

        register = (Register) getIntent().getSerializableExtra(IwantUApp.ONTOLOGY_REGISTER);

        if(register == null) {
            register = new Register();
        }

        String phoneNum = register.getPhoneNum();
        if(null != phoneNum && phoneNum.length() > 11) {
            et_phoneNum.setText(phoneNum.toCharArray(), 0, phoneNum.length());
        }
    }

    private class OnClickListener_next implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String phoneNum = et_phoneNum.getText().toString();
            //检测是否为正确的手机号
            if(false == AppUtil.isPhoneNumber(phoneNum)) {
                /*Toast.makeText(getApplicationContext(),
                        R.string.toast_phonenum_illegal, Toast.LENGTH_SHORT)
                        .show();*/
                AppUtil.toast(getApplicationContext(), R.string.toast_phonenum_illegal);
                return;
            }

            register.setPhoneNum(phoneNum);
            register.setImsi(app.getIMSI());
            register.setOSversion(Integer.toString(android.os.Build.VERSION.SDK_INT));
            register.setPhoneManufacturer(android.os.Build.MANUFACTURER);
            register.setPhoneModel(android.os.Build.MODEL);

            Intent intent = new Intent();
            //需要改动
            intent.setClass(getApplicationContext(), RegisterStep2Activity.class);
            intent.putExtra(IwantUApp.ONTOLOGY_REGISTER, register);
            ComponentName cn = getCallingActivity();
            if (null != cn) {
                if (RegisterStep2Activity.class.getName().equals(cn.getClassName())) {
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }else{
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        long timeOff = now - lastPressedTime;

        //结束APP
        if (timeOff < IwantUApp.CONS_DOUBLE_CLICK_EXIT_INTERVAL) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        } else {
            lastPressedTime =  now;
            Toast.makeText(this, R.string.toast_doubleclick_exit,
                    Toast.LENGTH_SHORT).show();
        }
    }
}

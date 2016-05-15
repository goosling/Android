package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Member;
import com.example.joe.mashangpinche.db.Register;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;

/**
 * Created by joe on 2016/4/30.
 */
public class RegisterStep2Activity extends Activity implements View.OnClickListener{

    private Register register;

    private Member member;

    private long timeGotVcode;

    private Button bt_pre = null;
    private Button bt_next = null;
    private Button bt_reGetVcode = null;
    private EditText et_vcode = null;
    private TextView tv_info = null;
    private IwantUApp app = null;

    private static Timer timer;

    private static final int REQUEST_CODE_STEP1 = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IwantUApp)this.getApplication();
        app.addActivity(this);

        setContentView(R.layout.register_step2);

        tv_info = (TextView)findViewById(R.id.rstep2_tv);
        bt_pre = (Button)findViewById(R.id.rstep2_bt_pre);
        bt_next = (Button)findViewById(R.id.rstep2_bt_next);
        bt_reGetVcode = (Button)findViewById(R.id.rstep2_bt_resend);
        et_vcode = (EditText) findViewById(R.id.rstep2_et);

        bt_pre.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        bt_reGetVcode.setOnClickListener(this);
        et_vcode.setInputType(InputType.TYPE_CLASS_NUMBER);

        Intent i = getIntent();
        register = (Register)i.getSerializableExtra(IwantUApp.ONTOLOGY_REGISTER);

        tv_info.setText(String.format(getResources().getString(R.string.tv_vcode_sent),
                register.getPhoneNum()));

        // APP从资源被系统回收中恢复过来，交由onRestoreInstanceState处理
        if (savedInstanceState != null) {
            return;
        }

        //获取验证码
        bt_reGetVcode.setText(R.string.view_vcode_resend_2);
        bt_reGetVcode.setEnabled(false);
        bt_next.setEnabled(false);
        new GetVCodeTask().execute();


    }

    public void onStart() {
        super.onStart();
        Log.d("rstep2", "onStart is invoked");
    }

    public void onResume() {
        super.onResume();
        Log.d("rstep2", "onResume is invoked");
        if (timer != null) {
            restoreTimer();
        }
    }

    public void onPause() {
        super.onPause();
        Log.d("rstep2", "onPause is invoked");
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        Log.d("rstep2", "onStop is invoked");
    }

    public void onRestart() {
        super.onRestart();
        Log.d("rstep2", "onRestart is invoked");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("timeGotVcode", timeGotVcode);
        Log.d("rstep2", "onSaveInstanceState is invoked");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("rstep2", "onRestoreInstanceState is invoked");

        if (savedInstanceState == null) {
            return;
        }

        timeGotVcode = savedInstanceState.getLong("timeGotVcode");
        restoreTimer();
    }

    /**
     * 进入会员信息界面*/
    private void startMemberInfoActivity() {
        Intent i = new Intent();
        member.setAge(IwantUApp.CONS_AGE_DEFAULT);
        member.setGender(IwantUApp.CONS_GENDER_DEFAULT);
        member.setPortraitFileName(IwantUApp.CONS_PORTRAIT_DEFAULT_NAME);

        //将member写入本地文件
        app.setMember(member);

        i.putExtra(IwantUApp.ONTOLOGY_MEMBER, (Parcelable) member);
        i.setClass(this, MemberInfoActivity.class);
        startActivity(i);

        finish();
    }

    /**
     * 检测验证码
     * */
    private class CheckVCodeTask extends AsyncTask<MediaType, Void, String> {

        private Register r;

        @Override
        protected void onPreExecute() {
            r = new Register();
            r.setId(register.getId());
            r.setvCode(register.getvCode());
        }

        @Override
        protected void onPostExecute(String s) {
            if(null == s) {
                return;
            }

            //验证失败
            if(IwantUApp.RESPONSE_CODE_LENGTH == s.length()
                && IwantUApp.RESPONSE_CODE_VCODE_FAIL == Integer.parseInt(
                    s, 16)) {
                IwantUApp.msgHandler
                        .sendEmptyMessage(IwantUApp.MSG_TO_RSTEP2_VCODE_FAIL);
                return;
            }

            //验证成功
            if(IwantUApp.OBJECT_ID_LENGTH == s.length()) {
                Bundle b = new Bundle();
                b.putString("memberID", s);
                Message msg = new Message();
                msg.setData(b);
                msg.what = IwantUApp.MSG_TO_RSTEP2_VCODE_SUCCESS;
                IwantUApp.msgHandler.sendMessage(msg);
            }
        }

        @Override
        protected String doInBackground(MediaType... params) {
            final String url = app.getServerBaseURL() + "checkVCode";
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set("Connection", "Close");
            requestHeaders.setContentType(MediaType.APPLICATION_XML);

            HttpEntity<Register> requestEntity = new HttpEntity<Register>(r,
                    requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(
                    new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(
                    new SimpleXmlHttpMessageConverter());
            ResponseEntity<String> response;

            try {
                response = restTemplate.exchange(url, HttpMethod.POST,
                        requestEntity, String.class);

            } catch (Exception e) {
                if (e instanceof ConnectTimeoutException
                        || e.getCause() instanceof ConnectTimeoutException) {
                    IwantUApp.msgHandler
                            .sendEmptyMessage(IwantUApp.MSG_TO_RSTEP2_EX_CONNECT_TIMEOUT);
                    // 未知错误
                } else {
                    IwantUApp.msgHandler
                            .sendEmptyMessage(IwantUApp.MSG_TO_RSTEP2_EX_UNKNOWN);

                }
                return null;
            }
            return response.getBody();
        }
    }

    private class GetVCodeTask extends AsyncTask<MediaType, Void, String> {

        private Register r;

        @Override
        protected void onPreExecute() {
            r = new Register(register);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(MediaType... params) {
            return null;
        }
    }

    public void handleMsg(Message msg) {

    }
}

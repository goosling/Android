package com.example.joe.mashangpinche.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by JOE on 2016/5/25.
 */
public class MyService extends Service {

    private MediaRecorder mediaRecorder;
    private String num;

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager manager = (TelephonyManager)getApplication().getSystemService(TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    //状态监听器
    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                //来电话了
                case TelephonyManager.CALL_STATE_RINGING:
                    num = incomingNumber;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    start();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    stop();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void start() {
        mediaRecorder = new MediaRecorder();
        //指定音频源，麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        // 设置输出格式(3gp)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile("/mnt/sdcard/" + num + "-" + System.currentTimeMillis() + ".3gp");    // 指定文件路径(SD卡)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);       // 指定编码(AMR_NB)
        try{
            mediaRecorder.prepare();//准备
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //录音
    private void stop() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}

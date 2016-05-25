package com.example.joe.mashangpinche.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joe.mashangpinche.R;

/**
 * Created by JOE on 2016/5/25.
 */
public class ToastUtil {
    private static final int LENGTH_SHORT = 0;
    private static final int LENGTH_LONG = 1;
    private static View oldView, toastView;
    private WindowManager mWindowManager;
    private static int mDuration;
    private static final int WHAT = 100;
    private static Toast toast;
    private static CharSequence oldText;
    private static CharSequence currentText;
    private static ToastUtil instance = null;
    private static TextView textView;

    private ToastUtil(Context context) {
        mWindowManager = (WindowManager)context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        textView = (TextView)toastView.findViewById(R.id.toast_text);
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    private static ToastUtil getInstance(Context context) {
        if(instance == null) {
            synchronized (ToastUtil.class) {
                if(instance == null) {
                    instance = new ToastUtil(context);
                }
            }
        }
        return instance;
    }

    public static ToastUtil makeText(Context context, CharSequence charSequence,
                                     int duration) {
        ToastUtil util = getInstance(context);
        mDuration = duration;
        toast.setText(charSequence);
        currentText = charSequence;
        textView.setText(charSequence);
        return util;
    }

    public static ToastUtil makeText(Context context, int resId, int duration){
        ToastUtil util = getInstance(context);
        mDuration = duration;
        toast.setText(resId);
        currentText = context.getResources().getString(resId);
        textView.setText(context.getResources().getString(resId));
        return util;
    }

    /**
     * 进行Toast显示，在显示之前会取消当前已经存在的Toast
     */
    public void show() {
        long time = 0;
        switch(mDuration) {
            case LENGTH_SHORT:
                time = 2000;
                break;
            case LENGTH_LONG:
                time = 3500;
                break;
            default:
                time = 2000;
                break;
        }
        if (currentText.equals(oldText) && oldView.getParent() != null) {
            toastHandler.removeMessages(WHAT);
            toastView = oldView;
            oldText = currentText;
            toastHandler.sendEmptyMessageDelayed(WHAT, time);
            return;
        }

        cancelOldAlert();
        toastHandler.removeMessages(WHAT);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = android.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.gravity = toast.getGravity();
        params.y = toast.getYOffset();
        if (toastView.getParent() == null) {
            mWindowManager.addView(toastView, params);
        }
        oldView = toastView;
        oldText = currentText;
        toastHandler.sendEmptyMessageDelayed(WHAT, time);
    }

    private Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cancelOldAlert();
            int id = msg.what;
            if(WHAT == id) {
                cancelCurrentAlert();
            }
        }
    };

    private void cancelOldAlert() {
        if (oldView != null) { // 去掉 oldView.getParent() != null 这个参数，然后加上try catch代码块，解决在部分Pad上oldView.getParent()不准确的问题
            try {
                mWindowManager.removeView(oldView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelCurrentAlert() {
        if (toastView != null) {
            try {
                // 去掉 oldView.getParent() != null 这个参数，然后加上try catch代码块，解决在部分Pad上oldView.getParent()不准确的问题
                mWindowManager.removeView(toastView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (oldView != null) {
            try {
                mWindowManager.removeView(oldView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

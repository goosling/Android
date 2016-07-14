package com.example.joe.weatherclock.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.example.joe.weatherclock.R;

import java.io.IOException;

/**
 * Created by JOE on 2016/7/14.
 *
 * 音频播放器
 */
public class AudioPlayer {
    //音频播放
    private MediaPlayer mPlayer;

    //共通音频播放器实例
    private static AudioPlayer mAudioPlayer;

    private final Context mContext;

    //振动
    private Vibrator mVibrator;

    public static boolean sIsStopRecordMusic = false;

    private AudioPlayer(Context context) {
        mContext = context;
    }

    /**
     * 取得音频播放器实例
     *
     * @param context context
     * @return 音频播放器实例
     */
    public static AudioPlayer getInstance(Context context) {
        if(mAudioPlayer == null) {
            synchronized (AudioPlayer.class) {
                if(mAudioPlayer == null) {
                    mAudioPlayer = new AudioPlayer(context);
                }
            }
        }
        return mAudioPlayer;
    }

    /**
     * 停止播放，振动
     */
    public void stop() {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        if(mVibrator != null) {
            mVibrator.cancel();
        }
    }

    private void stopPlay() {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 开始播放
     *
     * @param url     音频文件地址
     * @param looping 是否循环播放
     * @param vibrate 是否振动
     */
    public void play(String url, final boolean looping, final boolean vibrate) {
        stop();
        if(vibrate) {
            vibrate();
        }

        mPlayer = new MediaPlayer();
        try{
            mPlayer.setDataSource(url);
            //设置异步阻塞
            mPlayer.prepareAsync();
        }catch(IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e) {

        }

        // 当准备好时
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                if (looping) {
                    mPlayer.setLooping(true);
                    mPlayer.start();
                } else {
                    mPlayer.start();
                }
            }
        });

        // 当播放出现错误时
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtil.showShortToast(mContext,
                        mContext.getString(R.string.play_fail));
                return false;
            }

        });
    }

    /**
     * 开始播放
     *
     * @param resId   音频资源文件ID
     * @param looping 是否循环播放
     * @param vibrate 是否振动
     */
    public void playRaw(final int resId, boolean looping, boolean vibrate) {
        stop();
        // 当设为振动时
        if (vibrate) {
            vibrate();
        }

        // 设置音频资源文件
        mPlayer = MediaPlayer.create(mContext, resId);
        if (looping) {
            mPlayer.setLooping(true);
            mPlayer.start();
        } else {
            mPlayer.start();
        }
        // 当播放录音停止音时
        if (resId == R.raw.record_stop) {
            sIsStopRecordMusic = true;
        }
        // 当播放完成时
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mPlayer.isLooping()) {
                    stopPlay();
                }

                // 当播放录音停止音时
                if (resId == R.raw.record_stop) {
                    sIsRecordStopMusic = false;
                }

            }
        });
        // 当播放出现错误时
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtil.showShortToast(mContext,
                        mContext.getString(R.string.play_fail));
                return false;
            }

        });
    }

    /**
     * 振动
     */
    public void vibrate() {
        mVibrator = (Vibrator) mContext
                .getSystemService(Context.VIBRATOR_SERVICE);
        // 前一个代表等待多少毫秒启动vibrator，后一个代表vibrator持续多少毫秒停止。
        // 从repeat索引开始的振动进行循环。-1表示只振动一次，非-1表示从pattern的指定下标开始重复振动。
        mVibrator.vibrate(new long[]{1000, 1000}, 0);
    }
}

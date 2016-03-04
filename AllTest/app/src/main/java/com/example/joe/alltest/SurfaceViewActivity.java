package com.example.joe.alltest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

/**
 * Created by JOE on 2016/3/3.
 */
public class SurfaceViewActivity extends Activity {

    private Button play, pause, replay, stop;
    private EditText et_path;
    private SurfaceView sv;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    int currentPosition = 0;
    private boolean isPlaying;
    private final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surfaceview_layout);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        sv = (SurfaceView)findViewById(R.id.sv);
        et_path = (EditText)findViewById(R.id.et_path);

        play = (Button)findViewById(R.id.play);
        pause = (Button)findViewById(R.id.pause);
        replay = (Button)findViewById(R.id.replay);
        stop = (Button)findViewById(R.id.stop);

        play.setOnClickListener(click);
        pause.setOnClickListener(click);
        replay.setOnClickListener(click);
        stop.setOnClickListener(click);

        //为SurfaceHolder添加回调
        sv.getHolder().addCallback(callback);
        //4.0版本下需要设置的属性
        //sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        seekBar.setOnSeekBarChangeListener(change);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if(currentPosition>0) {
                play(currentPosition);
                currentPosition = 0;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
    };

    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {


        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(progress);
            }
        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play:
                    play(0);
                    break;
                case R.id.pause:
                    pause();
                    break;
                case R.id.replay:
                    replay();
                    break;
                case R.id.stop:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };

    protected void stop() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            play.setEnabled(true);
            isPlaying = false;
        }
    }

    protected void play(final int msec) {
        String path = et_path.getText().toString().trim();
        File file = new File(path);
        if(!file.exists()) {
            Toast.makeText(this, "视频文件错误", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setDisplay(sv.getHolder());
            Log.i(TAG, "开始装载");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.seekTo(msec);
                    seekBar.setMax(mediaPlayer.getDuration());
                    //开始进程，更新进度条刻度
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer.getCurrentPosition();
                                    seekBar.setProgress(current);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    play.setEnabled(false);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    play(0);
                    isPlaying = false;
                    return false;
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void replay() {
        if(mediaPlayer.isPlaying() && mediaPlayer!=null) {
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "ChongxinBoFang", Toast.LENGTH_SHORT).show();
            pause.setText("暂停");
            return;
        }
        isPlaying = false;
        play(0);
    }

    protected void pause() {
        if(pause.getText().toString().trim().equals("继续")) {
            pause.setText("暂停");
            mediaPlayer.start();
            Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mediaPlayer.isPlaying() && mediaPlayer!=null) {
            mediaPlayer.pause();
            pause.setText("继续");
            Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
        }
    }
}

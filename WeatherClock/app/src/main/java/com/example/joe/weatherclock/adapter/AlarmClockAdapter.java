package com.example.joe.weatherclock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.joe.weatherclock.R;
import com.example.joe.weatherclock.bean.AlarmClock;

import java.util.List;

/**
 * Created by JOE on 2016/6/28.
 */
public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.MyViewHolder>{


    private final Context mContext;

    /**
     * 是否显示删除按钮
     */
    private boolean mIsDisplayDeleteBtn = false;

    /**
     * 白色
     */
    private int mWhite;

    /**
     * 淡灰色
     */
    private int mWhiteTrans;

    private List<AlarmClock> mList;

    private boolean isCanClick = true;

    public void setIsCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
    }

    public AlarmClockAdapter(Context context, List<AlarmClock> list) {
        mContext = context;
        mList = list;
        mWhite = mContext.getResources().getColor(android.R.color.white);
        mWhiteTrans = mContext.getResources().getColor(R.color.white_trans30);
    }

    

    /**
     * 保存控件实例
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout rippleView;

        TextView time;

        TextView repeat;

        TextView tag;

        ToggleButton tglBtn;

        ImageView deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            rippleView = (MaterialRippleLayout)itemView.findViewById(R.id.ripple_view);
            time = (TextView)itemView.findViewById(R.id.tv_time);
            repeat = (TextView)itemView.findViewById(R.id.tv_repeat);
            tag = (TextView)itemView.findViewById(R.id.tv_tag);
            tglBtn = (ToggleButton)itemView.findViewById(R.id.toggle_btn);
            deleteBtn = (ImageView) itemView.findViewById(R.id.alarm_list_delete_btn);
        }

    }

}

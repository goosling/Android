package com.example.joe.weatherclock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.joe.weatherclock.Listener.OnItemClickListener;
import com.example.joe.weatherclock.R;
import com.example.joe.weatherclock.bean.AlarmClock;
import com.example.joe.weatherclock.bean.Event.AlarmClockDeleteEvent;
import com.example.joe.weatherclock.db.AlarmClockOperate;
import com.example.joe.weatherclock.util.OttoAppConfig;

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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.lv_alarm_clock, parent, false
        ));
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AlarmClock alarmClock = mList.get(position);

        if(mOnItemClickListener != null) {
            holder.rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCanClick) {
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                    }
                }
            });

            holder.rippleView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (isCanClick) {
                        mOnItemClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                        return false;
                    }
                    return true;
                }
            });
        }

        if(alarmClock.isOnOff()) {
            holder.time.setTextColor(mWhite);
            holder.repeat.setTextColor(mWhite);
            holder.tag.setTextColor(mWhite);
        }else {
            holder.time.setTextColor(mWhiteTrans);
            holder.repeat.setTextColor(mWhiteTrans);
            holder.tag.setTextColor(mWhiteTrans);
        }

        //显示删除按钮
        if(mIsDisplayDeleteBtn) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmClockOperate.getInstance().deleteAlarmClock(alarmClock);
                    OttoAppConfig.getInstance().post(new AlarmClockDeleteEvent(holder.getAdapterPosition(), alarmClock));


                }
            });
        }
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

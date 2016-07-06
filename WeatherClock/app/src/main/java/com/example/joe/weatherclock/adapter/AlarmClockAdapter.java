package com.example.joe.weatherclock.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.joe.weatherclock.R;

/**
 * Created by JOE on 2016/6/28.
 */
public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.MyViewHolder>{


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

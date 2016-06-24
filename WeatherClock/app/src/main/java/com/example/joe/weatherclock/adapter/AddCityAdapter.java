package com.example.joe.weatherclock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.joe.weatherclock.R;

import java.util.List;

/**
 * Created by JOE on 2016/6/24.
 */
public class AddCityAdapter extends ArrayAdapter<String> {

    private final Context mContext;

    /**
     * 城市适配器构造方法
     *
     * @param context context
     * @param list    城市列表
     */
    public AddCityAdapter(Context context, List<String> list) {
        super(context, 0, list);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String cityName = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_add_city
                , parent, false);
            viewHolder = new ViewHolder();
            
        }
        return super.getView(position, convertView, parent);
    }

    //保存控件实例
    private final class ViewHolder {
        TextView cityName;
    }
}

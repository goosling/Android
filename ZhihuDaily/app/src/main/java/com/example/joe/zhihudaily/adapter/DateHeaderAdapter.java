package com.example.joe.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.example.joe.zhihudaily.R;
import com.example.joe.zhihudaily.bean.DailyNews;
import com.example.joe.zhihudaily.support.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JOE on 2016/6/12.
 */
public class DateHeaderAdapter implements StickyHeadersAdapter<DateHeaderAdapter.HeaderViewHolder> {

    private List<DailyNews> newsList;

    private DateFormat dateFormat = DateFormat.getDateInstance();

    public DateHeaderAdapter(List<DailyNews> newsList) {
        this.newsList = newsList;
    }

    public void setNewsList(List<DailyNews> newsList) {
        this.newsList = newsList;
    }

    @Override
    public HeaderViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.date_sticky_header, parent, false);

        return new HeaderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HeaderViewHolder headerViewHolder, int position) {
        Calendar calendar = Calendar.getInstance();

        try{
            calendar.setTime(Constants.Dates.simpleDateFormat.parse(newsList.get(position).getDate()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }catch (ParseException ignored) {

        }

        headerViewHolder.title.setText(dateFormat.format(calendar.getTime()));
    }



    @Override
    public long getHeaderId(int position) {
        return newsList.get(position).getDate().hashCode();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.date_text);
        }
    }
}

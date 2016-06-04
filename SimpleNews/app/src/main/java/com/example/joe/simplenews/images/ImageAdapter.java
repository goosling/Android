package com.example.joe.simplenews.images;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joe.simplenews.R;

/**
 * Created by JOE on 2016/6/3.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {


    private OnItemClickListener mOnItemClickListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle;

        public ImageView mImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            mImage = (ImageView)itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, this,getPosition());
            }
        }
    }
}

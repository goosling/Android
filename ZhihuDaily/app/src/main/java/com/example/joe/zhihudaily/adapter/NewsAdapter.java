package com.example.joe.zhihudaily.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.joe.zhihudaily.R;
import com.example.joe.zhihudaily.ZhihuDailyApplication;
import com.example.joe.zhihudaily.bean.DailyNews;
import com.example.joe.zhihudaily.bean.Question;
import com.example.joe.zhihudaily.support.Check;
import com.example.joe.zhihudaily.support.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JOE on 2016/6/12.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CardViewHolder> {

    private List<DailyNews> newsList;

    private ImageLoader mImageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.noimage)
            .showImageOnFail(R.mipmap.noimage)
            .showImageForEmptyUri(R.mipmap.lks_for_blank_url)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsAdapter(List<DailyNews> newsList) {
        this.newsList = newsList;

        setHasStableIds(true);
    }

    public void setNewsList(List<DailyNews> newsList) {
        this.newsList = newsList;
    }

    public void updateNewsList(List<DailyNews> newsList) {
        setNewsList(newsList);
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);

        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                browse(context, position);
            }

            @Override
            public void onOverflowClick(View v, final int position) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.contextual_news_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_share_url:
                                share(context, position);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        DailyNews dailyNews = newsList.get(position);
        mImageLoader.displayImage(dailyNews.getThumbnailUrl(), holder.newsImage, options, animateFirstListener);

        if(dailyNews.getQuestions().size() > 1) {
            holder.questionTitle.setText(dailyNews.getDailyTitle());
            holder.dailyTitle.setText(Constants.Strings.MULTIPLE_DISCUSSION);
        }else {
            holder.questionTitle.setText(dailyNews.getQuestions().get(0).getTitle());
            holder.dailyTitle.setText(dailyNews.getDailyTitle());
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).hashCode();
    }

    private void browse(Context context, int position) {
        DailyNews dailyNews = newsList.get(position);

        if(dailyNews.hasMultipleQuestions()) {
            AlertDialog dialog = createDialog(context, dailyNews,
                    makeGoToZhihuDialogClickListener(context, dailyNews));
            dialog.show();
        }else {
            goToZhihu(context, dailyNews.getQuestions().get(0).getUrl());
        }
    }

    private void share(Context context, int position) {
        DailyNews dailyNews = newsList.get(position);

        if(dailyNews.hasMultipleQuestions()) {
            AlertDialog dialog = createDialog(context, dailyNews,
                    makeGoToZhihuDialogClickListener(context, dailyNews));
            dialog.show();
        }else {
            shareQuestion(context, dailyNews.getQuestions().get(0).getTitle(),
                    dailyNews.getQuestions().get(0).getUrl());
        }
    }

    private AlertDialog createDialog(Context context, DailyNews dailyNews, DialogInterface.OnClickListener listener) {
        String[] questionTitles = getQuestionTitlesAsStringArray(dailyNews);

        return new AlertDialog.Builder(context)
                .setTitle(dailyNews.getDailyTitle())
                .setItems(questionTitles, listener)
                .create();
    }

    private DialogInterface.OnClickListener makeGoToZhihuDialogClickListener(final Context context, final DailyNews dailyNews) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String questionTitle = dailyNews.getQuestions().get(which).getTitle();
                String questionUrl = dailyNews.getQuestions().get(which).getUrl();
                shareQuestion(context, questionTitle, questionUrl);
            }
        };
    }

    private void goToZhihu(Context context, String url) {
        if(ZhihuDailyApplication.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_USE_CLIENT, false)) {
            openUsingBrowser(context, url);
        }else if(Check.isZhihuClientInstalled()) {
            openUsingZhihuClient(context, url);
        }else {
            openUsingBrowser(context, url);
        }
    }

    private void openUsingBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (Check.isIntentSafe(browserIntent)) {
            context.startActivity(browserIntent);
        } else {
            Toast.makeText(context, context.getString(R.string.no_browser), Toast.LENGTH_SHORT).show();
        }
    }

    private void openUsingZhihuClient(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.setPackage(Constants.Information.ZHIHU_PACKAGE_ID);
        context.startActivity(browserIntent);
    }

    private void shareQuestion(Context context, String questionTitle, String questionUrl) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        //noinspection deprecation
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT,
                questionTitle + " " + questionUrl + Constants.Strings.SHARE_FROM_ZHIHU);
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share_to)));
    }

    private String[] getQuestionTitlesAsStringArray(DailyNews dailyNews) {
        return Stream.of(dailyNews.getQuestions()).map(Question::getTitle).toArray(String[]::new);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView newsImage;
        public TextView questionTitle;
        public TextView dailyTitle;
        public ImageView overflow;

        private ClickResponseListener mClickResponseListener;

        public CardViewHolder(View v, ClickResponseListener clickResponseListener) {
            super(v);

            this.mClickResponseListener = clickResponseListener;

            newsImage = (ImageView)v.findViewById(R.id.thumbnail_image);
            questionTitle = (TextView)v.findViewById(R.id.question_title);
            dailyTitle = (TextView)v.findViewById(R.id.daily_title);
            overflow = (ImageView)v.findViewById(R.id.card_share_overflow);

            v.setOnClickListener(this);
            overflow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == overflow) {
                mClickResponseListener.onOverflowClick(v, getAdapterPosition());
            }else {
                mClickResponseListener.onWholeClick(getAdapterPosition());
            }
        }

        public interface ClickResponseListener {
            void onWholeClick(int position);

            void onOverflowClick(View v, int position);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage != null) {
                ImageView imageView = (ImageView)view;
                boolean firstDisplay = !displayImages.contains(imageUri);
                if(firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayImages.add(imageUri);
                }
            }
        }
    }
}

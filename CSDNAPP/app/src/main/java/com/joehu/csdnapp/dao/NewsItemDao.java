package com.joehu.csdnapp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.NewsItem;

/**
 * Created by joe on 2016/4/12.
 */
public class NewsItemDao {

    private DBHelper dbHelper;

    private static final String SQL_ADD = "insert into tb_newsItem (title,link,date,imgLink,content,newstype) values(?,?,?,?,?,?);";

    private static final String SQL_DELETE = "delete from tb_newsItem where newstype = ?";

    public NewsItemDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();

    public void add(NewsItem newsItem) {
        db.execSQL(SQL_ADD, new Object[] {newsItem.getTitle(), newsItem.getLink(), newsItem.getDate(), newsItem.getImgLink(),
                newsItem.getContent(), newsItem.getNewsType()});
        db.close();
    }

    public void deleteAll(int newsType) {
        db.execSQL(SQL_DELETE, new Object[] {newsType});
        db.close();
    }

    public void add(List<NewsItem> newsItems) {
        for(NewsItem newsItem: newsItems) {
            add(newsItem);
        }
    }

    public List<NewsItem> list(int newsType, int currentPage) {
        List<NewsItem> newsItems = new ArrayList<>();
        try{
            int offset = 10 * (currentPage - 1);
            String sql = "select title,link,date,imgLink,content,newstype from tb_newsItem where newstype = ? limit ?,? ";
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(sql, new String[] { newsType + "", offset + "", "" + (offset + 10) });

            NewsItem newsItem = null;

            while (c.moveToNext()) {
                newsItem = new NewsItem();

                String title = c.getString(0);
                String link = c.getString(1);
                String date = c.getString(2);
                String imgLink = c.getString(3);
                String content = c.getString(4);
                Integer newstype = c.getInt(5);

                newsItem.setTitle(title);
                newsItem.setLink(link);
                newsItem.setImgLink(imgLink);
                newsItem.setDate(date);
                newsItem.setNewsType(newstype);
                newsItem.setContent(content);

                newsItems.add(newsItem);

            }
            c.close();
            db.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newsItems;
    }
}

package db;

import org.litepal.crud.DataSupport;

/**
 * Created by joe on 2016/4/9.
 */
public class Comment extends DataSupport {

    private int id;

    private String comment;

    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }
}

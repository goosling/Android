package bean;

/**
 * Created by joehu on 2016/3/29.
 */
public class NewsItem {
    private int id;

    private String title;

    private String link;

    private String date;

    private String imgLink;

    private String content;

    private int newsType;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getContent() {
        return content;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    @Override
    public String toString() {
        return "NewsItem [id=" + id + ", title=" + title + ", link=" + link + ", date=" + date + ", imgLink=" + imgLink
                + ", content=" + content + ", newsType=" + newsType + "]";
    }
}

package bean;

/**
 * Created by joe on 2016/4/12.
 */
public class News {

    public static interface NewsType {
        public static final int TITLE = 1;
        public static final int SUMMARY = 2;
        public static final int CONTENT = 3;
        public static final int IMG = 4;
        public static final int BOLD_TITLE = 5;
    }

    private String title;

    private String summary;

    private String content;

    private String imgLink;

    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "News [title=" + title + ", summary=" + summary + ", content=" + content + ", imageLink=" + imgLink
                + ", type=" + type + "]";
    }
}

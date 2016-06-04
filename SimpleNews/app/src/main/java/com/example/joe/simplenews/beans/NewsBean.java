package com.example.joe.simplenews.beans;

import java.io.Serializable;

/**
 * Created by JOE on 2016/6/3.
 */
public class NewsBean implements Serializable {

    private String docId;

    private String title;

    /**小内容*/
    private String digest;

    /**图片地址*/
    private String imgSrc;

    /**来源
     * */
    private String source;

    private String ptime;

    private String tag;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

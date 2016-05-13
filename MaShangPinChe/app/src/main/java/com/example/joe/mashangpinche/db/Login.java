package com.example.joe.mashangpinche.db;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by sodiaw on 2016/5/10.
 */
@Root(name = "login")
public class Login implements Serializable {

    private static final long serialVersionUID = -8245407402947118589L;

    @Element
    private String taID;

    @Element
    private String phoneNum;

    @Element(required = false)
    private String imsi;

    @Element
    private long datetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTaID() {
        return taID;
    }

    public void setTaID(String taID) {
        this.taID = taID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}

package com.example.joe.mashangpinche.db;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by sodiaw on 2016/5/12.
 */
@Root(name = "register")
public class Register implements Serializable{

    private static final long serialVersionUID = -7957290759006082738L;
    @Element(required = false)
    private String id;
    @Element(required = false)
    private String phoneNum;
    @Element(required = false)
    private String imsi;
    @Element(required = false)
    private String vCode;
    @Element(required = false)
    private long datetime;
    @Element(required = false)
    private String OSversion;
    @Element(required = false)
    private String phoneModel;
    @Element(required = false)
    private String phoneManufacturer;

    public Register() {
        id = "";
        phoneNum = "";
        imsi = "";
        vCode = "";
        datetime = 0;
    }

    public Register(Register r) {
        id = r.getId();
        phoneNum = r.getPhoneNum();
        imsi = r.getImsi();
        vCode = r.getvCode();
        datetime = r.getDatetime();
        OSversion=r.getOSversion();
        phoneModel=r.getPhoneModel();
        phoneManufacturer=r.getPhoneManufacturer();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String toString() {
        return "id:"
                + id
                + ", phoneNum:"
                + phoneNum
                + ", vCode:"
                + vCode
                + ", dateTime:"
                + new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSS", Locale.CHINA)
                .format(datetime);
    }

    public String getOSversion() {
        return OSversion;
    }

    public void setOSversion(String oSversion) {
        OSversion = oSversion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPhoneManufacturer() {
        return phoneManufacturer;
    }

    public void setPhoneManufacturer(String phoneManufacturer) {
        this.phoneManufacturer = phoneManufacturer;
    }

}

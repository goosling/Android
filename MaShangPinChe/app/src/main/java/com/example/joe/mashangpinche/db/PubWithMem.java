package com.example.joe.mashangpinche.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by JOE on 2016/5/29.
 */
public class PubWithMem implements Serializable, Parcelable {

    private static final long serialVersionUID = 7072151070830376014L;

    @Element
    private String id;
    @Element
    private String taID;
    @Element(required = false)
    private String phoneNum;
    @Element
    private long datetime;
    @Element
    private double distance;

    @Element(required = false)
    private String gender;
    @Element(required = false)
    private int age;
    @Element(required = false)
    private String portraitFileName;
    @Element
    private double longitude;
    @Element
    private double latitude;

    @Element(required = false)
    private String destName;
    @Element(required = false)
    private double destLongitude;
    @Element(required = false)
    private double destLatitude;
    @Element(required = false)
    private String serviceDescription;

    public Publication getPublication() {
        Publication p = new Publication();
        p.setId(id);
        p.setTaID(taID);
        p.setLatitude(latitude);
        p.setLongitude(longitude);
        p.setDatetime(datetime);
        p.setDestLatitude(destLatitude);
        p.setDestLongitude(destLongitude);
        p.setServiceDescription(serviceDescription);
        return p;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PubWithMem() {
        id = " ";
        taID = " ";
        datetime = 0;
        longitude = 0;
        latitude = 0;
        serviceDescription = "";

    }

    public PubWithMem(Publication pub) {
        this.id = pub.getId();
        this.taID = pub.getTaID();
        this.latitude = pub.getLatitude();
        this.longitude = pub.getLongitude();
        this.destLatitude = pub.getDestLatitude();
        this.destLatitude = pub.getDestLongitude();
        this.destName = pub.getDestName();
        this.datetime = pub.getDatetime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaID() {
        return taID;
    }

    public void setTaID(String taID) {
        this.taID = taID;
    }

    public long getDatetime() {
        return datetime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public PubWithMem(String theTaID) {
        taID = theTaID;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getPortraitFileName() {
        return portraitFileName;
    }

    public void setPortraitFileName(String portraitFileName) {
        this.portraitFileName = portraitFileName;
    }

    public String toString() {
        String formattedStr = "id=%1$s, phonNum=%2$s, datetime is: %3$s";
        return String.format(formattedStr, id, phoneNum, new SimpleDateFormat(
                "yyyy/MM/dd hh:mm:ss:SSS").format(datetime));
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(taID);
        dest.writeString(phoneNum);
        dest.writeLong(datetime);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(portraitFileName);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(destName);
        dest.writeDouble(destLongitude);
        dest.writeDouble(destLatitude);
        dest.writeString(serviceDescription);
    }

    public static final Parcelable.Creator<PubWithMem> CREATOR = new Parcelable.Creator<PubWithMem>() {
        public PubWithMem createFromParcel(Parcel in) {
            return new PubWithMem(in);
        }

        public PubWithMem[] newArray(int size) {
            return new PubWithMem[size];
        }
    };


    private PubWithMem(Parcel in) {
        id = in.readString();
        taID = in.readString();
        phoneNum = in.readString();
        datetime = in.readLong();
        gender = in.readString();
        age = in.readInt();
        portraitFileName = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        destName = in.readString();
        destLongitude = in.readLong();
        destLatitude = in.readLong();
        serviceDescription = in.readString();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

package com.example.joe.mashangpinche.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by joe on 2016/4/28.
 */
@Root(name = "member")
public class Member implements Parcelable{
    private static final long serialVersionUID = 1L;
    @Element
    private String id;
    @Element(required = false)
    private String phoneNum;
    @Element(required = false)
    private String imsi;
    @Element(required = false)
    private long registerDateTime;
    @Element(required = false)
    private String gender;
    @Element(required = false)
    private int age;
    @Element(required = false)
    private String portraitFileName;

    public Member(){
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(long registerDateTime) {
        this.registerDateTime = registerDateTime;
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

    public String getPortraitFileName() {
        return portraitFileName;
    }

    public void setPortraitFileName(String portraitFileName) {
        this.portraitFileName = portraitFileName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(phoneNum);
        dest.writeString(imsi);
        dest.writeLong(registerDateTime);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(portraitFileName);
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    private Member(Parcel in) {
        id = in.readString();
        phoneNum = in.readString();
        imsi = in.readString();
        registerDateTime = in.readLong();
        gender = in.readString();
        age = in.readInt();
        portraitFileName = in.readString();
    }
}

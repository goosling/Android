package com.example.joe.lightsensortest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JOE on 2016/2/20.
 */
public class Person implements Parcelable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    public static final Parcelable.Creator<Person>CREATOR =
            new Parcelable.Creator<Person>() {
                @Override
                public Person createFromParcel(Parcel source) {
                    return null;
                }

                @Override
                public Person[] newArray(int size) {
                    return new Person[0];
                }
            };
}

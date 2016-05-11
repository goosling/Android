package com.example.joe.mashangpinche.db;

/**
 * Created by sodiaw on 2016/5/11.
 */
public class Member_1 {

    private final String id;

    private final String phoneNum;

    private final String imsi;

    private final long registerTime;

    private final String gender;

    private final int age;

    public static class Builder {
        //required parameters
        private final String id;

        private final String phoneNum;

        private final String imsi;

        //可选参数
        private long registerTime = 0;

        private String gender = "";

        private int age = 0;

        public Builder(String id, String phoneNum, String imsi) {
            this.id = id;
            this.phoneNum = phoneNum;
            this.imsi = imsi;
        }

        public Builder registerTime(long val) {
            registerTime = val;
            return this;
        }

        public Builder gender(String val) {
            gender = val;
            return this;
        }

        public Builder age(int val) {
            age = val;
            return this;
        }

        public Member_1 build() {
            return new Member_1(this);
        }
    }

    private Member_1(Builder builder) {
        id = builder.id;
        phoneNum = builder.phoneNum;
        imsi = builder.imsi;
        registerTime = builder.registerTime;
        age = builder.age;
        gender = builder.gender;
    }
}

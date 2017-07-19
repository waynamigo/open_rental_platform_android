package com.example.yizu.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/15.
 */
public class User extends BmobObject implements Serializable{
    private String name;
    private String phoneNumber;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

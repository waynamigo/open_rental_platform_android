package com.example.yizu.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by q on 2017/7/15.
 */
public class User extends BmobObject implements Serializable{
    private String name;
    private String phoneNumber;
    private String password;
    private Integer grade;//积分
    private String gender;//性别
    private BmobFile TouXiang;

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
    public Integer getGrade()
    {
        return grade;
    }
    public void setGrade(Integer grade){
        this.grade=grade;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public BmobFile getTouXiang() {
        return TouXiang;
    }

    public void setTouXiang(BmobFile touXiang) {
        TouXiang = touXiang;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", grade=" + grade +
                ", gender='" + gender + '\'' +
                ", TouXiang=" + TouXiang +
                '}';
    }
}

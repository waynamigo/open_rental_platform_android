package com.example.yizu.tool;

import android.util.Log;

import com.example.yizu.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by q on 2017/7/19.
 */
public class QueryUser {
    List<User> list = null;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public void  queryByPhone(String phoneNumer){

    }
}

package com.example.yizu;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Created by q on 2017/7/20.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, "e740da9c36e83e41ae0e7d14d7e5c067", "bmob");
        LitePalApplication.initialize(context);
        MobSDK.init(context, "1f6fe120545f1", "1af3024cc2ebad4a2eb99c01219c8051");
    }
    public static Context getContext(){
        return context;
    }
}

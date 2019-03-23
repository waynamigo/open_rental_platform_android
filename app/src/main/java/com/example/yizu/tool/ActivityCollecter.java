package com.example.yizu.tool;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by q on 2017/7/25.
 */

public class ActivityCollecter {
   public static ArrayList<Activity> activities = new ArrayList<>();
    public static void addActivty(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishALL(){
        for(Activity activity:activities){
            activity.finish();
        }
    }
}

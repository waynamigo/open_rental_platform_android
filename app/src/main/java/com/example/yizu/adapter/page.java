package com.example.yizu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by q on 2017/7/25.
 */

public class page {
    private View view;
    private Activity activity;
    public page(Activity activity) {
        this.activity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.activity_view,null);
    }
    public View getView(){
        return view;
    }


}

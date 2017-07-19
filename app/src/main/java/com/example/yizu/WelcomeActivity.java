package com.example.yizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.yizu.bean.Evaluation;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.tool.QueryUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "e740da9c36e83e41ae0e7d14d7e5c067", "bmob");

        Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}

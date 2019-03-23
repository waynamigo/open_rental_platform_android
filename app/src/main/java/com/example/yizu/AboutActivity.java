package com.example.yizu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.tool.ActivityCollecter;

public class AboutActivity extends Activity {
    private ImageView about_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActivityCollecter.addActivty(this);
        about_back=(ImageView)findViewById(R.id.about_back);
        about_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_text:
                Intent intent = new Intent(this,EActivity.class);
                startActivity(intent);
                break;
            case R.id.jifen_text:
                Intent intent1 = new Intent(this, RActivity.class);
                startActivity(intent1);
                break;
            default:
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}

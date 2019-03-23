package com.example.yizu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.yizu.bean.User;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class RsetPasswordActivity extends Activity {
    private ImageView rps_back1;
    private EditText oldpassord;
    private EditText newpassword;
    private EditText newpassword_r;
    private LoadingButton sure;
    private int value=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rset_password);
        ActivityCollecter.addActivty(this);
        rps_back1 = (ImageView) findViewById(R.id.rps_back1);
        oldpassord = (EditText) findViewById(R.id.oldpassword);
        newpassword = (EditText) findViewById(R.id.newpsaaword);
        newpassword_r = (EditText) findViewById(R.id.newpsaaword_r);
        sure = (LoadingButton) findViewById(R.id.ps_sure);
        rps_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sure.startLoading();
                final String oldp = oldpassord.getText().toString().trim();
                final String newp = newpassword.getText().toString().trim();
                final String newpr= newpassword_r.getText().toString().trim();
                if(TextUtils.isEmpty(oldp)){
                    Toast.makeText(RsetPasswordActivity.this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
                    sure.loadingFailed();
                    return;
                }
                if (TextUtils.isEmpty(newp) ||TextUtils.isEmpty(newpr)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    sure.loadingFailed();
                    return;
                }
                if (!newp.equals(newpr)) {
                    Toast.makeText(getApplicationContext(), "密码不一致",
                            Toast.LENGTH_SHORT).show();
                    sure.loadingFailed();
                    return;
                }
                if (newp.length()<6) {
                    Toast.makeText(getApplicationContext(), "密码长度应大于等于6",
                            Toast.LENGTH_SHORT).show();
                    sure.loadingFailed();
                    return;
                }
                BmobQuery<User> query = new BmobQuery<User>();
                query.setLimit(1);
                String objectId = ShareStorage.getShareString(RsetPasswordActivity.this,"ObjectId");
                query.getObject(objectId, new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            if (user.getPassword().equals(oldp)) {
                                user.setPassword(newp);
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            sure.loadingSuccessful();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    sure.reset();
                                                }
                                            }, 1000);
                                        }else {
                                            Toast.makeText(RsetPasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            sure.loadingFailed();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RsetPasswordActivity.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                                sure.loadingFailed();
                            }
                        } else {
                            Toast.makeText(RsetPasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            sure.loadingFailed();
                        }
                    }
                });
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}

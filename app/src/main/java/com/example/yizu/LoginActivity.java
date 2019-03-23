package com.example.yizu;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;


import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.yizu.bean.User;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PhoneNumberConfirm;
import com.iflytek.cloud.thirdparty.L;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //private static final String TAG = "LoginActivity";
    LoadingButton login;
    TextView forget,register;
    EditText userNameEdit,passwordEdit;
    CheckBox remember;
    private View animateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollecter.addActivty(this);
        pref= getSharedPreferences("data", Context.MODE_PRIVATE);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        userNameEdit=(EditText)findViewById(R.id.account);
        passwordEdit=(EditText)findViewById(R.id.password);
        remember=(CheckBox)findViewById(R.id.remember);
        login=(LoadingButton)findViewById(R.id.login);
        forget=(TextView) findViewById(R.id.forget);
        register=(TextView)findViewById(R.id.register);
        login.setResetAfterFailed(true);


        animateView = findViewById(R.id.animate_view);
        boolean isRemember=pref.getBoolean("remember",false);
        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            userNameEdit.setText(account);
            passwordEdit.setText(password);
            remember.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.startLoading();
                final String un = userNameEdit.getText().toString().trim();
                final String up = passwordEdit.getText().toString().trim();
                if(!PhoneNumberConfirm.isMobileNO(un)){
                    login.loadingFailed();
                    Toast.makeText(LoginActivity.this, "您输入的手机号有误！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(up)) {
                    login.loadingFailed();
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 登陆
                    //是否保存密码
                    BmobQuery<User> query = new BmobQuery<User>();// 查询类
                    query.setLimit(1);
                    query.addWhereEqualTo("phoneNumber", un);// 查询条件
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(final List<User> userList, BmobException e) {
                            if (e == null) {
                                login.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(userList.size()==0){
                                            login.loadingFailed();
                                            Toast.makeText(LoginActivity.this,"手机号不存在",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        User user =userList.get(0);
                                        if (user.getPassword().equals(up)) {
                                            editor = pref.edit();
                                            if (remember.isChecked()) {
                                                editor.putBoolean("remember", true);
                                                editor.putString("account", user.getPhoneNumber());
                                                editor.putString("password", user.getPassword());

                                            } else {
                                                editor.clear();
                                            }
                                            editor.putString("ObjectId",user.getObjectId());
                                            editor.putString("PN",user.getPhoneNumber());
                                            editor.apply();
                                            login.loadingSuccessful();
                                            login.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                                                    toNextPage();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                            login.loadingFailed();
                                        }
                                    }
                                },1000);

                            } else {
                                login.loadingFailed();
                                Toast.makeText(LoginActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });


                }

            }
        });

        forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(LoginActivity.this,PhoneVerificationActivity.class);//跳转到忘记密码的页面
                startActivityForResult(intent,1);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);//跳转到注册的页面
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    User user = (User)data.getSerializableExtra("NewUser");
                    userNameEdit.setText(user.getPhoneNumber());
                    passwordEdit.setText(user.getPassword());
                }
                break;
            default:;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void toNextPage(){

        int cx = (login.getLeft() + login.getRight()) / 2;
        int cy = (login.getTop() + login.getBottom()) / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(animateView,cx,cy,0,getResources().getDisplayMetrics().heightPixels * 1.2f);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animateView.setVisibility(View.VISIBLE);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                login.reset();
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}

package com.example.yizu;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.yizu.bean.Evaluation;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.control.DoubleDatePickerDialog;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.OrderInfoUtil2_0;
import com.example.yizu.tool.PayResult;
import com.example.yizu.tool.ShareStorage;

import org.apache.http.impl.entity.EntityDeserializer;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class ConfirmOrderActivity extends AppCompatActivity {
    TextView Startime,Endtime;
    TextView RentMoney,DepMoney,Count,All,Grade,rentedTV,nameTV;
    LinearLayout showorder;
    String initDateTime="2017年7月23日 14:44";
    Button Settle;
   Goods currentgoods;
    Button pay;
    User rentedUser,rentingUser;
    private String GoodsId;
    Long c;
    Date start,end;
    private double needMoney;

    private static final int SDK_PAY_FLAG = 1;
    private String RSA_PRIVATE ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDI/Z4U6SvB4e0HVtHjuUpq20ee6U+xInzm3Fy2zYujERyvK02jHa/REcmgrZxvKvYyMvHq9SfSir2Aryle9SJfFbTyLOawBrj7dtVosJytJoszWMAjfTfdQr96caeGuRBHNWA0zJ7ny1vOPxCjYs4Jue8VjdOg1FnT1D3vpt4/9eHu5Q4p7DM98TyiIPYh9iH+coakEFOQCkCdtQIdbV/9iI2Gk6kPMZvALzKYc1XdUdMyc8eeccbil2RVG88P42BRSUgHqKpCvPhTz18b1MRwZBFPU588/wGessO0HzhpIqcTwO2PxQAQvJRiFMdAHiYb2qnuZ1eN7a9EkG8Lmg35AgMBAAECggEBAIgfA84DsOfpO9+JlDLXTagbBGGCf79mrRKpUX1YreZtc3DLMhRzyZwdoOFaUyUTApQy3paTHDpCTl91wd4WxQWCrP6LHF1A4E0mSRhS7Hl0LjhDNjWS25/VUdr6WEwsz/J6GsMHheNUXcPMAFe3VfVOYLbHS/mTd5a4Go5RBBpqGC2/haKEIx69328jGubDCwUaZ27kpnn/bB3KRwRgyWuW+iPdCxlZb1iLNv4QMbJ6/L6aB1Sn6TzN8RNDpOi6FDeqUt+W3CB4uUQ/b6yL2EdE/D+b/YEqiffBnaFDWGhUNVtqpbynIgFCowy0NlLFJGMYr8dortEZgfaiyNHBv/UCgYEA49iBx8HGHznZycqEGrywk4vLCOt+FDb2dVrz5viGgoepYrNuSswTzi+sguuTlULNpOBzP7tFrWzCrF6chMNf+CFcrq0oYYQX8/xDu6uyJHBmFUNFiHj22mIrQTT9ReIaREo5bWMaNZIqE1pnj/vdPUeoM1csY5BvFfxiCI6z2PcCgYEA4dOZcXTy2H5uPXqyj4oU4krTZE//Eg/YYU5+v+UnA8dp1JkvZgL/2l5zAyNmRLQTLD2UdK1M6CXgukk8mN7Mx0T6oPen0s9yYjqfQT+hrV5uYIRqEOYqOO4aNB0z5f2G4p0MdQeO9T4Rd4jMA+wqlO4rMN1CW4cXz9xrMfhABI8CgYBDawz7zpqRIs0OqJ3uS2b0QakSOpxT3u+OShthfKhQd1PnoLBB8aDqobCqDIre36V54/A2K3OynTv9RQGLR0ReZ4DFLveD41IzH7HrFiLgJWIPaJhqiWCBvgqOgN9wjja3fj7/3xMiRt2sXFPIhv4v48QhbOn1jAdSuhtH7PHKgQKBgATZJR8xcpFBTZ6WRsUcIsyk7JrnuMMrgeq84628/rRvEI+W5lSbVeDbTD7SAL0S3KKDg+9pVRBa19NIBTwG9ICGs5bHGlxFReBj/81Hz3HIaDg8P4azWk60le1ufxG9+qpc0sqkaeZL+dYGItMmu9dMsbKv3V/Xq6QzyD1fZghzAoGAFG8sRtio6uYEdSVQBzKN0HgwlXhzxnXoqCBtItXiDACn+7l4mEHfk4ySeg5mfkFITgr042hXjaz9b1sFL3t7dfcilb0VF7QNzYFpi/J8zFudWRgi6jTzSPeB82BrM7AtXPhatBN6OIoCo2iakP6aCymxBBvwZGor5z0L+zc5sus=";
    public static final String APPID = "2016080500171656";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Intent backData = new Intent();
                        backData.putExtra("state","1");
                        setResult(RESULT_OK,backData);
                        changeGoodsState();
                        saveRecord();

                    } else {
                        Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.ConfirmToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ActivityCollecter.addActivty(this);
        Startime =(TextView) findViewById(R.id.starttime);
        Endtime=(TextView) findViewById(R.id.endtime);
        Settle =(Button)findViewById(R.id.settlebtn);
        showorder=(LinearLayout)findViewById(R.id.show);
        RentMoney=(TextView)findViewById(R.id.rentmoney);
        DepMoney=(TextView)findViewById(R.id.depmoney);
        Count=(TextView)findViewById(R.id.count);
        Grade=(TextView)findViewById(R.id.grade);
        All=(TextView)findViewById(R.id.All);
        pay = (Button) findViewById(R.id.lijizhifu);
        rentedTV = (TextView)findViewById(R.id.rentedTextView);
        nameTV = (TextView)findViewById(R.id.goodsTextView);
        Intent intent = getIntent();
        GoodsId = intent.getStringExtra("GoodsId");
        rentingUser = new User();
        rentingUser.setObjectId(ShareStorage.getShareString(ConfirmOrderActivity.this,"ObjectId"));
        queryRentingUser();
        queryGoods();
        Startime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubleDatePickerDialog dateTimePicKDialog=new DoubleDatePickerDialog(ConfirmOrderActivity.this,initDateTime);
                dateTimePicKDialog.dateTimePicKDialog(Startime);
            }
        });

        Endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubleDatePickerDialog dateTimePicKDialog=new DoubleDatePickerDialog(ConfirmOrderActivity.this,initDateTime);
                dateTimePicKDialog.dateTimePicKDialog(Endtime);
            }
        });

        Settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //计算间隔天数
                if(TextUtils.isEmpty(Startime.getText()) || TextUtils.isEmpty(Endtime.getText())){
                    Toast.makeText(ConfirmOrderActivity.this, "请先选择租用日期！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    start = smdf.parse( Startime.getText().toString());
                    end =smdf.parse(Endtime.getText().toString());
                    c = (end.getTime()-start.getTime())/ (3600 * 24 * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("debug1",start.getTime()-10*3600*1000*24+"");
                Log.d("debug1",calendar.getTime().getTime()+"");
                if(c<=0){
                    Toast.makeText(ConfirmOrderActivity.this, "请输入正确结束日期", Toast.LENGTH_SHORT).show();
                    return;
                }else if(start.getTime()+3600*1000*24<calendar.getTime().getTime()){
                    Toast.makeText(ConfirmOrderActivity.this, "不能选取今天之前的日期", Toast.LENGTH_SHORT).show();
                    return;
                }else if(start.getTime()-10*3600*1000*24>calendar.getTime().getTime()) {
                    Toast.makeText(ConfirmOrderActivity.this, "预约时间不能超过十天哦", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    DepMoney.setText(currentgoods.getDeposit()+"元");//押金
                    //计算本次折扣
                    Double count;
                    if(rentingUser.getGrade()<50){
                        count=1.0;
                    }
                    else if(rentingUser.getGrade()>=50&&rentingUser.getGrade()<100){
                        count=0.98;
                    }
                    else if(rentingUser.getGrade()<200){
                        count=0.95;
                    }
                    else{
                        count=0.90;
                    }
                    Count.setText(count+"折");
                    Grade.setText(rentingUser.getGrade().toString());
                    All.setText(currentgoods.getDeposit()+c*count+"元");
                    RentMoney.setText(currentgoods.getMoneyPer()*count*c+"元");//打折后的租金
                    needMoney = currentgoods.getMoneyPer()*count*c;
                    showorder.setVisibility(View.VISIBLE);
                    Settle.setVisibility(View.GONE);
                }

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rsa = true;
                //构造支付订单参数列表
                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa,needMoney+currentgoods.getDeposit().doubleValue());
                //构造支付订单参数信息
                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                //对支付参数信息进行签名
                String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE, rsa);
                //订单信息
                final String orderInfo = orderParam + "&" + sign;
                //异步处理
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        //新建任务
                        PayTask alipay = new PayTask(ConfirmOrderActivity.this);
                        //获取支付结果
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();


            }
        });
    }
    private void queryGoods(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("user");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.getObject(GoodsId, new QueryListener<Goods>() {
            @Override
            public void done(Goods object, BmobException e) {
                if(e==null){
                    currentgoods = object;
                    rentedUser = currentgoods.getUser();
                    rentedTV.setText(rentedUser.getName());
                    nameTV.setText(currentgoods.getGoodsName());
                }else{
                    Toast.makeText(ConfirmOrderActivity.this,"订单异常",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

        });
    }
   private void queryRentingUser(){
       BmobQuery<User> query = new BmobQuery<User>();
       query.getObject(rentingUser.getObjectId(), new QueryListener<User>() {
           @Override
           public void done(User object, BmobException e) {
               if(e==null){
                   rentingUser = object;
               }else{
                   Toast.makeText(ConfirmOrderActivity.this,"订单异常",Toast.LENGTH_LONG).show();
                   finish();
               }
           }

       });
   }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveRecord(){
        Record record = new Record();
        record.setMake(currentgoods);
        record.setRenting(rentingUser);
        record.setRented(rentedUser);
        record.setMoney(needMoney);
        record.setEval(false);
        record.setDeposit(currentgoods.getDeposit());
        record.setState("交易中");
        record.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                finish();
            }
        });
    }
    private void changeGoodsState(){
        currentgoods.setState("正在租用");
        currentgoods.update(currentgoods.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }
}

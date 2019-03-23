package com.example.yizu;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dalong.carrousellayout.CarrouselLayout;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PictureTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsItemActivity extends AppCompatActivity {
    private TextView itemName1,itemClassification1,itemState1,detail,totalRentMoney,totalRentTimes;
    private ImageView pic1,pic2,pic3;
    private FloatingActionButton button;
    private FloatingActionButton button1;
    private ImageView back;
    private CarrouselLayout carrousel;
    Goods goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);
        ActivityCollecter.addActivty(this);
        itemName1=(TextView)findViewById(R.id.itemName2);
        itemClassification1=(TextView)findViewById(R.id.itemClassification2);
        itemState1=(TextView)findViewById(R.id.itemState2);
        detail=(TextView)findViewById(R.id.itemdetail2);
        totalRentMoney=(TextView)findViewById(R.id.itemtotalRentMoney2);
        totalRentTimes=(TextView)findViewById(R.id.itemtotalRentTimes2);
        back = (ImageView)findViewById(R.id.goodsBack);
        button=(FloatingActionButton)findViewById(R.id.holdon);
        button1=(FloatingActionButton)findViewById(R.id.end);
        pic1 = (ImageView) findViewById(R.id.p1);
        pic2 = (ImageView) findViewById(R.id.p2);
        pic3 = (ImageView) findViewById(R.id.p3);
        Intent intent = getIntent();
        goods = (Goods)intent.getSerializableExtra("myGoods");
        itemName1.setText(goods.getGoodsName());
        itemClassification1.setText(goods.getClassification());
        detail.setText(goods.getDescription());
        pic1.setImageBitmap(PictureTool.showImage(goods.getPath(0)));
        pic2.setImageBitmap(PictureTool.showImage(goods.getPath(1)));
        pic3.setImageBitmap(PictureTool.showImage(goods.getPath(2)));
        carrousel= (CarrouselLayout) findViewById(R.id.itemPic);
       // DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
     //   windowManager.getDefaultDisplay().getMetrics(dm);
        int width=windowManager.getDefaultDisplay().getWidth();
        carrousel.setR(width/3+60)//设置R的大小
                .setAutoRotation(true)//是否自动切换
                .setAutoRotationTime(2000);//自动切换的时间  单位毫秒
        totalMoney();
        totalTime();
        queryState();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(goods.getState().equals("租用结束")){
                    Toast.makeText(GoodsItemActivity.this,"当前物品可租用！！！",Toast.LENGTH_SHORT).show();
                    goods.setState("可租用");
                    itemState1.setText("可租用");
                    saveGoodsState();
                }else {
                    Toast.makeText(GoodsItemActivity.this,"当前物品已下架或已出租！！！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(goods.getState().equals("租用结束") || goods.getState().equals("可租用")){
                    Toast.makeText(GoodsItemActivity.this,"下架成功！！！",Toast.LENGTH_SHORT).show();
                    goods.setState("已下架");
                    itemState1.setText("已下架");
                    saveGoodsState();
                }else {
                    Toast.makeText(GoodsItemActivity.this,"当前物品已下架或已出租！！！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void totalTime(){
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.addWhereEqualTo("make", goods);
        query.count(Record.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    totalRentTimes.setText(""+count);
                }else{

                }
            }
        });
    }
    private void totalMoney(){
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.sum(new String[] { "money" });
        query.addWhereEqualTo("make", goods);
        query.findStatistics(Record.class,new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    if(ary!=null){//
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            double sum = obj.getDouble("_sumMoney");//_(关键字)+首字母大写的列名
                            totalRentMoney.setText(""+sum);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        totalRentMoney.setText("0");
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }
    private void queryState(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addQueryKeys("state");
        query.getObject(goods.getObjectId(), new QueryListener<Goods>() {
            @Override
            public void done(Goods queryGoods, BmobException e) {
                if(e==null){
                    goods.setState(queryGoods.getState());
                    itemState1.setText(goods.getState());
                }
            }
        });
    }
    private void  saveGoodsState(){
       goods.update(goods.getObjectId(), new UpdateListener() {
           @Override
           public void done(BmobException e) {

           }
       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}

package com.example.yizu;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.control.ResultAnimation;
import com.example.yizu.tool.ActivityCollecter;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class SettlementActivity extends AppCompatActivity implements View.OnClickListener{
    private Record record;
    private Goods goods;
    private String rate;
    TextView rateView,rateMoneyView,totalView;
    Button sub_tn;
    private ResultAnimation resultAnimation;
    private ValueAnimator valueAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.settleToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rateView = (TextView)findViewById(R.id.rate);
        rateMoneyView = (TextView)findViewById(R.id.rateMoney);
        totalView = (TextView)findViewById(R.id.totalMoney);
        sub_tn = (Button)findViewById(R.id.submit);
        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra("goSettle");
        goods = new Goods();
        goods.setObjectId(intent.getStringExtra("GoodsId"));
        rate = intent.getStringExtra("rate");
        sub_tn.setOnClickListener(this);
        show();
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    void show(){
        rateMoneyView.setText("￥"+record.getLossOfExpense());
        rateView.setText(rate+"%");
        totalView.setText("￥"+(record.getMoney()+record.getLossOfExpense()));
    }

    @Override
    public void onClick(View v) {
        updateGoods();
    }
    void updateRecord(){
        record.setState("交易成功");
        record.update(record.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                resultAnimation =new ResultAnimation(SettlementActivity.this);
                if(e==null){
                    resultAnimation.setmResultType(1);
                    valueAnimator=new ValueAnimator();
                    setContentView(resultAnimation);
                    resultAnimation.onAnimationUpdate(valueAnimator);
                    Intent intent = new Intent();
                    intent.putExtra("return","1");
                    setResult(RESULT_OK,intent);
               //     finish();
                }else {

                    resultAnimation.setmResultType(2);
                    valueAnimator=new ValueAnimator();
                    setContentView(resultAnimation);
                    resultAnimation.onAnimationUpdate(valueAnimator);

                }
            }
        });
    }
    void updateGoods(){
        goods.setState("租用结束");
        goods.update(goods.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    updateRecord();
                }else {
                    resultAnimation.setmResultType(2);
                    valueAnimator=new ValueAnimator();
                    setContentView(resultAnimation);
                    resultAnimation.onAnimationUpdate(valueAnimator);

                }
            }
        });
    }

}

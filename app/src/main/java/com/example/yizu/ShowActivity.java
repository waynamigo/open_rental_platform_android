package com.example.yizu;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yizu.adapter.ImageAdapter;
import com.example.yizu.adapter.SimpleFragmentPagerAdapter;
import com.example.yizu.adapter.page;
import com.example.yizu.bean.Goods;
import com.example.yizu.fragment.OneFragment;
import com.example.yizu.fragment.TwoFragment;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShowDialog;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class ShowActivity extends AppCompatActivity {
    private ArrayList<View> mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPaintDis;
    private ArrayList<Fragment> list_fragment=new ArrayList<>();                                //定义要装fragment的列表
    private ArrayList<String> list_title=new ArrayList<>();                                //定义要装fragment的列表
    private OneFragment mOneFragment;              //热门推荐fragment
    private TwoFragment mTwoFragment;            //热门收藏fragment
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ImageAdapter imageAdapter;
    private ViewPager viewPager1,viewPager2;
    private final int REQUEST_CODE = 0x1001;
    private TabLayout tabLayout;
    private TextView call,buy,name,rent,position,deposit,classification,starRating;
    private ImageView back;
    private Goods checkedGoods;
    private String rentedPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show);
        ActivityCollecter.addActivty(this);
        Intent intent = getIntent();
        checkedGoods = (Goods) intent.getSerializableExtra("searchGoods");
        if(checkedGoods.getPic2()==null) Log.d("debug1","1null");
        mImageViewList = new ArrayList<>();
        viewPager1=(ViewPager)findViewById(R.id.viewpager1);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red);
        call=(TextView)findViewById(R.id.call);
        buy=(TextView)findViewById(R.id.buy);
        back=(ImageView)findViewById(R.id.back);
        //include_top的文件中的TextView的查找
        name=(TextView)findViewById(R.id.name);
        rent=(TextView)findViewById(R.id.rent);
        position=(TextView)findViewById(R.id.position);
        deposit=(TextView)findViewById(R.id.deposit);
        classification=(TextView)findViewById(R.id.classification);
        starRating=(TextView)findViewById(R.id.starRating);

        //include_top的文件中的TextView的赋值
        name.setText(checkedGoods.getGoodsName());
        rent.setText(String.valueOf(checkedGoods.getMoneyPer()));
        rent.append("元/天");
        position.append(checkedGoods.getArea());
        deposit.append(String.valueOf(checkedGoods.getDeposit()));
        classification.append(checkedGoods.getClassification());
        starRating.append(String.valueOf(checkedGoods.getStarRating()));
        queryPhoneNUM();
        //initData();
        initControls();
       call.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new AlertDialog.Builder(ShowActivity.this)
                       .setTitle("您确定拨打电话？")
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               testCallPhone();

                           }
                       })
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       }).show();
           }
       });
        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkedGoods.getState().equals("可租用")){
                    Intent intent=new Intent(ShowActivity.this,ConfirmOrderActivity.class);//跳转到购买页面
                    intent.putExtra("GoodsId",checkedGoods.getObjectId());
                    startActivityForResult(intent,2);
                }else{
                    ShowDialog.showZhuceDialog(ShowActivity.this,"亲，该物品已经被租用了！");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });


    }


    private void initData(){
        BmobFile file[] = new BmobFile[3];
        file[0] = checkedGoods.getPic1();
        file[1] = checkedGoods.getPic2();
        file[2] = checkedGoods.getPic3();
        imageAdapter = new ImageAdapter(mImageViewList,this,file);
        viewPager1.setAdapter(imageAdapter);
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //避免重复回调        出于兼容性考虑，使用了过时的方法
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //布局完成了就获取第一个小灰点和第二个之间left的距离
                mPaintDis =   llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
                //System.out.println("距离："+mPaintDis);
            }
        });
        //ViewPager滑动Pager监听
        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动过程中的回调
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当滑到第二个Pager的时候，positionOffset百分比会变成0，position会变成1，所以后面要加上position*mPaintDis
                int letfMargin = (int)(mPaintDis*positionOffset)+position*mPaintDis;
                //在父布局控件中设置他的leftMargin边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivRedPoint.getLayoutParams();
                params.leftMargin = letfMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i=0; i<3; i++){
            mImageViewList.add(new page(this).getView());
            //小圆点    一个小灰点是一个ImageView
            ImageView pointView = new ImageView(this);
            pointView.setImageResource(R.drawable.shape_grey);
            //初始化布局参数，父控件是谁，就初始化谁的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i>0){
                //当添加的小圆点的个数超过一个的时候就设置当前小圆点的左边距为10dp;
                params.leftMargin=10;
            }
            //设置小灰点的宽高包裹内容
            pointView.setLayoutParams(params);
            //将小灰点添加到LinearLayout中
            llContainer.addView(pointView);
            imageAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 初始化各控件
     */
    private void initControls() {
        //初始化各fragment
        mOneFragment = new OneFragment();
        mTwoFragment = new TwoFragment();

        //将fragment装进列表中

        list_fragment.add(mOneFragment);
        list_fragment.add(mTwoFragment);

        list_title.add("详细介绍");
        list_title.add("物品评论");


        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this,list_fragment,list_title);
//        viewPager = (ChildViewPager) findViewById(R.id.viewpager);
        viewPager2 = (ViewPager) findViewById(R.id.viewpager2);
        viewPager2.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void testCallPhone() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionChecker.checkSelfPermission(ShowActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE);
            } else {
                callPhone(rentedPhoneNumber);
            }

        } else {
            callPhone(rentedPhoneNumber);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(ShowActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callPhone(rentedPhoneNumber);
        }
    }
    //直接拨号
    private void callPhone(String phoneNum) {
        Uri uri = Uri.parse("tel:" + phoneNum);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }
    void queryPhoneNUM(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("user[phoneNumber]");
        query.getObject(checkedGoods.getObjectId(), new QueryListener<Goods>() {
            @Override
            public void done(Goods goods, BmobException e) {
                rentedPhoneNumber = goods.getUser().getPhoneNumber();
                checkedGoods = goods;
                initData();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//处理结果
        switch (requestCode) {
            case 2:
                if(resultCode==RESULT_OK){
                    if(data!=null && data.getStringExtra("state").equals("1"))checkedGoods.setState("正在租用");
                }
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

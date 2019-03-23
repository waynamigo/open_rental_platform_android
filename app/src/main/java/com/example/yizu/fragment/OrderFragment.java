package com.example.yizu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizu.OrderActivity;
import com.example.yizu.R;
import com.example.yizu.adapter.BusinessAdapter;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.control.RefreshHeader;
import com.example.yizu.control.RefreshLoad;
import com.example.yizu.tool.ShareStorage;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private List<Record> mList = new ArrayList<>();
    private TwinklingRefreshLayout swipeRefresh;

    RecyclerView recyclerView;
    BusinessAdapter adapter;
    View view;
    private int currentPage = 1;
    private String code;
    OrderActivity orderActivity;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);//布局创建
        swipeRefresh = (TwinklingRefreshLayout) view.findViewById(R.id.OrderRefresh);

        swipeRefresh.setEnableLoadmore(false);
        swipeRefresh.setOverScrollBottomShow(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.OrderRecyclerView);
        return  view;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//活动创建  监听事件
        orderActivity = (OrderActivity)getActivity();
        Intent intent = orderActivity.getIntent();
        code = intent.getStringExtra("flag")+currentPage;
        GridLayoutManager layoutManager = new GridLayoutManager(orderActivity, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter =new BusinessAdapter(mList);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setHeaderView(new RefreshHeader(orderActivity));
        swipeRefresh.setBottomView(new RefreshLoad(orderActivity));
        initRV();
        swipeRefresh.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshBusiness();
                        refreshLayout.finishRefreshing();
                    }
                },1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });
    }


    void initRV() {
        BmobQuery<Record> query = new BmobQuery<Record>();
        List<BmobQuery<Record>> andQuerys = new ArrayList<BmobQuery<Record>>();
        BmobQuery<Record> first = new BmobQuery<Record>();
        BmobQuery<Record> second = new BmobQuery<Record>();
        User me = new User();
        me.setObjectId(ShareStorage.getShareString(orderActivity,"ObjectId"));
        Log.d("debug1",code);
        if(code.equals("11")){
             first.addWhereEqualTo("rented",me);
             second.addWhereEqualTo("state","交易中");
        }else if(code.equals("12")){
            first.addWhereEqualTo("rented",me);
            second.addWhereEqualTo("state","交易成功");
        }else if(code.equals("21")){
            first.addWhereEqualTo("renting",me);
            second.addWhereEqualTo("state","交易中");
        }else{
            first.addWhereEqualTo("renting",me);
            second.addWhereEqualTo("state","交易成功");
        }
        andQuerys.add(first);
        andQuerys.add(second);
        query.and(andQuerys);
        query.order("-createdAt");
        query.include("make[objectId|goodsName]");
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> object, BmobException e) {
                //mList = object;
                mList.clear();
                mList.addAll(object);
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void refreshBusiness(){
        initRV();
    }
}

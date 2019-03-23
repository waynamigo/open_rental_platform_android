package com.example.yizu.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yizu.ArticlesActivity;
import com.example.yizu.MainActivity;
import com.example.yizu.R;
import com.example.yizu.control.RefreshHeaderArticle;
import com.example.yizu.control.RefreshLoadArticle;
import com.example.yizu.adapter.ArticleAdapter;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShareStorage;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;



/**
 * ViewPager
 */
public class ArticleFragment extends Fragment {
    private TwinklingRefreshLayout refreshLayout;
    private View view;
    private AVLoadingIndicatorView avloadingIndicatorView_BallPulse;
    int currentPage;
    private Context mContext;
    private List<Goods> articleList = new ArrayList<>();
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private String Name;
    private ArticlesActivity activity;
    private String Positioning;//市
    private String area;//区
    private Double StarRating;
    private int skip;
    private int limit=6;
    private String flag;
    public ArticleFragment() {
        // Required empty public constructor
    }

    public void setNumber(int number) {
        currentPage = number;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skip=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.articles, container, false);
        avloadingIndicatorView_BallPulse=(AVLoadingIndicatorView)view.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        recyclerView = (RecyclerView) view.findViewById(R.id.ArticleRecyclerView);
        refreshLayout=(TwinklingRefreshLayout)view.findViewById(R.id.ArticleRefresh);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOverScrollBottomShow(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (ArticlesActivity) getActivity();
        refreshLayout.setHeaderView(new RefreshHeaderArticle(activity));
        refreshLayout.setBottomView(new RefreshLoadArticle(activity));
        String position [] = new String[2];
        position = ShareStorage.getShareString(activity,"mainShi","mainQu");
        Positioning = position[0];
        area = position[1];
        Intent intent = activity.getIntent();
        Name = intent.getStringExtra("SNTSearch");
        flag = intent.getStringExtra("SearchFlag");
        Log.d("debug1",Name);
        Log.d("debug1",flag);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);
        queryGoods();
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                },1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                        refreshLayout.finishLoadmore();
                    }
                },1000);
            }
        });


    }
    public  void queryGoods() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        BmobQuery<Goods> eq1 = new BmobQuery<Goods>();
        if(flag.equals("1")){
            eq1.addWhereEqualTo("classification", Name);//分类
        }else{
            eq1.addWhereEqualTo("goodsName", Name);//物品名
        }
        //--and条件2
        BmobQuery<Goods> eq2 = new BmobQuery<Goods>();
        eq2.addWhereEqualTo("Positioning", Positioning);//市定位
        BmobQuery<Goods> eq5 = new BmobQuery<Goods>();
        eq2.addWhereEqualTo("state", "可租用");
        List<BmobQuery<Goods>> andQuerys = new ArrayList<BmobQuery<Goods>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        andQuerys.add(eq5);
        switch (currentPage) {
            case 0:
                query.and(andQuerys);
                break;
            case 1:
                BmobQuery<Goods> eq4 = new BmobQuery<Goods>();
                eq4.addWhereEqualTo("area", area);//区定位
                andQuerys.add(eq4);
                query.and(andQuerys);
                break;
            case 2:
                query.and(andQuerys);
                query.order("moneyPer");
                break;
            case 3:
                query.and(andQuerys);
                query.order("-StarRating");
                break;
            default:break;
        }
        query.setSkip(skip).setLimit(limit);
        skip+=6;
        query.findObjects(new FindListener<Goods>()
        {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (goodsList == null) {
                    Toast.makeText(activity, "无此物品的相关信息！", Toast.LENGTH_SHORT).show();
                } else {
                    //articleList.clear();
                    articleList.addAll(goodsList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void refresh(){
        queryGoods();
    }
    void startAnim(){
        avloadingIndicatorView_BallPulse.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avloadingIndicatorView_BallPulse.hide();
        // or avi.smoothToHide();
    }


}














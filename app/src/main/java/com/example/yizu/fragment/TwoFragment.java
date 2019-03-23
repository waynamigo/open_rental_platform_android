package com.example.yizu.fragment;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.adapter.FragmentTwoAdapter;
import com.example.yizu.bean.Evaluation;
import com.example.yizu.bean.Goods;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
    View  mView;
    FragmentTwoAdapter adapterTwo;
    private ShowActivity showActivity;
    private List<Evaluation> evaluationsList = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        showActivity = (ShowActivity) getActivity();
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(showActivity));
      //  evaluationsList.add(evaluation1);
       // evaluationsList.add(evaluation2);
        GridLayoutManager layoutManager = new GridLayoutManager(showActivity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapterTwo = new FragmentTwoAdapter(evaluationsList);
        recyclerView.setAdapter(adapterTwo);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = showActivity.getIntent();
        Goods goods = (Goods)intent.getSerializableExtra("searchGoods");
        initList(goods);
    }
    void initList(Goods goods){
        BmobQuery<Evaluation> query = new BmobQuery<Evaluation>();
        query.addWhereEqualTo("BelongTo", goods);
        query.include("eval[name]");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Evaluation>() {

            @Override
            public void done(List<Evaluation> object,BmobException e) {
                if(e==null){
                     evaluationsList.addAll(object);
                    Log.d("debug1",""+object.size());
                    adapterTwo.notifyDataSetChanged();
                }
            }

        });
    }
}

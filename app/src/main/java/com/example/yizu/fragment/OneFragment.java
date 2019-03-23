package com.example.yizu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;
import com.example.yizu.control.MyScrollView;
import com.example.yizu.control.PublicStaticClass;

public class OneFragment extends Fragment {
    View mView;
    private Goods goods;
    private TextView name1,rent1,position1,deposit1,classification1,starRating1,description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one, container, false);
        name1=(TextView)mView.findViewById(R.id.name1);
        rent1=(TextView)mView.findViewById(R.id.rent1);
        position1=(TextView)mView.findViewById(R.id.position1);
        deposit1=(TextView)mView.findViewById(R.id.deposit1);
        classification1=(TextView)mView.findViewById(R.id.classification1);
        starRating1=(TextView)mView.findViewById(R.id.starRating1);
        description=(TextView)mView.findViewById(R.id.description);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        goods = (Goods)intent.getSerializableExtra("searchGoods");
        name1.append("       ");
        name1.append(goods.getGoodsName());
        rent1.append("       ");
        rent1.append(String.valueOf(goods.getMoneyPer()));
        rent1.append("元/天");
        position1.append("       ");
        position1.append(goods.getPositioning());
        position1.append(goods.getArea());
        deposit1.append("       ");
        deposit1.append(String.valueOf(goods.getDeposit()));
        classification1.append("       ");
        classification1.append(goods.getClassification());
        starRating1.append("       ");
        starRating1.append(String.valueOf(goods.getStarRating()));
        description.append("       ");
        description.append(goods.getDescription());
    }

    private void initView() {
        MyScrollView oneScrollView= (MyScrollView) mView.findViewById(R.id.oneScrollview);
        oneScrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClass.IsTop = true;
                } else {
                    PublicStaticClass.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }
}

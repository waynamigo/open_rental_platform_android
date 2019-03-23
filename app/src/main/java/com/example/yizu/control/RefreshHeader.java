package com.example.yizu.control;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yizu.OrderActivity;
import com.example.yizu.R;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * Created by 10591 on 2017/7/25.
 */

public class RefreshHeader extends FrameLayout implements IHeaderView {
    View rootView;
    Context context;
    ImageView refreshArrow;
    TextView refreshTextView;
    ImageView loadingView;
    public RefreshHeader(@NonNull Context context) {
        super(context);
        this.context  = context;
        if (rootView == null) {
            rootView = View.inflate(context, R.layout.simple, null);
            refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            refreshTextView = (TextView) rootView.findViewById(R.id.tv);
            loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
            addView(rootView);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) refreshTextView.setText("正在为您刷新");
        if (fraction > 1f) refreshTextView.setText("刷新即将完成");
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);



    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            refreshTextView.setText("刷新完成");
            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (refreshArrow.getVisibility() == GONE) {
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText("请稍等");
        refreshArrow.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {

    }

}

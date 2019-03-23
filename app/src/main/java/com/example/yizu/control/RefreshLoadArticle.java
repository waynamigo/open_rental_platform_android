package com.example.yizu.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.yizu.R;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by 10591 on 2017/7/25.
 */

public class RefreshLoadArticle extends FrameLayout implements IBottomView {
    View rootView;
    Context context;
    AVLoadingIndicatorView avloadingIndicatorView_BallPulse;
    TextView refreshTextView;
    public RefreshLoadArticle(@NonNull Context context) {
        super(context);
        this.context  = context;
        if (rootView == null) {
            rootView = View.inflate(context, R.layout.footer1, null);
            refreshTextView = (TextView) rootView.findViewById(R.id.tv1);
            avloadingIndicatorView_BallPulse=(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
            addView(rootView);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) refreshTextView.setText("正在为您加载");
        if (fraction > 1f) refreshTextView.setText("加载即将完成");
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText("请稍等");
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish() {
        refreshTextView.setText("加载完成");


    }

    @Override
    public void reset() {

    }
}

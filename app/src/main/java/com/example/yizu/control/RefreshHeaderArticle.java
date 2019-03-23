package com.example.yizu.control;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.example.yizu.ArticlesActivity;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * Created by 10591 on 2017/7/25.
 */

public class RefreshHeaderArticle extends FrameLayout implements IHeaderView {

    public RefreshHeaderArticle(@NonNull ArticlesActivity context) {
        super(context);


    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {



    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {


    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {

    }

}

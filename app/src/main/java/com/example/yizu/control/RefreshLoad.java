package com.example.yizu.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.yizu.R;
import com.lcodecore.tkrefreshlayout.IBottomView;


/**
 * Created by 10591 on 2017/7/25.
 */

public class RefreshLoad extends FrameLayout implements IBottomView {
    View rootView;
    Context context;
    TextView refreshTextView;

    public RefreshLoad(@NonNull Context context) {
        super(context);
        this.context  = context;
        if (rootView == null) {
            rootView = View.inflate(context, R.layout.footer, null);
            refreshTextView = (TextView) rootView.findViewById(R.id.tv);
            addView(rootView);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {

    }
}

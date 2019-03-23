package com.example.yizu.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by waynamigo on 17-8-4.
 */

public class ResultAnimation extends View implements ValueAnimator.AnimatorUpdateListener {
    private Context mContext;
    /**
     * paint对象
     */
    private Paint mPaint;
    /**
     * Path和对应的空Path用来填充
     */
    private Path mPathCircle;
    private Path mPathCircleDst;
    private Path mPathRight;
    private Path mPathRightDst;
    private Path mPathWrong1;
    private Path mPathWrong2;
    private Path mPathWrong1Dst;
    private Path mPathWrong2Dst;
    /**
     * Path管理
     */
    private PathMeasure mPathMeasure;
    /**
     * 动画
     */
    private ValueAnimator mCircleAnimator;
    private ValueAnimator mRightAnimator;
    private ValueAnimator mWrong1Animator;
    private ValueAnimator mWrong2Animator;
    /**
     * 当前绘制进度占总Path长度百分比
     */
    private float mCirclePercent;
    private float mRightPercent;
    private float mWrong1Percent;
    private float mWrong2Percent;
    /**
     * 线宽
     */
    private int mLineWidth;
    /**
     * 正确动画 错误动画
     */
    public static final int RESULT_RIGHT = 1;
    public static final int RESULT_WRONG = 2;
    /**
     * 当前结果类型
     */
    private int mResultType = RESULT_WRONG;

    public ResultAnimation(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mLineWidth = dp2px(3);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);

        initPath();
    }

    private void initPath() {
        mPathCircle = new Path();
        mPathCircleDst = new Path();
        mPathRight = new Path();
        mPathRightDst = new Path();
        mPathWrong1 = new Path();
        mPathWrong2 = new Path();
        mPathWrong1Dst = new Path();
        mPathWrong2Dst = new Path();

        mPathMeasure = new PathMeasure();

        //实例化对象
        mCircleAnimator = ValueAnimator.ofFloat(0, 1);
        //设置时长为1000ms


        mCircleAnimator.setDuration(1000);
        //开始动画
        mCircleAnimator.start();
        //设置动画监听
        mCircleAnimator.addUpdateListener(this);

        mRightAnimator = ValueAnimator.ofFloat(0, 1);
        mRightAnimator.setDuration(500);
        mRightAnimator.addUpdateListener(this);

        mWrong1Animator = ValueAnimator.ofFloat(0, 1);
        mWrong1Animator.setDuration(300);
        mWrong1Animator.addUpdateListener(this);
        mWrong2Animator = ValueAnimator.ofFloat(0, 1);
        mWrong2Animator.setDuration(300);
        mWrong2Animator.addUpdateListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mResultType == RESULT_RIGHT) {
            mPaint.setColor(Color.BLUE);
        } else {
            mPaint.setColor(Color.RED);
        }

        //画圆  300 300 100
        mPathCircle.addCircle(540,540, 110, Path.Direction.CW);
        mPathMeasure.setPath(mPathCircle, false);
        mPathMeasure.getSegment(0, mCirclePercent * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);
        if (mResultType == RESULT_RIGHT) {
            //画对勾
            mPathRight.moveTo(460,530);
            mPathRight.lineTo(530,600);
            mPathRight.lineTo(610,490);
            if (mCirclePercent == 1) {
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mPathRight, false);
                mPathMeasure.getSegment(0, mRightPercent * mPathMeasure.getLength(), mPathRightDst, true);
                canvas.drawPath(mPathRightDst, mPaint);
                    String Payst = "支付成功";
                    Paint mPaint = new Paint();
                    mPaint.setStrokeWidth(3);
                    mPaint.setTextSize(80);
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextAlign(Paint.Align.LEFT);
                    Rect bounds = new Rect();
                    mPaint.getTextBounds(Payst, 0, Payst.length(), bounds);
                    canvas.drawText(Payst,385, 800, mPaint);

            }
        } else {
            mPathWrong1.moveTo(480,480);
            mPathWrong1.lineTo(600,600);

            mPathWrong2.moveTo(600,480);
            mPathWrong2.lineTo(480,600);

            if (mCirclePercent == 1) {
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mPathWrong1, false);
                mPathMeasure.getSegment(0, mWrong1Percent * mPathMeasure.getLength(), mPathWrong1Dst, true);
                canvas.drawPath(mPathWrong1Dst, mPaint);
            }
            if (mWrong1Percent == 1) {
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mPathWrong2, false);
                mPathMeasure.getSegment(0, mWrong2Percent * mPathMeasure.getLength(), mPathWrong2Dst, true);
                canvas.drawPath(mPathWrong2Dst, mPaint);
                String Payst = "支付失败";
                Paint mPaint = new Paint();
                mPaint.setStrokeWidth(3);
                mPaint.setTextSize(80);
                mPaint.setColor(Color.BLACK);
                mPaint.setTextAlign(Paint.Align.LEFT);
                Rect bounds = new Rect();
                mPaint.getTextBounds(Payst, 0, Payst.length(), bounds);
                canvas.drawText(Payst,385, 800, mPaint);
            }
        }
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //圆形动画
        if (animation.equals(mCircleAnimator)) {
            mCirclePercent = (float) animation.getAnimatedValue();
            invalidate();

            if (mCirclePercent == 1) {
                if (mResultType == RESULT_RIGHT)
                    mRightAnimator.start();
                else
                    mWrong1Animator.start();
            }
        }
        //正确时 对勾动画
        else if (animation.equals(mRightAnimator)) {
            mRightPercent = (float) animation.getAnimatedValue();
            invalidate();
        }
        //错误时 右侧动画
        else if (animation.equals(mWrong1Animator)) {
            mWrong1Percent = (float) animation.getAnimatedValue();
            invalidate();
            if (mWrong1Percent == 1) {
                mWrong2Animator.start();
            }
        }
        //错误时 左侧动画
        else if (animation.equals(mWrong2Animator)) {
            mWrong2Percent = (float) animation.getAnimatedValue();
            invalidate();
        }
    }

    public void setmResultType(int mResultType) {
        this.mResultType = mResultType;
        invalidate();
    }

    /**
     * 固定写死了宽高，可重新手动调配
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(dp2px(300), dp2px(300));
    }
}
package com.example.yizu.control;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.yizu.R;

/**
 * Created by 10591 on 2017/7/22.
 */

import android.content.Context;
import android.text.TextUtils;

import android.widget.Button;


import android.annotation.TargetApi;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;

import java.util.regex.Pattern;


public class NumberAddSubView extends LinearLayout implements View.OnClickListener{
    private Button btn_sub;
    private Button btn_add;
    private EditText tv_value;
    //属性监听
    private int value = 1;//默认值
    private int minValue = 1;//最小值
    private int maxValue = 1000;//最大值

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();//文本的内容
        if (!TextUtils.isEmpty(valueStr)){
            value = Integer.valueOf(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //要让布局和当前类形成一个整体
        View.inflate(context, R.layout.number_add_sub_view, this);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_value = (EditText) findViewById(R.id.tv_value);
        getValue();//获得当前值
        //设置点击事件
        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
        tv_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = tv_value.getText().toString().trim();
                if(TextUtils.isEmpty(num)){
                    value = 0;
                    return;
                }else{
                    if(!isNumber(num))value = 0;
                    else {
                        int Num = Integer.valueOf(num);
                        if(Num>maxValue) value = maxValue;
                        else if(Num<minValue) value = minValue;
                        else value = Num;
                    }

                }

            }
        });
        //得到属性
        if (attrs != null){
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes

                    (context,attrs,R.styleable.NumberAddSubView);
            int value = typedArray.getInt(R.styleable.NumberAddSubView_value,0);
            if (value > 0){
                setValue(value);
            }
            int minValue = typedArray.getInt(R.styleable.NumberAddSubView_minValue,0);
            if (minValue > 0){
                setMinValue(minValue);
            }
            int maxValue = typedArray.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if (maxValue > 0){
                setMaxValue(maxValue);
            }

            Drawable numberAddSubBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberAddSubBackground);
            if (numberAddSubBackground != null){
                setBackground(numberAddSubBackground);
            }
            Drawable numberAddBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberAddBackground);
            if (numberAddBackground != null){
                btn_add.setBackground(numberAddBackground);
            }
            Drawable numberSubBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberSubBackground);
            if (numberSubBackground != null){
                btn_sub.setBackground(numberSubBackground);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sub://减
                subNumber();
                if (listener != null){
                    listener.onButtonSub(v,value);
                }
                break;
            case R.id.btn_add://加
                addNumber();
                if (listener != null){
                    listener.onButtonAdd(v,value);
                }
                break;
        }
    }

    /**
     * 减
     */
    private void subNumber() {
        if (value > minValue){
            value -= 1;
        }
        setValue(value);
    }

    /**
     * 加
     */
    private void addNumber() {
        if (value < maxValue){
            value += 1;
        }
        setValue(value);
    }

    /**
     * 监听数字增加减少控件
     */
    public interface OnNumberClickListener{
        /**
         * 当减少按钮被点击的时候回调
         * @param view
         * @param value
         */
        void onButtonSub(View view,int value);
        /**
         * 当增加按钮被点击的时候回调
         * @param view
         * @param value
         */
        void onButtonAdd(View view,int value);
    }

    public OnNumberClickListener listener;

    /**
     * 设置监听数字按钮
     * @param listener
     */
    public void setOnNumberClickListener(OnNumberClickListener listener){
        this.listener = listener;
    }
    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}




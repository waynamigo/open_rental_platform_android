package com.example.yizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yizu.db.HistoryRecord;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Button search;
    ImageButton back,clear;
    TextView t[] = new TextView[5];
    EditText editText;
    Button yuYIn;
    private String objectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5975f0a0");
        ActivityCollecter.addActivty(this);
        back = (ImageButton)findViewById(R.id.fanhui);
        search = (Button)findViewById(R.id.search);
        clear = (ImageButton)findViewById(R.id.clear);
        editText = (EditText)findViewById(R.id.context);
        yuYIn=(Button)findViewById(R.id.yuyin);
        t[0] = (TextView)findViewById(R.id.his1);
        t[1] = (TextView)findViewById(R.id.his2);
        t[2]= (TextView)findViewById(R.id.his3);
        t[3] = (TextView)findViewById(R.id.his4);
        t[4] = (TextView)findViewById(R.id.his5);
        objectId = ShareStorage.getShareString(this,"ObjectId");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DataSupport.deleteAll(HistoryRecord.class);
                 initHis();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryRecord historyRecord = new HistoryRecord();
                historyRecord.setDate(new Date(System.currentTimeMillis()));
                String temp = editText.getText().toString().trim();
                if(TextUtils.isEmpty(temp)){
                    Toast.makeText(SearchActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                historyRecord.setRecord(temp);
                historyRecord.setObjectId(objectId);
                historyRecord.save();
                Log.e("debug1",temp);
                Intent intent=new Intent(SearchActivity.this,ArticlesActivity.class);
                intent.putExtra("SNTSearch",temp);
                intent.putExtra("SearchFlag","2");
                startActivity(intent);
            }
        });
        yuYIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVoice();
            }
        });
        addListener();
        initHis();
    }
    //TODO 开始说话：
    private void btnVoice(){
        RecognizerDialog dialog = new RecognizerDialog(SearchActivity.this,null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
    }
    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        text=text.substring(0,text.length()-1);
        editText.append(text);

    }
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
    void initHis(){

        List<HistoryRecord> list = DataSupport.where("objectId = ?",objectId).order("date desc").limit(5).find(HistoryRecord.class);
        int i;
        for(i=0;i<list.size();i++){
            HistoryRecord historyRecord = list.get(i);
            t[i].setText(historyRecord.getRecord());
        }
        for(;i<5;i++){
            t[i].setText("");
        }
    }
    void addListener(){
        for(int i = 0;i<5;i++){
            final int finalI = i;
            t[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this,ArticlesActivity.class);
                    intent.putExtra("SNTSearch",t[finalI].getText());
                    intent.putExtra("SearchFlag","2");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}

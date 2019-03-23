package com.example.yizu;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yizu.tool.ActivityCollecter;

public class ChangeNameActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.changenameToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        button = (Button)findViewById(R.id.right);
        editText = (EditText)findViewById(R.id.newname);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backData = new Intent();
                String newName = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(newName)){
                    backData.putExtra("ReName",editText.getText().toString().trim());
                    setResult(RESULT_OK,backData);
                    finish();
                }else{
                    Toast.makeText(ChangeNameActivity.this,"昵称不能为空",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}

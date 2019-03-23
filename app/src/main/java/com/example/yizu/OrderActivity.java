package com.example.yizu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.yizu.adapter.TitleFragmentPagerAdapter;
import com.example.yizu.fragment.OrderFragment;
import com.example.yizu.tool.ActivityCollecter;

import java.util.ArrayList;
import java.util.List;


public class OrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.OrderToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tabLayout=(TabLayout)findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        List<Fragment> fragments = new ArrayList<>();
        OrderFragment a = new OrderFragment();
        OrderFragment b = new OrderFragment();
        a.setCurrentPage(1);
        b.setCurrentPage(2);
        fragments.add(a);
        fragments.add(b);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(),fragments, new String[]{"未完成", "已完成"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
package com.example.yizu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.yizu.adapter.ViewPagerAdapter;
import com.example.yizu.fragment.ArticleFragment;
import com.example.yizu.tool.ActivityCollecter;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity {

    ViewPagerAdapter viewAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        ActivityCollecter.addActivty(this);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.PagerToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final List<Fragment> fragments=new ArrayList<>();
         ArticleFragment a=new ArticleFragment();
        ArticleFragment b=new ArticleFragment();
        ArticleFragment c=new ArticleFragment();
        ArticleFragment e=new ArticleFragment();
        a.setNumber(0);
        b.setNumber(1);
        c.setNumber(2);
        e.setNumber(3);
        fragments.add(a);
        fragments.add(b);
        fragments.add(c);
        fragments.add(e);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tab);
        viewAdapter=new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"全部", "距我最近", "价格","评分"});
        viewPager.setAdapter(viewAdapter);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            Drawable d = null;
            switch (i) {
                case 0:
                    d = getResources().getDrawable(R.drawable.total_goods);

                    break;
                case 1:
                    d = getResources().getDrawable(R.drawable.distance);
                    break;
                case 2:
                    d = getResources().getDrawable(R.drawable.goodsjifen);
                    break;
                case 3:
                    d = getResources().getDrawable(R.drawable.goodspingfen);
                    break;
            }
            tab.setIcon(d);
        }

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

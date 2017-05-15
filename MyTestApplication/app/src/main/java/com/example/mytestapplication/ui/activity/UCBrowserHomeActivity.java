package com.example.mytestapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mytestapplication.R;
import com.example.mytestapplication.model.SimpleFragmentPagerAdapter;

/**
 * Created by 张艳 on 2016/10/25.
 */

public class UCBrowserHomeActivity extends AppCompatActivity {
    //    private Toolbar toolbar;
//    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uc_test_screen);
        initView();
    }

    private void initView() {
//        initTopbar();
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
//
//    @TargetApi(16)
//    private void initTopbar() {
//        toolbar = (Toolbar) findViewById(toolbar);
////        toolbar.setHasTransientState(true);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoobar);
//        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
//    }

}

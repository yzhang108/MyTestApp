package com.example.mytestapplication.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.model.RecyclerViewDataInfo;
import com.example.mytestapplication.ui.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2016/7/28.
 */
public class ZhiHuActivity extends AppCompatActivity implements HomeAdapter.OnItemClickListener {

    public static final String TAG = "ZhiHuActivity";
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<RecyclerViewDataInfo> recyclerViewDataInfos;
    private FloatingActionButton floatingActionButton;
    private boolean isFabOpen = false;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_layout);
        initListData();
        initView();
        initEvent();
    }

    private void initListData() {
        if (recyclerViewDataInfos == null) {
            recyclerViewDataInfos = new ArrayList<>();
        } else {
            recyclerViewDataInfos.clear();
        }
        String[] menu_items = getResources().getStringArray(R.array.menu_item);
        for (int i = 0; i < menu_items.length; i++) {
            recyclerViewDataInfos.add(new RecyclerViewDataInfo(menu_items[i]));
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("知乎页面");
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);

        recyclerView = (RecyclerView) findViewById(R.id.rv_behavior);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeAdapter = new HomeAdapter(ZhiHuActivity.this, recyclerViewDataInfos, this);
        recyclerView.setAdapter(homeAdapter);


        textView=(TextView)findViewById(R.id.tv_add_menu);
    }

    private void initEvent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabOpen) {
                    openMenu(v);
                } else {
                    closeMenu(v);
                }
            }
        });
    }

    @Override
    public void onClickListener(int pos) {

    }

    public void openMenu(View view) {
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        textView.setVisibility(View.VISIBLE);

        AlphaAnimation alphaAnimation=new AlphaAnimation(0,0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(false);
        textView.startAnimation(alphaAnimation);

        isFabOpen=true;

    }

    public void closeMenu(View view) {
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        textView.setVisibility(View.VISIBLE);

        AlphaAnimation alphaAnimation=new AlphaAnimation(0.7f,0);
        alphaAnimation.setDuration(500);

        textView.startAnimation(alphaAnimation);
        textView.setVisibility(View.GONE);
        isFabOpen=false;
    }
}

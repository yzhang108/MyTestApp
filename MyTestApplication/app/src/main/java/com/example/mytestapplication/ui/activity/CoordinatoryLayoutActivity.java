package com.example.mytestapplication.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mytestapplication.R;
import com.example.mytestapplication.controller.HomepageController;
import com.example.mytestapplication.model.RecyclerViewDataInfo;
import com.example.mytestapplication.ui.adapter.HomeAdapter;
import com.example.mytestapplication.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2016/7/15.
 */
public class CoordinatoryLayoutActivity extends AppCompatActivity implements HomeAdapter.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<RecyclerViewDataInfo> recyclerViewDataInfos;
    private HomepageController homepageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinatory_layout_test);
        homepageController = new HomepageController(this);
        initTopbar();
        initListData();
        initView();
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

    @TargetApi(16)
    private void initTopbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setHasTransientState(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoobar);
        collapsingToolbarLayout.setTitle("不积跬步，无以至千里");
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
//        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.red_light));


    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //水平布局
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Grdilayout布局
//        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        homeAdapter = new HomeAdapter(CoordinatoryLayoutActivity.this, recyclerViewDataInfos, this);
        recyclerView.setAdapter(homeAdapter);
        //设置分割线
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(CoordinatoryLayoutActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.topbar_right_menu, menu);
//        getMenuInflater().inflate(R.menu.menu_topbar_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    @Override
    public void onClickListener(int pos) {
        switch (pos) {
            case 0:
                //打开政企项目
                homepageController.openApp();
                break;
            case 1:
                //HTML5测试
                homepageController.goToActivity(HtmlTestActivity.class);
                break;
            case 2:
                homepageController.createNotification(CoordinatoryLayoutActivity.class);
                break;
            case 3:
                homepageController.testRejected(CoordinatoryLayoutActivity.class);
                break;
            case 4:
                //内存测试
                break;
            case 5:
                //拷贝数据
                FileUtil.copyDBToSDcrad();
                break;
            case 6:
                //测试TreeMap与HashMap
                break;
            case 7:
                //TabLayout+Viewpage测试
                homepageController.goToActivity(TabLayoutTestFragment.class);
                break;
            case 8:
                //listview下拉图片放大
                homepageController.goToActivity(ListViewTest.class);
                break;
            case 9:
                //测试scrollto方法
                homepageController.goToActivity(TestScrollToActivity.class);
                break;
            case 10:
                //databinding
                homepageController.goToActivity(DataBindingTestActivity.class);
                break;
            case 11:
                homepageController.goToActivity(ZhiHuActivity.class);
                break;
            case 12:
                homepageController.goToActivity(com.example.mytestapplication.ui.activity.cardviewdemo.MainActivity.class);
                break;
            case 13:
                homepageController.goToActivity(WeiboActivity.class);
                break;
            case 14:
                homepageController.goToActivity(ToolBarTestActivity.class);
                break;
            case 15:
                homepageController.goToActivity(TitleGradeActivity.class);
                break;
            case 16:
                homepageController.goToActivity(UCBrowserHomeActivity.class);
                break;
            case 17:
                homepageController.goToActivity(CanvansAndAnimalActivity.class);
                break;

        }
    }
}

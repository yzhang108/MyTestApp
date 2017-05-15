package com.example.mytestapplication.ui.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.adapter.TestAdapter;
import com.example.mytestapplication.widge.recyclerview.EndlessRecyclerOnScrollListener;
import com.example.mytestapplication.widge.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.example.mytestapplication.widge.recyclerview.RecyclerViewStateUtils;
import com.example.mytestapplication.widge.recyclerview.RecyclerViewUtils;
import com.example.mytestapplication.widge.recyclerview.weight.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2016/10/11.
 */

public class TitleGradeActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> data = new ArrayList<>();
    private TestAdapter testAdapter;
    private final int ADD_DATA = 100;
    private int currentSize = 0;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter;
    private EndlessRecyclerOnScrollListener mOnScrollListener = null;
    private LinearLayoutManager linearLayoutManager;
    private int fHeight=450;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_facade_layout);
        initView();
        initSwipeRefreshLayout();
        addData();

        ListView listView;
    }


    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.getBackground().setAlpha(0);
        recyclerView = (RecyclerView) findViewById(R.id.lv_news);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        testAdapter = new TestAdapter(data, this);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(testAdapter);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);



        mOnScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
                if (state == LoadingFooter.State.Loading) {
                    Log.d("@Cundong", "the state is Loading, just wait..");
                    return;
                }

                if (currentSize < 100) {
                    // loading more
//                    RecyclerViewStateUtils.setFooterViewState(TitleGradeActivity.this, recyclerView, 10, LoadingFooter.CalendarState.Loading, null);
                    addData();

                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState(TitleGradeActivity.this, recyclerView, 10, LoadingFooter.State.TheEnd, null);
                }
            }


            @TargetApi(11)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemPosition=linearLayoutManager.findFirstVisibleItemPosition();
                if(firstItemPosition==0) {
                    int VerticalScrollOffset=recyclerView.computeVerticalScrollOffset();
//                    Log.e("zy","firstItemPosition="+firstItemPosition+", VerticalScrollOffset="+VerticalScrollOffset);
                    if (VerticalScrollOffset < 0) {
                        title.getBackground().setAlpha(0);
                    } else {
                        if (VerticalScrollOffset < fHeight) {
                            int progress = (int) (new Float(VerticalScrollOffset) / new Float(fHeight) * 200);//255
                            title.getBackground().setAlpha(progress);
                        } else {
                            title.getBackground().setAlpha(255 - 55);
                        }
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(mOnScrollListener);

        View headerView= View.inflate(this,R.layout.headview_image,null);
        RecyclerViewUtils.setHeaderView(recyclerView,headerView);


    }

    private void updateData() {
        data.clear();
        currentSize = 0;
        for (int i = 0; i < 10; i++) {
            data.add("item  " + (currentSize + i));
        }
        currentSize += 10;
        testAdapter.updateData(data);
    }

    private void addData() {
        data.clear();
        for (int i = 0; i < 10; i++) {
            data.add("item  " + (currentSize + i));
        }
        currentSize += 10;
        testAdapter.addData(data);
        Log.i("zy", "add data " + data.size());
    }


    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }



}

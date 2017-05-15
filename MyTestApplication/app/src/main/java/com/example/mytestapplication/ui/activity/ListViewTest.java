package com.example.mytestapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.ui.adapter.MessageListAdapter;
import com.example.mytestapplication.utils.DensityUtil;
import com.example.mytestapplication.widge.PullToZoomListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by 张艳 on 2016/7/13.
 */
public class ListViewTest extends Activity {

    //上拉刷新
    protected PtrClassicFrameLayout ptrClassicFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewtest_layout);

        initListView(getData());
    }
    private PullToZoomListView mListView;
    private MessageListAdapter mAdapter;
    private void initListView( List<HashMap<String, Object>> listItem) {
        ptrClassicFrameLayout=(PtrClassicFrameLayout)findViewById(R.id.load_more_list_view_ptr_frame) ;
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setLoadingMinTime(1000);
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });

        mListView=(PullToZoomListView) findViewById(R.id.listView01);
        mAdapter = new MessageListAdapter(listItem,R.layout.news_list_item_view,getLayoutInflater(),ListViewTest.this);

		/*========================= 设置头部的图片====================================*/
        mListView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        mListView.getHeaderView().setImageResource(R.drawable.story_member_center_bg);

		/*========================= 设置头部的高度 ====================================*/
        mListView.setmHeaderHeight(DensityUtil.dip2px(ListViewTest.this, 160));

		/*========================= 设置头部的的布局====================================*/
        View mHeaderView=getLayoutInflater().inflate(R.layout.layout_story_userinfo, null);
        mListView.getHeaderContainer().addView(mHeaderView);
        mListView.setHeaderView();


        mListView.setAdapter(mAdapter);
    }

    private List<HashMap<String, Object>> listItem;
    private List<HashMap<String, Object>> getData()	{
        listItem=new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<20;i++)	{
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("key", ""+i);
            listItem.add(map);
        }
        return listItem;
    }
}

package com.example.mytestapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mytestapplication.R;
import com.example.mytestapplication.widge.BadgeActionProvider;

/**
 * Created by 张艳 on 2016/10/9.
 */

public class ToolBarTestActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private BadgeActionProvider shareActionProvider,moreActionProvider;
    private String title="祝你好运！";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("zy","onCreate");
        setContentView(R.layout.toolbar_test_layout);
//        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.transent));
//        }
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)this.findViewById(R.id.collapsingToolBarLayout) ;
        setSupportActionBar(toolbar);
//        collapsingToolbarLayout.setTitle(title);
//        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(),R.color.color_blue));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("zy","onCreateOptionsMenu");
        return super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_topbar_right,menu);
//        MenuItem shareItem=menu.findItem(R.id.action_share);
//        shareActionProvider=(BadgeActionProvider) MenuItemCompat.getActionProvider(shareItem);
//        shareActionProvider.setOnClickListener(1, onMenuClickListener);
//
//        MenuItem moreItem=menu.findItem(R.id.action_more);
//        moreActionProvider=(BadgeActionProvider)MenuItemCompat.getActionProvider(moreItem);
//        moreActionProvider.setOnClickListener(2,onMenuClickListener);
//        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("zy","onWindowFocusChanged");
//        shareActionProvider.setTextInt(12);
//        shareActionProvider.setIcon(R.drawable.ic_event);
//        moreActionProvider.setTextInt(-1);
//        moreActionProvider.setIcon(R.drawable.ic_discuss);


    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("zy","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("zy","onResume");
    }

    private BadgeActionProvider.OnClickListener onMenuClickListener=new BadgeActionProvider.OnClickListener(){
        @Override
        public void onClick(int what) {
            switch (what){
                case 1:
                    Toast.makeText(getApplicationContext(),"点击了分享",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"点击了更多",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}

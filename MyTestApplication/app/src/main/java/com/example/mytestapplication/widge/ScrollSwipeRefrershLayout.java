package com.example.mytestapplication.widge;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by 张艳 on 2016/10/11.
 */

public class ScrollSwipeRefrershLayout extends SwipeRefreshLayout {

    public ScrollSwipeRefrershLayout(Context context) {
        super(context);
        Log.e("zy", "22222" );
    }

    public ScrollSwipeRefrershLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("zy", "11111111" );
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("zy", "t==" + t + ",oldt=" + oldt);
    }


}

package com.example.mytestapplication.widge.behavior;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mytestapplication.utils.DensityUtil;

/**
 * Created by 张艳 on 2016/10/11.
 */

public class TitleFacadeBehavior extends CoordinatorLayout.Behavior<View> {

    private final int height = 600;
    private int currentMovedY=0;

    public TitleFacadeBehavior() {
    }

    public TitleFacadeBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("zy", "200dp==" + DensityUtil.dip2px(context, 200) + "px");
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @TargetApi(11)
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        Log.i("zy","child.id="+child.getId()+", target.getId="+target.getId());
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//        Log.i("zy","dyConsumed="+dyConsumed+",dyUnconsumed="+dyUnconsumed);
//        currentMovedY+=dyConsumed;
//        Log.i("zy","currentMovedY="+currentMovedY+",dyconsumed="+dyConsumed);
//        if (currentMovedY <=0) {
//            Log.i("zy", "dy<0,设置成0，透明");
//            child.getBackground().setAlpha(0);
//        } else {
//            if (currentMovedY < height) {
//                int progress = (int) (new Float(currentMovedY) / new Float(height) * 200);//255
//                Log.i("zy", "dy<600,设置成" + progress + "半透明");
//                child.getBackground().setAlpha(progress);
//            } else {
//                Log.i("zy", "dy>600,设置成" + 200 + "几乎不透明");
//                child.getBackground().setAlpha(255 - 55);
//            }
//        }
    }
}

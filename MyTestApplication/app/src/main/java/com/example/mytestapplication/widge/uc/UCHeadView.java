package com.example.mytestapplication.widge.uc;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 张艳 on 2016/10/28.
 */
@CoordinatorLayout.DefaultBehavior(UCHeadView.HeadBehavior.class)
public class UCHeadView extends NestedScrollView {

    private int mOffsetDelta;

    public UCHeadView(Context context) {
        super(context);
    }

    public UCHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UCHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getmOffsetDelta() {
        return mOffsetDelta;
    }

    public void setmOffsetDelta(int mOffsetDelta) {
        this.mOffsetDelta = mOffsetDelta;
    }

    public static class HeadBehavior extends CoordinatorLayout.Behavior<UCHeadView>{
        public HeadBehavior() {
        }

        public HeadBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, UCHeadView child, View directTargetChild, View target, int nestedScrollAxes) {
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, UCHeadView child, View target) {
            super.onStopNestedScroll(coordinatorLayout, child, target);
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, UCHeadView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, UCHeadView child, View target, int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }

        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, UCHeadView child, View target, float velocityX, float velocityY, boolean consumed) {
            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, UCHeadView child, View target, float velocityX, float velocityY) {
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }
    }
}

package com.example.mytestapplication.widge.calendar.animation;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.example.mytestapplication.widge.calendar.view.MyCalendaryView;

/**
 * Created by 张艳 on 2017/5/11.
 */

public class CalendarViewMoveAnimation extends Animation {
    private MyCalendaryView mcalendarView;
    private float mDistance;

    public CalendarViewMoveAnimation(MyCalendaryView mcalendarView, float mDistance) {
        this.mcalendarView = mcalendarView;
        this.mDistance = mDistance;
        setDuration(300);
        setInterpolator(new DecelerateInterpolator(1.5f));
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        mcalendarView.dragView(mDistance);
    }
}

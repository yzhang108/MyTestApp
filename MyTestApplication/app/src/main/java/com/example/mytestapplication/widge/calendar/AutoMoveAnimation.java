package com.example.mytestapplication.widge.calendar;

import android.annotation.TargetApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.example.mytestapplication.utils.AppLogger;

/**
 * Created by 张艳 on 2017/5/11.
 */
@TargetApi(11)
public class AutoMoveAnimation extends Animation {
    private View mView;
    private float mDistance;
    private float mPositionY;

    public AutoMoveAnimation(View mView,float distance,int mDutation,float factor) {
        this.mView = mView;
        this.mDistance=distance;
        this.mPositionY=mView.getY();
        setDuration(mDutation);
        setInterpolator(new DecelerateInterpolator(factor));
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        AppLogger.e("interpolatedTime*mDistance="+interpolatedTime*mDistance+",mDistance="+mDistance+",interpolatedTime="+interpolatedTime);
        mView.setY(mPositionY+interpolatedTime*mDistance);
    }
}

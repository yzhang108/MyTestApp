package com.example.mytestapplication.model.databinding;

import android.util.Log;
import android.view.View;

/**
 * Created by 张艳 on 2016/7/14.
 */
public class EventHandler {
    public final static String TAG="EventHandler ";
    public void onCLickSubmit(View view){
        Log.d(TAG,"onCLickSubmit,"+view.getTag());

    }
}

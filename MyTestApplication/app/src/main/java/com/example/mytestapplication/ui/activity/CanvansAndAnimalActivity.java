package com.example.mytestapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mytestapplication.R;

/**
 * Created by 张艳 on 2016/11/18.
 */

public class CanvansAndAnimalActivity extends Activity {

    private LinearLayout linearLayout;
    private ImageView imageView;
    private Button btStart, btStop;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvans_and_animal_layout);
        imageView = (ImageView) findViewById(R.id.iv_icon);
        btStart = (Button) findViewById(R.id.bt_start);
        btStop = (Button) findViewById(R.id.bt_stop);

        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);



//        linearLayout=(LinearLayout)findViewById(R.id.llyt_root);
//        final DrawView view=new DrawView(this);
//        view.setMinimumHeight(1000);
//        view.setMinimumWidth(1200);
//        //通知view组件重绘
//        view.invalidate();
//        linearLayout.addView(view);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imageView.setAnimation(rotateAnimation);
//                rotateAnimation.start();
                imageView.startAnimation(rotateAnimation);
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateAnimation.cancel();
            }
        });
    }


}

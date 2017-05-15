package com.example.mytestapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.utils.DensityUtil;

import java.util.logging.Logger;

/**
 * Created by 张艳 on 2016/7/13.
 */
public class TestScrollToActivity extends Activity implements View.OnClickListener{
private final static String TAG="TestScrollToActivity ";
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private TextView mTextView;
    private ImageView imageView;
    private int position=-10;
    ViewGroup.LayoutParams params;
    private int currentHeight=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scrollto_layout);

        mTextView = (TextView) this.findViewById(R.id.tv);
        imageView=(ImageView)this.findViewById(R.id.iv_test);

        params=imageView.getLayoutParams();
        currentHeight=params.height;
        Log.d(TAG,"currentHeight="+currentHeight);

        mButton1 = (Button) this.findViewById(R.id.button_scroll1);
        mButton2 = (Button) this.findViewById(R.id.button_scroll2);
        mButton3 = (Button) this.findViewById(R.id.button_scroll3);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_scroll1:
//                position*=2;
//                mTextView.scrollTo(position, position);
                imageView.scrollTo(0,0);
                break;
            case R.id.button_scroll2:
//                mTextView.scrollBy(-2, -2);
                currentHeight+=50;
                params.height=currentHeight;
                imageView.setLayoutParams(params);
                break;
            case R.id.button_scroll3:
//                position=-10;
//                mTextView.scrollTo(0, 0);
                currentHeight= DensityUtil.dip2px(TestScrollToActivity.this,100);
                params.height=currentHeight;
                imageView.setLayoutParams(params);
                break;
            default:
                break;
        }
    }
}

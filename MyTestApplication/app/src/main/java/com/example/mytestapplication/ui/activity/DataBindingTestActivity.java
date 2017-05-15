package com.example.mytestapplication.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.mytestapplication.R;
import com.example.mytestapplication.databinding.DatabindingTestLayoutBinding;
import com.example.mytestapplication.model.databinding.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2016/7/14.
 */
public class DataBindingTestActivity extends Activity {
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabindingTestLayoutBinding binding= DataBindingUtil.setContentView(this, R.layout.databinding_test_layout);
        userInfo=new UserInfo("yan","zhang",true);
        binding.setUser(userInfo);
        List<UserInfo> userInfoList=new ArrayList<>();
        userInfoList.add(new UserInfo("xiao","niao",false));
        userInfoList.add(new UserInfo("da","xiang",false));
//        binding.setUserList(userInfoList);
//        EventHandler handler=new EventHandler();
//        binding.setHandlers(handler);
    }


}

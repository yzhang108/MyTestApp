package com.example.mytestapplication.model.databinding;

import android.util.Log;
import android.view.View;

/**
 * Created by 张艳 on 2016/7/14.
 */
public class UserInfo {
    public final String firstName;
    public final String lastName;
    public final boolean isAult;
    public UserInfo(String firstName, String lastName,boolean isAult) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAult=isAult;
    }

    public void submit(View v){
        Log.d("UserInfo","onClickSubmit,name="+lastName+" "+firstName);
    }
}

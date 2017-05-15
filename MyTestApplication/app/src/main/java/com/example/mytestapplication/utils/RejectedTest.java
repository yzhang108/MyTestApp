package com.example.mytestapplication.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 张艳 on 2016/4/18.
 */
public class RejectedTest {
    private final static String TAG = "RejectedTest";
    private static RejectedTest mRejectedInstance = new RejectedTest();

    public RejectedTest() {

    }

    public static RejectedTest getInstance() {
        if (mRejectedInstance == null) {
            mRejectedInstance = new RejectedTest();
        }

        return mRejectedInstance;
    }

    public static void testGetDeclaredMethods(Class<?> clazz) {
        Log.e(TAG, "testGetDeclaredMethods  ,clazz=" + clazz.toString());
        Method[] methods = null;
        if (clazz != null) {
            methods = clazz.getDeclaredMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    Log.i(TAG, method.getModifiers() + "  " + method.getName());
                }
            }
        }
    }

    public static void testGetMethods(Class<?> clazz) {
        Log.e(TAG, "testGetMethods  ,clazz=" + clazz.toString());
        Method[] methods = null;
        if (clazz != null) {
            methods = clazz.getMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    Log.i(TAG, method.getModifiers() + "  " + method.getName());
                }
            }
        }
    }

    public static void testGetFileds(Class<?> clazz){
        Field[] fields=null;
        if(clazz!=null){
            fields=clazz.getDeclaredFields();
            if(fields!=null && fields.length>0){
                for(Field field:fields){
                    if(field.isSynthetic())
                        continue;
                    Log.e(TAG,"file.getName="+field.getName());
                }
            }
        }
    }
}

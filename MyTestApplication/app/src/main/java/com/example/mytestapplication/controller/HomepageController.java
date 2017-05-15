package com.example.mytestapplication.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.mytestapplication.R;
import com.example.mytestapplication.utils.RejectedTest;

import java.util.Collections;
import java.util.List;

/**
 * Created by 张艳 on 2016/7/28.
 */
public class HomepageController {
    private Context context;

    public HomepageController(Context context) {
        this.context = context;
    }

    /**
     * 检查系统应用程序，并打开
     */
    public void openApp() {
        //应用过滤条件
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = context.getPackageManager();
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按报名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for (ResolveInfo res : mAllApps) {
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
            Log.e("==", "pkg=" + pkg);
            // 打开QQ
            if (pkg.contains("wx")) {
                ComponentName componet = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componet);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    public void createNotification(Class goToActivity) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, goToActivity), 0);

        //************************3.0版本以前的notification,实验时只有声音，没有文字内容
//        Notification notification = new Notification();
//        notification.tickerText = "哈喽，欢迎来自张艳的测试程序！";
//        notification.defaults = Notification.DEFAULT_SOUND;
//        notification.contentIntent = pendingIntent;


        //*************************创建消息构造器,在扩展包
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //设置当有消息是的提示，图标和提示文字
        builder.setSmallIcon(R.drawable.user_icon).setTicker("有新消息了");
        //需要样式
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle("上课通知");//通知的标题
        style.bigText("今天下午要在综B上jsp");//通知的文本内容
        //大视图文本具体内容
        style.setSummaryText("这是正常的课程安排，请各位同学按时上课");
        builder.setStyle(style);
        //显示消息到达的时间，这里设置当前时间
        builder.setWhen(System.currentTimeMillis());
        //获取一个通知对象
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;



        //**************************大视图图片通知
//        NotificationCompat.Builder builderPic = new NotificationCompat.Builder(context);
//        builderPic.setSmallIcon(R.drawable.user_icon).setTicker("新浪体育提醒");
//        //进行设置
//        NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
//        pictureStyle.setBigContentTitle("新浪体育 快船VS骑士 ");
//        pictureStyle.bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.settings));
//        pictureStyle.setSummaryText(" 快船VS骑士 天王山之战！！！");//不要在意文字
//        //设置样式
//        builderPic.setStyle(pictureStyle);
//        //设置显示的时间
//        builderPic.setWhen(System.currentTimeMillis());
//        Notification notification = pictureStyle.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;


        //notify()第一个参数id An identifier for this notification unique within your application
        //get?意思说，这个通知在你的应用程序中唯一的标识符
        notificationManager.notify(1, notification);
    }

    public void testRejected(Class currActivity) {
//        RejectedTest.getInstance().testGetDeclaredMethods(MainActivity.class);
//        RejectedTest.getInstance().testGetMethods(MainActivity.class);
        RejectedTest.getInstance().testGetFileds(currActivity);
    }

    public void goToActivity(Class goToActivity) {
        context.startActivity(new Intent(context, goToActivity));
    }
}

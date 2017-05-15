package com.example.mytestapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by 张艳 on 2016/3/23.
 */
public class AppUtil {
    public static boolean isPkgInstalled(Context context,String packageName) {

        if (packageName == null || "".equals(packageName))
            return false;
        android.content.pm.ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openApp(Context context,String AppLauncher, String token) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(AppLauncher));
        intent.putExtra("token", token);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Context getPackageContext(Context context, String packageName) {
        Log.e("getPackageContext", "start===");
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static Intent getAppOpenIntentByPackageName(Context context,String packageName){
        Log.e("","getAppOpenIntentByPackageName start===");
        // MainActivity完整名
        String mainAct = null;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }
    public static boolean openPackage(Context context, String packageName) {
        Log.e("openPackage","start===");
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        Log.e("openPackage", "intent=" + intent + ",pkgContext=" + pkgContext);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    public static void openAppWithPackageName(Context context,String packageName,String token){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent!=null) {
            intent.putExtra("token", token);
            context.startActivity(intent);
        }else{

            Log.e("", "intent is null");
        }
    }

    public static void downloadApp(Context context,String link) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(link);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static void getAllAppInfo(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mAllApps=context.getPackageManager().queryIntentActivities(mainIntent, 0);
        if(mAllApps!=null && mAllApps.size()>0){
            for(ResolveInfo info:mAllApps){
                String packageName=info.resolvePackageName;
                String mainActivityName=info.activityInfo.packageName+"."+info.activityInfo.name;
//                Log.e("**","packageName="+packageName+", mainActivityName="+mainActivityName);
            }
        }else {
        }
        context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(0);
        if(packageInfos!=null && packageInfos.size()>0){
            for(PackageInfo info:packageInfos){
//                Log.e("zy","info.name="+info.packageName);
            }
        }
    }

    public static void openApp(Context context,String packageName,String appLauncher,String token){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent==null) {
                intent = new Intent();
                intent.setData(Uri.parse(appLauncher));
            }
            intent.putExtra("token", token);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

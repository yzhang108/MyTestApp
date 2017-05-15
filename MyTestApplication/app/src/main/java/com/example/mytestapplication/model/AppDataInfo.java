package com.example.mytestapplication.model;

/**
 * Created by 张艳 on 2016/3/30.
 */
public class AppDataInfo {
    private String appName;
    private String appDownloadUrl;
    private String appPackageName;
    private String appLauncher;

    public AppDataInfo(String appName, String appDownloadUrl, String appPackageName, String appLauncher) {
        this.appName = appName;
        this.appDownloadUrl = appDownloadUrl;
        this.appPackageName = appPackageName;
        this.appLauncher = appLauncher;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppLauncher() {
        return appLauncher;
    }

    public void setAppLauncher(String appLauncher) {
        this.appLauncher = appLauncher;
    }
}

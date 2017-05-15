package com.example.mytestapplication.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.mytestapplication.R;
import com.example.mytestapplication.model.AppDataInfo;
import com.example.mytestapplication.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class HtmlTestActivity extends AppCompatActivity {

    private static final String TAG="HtmlTestActivity";

    private WebView webView;

    private List<AppDataInfo> appDataInfoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppData();
        setContentView(R.layout.activity_html_test);
        webView = (WebView) findViewById(R.id.wv_test);
        webView.loadUrl("file:///android_asset/test.html");
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                Log.e("shouldOverrideUrlLoad","url="+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                callJs();
            }

        });
        webView.addJavascriptInterface(new PayJavaScriptInterface(), "robot");
        webView.addJavascriptInterface(new OpenAppJsInterface(),"openApp");
    }

    private void initAppData(){
        AppDataInfo appDataInfo=new AppDataInfo("乐贝通家长版（河南省应用）","http://www.lebeitong.com/mobile/onload.jsp","com.shenzhou.lbt_jz","lbtjz://");
        appDataInfoList.add(appDataInfo);

        AppDataInfo appDataInfo1=new AppDataInfo("乐贝通教师版(河南省应用)","http://www.lebeitong.com/mobile/onload.jsp","com.shenzhou.lbt","lbtls://");
        appDataInfoList.add(appDataInfo1);

        AppDataInfo appDataInfo2=new AppDataInfo("学易学堂","http://cv3.enet.zxxk.com/apk/xy-chinamobile.apk","com.zxxk.xueyichinamobile","zxxk-xueyi://");
        appDataInfoList.add(appDataInfo2);

        AppDataInfo appDataInfo3=new AppDataInfo("乐词","http://s1.ileci.com/release/corp/leciMobile0520.apk","com.xdf.recite","LeciMobile://");
        appDataInfoList.add(appDataInfo3);

        AppDataInfo appDataInfo4=new AppDataInfo("E辅导","http://one.efudao.cn/android/jituan/efudao.apk","com.efudao","appefudao://mobile");
        appDataInfoList.add(appDataInfo4);
    }
    class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.i(TAG,"url="+url+",message="+message+",result="+result);
            return super.onJsConfirm(view, url, message, result);
        }
    }

    final class PayJavaScriptInterface {
        PayJavaScriptInterface() {

        }

        @JavascriptInterface

        public String getUserinfo() {
            return "zhangyan";
        }

        @JavascriptInterface
        public boolean needLogin() {
            return true;
        }

        @JavascriptInterface
        public String callFromJSBasicDataType(int x,float y,char c,boolean result){
            String str ="-"+(x+1)+"-"+(y+1)+"-"+c+"-"+result;
            Log.i(TAG, "str=" + str);
            return str;
        }

        @JavascriptInterface
        public void callAndroidMethod(){
            Toast.makeText(HtmlTestActivity.this,"Hello Html5!",Toast.LENGTH_LONG).show();
        }
    }


    public void callJs(){
        webView.loadUrl("javascript:callJs();");
    }

    private class OpenAppJsInterface {

        public OpenAppJsInterface(){

        }
        //打开app
        @JavascriptInterface
        public void nativeOpenApp(int index) {
            AppDataInfo appDataInfo=appDataInfoList.get(index);
            openOrDownloadApp(appDataInfo.getAppPackageName(),appDataInfo.getAppName(),appDataInfo.getAppLauncher(),appDataInfo.getAppDownloadUrl());
        }
    }

    private void openOrDownloadApp(String appId, String appName, String appSchemeUrl, String appDownloadUrl){
        AppUtil.getAllAppInfo(HtmlTestActivity.this);
        if(AppUtil.isPkgInstalled(HtmlTestActivity.this,appId)){
//            AppUtil.openAppWithPackageName(HtmlTestActivity.this,appId,"");
//            AppUtil.openApp(HtmlTestActivity.this,appSchemeUrl,"");
//            String token="z/B+1H9xl48mdt4Xrh+QFcxysrfKcSbl61x25q2+06GTIObhaUmNy7+xPKJa0iXWO/Ev1k9i9gchRr+pxOtk2Q==";
            String token="ytTVvP8r7/eXycoJ35Oy6r+at35Efo0cC2JNs//8wHpIaSOKKLA1GvN+kjC3XyewnytZuiaCBgCySu8W/Hk7Kg==";
            AppUtil.openApp(HtmlTestActivity.this,appId,appSchemeUrl,token);
        }else {
            AppUtil.downloadApp(HtmlTestActivity.this,appDownloadUrl);
        }
    }
}

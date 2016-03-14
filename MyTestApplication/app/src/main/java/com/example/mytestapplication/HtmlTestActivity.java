package com.example.mytestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class HtmlTestActivity extends AppCompatActivity {

    private static final String TAG="HtmlTestActivity";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_test);
        webView = (WebView) findViewById(R.id.wv_test);
        webView.loadUrl("file:///android_asset/test.html");

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
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                callJs();
            }

        });
        webView.addJavascriptInterface(new PayJavaScriptInterface(), "robot");

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
}

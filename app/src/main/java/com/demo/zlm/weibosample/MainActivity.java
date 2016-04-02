package com.demo.zlm.weibosample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.demo.zlm.weibosample.Thread.TokenThread;

public class MainActivity extends AppCompatActivity  {
    private ProgressBar mProgressBar;
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://api.weibo.com/oauth2/authorize?client_id=2996785772&redirect_uri=http://www.yingshibao.com&scope=all");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                Log.e(MainActivity.class.getSimpleName(),"url="+url);
                if(url.contains("code")) {
                    //huo qu code
                    String code = url.substring(url.lastIndexOf("=") + 1);
                    Intent intent=new Intent(MainActivity.this,ListActivity.class);
                    intent.putExtra("code",code);
                    startActivity(intent);
                    System.out.println("code===="+code);
                    
                }
            }
        });
    }
}

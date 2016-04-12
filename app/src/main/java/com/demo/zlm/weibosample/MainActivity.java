package com.demo.zlm.weibosample;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.activeandroid.query.Delete;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.api.WeiBoApi;
import com.demo.zlm.weibosample.model.Status;


public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;


    private WeiBoApi mWeiBoApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        boolean foreclogin;
        final Intent intent = getIntent();
        //是否强制登录
        foreclogin = intent.getBooleanExtra("foreclogin", false);
        String loadUrl = "https://api.weibo.com/oauth2/authorize?client_id="+Constant.APP_KEY+"&redirect_uri="+Constant.REDIRECT_URL+"&scope=all&forcelogin=" + foreclogin;

        mWebView.loadUrl(loadUrl);
        mWeiBoApi = new WeiBoApi();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                Log.e(MainActivity.class.getSimpleName(), "url=" + url);
                if (url.contains("code")) {
                    //获取code
                    String code = url.substring(url.lastIndexOf("=") + 1);
                    //请求token user weibo 并且存入数据库中
                    mWeiBoApi.getToken(Constant.APP_KEY,Constant.APP_SECRET,Constant.GRANT_TYPE,code,Constant.REDIRECT_URL);

                    Intent intent1=new Intent(MainActivity.this,ListActivity.class);
                    startActivity(intent1);
                }
            }
        });
    }


}

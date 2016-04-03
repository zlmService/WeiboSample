package com.demo.zlm.weibosample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.Thread.TokenThread;
import com.demo.zlm.weibosample.api.JsonRequst;


public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private int id;
    private int max_id;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        queue = Volley.newRequestQueue(this);
        boolean foreclogin;
        Intent intent = getIntent();
        foreclogin = intent.getBooleanExtra("foreclogin", false);
        String loadUrl = "https://api.weibo.com/oauth2/authorize?client_id=2996785772&redirect_uri=http://www.yingshibao.com&scope=all&forcelogin=" + foreclogin;


        mWebView.loadUrl(loadUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                Log.e(MainActivity.class.getSimpleName(), "url=" + url);
                if (url.contains("code")) {
                    //huo qu code
                    String code = url.substring(url.lastIndexOf("=") + 1);
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);

                    //传入code    请求token
                    new TokenThread(MainActivity.this, code).start();
                    getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);
                    //请求Json数据 存储到数据库中

                    String token = null;
                    String uid = null;
                    Cursor query = getContentResolver().query(Uri.parse("content://com.zlm.weibo.ContentProvider/token"), null, null, null, null);
                    if (query.moveToFirst()) {
                        token = query.getString(query.getColumnIndex("token"));
                        uid = query.getString(query.getColumnIndex("uid"));
                    }
                    //请求微博
                    String httpUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
                    String jsonUrl = httpUrl + "?access_token=" + token + "&since_id=" + id + "&max_id=" + max_id + "+&count=20";
                    new JsonRequst().getJson(MainActivity.this, queue, jsonUrl);

                    //请求用户信息
                    String userUrl="https://api.weibo.com/2/users/show.json?access_token="+token+"&uid="+uid;
                    new JsonRequst().getUser(MainActivity.this,queue,userUrl);
                    startActivity(intent);
                }
            }
        });
    }


}

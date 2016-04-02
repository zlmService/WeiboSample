package com.demo.zlm.weibosample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.Adatper.WeiBoAdapter;
import com.demo.zlm.weibosample.Thread.TokenThread;
import com.demo.zlm.weibosample.api.JsonRequst;

/**
 * Created by malinkang on 2016/3/30.
 */
public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {
    private ListView listView;
    private WeiBoAdapter adapter;
    private String code;
    RequestQueue queue;
    private int lastindex;
    private int firstindex;
    private int id = 0;
    private int max_id = 20;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_item_layout);
        init();
        getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);
        jsonRst();

    }

    public void init() {
        listView = (ListView) findViewById(R.id.token_list);
        queue = Volley.newRequestQueue(this);
        adapter = new WeiBoAdapter(this, null);
//        View footer=getLayoutInflater().inflate(R.layout.footer_view,null);
//        listView.addFooterView(footer);
//        View header=getLayoutInflater().inflate(R.layout.headerview,null);
//        listView.addHeaderView( header);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(this);
        //获取传过来的code  拿到code去请求 得到token
        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        getSupportLoaderManager().initLoader(0, null, this);
        System.out.println("code-----" + code);
        new TokenThread(this, code).start();

    }

    public void jsonRst() {
        String token = null;
        id = 0;
        max_id = 0;
        Cursor query = getContentResolver().query(Uri.parse("content://com.zlm.weibo.ContentProvider/token"), null, null, null, null);
        if (query.moveToFirst()) {
            token = query.getString(query.getColumnIndex("token"));
        }
        String httpUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
        String url = httpUrl + "?access_token=" + token + "&since_id=" + id + "&max_id=" + max_id + "+&count=20";
        new JsonRequst().jsonClick(this, url, queue);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse("content://com.zlm.weibo.ContentProvider/weibo");
        CursorLoader loader = new CursorLoader(this, uri, null, null, null, null);
        return loader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            adapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println(adapter.getCount()+"总共有多少条数据");
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && adapter.getCount() == lastindex) {
            Toast.makeText(ListActivity.this, "是最后一条了", Toast.LENGTH_SHORT).show();

            //如果停止滑动 并且是最后一条 就在加载20条数据
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            jsonRst();
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING && 0 == firstindex) {
            Toast.makeText(ListActivity.this, "第一条", Toast.LENGTH_SHORT).show();
            getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);

            jsonRst();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastindex = firstVisibleItem + visibleItemCount ;
        System.out.println(firstVisibleItem+"---"+visibleItemCount+"---"+totalItemCount);
        firstindex=firstVisibleItem;
        System.out.println(lastindex+"最后一条索引"+"-----"+"第一条数据="+firstindex);
    }

}

//    public void fangfa(){
//        final HttpClint clint = new HttpClint();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = "https://api.weibo.com/oauth2/access_token";
//
//                Map<String,String> map=new HashMap<>();
//                map.put("client_id",Constant.APP_KEY);
//                map.put("client_secret",Constant.APP_SECRET);
//                map.put("grant_type",Constant.GRANT_TYPE);
//                map.put("code",code);
//                map.put("redirect_uri",Constant.REDIRECT_URL);
//                String token=clint.postRequest(url,null,map);
//            }
//        });
//    }



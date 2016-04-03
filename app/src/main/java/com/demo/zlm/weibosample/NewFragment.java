package com.demo.zlm.weibosample;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.Adatper.WeiBoAdapter;
import com.demo.zlm.weibosample.api.JsonRequst;


public class NewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> ,AbsListView.OnScrollListener{
    private ListView listView;
    WeiBoAdapter adapter;
    private int lastindex;
    private int firstindex;
    private RequestQueue queue;
    private int page=2;
    public NewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new, container, false);
        queue = Volley.newRequestQueue(getContext());
        init(view);
        return view;


    }

    public void init(View view) {
        listView = (ListView) view.findViewById(R.id.token_list);
//        getContext().getContentResolver().query()
        adapter = new WeiBoAdapter(getContext(), null);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(this);
        getLoaderManager().initLoader(0, null, this);

        String token = getToken();
        String httpUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
        String jsonUrl = httpUrl + "?access_token=" + token + "&since_id=0&max_id=0&count=20";
        new JsonRequst().getJson(getContext(),queue,jsonUrl);



    }

    //CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse("content://com.zlm.weibo.ContentProvider/weibo");
        CursorLoader loader = new CursorLoader(getContext(), uri, null, null, null, null);
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

    //下拉刷新
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && adapter.getCount() == lastindex) {
            Toast.makeText(getContext(), "是最后一条了", Toast.LENGTH_SHORT).show();

            String token = getToken();
            String httpUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
            String jsonUrl = httpUrl + "?access_token=" + token + "&since_id=0&max_id=0&count=20"+"&page="+page;
            new JsonRequst().getJson(getContext(),queue,jsonUrl);
            page++;
            System.out.println(page+"----------------------------");
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && 0 == firstindex) {
            Toast.makeText(getContext(), "第一条", Toast.LENGTH_SHORT).show();
            String token = getToken();
            String httpUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
            String jsonUrl = httpUrl + "?access_token=" + token + "&since_id=0&max_id=0&count=20";
            new JsonRequst().getJson(getContext(),queue,jsonUrl);
            getContext().getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastindex = firstVisibleItem + visibleItemCount ;
        System.out.println(firstVisibleItem+"---"+visibleItemCount+"---"+totalItemCount);
        firstindex=firstVisibleItem;
        System.out.println(lastindex+"最后一条索引"+"-----"+"第一条数据="+firstindex);
    }

    public String getToken(){
        String token = null;
        Cursor query = getContext().getContentResolver().query(Uri.parse("content://com.zlm.weibo.ContentProvider/token"), null, null, null, null);
        if (query.moveToFirst()) {
            token = query.getString(query.getColumnIndex("token"));
        }
      return token;

    }
}

package com.demo.zlm.weibosample.fragment;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.content.ContentProvider;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.Adatper.WeiBoAdapter;
import com.demo.zlm.weibosample.R;
import com.demo.zlm.weibosample.api.DbUtils;
import com.demo.zlm.weibosample.model.Status;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class PubFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> ,AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {
    private PullToRefreshListView listView;
    WeiBoAdapter adapter;
    private RequestQueue queue;
    String token;
    int page=1;
    public PubFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        init(view);
         queue = Volley.newRequestQueue(getContext());

        DbUtils.getPublicWeiboJson(page,true);

        return view;


    }

    public void init(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.token_list);
        adapter = new WeiBoAdapter(getContext(), null);
        listView.setAdapter(adapter);
         token = DbUtils.getToken();
        listView.setOnScrollListener(this);
        getLoaderManager().initLoader(0, null, this);
        listView.getRefreshableView().setOnItemLongClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
//                        下拉刷新
                        DbUtils.getPublicWeiboJson(page,true);
                        //完成刷新
                        listView.onRefreshComplete();
                    }
                });
            }
        });

    }

    //CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getContext(), ContentProvider.createUri(Status.class, null), null, null, null, null);
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

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition()==(view.getCount()-1)){
            Toast.makeText(getContext(), "是最后一条了", Toast.LENGTH_SHORT).show();
            //分页加载
            DbUtils.getPublicWeiboJson(page,false);
            page++;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        final EditText editText=new EditText(getContext());
        new AlertDialog.Builder(getContext()).setTitle("转发理由").setView(editText)
        .setNegativeButton("取消",null)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text=editText.getText().toString();
                String url = "https://api.weibo.com/2/statuses/repost.json";
                String token=DbUtils.getToken();
                String weibo_id=DbUtils.getWeiboId(id)+"";
                DbUtils.setWeiboRepost(queue,url,token,weibo_id,text);
            }
        }).show();



        return true;
    }
}

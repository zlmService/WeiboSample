package com.demo.zlm.weibosample.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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


public class MeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {
    private PullToRefreshListView listView;
    WeiBoAdapter adapter;
    private RequestQueue queue;
    String uid;
    int page = 1;

    public MeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        queue = Volley.newRequestQueue(getContext());
        listView = (PullToRefreshListView) view.findViewById(R.id.token_list);
        adapter = new WeiBoAdapter(getContext(), null);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        getLoaderManager().initLoader(0, null, this);
        uid=DbUtils.getUid();
        DbUtils.getUserWeiboJson(page,uid,true);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        DbUtils.getUserWeiboJson(page,uid,true);
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

    //下拉刷新
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition()==(view.getCount()-1)) {
            Toast.makeText(getContext(), "是最后一条了", Toast.LENGTH_SHORT).show();

            DbUtils.getUserWeiboJson(page,uid,false);
            page++;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }


}

package com.demo.zlm.weibosample;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.Adatper.WeiBoAdapter;
import com.demo.zlm.weibosample.Thread.TokenThread;


public class NewFragment extends Fragment {
    private ListView listView;


    Context context;
    public NewFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_layou, container, false);
        init(view);
        return view;

    }

    public void init(View view) {
        listView = (ListView) view.findViewById(R.id.token_list);
        queue = Volley.newRequestQueue(context);
        adapter = new WeiBoAdapter(context, null);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(this);
        //获取传过来的code  拿到code去请求 得到token
        Intent intent = getContext().getIntent;
        code = intent.getStringExtra("code");

        getSupportLoaderManager().initLoader(0, null, this);
        System.out.println("code-----" + code);
        new TokenThread(this, code).start();

    }

}

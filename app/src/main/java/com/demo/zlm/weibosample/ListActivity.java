package com.demo.zlm.weibosample;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by malinkang on 2016/3/30.
 */
public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_item_layout);

        init();
    }

    public void init() {

        tv1= (TextView) findViewById(R.id.tv_1);
        tv2= (TextView) findViewById(R.id.tv_2);
        tv3= (TextView) findViewById(R.id.tv_3);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().
                add(R.id.frag_layout, new NewFragment())
                .commit();
        flag=true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_1:
                setTv1Color();
                getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);
                if (!flag) {
                    toggleFragment(new NewFragment());
                    flag=true;
                }

                break;
            case R.id.tv_2:
                setTv2Color();
                getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), null, null);
                toggleFragment(new PubFragment());
                flag=false;

                break;
            case R.id.tv_3:
                break;
        }
    }
    public void toggleFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frag_layout, fragment)
                .commit();
    }
    private void setTv1Color(){
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv2.setTextColor(Color.parseColor("#b0ffffff"));
        tv3.setTextColor(Color.parseColor("#b0ffffff"));
    }
    private void setTv2Color(){
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv1.setTextColor(Color.parseColor("#b0ffffff"));
        tv3.setTextColor(Color.parseColor("#b0ffffff"));
    }
    private void setTv3Color(){
        tv3.setTextColor(Color.parseColor("#ffffff"));
        tv2.setTextColor(Color.parseColor("#b0ffffff"));
        tv1.setTextColor(Color.parseColor("#b0ffffff"));
    }
}




package com.demo.zlm.weibosample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by malinkang on 2016/3/30.
 */
public class ContentActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton sign_in_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layou);
        sign_in_btn= (ImageButton) findViewById(R.id.sign_in_btn);
        sign_in_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}

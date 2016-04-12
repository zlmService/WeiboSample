package com.demo.zlm.weibosample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.demo.zlm.weibosample.fragment.MeFragment;
import com.demo.zlm.weibosample.fragment.NewFragment;
import com.demo.zlm.weibosample.fragment.PubFragment;
import com.demo.zlm.weibosample.model.Status;
import com.demo.zlm.weibosample.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by malinkang on 2016/3/30.
 */
public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv_userName;
    private CircleImageView userImg_iv;
    private TextView tv_userLocation;
    private boolean flag;
    private boolean foreclogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("aaa", "Create");
        setContentView(R.layout.ativity_item_layout);

        init();
    }

    public void init() {

        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        userImg_iv = (CircleImageView) findViewById(R.id.userImg_iv);
        tv_userName = (TextView) findViewById(R.id.userName_tv);
        tv_userLocation = (TextView) findViewById(R.id.userLocation_tv);
        getUserData();
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv_userName.setOnClickListener(this);
        userImg_iv.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().
                add(R.id.frag_layout, new NewFragment())
                .commit();
        flag = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("aaa", "onRestart");
        getUserData();
    }

    public void getUserData() {
        User user = new Select().from(User.class).executeSingle();
        String userName = null;
        String location = null;
        String imageUrl = null;
        if (user != null) {
            userName = user.getName();
            location = user.getLocation();
            imageUrl = user.getProfile_image_url();
        }
        ImageLoader.getInstance().displayImage(imageUrl,userImg_iv);

        tv_userName.setText(userName);
        tv_userLocation.setText(location);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_1:
                setTv1Color();
                new Delete().from(Status.class).execute();
                if (!flag) {
                    toggleFragment(new NewFragment());
                    flag = true;
                }
                break;

            case R.id.tv_2:
                setTv2Color();
                new Delete().from(Status.class).execute();
                toggleFragment(new PubFragment());
                flag = false;
                break;

            case R.id.tv_3:
                setTv3Color();
                new Delete().from(Status.class).execute();
                toggleFragment(new MeFragment());
                flag = false;
                break;

            case R.id.userName_tv:
                new Delete().from(Status.class).execute();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("foreclogin", foreclogin);
                startActivity(intent);
                break;
            case R.id.userImg_iv:
                Intent intent1 = new Intent(this, SendWeiBoActivity.class);
                startActivity(intent1);
                break;

        }
    }

    public void toggleFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frag_layout, fragment)
                .commit();
    }

    private void setTv1Color() {
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv2.setTextColor(Color.parseColor("#b0ffffff"));
        tv3.setTextColor(Color.parseColor("#b0ffffff"));
    }

    private void setTv2Color() {
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv1.setTextColor(Color.parseColor("#b0ffffff"));
        tv3.setTextColor(Color.parseColor("#b0ffffff"));
    }

    private void setTv3Color() {
        tv3.setTextColor(Color.parseColor("#ffffff"));
        tv2.setTextColor(Color.parseColor("#b0ffffff"));
        tv1.setTextColor(Color.parseColor("#b0ffffff"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "切换账号");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("foreclogin", foreclogin);
                startActivity(intent);
                Toast.makeText(ListActivity.this, "点击了Menu", Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}




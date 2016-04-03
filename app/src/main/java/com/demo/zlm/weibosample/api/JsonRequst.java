package com.demo.zlm.weibosample.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.zlm.weibosample.SQLite.DataMata;
import com.demo.zlm.weibosample.User.Statuses;
import com.demo.zlm.weibosample.User.User;
import com.demo.zlm.weibosample.User.WeiBo;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by malinkang on 2016/4/1.
 */
public class JsonRequst {

    //传过来url  请求JSon 数据
    public void jsonClick(final Context context, String url, RequestQueue queue) {


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                WeiBo weiBo = gson.fromJson(response, WeiBo.class);
                List<Statuses> statuses = weiBo.getStatuses();
                for (int i = 0; i < statuses.size(); i++) {
                    Statuses statuses1 = statuses.get(i);
                    String created_at = statuses1.getCreated_at();
                    String source = statuses1.getSource();
                    String text = statuses1.getText();
                    User user = statuses1.getUser();
                    String location = user.getLocation();
                    String name = user.getName();
                    String profile_image_url = user.getProfile_image_url();
                    System.out.println("-----------------微博："+"created_at=" + created_at + "source=" + source + "text=" + text + "location=" + location + "name=" + name + "image_url=" + profile_image_url);
                    //往数据库中添加数据
                    ContentValues values = new ContentValues();
                    values.put(DataMata.WeiBoTable.CREATED_AT, created_at);
                    values.put(DataMata.WeiBoTable.SOURCE, source);
                    values.put(DataMata.WeiBoTable.TEXT, text);
                    values.put(DataMata.WeiBoTable.LOCATION, location);
                    values.put(DataMata.WeiBoTable.NAME, name);
                    values.put(DataMata.WeiBoTable.IMAGE_URL, profile_image_url);
                    context.getContentResolver().insert(Uri.parse("content://com.zlm.weibo.ContentProvider/weibo"), values);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley 请求微博错误");
            }
        });
        queue.add(request);
    }

    public void getJson(Context context, RequestQueue queue,String jsonUrl){
        //传入jsonUrl 得到Json数据
         jsonClick(context, jsonUrl, queue);

    }
    public void getUser(final Context context, RequestQueue queue, String userUrl){
        StringRequest request = new StringRequest(Request.Method.GET, userUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);

                    String location = user.getLocation();
                    String name = user.getName();
                    String profile_image_url = user.getProfile_image_url();
                    System.out.println("--------------------用户："+"location=" + location + "name=" + name + "image_url=" + profile_image_url);
                    //往数据库中添加数据
                    ContentValues values = new ContentValues();
                    values.put(DataMata.WeiBoTable.LOCATION, location);
                    values.put(DataMata.WeiBoTable.NAME, name);
                    values.put(DataMata.WeiBoTable.IMAGE_URL, profile_image_url);
                context.getContentResolver().insert(Uri.parse("content://com.zlm.weibo.ContentProvider/user"), values);
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley 请求用户错误");
            }
        });
        queue.add(request);
    }
}

package com.demo.zlm.weibosample.api;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.zlm.weibosample.model.Status;
import com.demo.zlm.weibosample.model.Token;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by malinkang on 2016/3/31.
 */
public class DbUtils {

    private Context context;

    public DbUtils(Context context) {
        this.context = context;
    }

    //获取token
    public static String getToken() {
        String tokenCode = null;
        Token token = new Select().from(Token.class).executeSingle();
        if (token != null) {
            tokenCode = token.getToken();
        }
        return tokenCode;

    }


    /**
     * //获取微博的 微博id  用于转发
     *
     * @param id 数据库中对应的id
     * @return
     */
    public static long getWeiboId(long id) {
        long weiboIdJson = 0;
        Status weibo = new Select().from(Status.class).where("_id = ?", id).executeSingle();
        if (weibo != null) {
            weiboIdJson = weibo.getWeibo_id();
        }
        return weiboIdJson;
    }

    //获取用户的Uid  用于查询用户发送的微博
    public static String getUid() {
        String uid = null;
        Token token = new Select().from(Token.class).executeSingle();
        if (token != null) {
            uid = token.getUid();
        }
        return uid;
    }


    /**
     * //获取关注人发送的微博
     *
     * @param page 分页
     * @param frag 是否删除数据库之前的数据
     */
    public static void getWeiboJson(int page, boolean frag) {
        String token = getToken();
        new WeiBoApi().getFriendsTimeline(token, "0", "0", "20", page, frag);
    }

    //获取公众号发送的微博
    public static void getPublicWeiboJson(int page, boolean frag) {
        String token = getToken();
        new WeiBoApi().getPublicTimeline(token, 20, page, frag);
    }

    //获取用户发送的微博
    public static void getUserWeiboJson(int page, String uid, boolean frag) {
        String token = getToken();
        new WeiBoApi().getUserTimeline(token, uid, page, frag);
    }

    //转发微博
    public static void setWeiboRepost(RequestQueue queue, String url, final String token, final String weiboId, final String status) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("access_token", token);
                map.put("id", weiboId);
                map.put("status", status);
                return map;
            }
        };
        queue.add(request);
    }

}

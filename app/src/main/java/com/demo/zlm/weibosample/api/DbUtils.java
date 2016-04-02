package com.demo.zlm.weibosample.api;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.demo.zlm.weibosample.User.Token;
import com.google.gson.Gson;

/**
 * Created by malinkang on 2016/3/31.
 */
public  class DbUtils  {

    private Context context;
    public DbUtils(Context context){
        this.context=context;
    }
    public  void  insertCode(String code){

        String token_code = formGson(null);
        ContentValues values=new ContentValues();
        values.put("token",token_code);
        context.getContentResolver().insert(Uri.parse("content://com.zlm.weibo.ContentProvider/token"),values);

    }
    public  String formGson(String json){
        Gson gson=new Gson();
        Token token = gson.fromJson(json.toString(),Token.class);
        System.out.println("请求的token"+token.toString());
        String token_code = token.getToken();
       return token_code;
    }
}

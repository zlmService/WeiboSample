package com.demo.zlm.weibosample.Thread;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.demo.zlm.weibosample.Constant;
import com.demo.zlm.weibosample.SQLite.DataHelper;
import com.demo.zlm.weibosample.User.Token;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by malinkang on 2016/3/30.
 */
public class TokenThread extends Thread {
//    https://api.weibo.com/oauth2/access_token?client_id=2996785772&client_secret=95c756465604dc61f3a69ec4ea861ee4&grant_type=authorization_code&code=00efbb00ea6ff1f8e8f57c6b9bdb6ad7&redirect_uri=http://www.yingshibao.com

    String code;
    String url = "https://api.weibo.com/oauth2/access_token";
//    getUrl = url+"?name="+ URLEncoder.encode(name, "utf-8")+"&age="+age;
    Context context;
    public TokenThread(Context context, String code) {
        this.code = code;
        this.context=context;
    }

    @Override
    public void run() {
//        context.getContentResolver().delete(Uri.parse("content://com.zlm.weibo.ContentProvider/token"),null,null);

        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            OutputStream outputStream = conn.getOutputStream();
            //把提交的数据 通过outputStream 提交到服务器
            String content = "client_id=" + Constant.APP_KEY + "&client_secret=" + Constant.APP_SECRET + "&grant_type="
                    + Constant.GRANT_TYPE + "&code=" + code + "&redirect_uri=" + Constant.REDIRECT_URL;
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.flush();
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = bf.readLine()) != null) {
                sb.append(line);  //sb就是 返回的令牌 然后 通过令牌  在去请求  得到Json数据
            }
            Gson gson=new Gson();
            Token token = gson.fromJson(sb.toString(), Token.class);
            System.out.println("请求的token"+token.toString());
            String token_code = token.getToken();
            //先判断数据库里面是否已经存在;
            Cursor query = context.getContentResolver().query(Uri.parse("content://com.zlm.weibo.ContentProvider/token"), null, null, null, null);
            if(query.moveToFirst()){
                String code_token= query.getString(query.getColumnIndex("token"));
                if (code_token.equals(token_code)){
                    System.out.println("相等，不存储了");
                    return;
                }
                else{
                    ContentValues values=new ContentValues();
                    values.put("token",token_code);
                    context.getContentResolver().insert(Uri.parse("content://com.zlm.weibo.ContentProvider/token"),values);
                }
            }
            else{
                ContentValues values=new ContentValues();
                values.put("token",token_code);
                context.getContentResolver().insert(Uri.parse("content://com.zlm.weibo.ContentProvider/token"),values);
            }





        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String postRequest(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (headers != null) {
                Set<String> keys = headers.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = params.get(key);
                    // 添加请求头
                    connection.addRequestProperty(key, value);
                }
            }

            if (params != null) {
                Set<String> keys = params.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = params.get(key);
                    if (baos.size() > 0) {
                        baos.write('&');
                    }
                    baos.write(URLEncoder.encode(key, "UTF-8").getBytes("UTF-8"));
                    baos.write('=');
                    baos.write(URLEncoder.encode(value, "UTF-8").getBytes("UTF-8"));
                }
            }
            System.out.println(new String(baos.toByteArray()));
            OutputStream os = connection.getOutputStream();
            os.write(baos.toByteArray());
            int status = connection.getResponseCode();
            InputStream is;
            if (status >= 400) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufr = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufr.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}


package com.demo.zlm.weibosample.Thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by malinkang on 2016/4/1.
 */
public class ImageThread extends Thread {

    String url;
    Handler handler;

    public  ImageThread(String url, Handler handler){
        this.url=url;
        this.handler=handler;
    }
    @Override
    public void run() {
        super.run();
        try {
            URL imgUrl=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) imgUrl.openConnection();
            conn.setReadTimeout(5000);
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            Message msg=new Message();
            msg.what=1;
            msg.obj=bitmap;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.demo.zlm.weibosample.Thread;

import com.demo.zlm.weibosample.api.MytmArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * Created by malinkang on 2016/3/31.
 */
public class JsonThread extends Thread {

    private String token;
    private String httpUrl="https://api.weibo.com/2/statuses/friends_timeline.json";
    static TrustManager[] xtmArray = new MytmArray[] { new MytmArray() };
    public JsonThread(String token) {
        this.token = token;
    }

    @Override
    public void run() {
        System.out.println(token+"--token--");
        HttpURLConnection http = null;
        try {
            URL url = new URL(httpUrl+"?access_token="+token+"&since_id=0&max_id=0&count=20");
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                http = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认

            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setReadTimeout(5000);
            http.connect();
            int status = http.getResponseCode();
            InputStream is;
            if (status >= 400) {
                is = http.getErrorStream();
            } else {
                is = http.getInputStream();
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            bf.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android 采用X509的证书信息机制
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, xtmArray, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            // HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
            // 不进行主机名确认
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            // System.out.println("Warning: URL Host: " + hostname + " vs. "
            // + session.getPeerHost());
            return true;
        }
    };

}

package com.demo.zlm.weibosample.api;

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
 * Created by malinkang on 2016/3/31.
 */
public class HttpClint {
    public static final int  CONNECT_TIMEOUT = 1000*30;
    public static final int  READ_TIMEOUT = 1000*30;

    public static String postRequest(String url, Map<String, String> headers, Map<String, String> params) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
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

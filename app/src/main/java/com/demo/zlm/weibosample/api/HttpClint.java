package com.demo.zlm.weibosample.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    // 构建分割线
    private static byte[] buildBoundary(String boundary, boolean last) {
        try {
            // Pre-size for the last boundary, the worst case scenario.
            StringBuilder sb = new StringBuilder(boundary.length() + 8);
            sb.append("\r\n");
            sb.append("--");
            sb.append(boundary);
            if (last) {
                sb.append("--");
            }
            sb.append("\r\n\r\n");
            return sb.toString().getBytes("UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write multipart boundary", ex);
        }
    }
    //构建请求头
    private static byte[] buildHeader(String name, Object value) {
        try {
            String transferEncoding = "binary";
            // Initial size estimate based on always-present strings and conservative value lengths.
            StringBuilder headers = new StringBuilder(128);

            headers.append("Content-Disposition: form-data; name=\"");
            headers.append(name);
            long length = 0;
            if (value instanceof File) {
                String fileName = ((File) value).getName();
                if (fileName != null) {
                    headers.append("\"; filename=\"");
                    headers.append(fileName);
                }
                headers.append("\"\r\nContent-Type: ");
                headers.append(URLConnection.guessContentTypeFromName(((File) value).getPath()));
                length = ((File) value).length();
            } else if (value instanceof String) {
                headers.append("\"\r\nContent-Type: ");
                headers.append("text/plain;charset=UTF-8");
                length = ((String) value).getBytes().length;
            }
            if (length != -1) {
                headers.append("\r\nContent-Length: ").append(length);
            }

            headers.append("\r\nContent-Transfer-Encoding: ");
            headers.append(transferEncoding);
            headers.append("\r\n\r\n");

            return headers.toString().getBytes("UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write multipart header", ex);
        }
    }

    public static String upload(String url, Map<String, String> headers, Map<String, Object> params) {
        try {
            String BOUNDARY = UUID.randomUUID().toString();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (headers != null) {
                Set<String> keys = headers.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = headers.get(key);
                    // 添加请求头
                    connection.addRequestProperty(key, value);
                }
            }
            connection.addRequestProperty("Content-Type", "multipart/form-data; boundary="+BOUNDARY);


            if (params != null) {
                Set<String> keys = params.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = params.get(key);
                    baos.write(buildBoundary(BOUNDARY, false));
                    baos.write(buildHeader(key, value));
                    if (value instanceof String) {
                        baos.write(((String) value+"\r\n").getBytes("UTF-8"));
                    } else if (value instanceof File) {
                        FileInputStream fis = new FileInputStream((File) value);
                        byte[] bytes = new byte[4096];
                        int hasRead = 0;
                        while ((hasRead = fis.read(bytes)) != -1) {
                            baos.write(bytes, 0, hasRead);
                        }
                    }
                }
            }

            baos.write(buildBoundary(BOUNDARY, true));
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
            System.out.println(sb);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }
    }
}

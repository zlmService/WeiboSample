package com.demo.zlm.weibosample;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.api.HttpClint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by malinkang on 2016/4/5.
 */
public class SendWeiBoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private ImageView imageView;
    private Button yes_btn;
    private Button no_btn;
    private RequestQueue queue;
    private Uri imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_bo);

        init();
    }

    public void init() {
        editText = (EditText) findViewById(R.id.text_send_ed);
        imageView = (ImageView) findViewById(R.id.photo);
        yes_btn = (Button) findViewById(R.id.yes_btn);
        no_btn = (Button) findViewById(R.id.no_btn);
        queue = Volley.newRequestQueue(this);
        yes_btn.setOnClickListener(this);
        no_btn.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes_btn:
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String url="https://upload.api.weibo.com/2/statuses/upload.json";
                        String status=editText.getText().toString();
                        String token=getToken();
                        Drawable drawable = imageView.getDrawable();
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("status",status);
                        map.put("access_token",token);
                        File file=new File(imgUrl.getPath());
                        map.put("pic",file);
                       // sendWeiBo(url,token,status)
                        // )

                         HttpClint.upload(url,null,map);
                    }
                }).start();

                Toast.makeText(SendWeiBoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.no_btn:
                finish();
                break;
            case R.id.photo:
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,100);
                break;
        }
    }

    public void sendWeiBo(String url, final String token, final String status) {
        try {
            URL httpUrl=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();

            OutputStream os = conn.getOutputStream();
            String content = "access_token=" + URLEncoder.encode(token,"utf-8")+ "&status=" + URLEncoder.encode(status,"utf-8");
            os.write(content.getBytes("utf-8"));
            os.flush();
            int code = conn.getResponseCode();
            InputStream is;
            if (code >= 400) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufr = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufr.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getToken() {
        String token = null;
        Cursor query = getContentResolver().query(Uri.parse("content://com.zlm.weibo.ContentProvider/token"), null, null, null, null);
        if (query.moveToFirst()) {
            token = query.getString(query.getColumnIndex("token"));
        }
        return token;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            if(data!=null){
                imageView.setImageURI(data.getData());
                imgUrl=data.getData();
            }
        }
    }
}

package com.demo.zlm.weibosample;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.zlm.weibosample.api.DbUtils;
import com.demo.zlm.weibosample.api.WeiBoApi;

import java.io.File;

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
                String status=editText.getText().toString();
                String token= DbUtils.getToken();
                File file=new File(getRealPathFromURI(SendWeiBoActivity.this,imgUrl));
                new WeiBoApi().upload(token,status,file);
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
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            if(data!=null){
                imgUrl=data.getData();
                imageView.setImageURI(data.getData());
            }
        }
    }
}

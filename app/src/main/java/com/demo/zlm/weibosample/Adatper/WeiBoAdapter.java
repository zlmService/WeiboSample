package com.demo.zlm.weibosample.Adatper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.zlm.weibosample.R;
import com.demo.zlm.weibosample.SQLite.DataMata;
import com.demo.zlm.weibosample.Thread.ImageThread;


/**
 * Created by malinkang on 2016/3/31.
 */
public class WeiBoAdapter extends CursorAdapter {

    public WeiBoAdapter(Context context, Cursor c){
        super(context,c,true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.weibo_item,parent,false);
      new ViewHolder(v);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder= (ViewHolder) view.getTag();
        query(cursor,holder);
    }

    public void query(Cursor cursor, final ViewHolder holder){
        String text = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.TEXT));
        String name = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.NAME));
        String location = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.LOCATION));
        String image = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.IMAGE_URL));
        String time = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.CREATED_AT));
        holder.tv_name.setText(name);
        holder.tv_text.setText(text);
        holder.tv_location.setText(location);
        holder.tv_time.setText(time);
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    Bitmap bitmap= (Bitmap) msg.obj;
                    holder.img_profile.setImageBitmap(bitmap);
                }
            }
        };
        new ImageThread(image,handler).start();


    }

    public  class ViewHolder{
        ImageView img_profile;
        TextView tv_time;
        TextView tv_name;
        TextView tv_location;
        TextView tv_text;

        ViewHolder(View v){
            tv_location= (TextView) v.findViewById(R.id.tv_location);
            tv_text= (TextView) v.findViewById(R.id.tv_text);
            tv_name= (TextView) v.findViewById(R.id.tv_name);
            tv_time= (TextView) v.findViewById(R.id.tv_time);
            img_profile= (ImageView) v.findViewById(R.id.img_profile);
            v.setTag(this);
        }
    }
}

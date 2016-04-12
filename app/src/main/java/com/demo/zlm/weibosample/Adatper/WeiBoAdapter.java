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
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.demo.zlm.weibosample.R;
import com.demo.zlm.weibosample.SQLite.DataMata;
import com.demo.zlm.weibosample.Thread.ImageThread;
import com.demo.zlm.weibosample.model.Status;
import com.demo.zlm.weibosample.model.Token;
import com.demo.zlm.weibosample.model.User;
import com.demo.zlm.weibosample.model.WeiBoUser;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by malinkang on 2016/3/31.
 */
public class WeiBoAdapter extends CursorAdapter {

    public WeiBoAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.weibo_item, parent, false);
        new ViewHolder(v);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        query(cursor, holder);
    }

    public void query(Cursor cursor, final ViewHolder holder) {

        String text = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.TEXT));
        String time = cursor.getString(cursor.getColumnIndex(DataMata.WeiBoTable.CREATED_AT));

        int user = cursor.getInt(cursor.getColumnIndex("user"));
        WeiBoUser model= getRandom(user);
//        WeiBoUser model = new Select().from(WeiBoUser.class).where("_id =", user).executeSingle();
        if (model != null) {
            String name=  model.getName();
            String location= model.getLocation();
            String image=model.getProfile_image_url();
            holder.tv_name.setText(name);
            holder.tv_location.setText(location);
            ImageLoader.getInstance().displayImage(image, holder.img_profile);
        }
        holder.tv_text.setText(text);
        holder.tv_time.setText(time);
    }
    public static WeiBoUser getRandom(int user) {
        return new Select()
                .from(WeiBoUser.class)
                .where("_id = ?", user)
                .executeSingle();
    }
    public class ViewHolder {
        ImageView img_profile;
        TextView tv_time;
        TextView tv_name;
        TextView tv_location;
        TextView tv_text;

        ViewHolder(View v) {
            tv_location = (TextView) v.findViewById(R.id.tv_location);
            tv_text = (TextView) v.findViewById(R.id.tv_text);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            img_profile = (ImageView) v.findViewById(R.id.img_profile);
            v.setTag(this);
        }
    }
}

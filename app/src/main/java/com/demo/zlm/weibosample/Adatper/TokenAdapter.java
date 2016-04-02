package com.demo.zlm.weibosample.Adatper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.demo.zlm.weibosample.R;
import com.demo.zlm.weibosample.SQLite.DataMata;
import com.demo.zlm.weibosample.Thread.TokenThread;

/**
 * Created by malinkang on 2016/3/31.
 */
public class TokenAdapter extends CursorAdapter {


    public TokenAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }
    public TokenAdapter(Context context,Cursor c){
        super(context,c,true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.token_item,parent,false);
      new ViewHolder(v);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder= (ViewHolder) view.getTag();
        String token = cursor.getString(cursor.getColumnIndex(DataMata.TokenTable.TOKEN));
        holder.tv_token.setText(token);


    }

    public  class ViewHolder{
        TextView tv_token;

        ViewHolder(View v){
            tv_token= (TextView) v.findViewById(R.id.tv_token);
            v.setTag(this);
        }
    }
}

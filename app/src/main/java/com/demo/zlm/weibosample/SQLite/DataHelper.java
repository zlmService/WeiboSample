package com.demo.zlm.weibosample.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malinkang on 2016/3/31.
 */
public class DataHelper extends SQLiteOpenHelper {


    public static final String DB_NAME="weibo.db";
    //老版本
    public static final int VERSION=1;
    //新版本
    public static final int VERSION_TWO=2;
    public DataHelper(Context context){
        super(context, DB_NAME, null, VERSION_TWO);

    }
    public static final String CREATE_TOKEN="CREATE TABLE IF NOT EXISTS token_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT,token Text)";
    public static final String CREATE_WEIBO="CREATE TABLE IF NOT EXISTS weibo_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT,CREATED_AT Text,SOURCE Text,TEXT Text,LOCATION Text,NAME Text,IMAGE_URL Text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOKEN);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_WEIBO);

    }
}

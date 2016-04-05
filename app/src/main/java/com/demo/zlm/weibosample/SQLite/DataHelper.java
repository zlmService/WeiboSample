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
    public static final int VERSION_FIVE=5;
    public static final int VERSION_Four=4;

    public DataHelper(Context context){
        super(context, DB_NAME, null, VERSION_FIVE);

    }
    public static final String CREATE_TOKEN="CREATE TABLE IF NOT EXISTS token_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT,token Text,uid Text)";
    public static final String CREATE_WEIBO="CREATE TABLE IF NOT EXISTS weibo_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT,WEIBO_ID Integer,CREATED_AT Text,SOURCE Text,TEXT Text,LOCATION Text,NAME Text,IMAGE_URL Text)";
    public static final String CREATE_USER="CREATE TABLE IF NOT EXISTS user_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT,name Text,image_url Text,location Text)";

    public static final String DROP_WEIBO="DROP TABLE weibo_tb";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOKEN);
        db.execSQL(CREATE_WEIBO);
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

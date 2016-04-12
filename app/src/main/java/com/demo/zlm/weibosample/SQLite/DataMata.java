package com.demo.zlm.weibosample.SQLite;

import android.provider.BaseColumns;

/**
 * Created by malinkang on 2016/3/31.
 */
public class DataMata  {

    public static abstract  class TokenTable implements BaseColumns{
        public static final String TABLE_NAME="token_tb";
        public static final String TOKEN="token";
        public static final String UID="uid";
        public static final String REMIND_IN="remind_in";
        public static final String EXPIRES_IN="expires_in";
    }
    public static abstract  class WeiBoTable implements  BaseColumns{
        public static final String TABLE_NAME="weibo_tb";
        //时间 来源设备 文本 位置 名字 头像
        public static final String CREATED_AT="CREATED_AT";
        public static final String  SOURCE="SOURCE";
        public static final String  TEXT="TEXT";
        public static final String  LOCATION="LOCATION";
        public static final String  NAME="NAME";
        public static final String IMAGE_URL="IMAGE_URL";
        public static final String WEIBO_ID="WEIBO_ID";

    }

    public static abstract  class UserTable implements  BaseColumns{
        public static final String TABLE_NAME="user_tb";
        //用户信息
        public static final String  LOCATION="location";
        public static final String  NAME="name";
        public static final String IMAGE_URL="image_url";

    }
    public static abstract  class WeiBoUserTable implements  BaseColumns{
        public static final String TABLE_NAME="weiboUser_tb";
        //用户信息
        public static final String  LOCATION="location";
        public static final String  NAME="name";
        public static final String IMAGE_URL="image_url";

    }
//    public static abstract class
}

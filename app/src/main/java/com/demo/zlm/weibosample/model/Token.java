package com.demo.zlm.weibosample.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.demo.zlm.weibosample.SQLite.DataMata;

/**
 * Created by malinkang on 2016/3/31.
 */

@Table(name= DataMata.TokenTable.TABLE_NAME,id = DataMata.UserTable._ID)
public class Token extends Model {
    @Column(name=DataMata.TokenTable.TOKEN,unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    private String access_token;
    @Column(name=DataMata.TokenTable.REMIND_IN)
    private String remind_in;
    @Column(name=DataMata.TokenTable.EXPIRES_IN)
    private String expires_in;
    @Column(name=DataMata.TokenTable.UID)
    private String  uid;

    public String getRemind_in() {
        return remind_in;
    }

    public void setRemind_in(String remind_in) {
        this.remind_in = remind_in;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return access_token;
    }
    public void setToken(String token) {
        this.access_token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + access_token + '\'' +
                ", remind_in='" + remind_in + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}

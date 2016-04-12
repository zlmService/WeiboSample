package com.demo.zlm.weibosample.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.demo.zlm.weibosample.SQLite.DataMata;
import com.google.gson.annotations.SerializedName;

/**
 * Created by malinkang on 2016/4/9.
 */
@Table(name=DataMata.WeiBoTable.TABLE_NAME,id = DataMata.UserTable._ID)
public class Status extends Model {
    @SerializedName("id")
    @Column(name=DataMata.WeiBoTable.WEIBO_ID,unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    public Long weibo_id;
    @Column(name=DataMata.WeiBoTable.CREATED_AT)
    public String created_at;
    @Column(name=DataMata.WeiBoTable.SOURCE)
    public String source;
    @Column(name=DataMata.WeiBoTable.TEXT)
    public String text;
    @Column(name="user")
    public WeiBoUser user;

    public Long getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(Long weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WeiBoUser getWeiBoUser() {
        return user;
    }

    public void setWeiBoUser(WeiBoUser user) {
        this.user = user;
    }
}

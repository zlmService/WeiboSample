package com.demo.zlm.weibosample.User;

import java.util.List;

/**
 * Created by malinkang on 2016/3/31.
 */
public class Statuses {
    // 时间  内容   来自哪里 发送人信息
    String created_at;
    String text;
    String source;
    User user;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Statuses{" +
                "created_at='" + created_at + '\'' +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", user=" + user +
                '}';
    }
}

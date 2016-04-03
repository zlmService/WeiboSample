package com.demo.zlm.weibosample.User;

/**
 * Created by malinkang on 2016/3/31.
 */
public class Token {
    private String access_token;
    private String remind_in;
    private String expires_in;
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

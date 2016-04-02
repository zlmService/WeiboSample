package com.demo.zlm.weibosample.User;

/**
 * Created by malinkang on 2016/3/31.
 */
public class User {
    //名字   地址  头像
    String name;
    String location;
    String profile_image_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                '}';
    }
}

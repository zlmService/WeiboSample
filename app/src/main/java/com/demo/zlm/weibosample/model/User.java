package com.demo.zlm.weibosample.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.demo.zlm.weibosample.SQLite.DataMata;

/**
 * Created by malinkang on 2016/3/31.
 */
@Table(name= DataMata.UserTable.TABLE_NAME,id = DataMata.UserTable._ID)
public class User extends Model{
    @Column(name= DataMata.UserTable.NAME)
   public  String name; //名字
    @Column(name=DataMata.UserTable.LOCATION)
    public  String location;//位置
    @Column(name=DataMata.UserTable.IMAGE_URL)
    public  String profile_image_url; //头像

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

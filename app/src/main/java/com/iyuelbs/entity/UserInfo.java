package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class UserInfo extends BmobObject {
    private User user;
    private String avatar;
    private boolean male;
    private String locStatus;
    private BmobGeoPoint geoLocation;
    private BmobRelation tags;
    private BmobRelation friends;
    private BmobRelation messages;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getLocStatus() {
        return locStatus;
    }

    public void setLocStatus(String locStatus) {
        this.locStatus = locStatus;
    }

    public BmobGeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(BmobGeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public BmobRelation getTags() {
        return tags;
    }

    public void setTags(BmobRelation tags) {
        this.tags = tags;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }
}

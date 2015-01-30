package com.iyuelbs.entity;

import android.content.Context;
import android.util.Log;

import com.iyuelbs.app.AppHelper;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class User extends BmobUser {

    public static final String USER_TABLE = "user";

    private BmobRelation friends;
    private String phoneNumber;
    private String avatarUrl;
    private String nickName;
    private boolean male;
    private String locStatus;
    private BmobGeoPoint geoLocation;
    private BmobRelation tags;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSignedAvatar(Context context) {
        String avatar = getAvatarUrl();
        String filename = avatar.substring(avatar.lastIndexOf("/") + 1, avatar.length());
        String result = AppHelper.signAvatar(context, filename, getAvatarUrl());
        Log.e("xifan", "sign " + filename + " " + result);
        return result;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public boolean isMale() {
        return male;
    }

    public void setIsMale(boolean male) {
        this.male = male;
    }
}

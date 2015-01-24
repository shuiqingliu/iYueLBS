package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class Tag extends BmobObject {
    private String title;
    private String message;
    private int tagType;
    private BmobDate appointTime;
    private Places place;
    private UserInfo userInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BmobDate getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(BmobDate appointTime) {
        this.appointTime = appointTime;
    }

    public Places getPlace() {
        return place;
    }

    public void setPlace(Places place) {
        this.place = place;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

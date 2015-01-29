package com.iyuelbs.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class User extends BmobUser {
    public static final String USERINFO_TABLE_NAME = "userInfo";

    private BmobPointer userInfo;
    private BmobRelation friends;
    private String phoneNumber;
    private String avatarUrl;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BmobPointer getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BmobPointer userInfo) {
        this.userInfo = userInfo;
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
}

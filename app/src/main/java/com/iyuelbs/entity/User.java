package com.iyuelbs.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class User extends BmobUser {
    private BmobPointer userInfo;
    private BmobRelation friends;
    private long phoneNumber;

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BmobPointer getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BmobPointer userInfo) {
        this.userInfo = userInfo;
    }
}

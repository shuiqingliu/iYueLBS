package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class Friend extends BmobObject {
    private UserInfo friendInfo;

    public UserInfo getFriendInfo() {
        return friendInfo;
    }

    public void setFriendInfo(UserInfo friendInfo) {
        this.friendInfo = friendInfo;
    }
}

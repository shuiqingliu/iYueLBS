package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class Friend extends BmobObject {

    private static final String USER_FRIEND_TABLE = "userFriend";

    private User user;
    private User userFriend;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(User userFriend) {
        this.userFriend = userFriend;
    }
}

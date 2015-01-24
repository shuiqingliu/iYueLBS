package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class UserMessage extends BmobObject {
    private UserInfo userInfo;
    private UserInfo receiverInfo;
    private boolean isMsg;
    private Tag relatedTag;
    private String message;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(UserInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public Tag getRelatedTag() {
        return relatedTag;
    }

    public void setRelatedTag(Tag relatedTag) {
        this.relatedTag = relatedTag;
    }

    public boolean isMsg() {
        return isMsg;
    }

    public void setMsg(boolean isMsg) {
        this.isMsg = isMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

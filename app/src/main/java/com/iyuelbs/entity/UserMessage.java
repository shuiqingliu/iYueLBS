package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class UserMessage extends BmobObject {
    private User sender;
    private User receiver;
    private Tag relatedTag;
    private String message;
    private boolean isMsg;
    private boolean read;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Tag getRelatedTag() {
        return relatedTag;
    }

    public void setRelatedTag(Tag relatedTag) {
        this.relatedTag = relatedTag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsMsg() {
        return isMsg;
    }

    public void setIsMsg(boolean isMsg) {
        this.isMsg = isMsg;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}

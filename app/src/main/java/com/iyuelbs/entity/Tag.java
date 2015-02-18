package com.iyuelbs.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.Date;

/**
 * Created by Bob Peng on 2015/1/24.
 */
@AVClassName("Tag")
public class Tag extends AVObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_APPOINT_TIME = "appotointTime";
    public static final String KEY_PLACE = "place";
    public static final String KEY_USER = "user";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDetail() {
        return getString(KEY_DETAIL);
    }

    public void setDetail(String detail) {
        put(KEY_DETAIL, detail);
    }

    public Date getAppointTime() {
        return getDate(KEY_APPOINT_TIME);
    }

    public void setAppointTime(Date appointTime) {
        put(KEY_APPOINT_TIME, appointTime);
    }

    public Place getPlace() {
        try {
            return getAVObject(KEY_PLACE, Place.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPlace(Place place) {
        put(KEY_PLACE, place);
    }

    public User getUser() {
        return getAVUser(KEY_USER, User.class);
    }

    public void setUser(User user) {
        put(KEY_USER, user);
    }
}

package com.iyuelbs.entity;

import android.content.Context;

import com.iyuelbs.utils.Utils;
import com.iyuelbs.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class User extends BmobUser {

    public static final String TABLE_NAME = "user";
    public static final String EMAIL = "email";
    public static final String PHONE = "phoneNumber";

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

    /**
     * helper method for user login with phone or email, don't forget set password before it.
     */
    public void multiLogin(final Context context, String key, final SaveListener listener) {
        boolean isEmail = Utils.isEmailString(key);
        if (isEmail || Utils.isPhoneString(key)) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo(isEmail ? EMAIL : PHONE, key);
            query.findObjects(context, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> users) {
                    if (users.size() > 0) {
                        setUsername(users.get(0).getUsername());
                        login(context, listener);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    if (listener == null) {
                        ViewUtils.showToast(context, s);
                    } else {
                        listener.onFailure(i, s);
                    }
                }
            });
        } else {
            setUsername(key);
            login(context, listener);
        }
    }
}

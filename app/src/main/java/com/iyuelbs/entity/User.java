package com.iyuelbs.entity;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.iyuelbs.R;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class User extends AVUser {

    public static final String KEY_NICKNAME = "nickName";
    public static final String KEY_MALE = "male";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_BANNER_URL = "bannerUrl";
    public static final String KEY_SIGNATURE = "signature";
    public static final String KEY_LOC_STATUS = "locStatus";
    public static final String KEY_GEO_LOCATION = "geoLocation";
    public static final String KEY_TAGS = "tags";

//    public void setPassword(String pwd) {
//        super.setPassword(Utils.md5(pwd));
//    }

    /**
     * helper method for user login with phone or email, don't forget set password before it.
     */
    public static void multiLogin(String key, String password, final LogInCallback<User> listener) {
        boolean isPhone = Utils.isPhoneString(key);
        if (isPhone) {
            User.loginByMobilePhoneNumberInBackground(key, password, listener, User.class);
        } else {
            User.logInInBackground(key, password, listener, User.class);
        }
    }

    @Override
    public void setUsername(String username) {
        username = username.toLowerCase().trim();
        super.setUsername(username);
    }

    public String getNickName() {
        return getString(KEY_NICKNAME);
    }

    public void setNickName(String nickName) {
        put(KEY_NICKNAME, nickName);
    }

    public boolean isMale() {
        return getBoolean(KEY_MALE);
    }

    public void setIsMale(boolean male) {
        put(KEY_MALE, male);
    }

    public String getAvatarUrl() {
        AVFile file = getAVFile(KEY_AVATAR);
        return file == null ? ViewUtils.getDrawableUri(R.drawable.ic_default_avatar) : file.getUrl();
    }

    public void setAvatar(AVFile avatar) {
        put(KEY_AVATAR, avatar);
    }

    public String getBanner() {
        return getString(KEY_BANNER_URL);
    }

    public void setBannerUrl(String bannerUrl) {
        put(KEY_BANNER_URL, bannerUrl);
    }

    public String getSignature() {
        return getString(KEY_SIGNATURE);
    }

    public void setSignature(String signature) {
        put(KEY_SIGNATURE, signature);
    }

    public String getLocStatus() {
        return getString(KEY_LOC_STATUS);
    }

    public void setLocStatus(String locStatus) {
        put(KEY_LOC_STATUS, locStatus);
    }

    public AVGeoPoint getGeoLocation() {
        return getAVGeoPoint(KEY_GEO_LOCATION);
    }

//    public AVRelation<Tag> getTags() {
//        return getRelation(KEY_TAGS);
//    }
//
//    public void setTags(Tag tags) {
//        put(KEY_TAGS, tags);
//    }

    public void setGeoLocation(AVGeoPoint geoLocation) {
        put(KEY_GEO_LOCATION, geoLocation);
    }
}

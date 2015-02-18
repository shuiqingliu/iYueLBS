package com.iyuelbs.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;

/**
 * Created by Bob Peng on 2015/1/24.
 */
@AVClassName("Place")
public class Place extends AVObject {

    public static final String KEY_PLACE_NAME = "placeName";
    public static final String KEY_GEO_LOCATION = "geoLocation";
    public static final String KEY_MARKED_NUM = "markedNum";
    public static final String KEY_COMPLETED_NUM = "completedNum";

    public String getPlaceName() {
        return getString(KEY_PLACE_NAME);
    }

    public void setPlaceName(String placeName) {
        put(KEY_PLACE_NAME, placeName);
    }

    public AVGeoPoint getGeoLocation() {
        return getAVGeoPoint(KEY_GEO_LOCATION);
    }

    public void setGeoLocation(AVGeoPoint geoLocation) {
        put(KEY_GEO_LOCATION, geoLocation);
    }

    public int getMarkedNum() {
        return getInt(KEY_MARKED_NUM);
    }

    public void setMarkedNum(int markedNum) {
        put(KEY_MARKED_NUM, markedNum);
    }

    public int getCompletedNum() {
        return getInt(KEY_COMPLETED_NUM);
    }

    public void setCompletedNum(int completedNum) {
        put(KEY_COMPLETED_NUM, completedNum);
    }
}

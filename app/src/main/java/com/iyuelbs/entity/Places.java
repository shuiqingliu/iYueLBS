package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class Places extends BmobObject {
    private BmobGeoPoint geoLocation;
    private int markNum;
    private int finishNum;
    private BmobRelation historyTags;

    public BmobGeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(BmobGeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public int getMarkNum() {
        return markNum;
    }

    public void setMarkNum(int markNum) {
        this.markNum = markNum;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public BmobRelation getHistoryTags() {
        return historyTags;
    }

    public void setHistoryTags(BmobRelation historyTags) {
        this.historyTags = historyTags;
    }
}

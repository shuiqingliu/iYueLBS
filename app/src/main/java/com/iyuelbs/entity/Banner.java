package com.iyuelbs.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by Bob Peng on 2015/2/10.
 */
@AVClassName("Banner")
public class Banner extends AVObject {

    private static final String KEY_ID = "id";
    private static final String KEY_BANNER = "banner";

    public int getId() {
        return getInt(KEY_ID);
    }

    public void setId(int id) {
        put(KEY_ID, id);
    }

    public AVFile getBanner() {
        return getAVFile(KEY_BANNER);
    }

    public void setBanner(AVFile file) {
        put(KEY_BANNER, file);
    }
}

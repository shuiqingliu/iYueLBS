package com.iyuelbs.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class Banners extends BmobObject {
    private BmobFile banner;

    public BmobFile getBanner() {
        return banner;
    }

    public void setBanner(BmobFile banner) {
        this.banner = banner;
    }
}

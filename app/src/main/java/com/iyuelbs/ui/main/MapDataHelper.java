package com.iyuelbs.ui.main;


import com.iyuelbs.entity.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qingliu on 6/6/15.
 */
public class MapDataHelper implements Serializable {
    private User user;
    private String username;     //用户名
    private String title;     //tag title
    private String detail;     //tag 内容
    private String userav;    //用户头像
    private Date date;    //时间
    private String placename;    //地名

    public MapDataHelper() {
    }

    public MapDataHelper(User user,String username, String title, String detail, String userav,
                Date date, String placename) {
        super();
        this.user = user;
        this.username = username;
        this.title = title;
        this.detail = detail;
        this.userav = userav;
        this.date = date;
        this.placename = placename;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getUserAv() {
        return userav;
    }

    public Date getDate() {
        return date;
    }

    public String getPlacename() {
        return placename;
    }
    public User getUser() {
        return user;
    }
}
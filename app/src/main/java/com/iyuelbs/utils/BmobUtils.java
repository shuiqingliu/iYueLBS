package com.iyuelbs.utils;

import android.content.Context;

/**
 * Created by Bob Peng on 2015/2/6.
 */
public class BmobUtils {
    public static void onFailure(Context context, int code, String msg) {
        switch (code) {
            case 101:
                msg = "用户名或密码错误，请重试";
                break;
            case 102:
            case 103:
                msg = "文本格式不正确，请重新输入";
                break;
            case 202:
                msg = "该用户名已被注册，换一个试试吧^_^";
                break;
            case 203:
                msg = "该邮箱已被注册，请重新输入";
                break;
            case 301:
                msg = "请输入正确的电子邮箱";
        }
        ViewUtils.showToast(context, "code " + code + ": " + msg);
    }
}

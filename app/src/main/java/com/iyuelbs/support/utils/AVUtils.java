package com.iyuelbs.support.utils;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.iyuelbs.entity.User;

/**
 * Created by Bob Peng on 2015/2/6.
 */
public class AVUtils {

    public static void onFailure(Context context, AVException exception) {
        String msg = exception.getMessage();
        int code = exception.getCode();
        switch (code) {
            case AVException.INTERNAL_SERVER_ERROR:
                msg = "服务器发生错误，请稍候再试";
                break;
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
            case AVException.USER_MOBILE_PHONENUMBER_TAKEN:
                msg = "该手机号已被注册，请重新输入";
                break;
            case AVException.USER_MOBILEPHONE_NOT_VERIFIED:
                msg = "手机号尚未验证，请验证后继续";
                break;
            case 301:
                msg = "请输入正确的电子邮箱";
        }
        ViewUtils.showToast(context, "code " + code + ": " + msg);
    }

    public static boolean isUserInfoComplete(User user) {
        return user.getNickName() != null && user.getMobilePhoneNumber() != null;
    }
}

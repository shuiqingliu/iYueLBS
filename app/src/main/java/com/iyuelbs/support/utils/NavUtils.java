package com.iyuelbs.support.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Bob Peng on 2015/3/2.
 */
public class NavUtils {
    public static void go(Context context, Class<?> clazz) {
        go(context, clazz, null, 0);
    }

    public static void go(Context context, Class<?> clazz, Bundle bundle) {
        go(context, clazz, bundle, 0);
    }

    public static void go(Context context, Class<?> clazz, int flags) {
        go(context, clazz, null, flags);
    }

    public static void go(Context context, Class<?> clazz, Bundle bundle, int flags) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flags > 0) {
            intent.addFlags(flags);
        }
        context.startActivity(intent);
    }

    public static void go(Context context, Class<?> clazz, Intent newIntent) {
        Intent intent = new Intent(newIntent);
        intent.setComponent(new ComponentName(context, clazz));
        context.startActivity(intent);
    }

    public static void goForResult(Context context, Class<?> clazz, int requestCode) {
        Intent intent = new Intent(context, clazz);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}

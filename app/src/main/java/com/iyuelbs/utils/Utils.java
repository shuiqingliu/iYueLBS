package com.iyuelbs.utils;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Created by Bob Peng on 2015/1/30.
 */
public class Utils {
    public static long getTimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() / 1000l;
    }

    public static Bundle toBundle(JSONObject json) {
        Iterator<String> iterator = json.keys();
        Bundle bundle = new Bundle();
        try {
            while (iterator.hasNext()) {
                String key = iterator.next();
                bundle.putString(key, json.getString(key));
                Log.e("xifan", "key=" + key + " value=" + json.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bundle;
    }
//
//    public static Bundle parseUrl(String url) {
//        try {
//            URL u = new URL(url);
//            Bundle b = decodeUrl(u.getQuery());
//            b.putAll(decodeUrl(u.getRef()));
//            return b;
//        } catch (MalformedURLException e) {
//            return new Bundle();
//        }
//    }
//
//    public static Bundle decodeUrl(String s) {
//        Bundle params = new Bundle();
//        if (TextUtils.isEmpty(s)) {
//            return params;
//        }
//
//        String array[] = s.split("&");
//        for (String parameter : array) {
//            if (TextUtils.isEmpty(parameter)) {
//                continue;
//            }
//            String v[] = parameter.split("=");
//            if (v.length < 2) {
//                continue;
//            }
//            params.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
//        }
//
//        return params;
//    }
}

package com.iyuelbs.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iyuelbs.app.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Bob Peng on 2015/1/30.
 */
public class Utils {
    private static final String REGEX_PHONE = "^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_\\.]+@[a-zA-Z0-9-]+[\\.a-zA-Z]+$";

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

    public static boolean isPhoneString(String str) {
        return str.matches(REGEX_PHONE);
    }

    public static boolean isEmailString(String str) {
        return str.matches(REGEX_EMAIL);
    }

    public static String md5(String input) {
        if (!TextUtils.isEmpty(input)) {
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(input.getBytes());
                byte digestResult[] = messageDigest.digest();

                StringBuffer sb = new StringBuffer();
                for (byte digit : digestResult) {
                    sb.append(Character.forDigit((digit & 240) >> 4, 16));
                    sb.append(Character.forDigit(digit & 15, 16));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File generateTempFile(String prefix, String suffix) {
        String filename = getRandomFilename(prefix, suffix);
        File tmpFile = new File(AppHelper.getCacheDirPath(), filename);
        if (tmpFile.exists()) {
            tmpFile.delete();
            tmpFile = new File(AppHelper.getCacheDirPath(), getRandomFilename(prefix, suffix));
        }
        return tmpFile;
    }

    private static String getRandomFilename(String prefix, String suffix) {
        return prefix + new Random(Utils.getTimestamp()).nextInt(1000) + suffix;
    }
}

package com.iyuelbs.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iyuelbs.app.AppConfig;
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
    private static final String KEY_RANDOM_FILES = "random_files";

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

    /**
     * generate new temp file
     */
    public static File generateTempFile(String prefix, String suffix) {
        String filename = getRandomFilename(prefix, suffix);
        String fileStr = AppConfig.getString(KEY_RANDOM_FILES, "");

        if (!TextUtils.isEmpty(fileStr)) {
            String[] temps = fileStr.split("\n");
            boolean needClear = false;
            for (String temp : temps) {
                if (temp.equals(filename)) {
                    needClear = true;
                    break;
                }
            }
            if (needClear) {
                clearTempFileArray(temps);
            }
        }

        return new File(AppHelper.getCacheDirPath(), filename);
    }

    public static File getLatestTempFile() {
        String fileStr = AppConfig.getString(KEY_RANDOM_FILES, "");
        if (TextUtils.isEmpty(fileStr))
            return null;

        String filename = fileStr.substring(fileStr.lastIndexOf("\n") + 1, fileStr.length());
        File file = new File(AppHelper.getCacheDirPath(), filename);
        return file.exists() ? file : null;
    }

    private static String getRandomFilename(String prefix, String suffix) {
        String fileName = prefix + new Random(Utils.getTimestamp()).nextInt(1000) + suffix;
        AppConfig.putString(KEY_RANDOM_FILES, AppConfig.getString(KEY_RANDOM_FILES, "") + "\n" + fileName);
        return fileName;
    }

    public static void clearTempFiles() {
        String[] files = AppConfig.getString(KEY_RANDOM_FILES, "").split("\n");
        clearTempFileArray(files);
    }

    public static void clearTempFileArray(String[] files) {
        for (String str : files) {
            File file = new File(AppHelper.getCacheDirPath(), str);
            if (file.exists()) {
                file.delete();
            }
        }
        AppConfig.putString(KEY_RANDOM_FILES, "");
    }

    public static boolean onUpKeySelected(int id) {
        return id == android.R.id.home;
    }
}

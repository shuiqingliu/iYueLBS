package com.iyuelbs.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Bob Peng on 2015/1/30.
 */
public class Utils {
    public static long getTimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() / 1000l;
    }
}

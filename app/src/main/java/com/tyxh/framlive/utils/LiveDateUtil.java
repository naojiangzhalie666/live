package com.tyxh.framlive.utils;

public class LiveDateUtil {

    /**
     * 时间转换  秒转分   分：秒
     */
    public static String GetMinutes(int s) {

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;

        minute = s / 60;
        second = s % 60;
        timeStr = unitFormat(minute) + ":" + unitFormat(second);
        return timeStr;
    }

    /**
     * 时间转换  秒转分  时：分：秒
     */
    public static String GetHourMinutes(int s) {

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;

        minute = s / 60;
        if (minute < 60) {
            second = s % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        } else {
            hour = minute / 60;
            if (hour > 99)
                return "";
            minute = minute % 60;
            second = s - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }

        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }
}

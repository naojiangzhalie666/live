package com.tyxh.framlive.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * 对比两个时间
     *
     * @param startTime
     * @param endTime
     * @param format     对比的时间格式 如yyyy-MM-dd HH:mm:ss   yyyy年MM月
     * @return 1开始>结束  2开始=结束  3开始<结束
     */
    public static int getTimeCompareSize(String startTime, String endTime,String format) {
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //正常情况下的逻辑操作.
                i = 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + (second>0?second + "秒":"");
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour +  "时" + (min>0?min + "分":"") + (second>0?second + "秒":"");
                if (hour % 24 == 0) {
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day +  "天";
                } else if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + (hour>0?hour + "时":"") +(min>0?min + "分":"")+ (second>0?second + "秒":"");
                }
            }
        }
        return timeStr;
    }
}

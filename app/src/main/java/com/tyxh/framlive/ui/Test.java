package com.tyxh.framlive.ui;

import com.tyxh.framlive.utils.Common.util.DateUtils;
import com.tyxh.framlive.utils.LiveDateUtil;

import java.util.Date;

public class Test {
    private static int pushId = 9;
    private static long second = 60;
    private static String now_time = "";
    public static void main(String[] args) {
//        System.out.println(String.valueOf(pushId));
//        System.out.println(pushId+"");
//        System.out.println(""+ formatSeconds(66));
//        System.out.println(Integer.valueOf(now_time));
//        System.out.println(new Random(1).nextInt(51));
//        String str = "你好";
//        try {
//            String result = URLEncoder.encode(str, "utf-8");
//            System.out.println(result);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Date st_da= DateUtils.parseDate("2021-07-16 11:05:12");
        Date ed_da =new Date();
        long second = ed_da.getTime()-st_da.getTime();
        String time = LiveDateUtil.formatSeconds(second/1000);
        System.out.println("秒钟："+second/1000+"  时间"+time);
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

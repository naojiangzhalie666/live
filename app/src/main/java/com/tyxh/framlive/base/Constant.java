package com.tyxh.framlive.base;

import com.tyxh.xzb.Constantc;

public class Constant {

    /*app信息*/
    public static final boolean PRINT_LOG = true;
    public static final boolean IS_JIAMI = false;
    public static final String BASE_URL = "http://172.16.66.225:9091/";
    public static String TOKEN = null;
    public static boolean has_navi = false;

    /*临时使用*/
    public static final boolean IS_ZIXUNSHI = false;//主播是否为咨询师
    public static boolean IS_SHENHEING = false;//入驻是否在审核中

    /*腾讯云直播*/
    public static final String LICENCEURL = "http://license.vod2.myqcloud.com/license/v1/17a46e881d1eafbddf54d6a617619267/TXLiveSDK.licence"; // 获取到的 licence url
    public static final String LICENCEKEY = "9fdb190cae9f7bb82780efdb05dd16c5"; // 获取到的 licence key

    /*im聊天*/
    public static final String SECRETKEY = Constantc.SECRETKEY;//聊天key
    public static final int SDKAPPID = Constantc.SDKAPPID;//聊天APPID

    /*微信*/
    public static final String APP_ID = "wxf577d166ba856289";//微信Appid
    public static final String APP_SECRECT = "2ed4c15ba396f03ed7f79cc5388d3d13";//微信Appsec

    /*身份状态分类*/
    public static final int POWER_NORMAL = 1;//普通
    public static final int POWER_ZIXUNSHI = 2;//咨询师
    public static final int POWER_ZIXUNJIGOU = 3;//咨询机构
    public static final int POWER_ZIZIXNSHI = 4;//子咨询师

    /*常量存储*/
    public static final int LIVE_UPDATE_CODE = 1001;//首次设置后获取完用户信息--关闭登录页

    /*存储*/
    public static final String CHAT_INFO = "chatInfo";
    public static final String USERINFO = "userInfo";
    public static final String ACCOUNT = "account";
    public static final String PWD = "password";
    public static final String ROOM = "room";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LOGOUT = "logout";
    public static final String ICON_URL = "icon_url";

    /*直播*/
    public static final String LIVE_JGNAME ="jigou_name";
    public static final String LIVE_ISJG=  "jigou_yes";
    public static final String LIVE_ISGZ = "user_guanzhu";
    public static final String LVIE_ISLIANX = "live_islianxian";
    public static final String LIVE_LIANXHEAD ="live_lxhead";
    public static final String LIVE_LXUSERID = "live_lxuserid";

}

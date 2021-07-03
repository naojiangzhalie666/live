package com.tyxh.framlive.base;

import com.tyxh.xzb.Constantc;

public class Constant {

    /*app信息 */
    /*
     * 正式地址  ：https://appshop.bkxinli.com/portal/
     * 测试地址  ：http://172.16.66.225:9091/
     * */
    public static final String BASE_URL = "https://appshop.bkxinli.com/portal/";
    public static final String BASE_WEB = "https://appshop.bkxinli.com/bkapp/";//各种web页拼接地址
    public static final boolean IS_DEBUG = true;
    /**
     * Socket地址：
     * 正式地址   ：https://appshop.bkxinli.com/portal/socket/
     * 测试地址   ：http://172.16.66.225:9091/socket/
     * 云龙地址   ：http://172.16.3.235:8081/socket/
     */
    public static final String SOCKET_URL = "wss://appshop.bkxinli.com/portal/socket/";
    /*分享公共*/
    public static final String SHARE_URL = "https://a.app.qq.com/o/simple.jsp?pkgname=com.tyxh.framlive";
    public static final String SHARE_NAME = "你专属的心理咨询师";
    public static final String SHARE_MS  ="随时随地与心理咨询师面对面";

    public static final boolean PRINT_LOG = IS_DEBUG;
    public static final boolean IS_JIAMI = false;
    public static String TOKEN = null;
    public static boolean has_navi = false;

    /*app使用*/
    public static boolean IS_SHENHEING = false;//入驻是否在审核中
    public static  String USER_STATE = "1"; //用户状态  state状态【1：在线；2：直播中；3：连线中；4：离线】

    public static final String BUGLY_APPID = "b1b287bf47";


    /*腾讯云一键登录appid*/
    public static final String TENCENT_PHONE="1400527451";

    /*腾讯云直播*/
    public static final String LICENCEURL = "http://license.vod2.myqcloud.com/license/v1/b0bad46a4f678c3fb63e229c6633da58/TXLiveSDK.licence"; // 获取到的 licence url
    public static final String LICENCEKEY = "abee753ed54f7e7e2e54391d8e1b9d20"; // 获取到的 licence key

    /*im聊天及直播通用*/
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

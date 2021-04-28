package com.example.myapplication.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.myapplication.chat.helper.ConfigHelper;
import com.example.xzb.important.MLVBLiveRoomImpl;
import com.example.xzb.utils.login.TCUserMgr;
import com.ljy.devring.DevRing;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.rtmp.TXLiveBase;
import com.yf.xzbgift.important.TUIKitLive;

import androidx.multidex.MultiDex;

public class LiveApplication extends Application {
    public static volatile LiveApplication mInstance;
    public static IWXAPI api;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
        initTent();
        initWachat();

    }

    public static LiveApplication getmInstance() {
        return mInstance;
    }

    private void init() {
        DevRing.init(this);
        DevRing.configureHttp().setBaseUrl(Constant.BASE_URL).setIsUseCookie(true).setConnectTimeout(60).setIsUseLog(Constant.PRINT_LOG);
        DevRing.configureOther().setIsUseCrashDiary(true);
        DevRing.configureImage();
        DevRing.create();

    }

    private void initTent(){
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());
        TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());
        TXLiveBase.getInstance().setLicence(this, Constant.LICENCEURL, Constant.LICENCEKEY);
        TXLiveBase.setConsoleEnabled(true);
        // 必须：初始化 MLVB 组件
        MLVBLiveRoomImpl.sharedInstance(this);
        // 必须：初始化全局的 用户信息管理类，记录个人信息。
        TCUserMgr.getInstance().initContext(getApplicationContext());
        // 礼物初始化
        TUIKitLive.init(this);
    }
    //初始化  注册微信
    private void initWachat(){
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                api.registerApp(Constant.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

}

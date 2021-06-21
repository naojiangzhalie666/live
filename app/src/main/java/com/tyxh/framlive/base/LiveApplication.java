package com.tyxh.framlive.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.heytap.msp.push.HeytapPushManager;
import com.huawei.hms.push.HmsMessaging;
import com.ljy.devring.DevRing;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.rich.oauth.core.RichAuth;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.rtmp.TXLiveBase;
import com.tyxh.framlive.chat.helper.ConfigHelper;
import com.tyxh.framlive.thirdpush.BrandUtil;
import com.tyxh.framlive.thirdpush.HUAWEIHmsMessageService;
import com.tyxh.framlive.thirdpush.MessageNotification;
import com.tyxh.framlive.thirdpush.PrivateConstants;
import com.tyxh.framlive.ui.SplashActivity;
import com.tyxh.framlive.utils.LiveLog;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.xzbgift.important.TUIKitLive;
import com.tyxh.xzb.important.MLVBLiveRoomImpl;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import androidx.multidex.MultiDex;

public class LiveApplication extends Application {
    private static final String TAG = "LiveApplication";
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
        /*同意协议后才可初始化*/
        boolean agree = LiveShareUtil.getInstance(this).getAgree();
        if(agree) {
            toInitAll();
        }
    }

    public void toInitAll(){
        init();
        initTent();
        initWachat();
        initUmeng();
        initTuisong();
        initTencentPhone();
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

    private void initTent() {
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
    private void initWachat() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                api.registerApp(Constant.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    private void initUmeng() {
        UMConfigure.init(this, "608a3f9253b6726499e67eb2", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        // 微信设置
        PlatformConfig.setWeixin(Constant.APP_ID, Constant.APP_SECRECT);
        PlatformConfig.setWXFileProvider("com.tyxh.framlive.fileprovider");
        // 新浪微博设置--目前没用到
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setSinaFileProvider("com.tyxh.framlive.fileprovider");
      /*  // QQ设置
        PlatformConfig.setQQZone("101830139","5d63ae8858f1caab67715ccd6c18d7a5");
        PlatformConfig.setQQFileProvider("com.tencent.sample2.fileprovider");
        // 企业微信设置
        PlatformConfig.setWXWork("wwac6ffb259ff6f66a","EU1LRsWC5uWn6KUuYOiWUpkoH45eOA0yH-ngL8579zs","1000002","wwauthac6ffb259ff6f66a000002");
        PlatformConfig.setWXWorkFileProvider("com.tencent.sample2.fileprovider");*/


    }
    private void initTencentPhone(){
        RichAuth.getInstance().init(this, Constant.TENCENT_PHONE);
    }

    /*各大平台推送初始化*/
    private void initTuisong() {
        HeytapPushManager.init(this, true);
        if (BrandUtil.isBrandXiaoMi()) {
            // 小米离线推送
            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
        } else if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送，设置是否接收Push通知栏消息调用示例
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
                @Override
                public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
                    if (task.isSuccessful()) {
                        LiveLog.i(TAG, "huawei turnOnPush Complete");
                    } else {
                        LiveLog.e(TAG, "huawei turnOnPush failed: ret=" + task.getException().getMessage());
                    }
                }
            });
        } else if (MzSystemUtils.isBrandMeizu(this)) {
            // 魅族离线推送
            PushManager.register(this, PrivateConstants.MZ_PUSH_APPID, PrivateConstants.MZ_PUSH_APPKEY);
        } else if (BrandUtil.isBrandVivo()) {
            // vivo离线推送
            PushClient.getInstance(getApplicationContext()).initialize();
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo离线推送，因为需要登录成功后向我们后台设置token，所以注册放在MainActivity中做
        }
//        else if (BrandUtil.isGoogleServiceSupport()) {
//            FirebaseInstanceId.getInstance().getInstanceId()
//                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<InstanceIdResult>() {
//                        @Override
//                        public void onComplete(Task<InstanceIdResult> task) {
//                            if (!task.isSuccessful()) {
//                                LiveLog.w(TAG, "getInstanceId failed exception = " + task.getException());
//                                return;
//                            }
//
//                            // Get new Instance ID token
//                            String token = task.getResult().getToken();
//                            LiveLog.i(TAG, "google fcm getToken = " + token);
//
//                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
//                        }
//                    });
//        };

        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());

    }

    class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;
        private IMEventListener mIMEventListener = new IMEventListener() {
            @Override
            public void onNewMessage(V2TIMMessage msg) {
                MessageNotification notification = MessageNotification.getInstance();
                notification.notify(msg);
            }
        };

        private ConversationManagerKit.MessageUnreadWatcher mUnreadWatcher = new ConversationManagerKit.MessageUnreadWatcher() {
            @Override
            public void updateUnread(int count) {
                //TODO 华为离线推送角标  接入华为后开放
                HUAWEIHmsMessageService.updateBadge(LiveApplication.this, count);
            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            LiveLog.i(TAG, "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // 若bundle不为空则程序异常结束
                // 重启整个程序
                Intent intent = new Intent(activity, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
                LiveLog.i(TAG, "application enter foreground");
                V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LiveLog.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LiveLog.i(TAG, "doForeground success");
                    }
                });
                TUIKit.removeIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().removeUnreadWatcher(mUnreadWatcher);
                MessageNotification.getInstance().cancelTimeout();
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // 应用切到后台
                LiveLog.i(TAG, "application enter background");
                int unReadCount = ConversationManagerKit.getInstance().getUnreadTotal();
                V2TIMManager.getOfflinePushManager().doBackground(unReadCount, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LiveLog.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LiveLog.i(TAG, "doBackground success");
                    }
                });
                // 应用退到后台，消息转化为系统通知
                TUIKit.addIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().addUnreadWatcher(mUnreadWatcher);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}

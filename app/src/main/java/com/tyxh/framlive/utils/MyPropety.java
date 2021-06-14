package com.tyxh.framlive.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class MyPropety {
    private static final String TAG = "MyPropety";
    private static String token = "";
    private static String id = "";
    private static UserInfoBean user_Info;

    private volatile static MyPropety instance;

    private MyPropety() {
    }

    public static MyPropety getInstance() {
        if (instance == null) {
            synchronized (MyPropety.class) {
                if (instance == null) {
                    token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
                    user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
                    id = user_Info.getRetData().getId();
                    instance = new MyPropety();
                }
            }
        }
        return instance;
    }


    /*我的资产*/
    public void getMineAsset(OnAssetBackListener listener) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, id), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    if (listener != null) {
                        listener.onAssetSuccessListener(assetBean);
                    }
                } else {
                    if (listener != null) {
                        listener.onAssetFailLIstener();
                    }
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (listener != null) {
                    listener.onAssetFailLIstener();
                }
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    public interface OnAssetBackListener {
        void onAssetSuccessListener(AssetBean assetBean);

        void onAssetFailLIstener();
    }

    /*获取用户信息*/
    public  void getUserInfo(OnUserInfoBackListener listener) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(token), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    if(listener!=null)
                        listener.onUserInfoSuccessListener(userInfoBean);
                }else{
                    if(listener!=null)
                        listener.onUserInfoFailLIstener();
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if(listener!=null)
                    listener.onUserInfoFailLIstener();
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    public interface OnUserInfoBackListener {
        void onUserInfoSuccessListener(UserInfoBean userInfoBean);
        void onUserInfoFailLIstener();
    }

}

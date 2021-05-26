package com.tyxh.framlive.utils.httputil;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.EventMessage;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.NetworkUtil;
import com.superc.yyfflibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;


public class LiveHttp {
    private static final String TAG = "LiveHttp";
    private static volatile LiveHttp instance;


    private LiveHttp() {

    }

    public static LiveHttp getInstance() {
        if (instance == null) {
            synchronized (LiveHttp.class) {
                if (instance == null) {
                    instance = new LiveHttp();
                }
            }
        }
        return instance;
    }

    public ApiService getApiService() {
        return DevRing.httpManager().getService(ApiService.class);
    }

    /*public void toGetDataSig(Observable observable, HttpBackListener backListener) {
        DevRing.httpManager().commonRequest(observable, new CommonObserver<Object>() {
            @Override
            public void onResult(Object result) {
                if (!(result instanceof String)) {
                    try {
                        result = JSONObject.toJSONString(result);
                    } catch (Exception e) {
                        Log.d(TAG, "onResult:加密数据" + e.toString());
                    }
                }
                if (backListener != null) {
                    backListener.onSuccessListener(result);
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (backListener != null) {
                    String error_msg = throwable.toString();
                    backListener.onErrorLIstener(error_msg);
                    if (error_msg.contains("403")) {
                        EventMessage eventMessage = new EventMessage(403);
                        EventBus.getDefault().post(eventMessage);
                    }
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }*/

    public void toGetData(Observable observable, HttpBackListener backListener) {
        if(!NetworkUtil.isNetWorkAvailable(LiveApplication.getmInstance())){
            ToastUtil.showToast(LiveApplication.getmInstance(),"网络不可用,请检查网络");
            return;
        }

        DevRing.httpManager().commonRequest(observable, new CommonObserver<JSONObject>() {
            @Override
            public void onResult(JSONObject result) {
                if (backListener != null) {
                    backListener.onSuccessListener(result);
                }
                Log.d(TAG, result.toJSONString());
            }

            @Override
            public void onError(HttpThrowable throwable) {
                 if (backListener != null) {
                    backListener.onErrorLIstener(throwable.toString());
                }
                 if(throwable.errorType==HTTP_ERROR){//重新登录
                     EventBus.getDefault().post(new EventMessage(1005));
                 }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }


}

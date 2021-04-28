package com.example.myapplication.utils.httputil.old;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.base.Constant;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.trello.rxlifecycle3.LifecycleTransformer;

import io.reactivex.Observable;

/********************************************************************
 @version: 1.0.0
 @description: 使用加密解密访问接口的工具类
 @author: EDZ
 @time: 2019/10/10 15:48
 @变更历史:
 ********************************************************************/
public class EncryPtionHttp {
    private static final String TAG = "EncryPtionHttp";
    private static EncryPtionHttp mInstance;
    private Context mContext;

    private EncryPtionHttp(Context context) {
        mContext = context;
    }

    public static EncryPtionHttp getInstance(Context context) {
        if (mInstance == null) {
            synchronized (EncryPtionHttp.class) {
                mInstance = new EncryPtionHttp(context);
            }
        }
        return mInstance;
    }

    /**
     * 访问接口获取数据
     *
     * @param jsonObjectObservable
     * @param OnHttpResult
     */
    public void getHttpResult(Observable<JSONObject> jsonObjectObservable, final OnHttpResult OnHttpResult) {
            DevRing.httpManager().commonRequest(jsonObjectObservable, new CommonObserver<JSONObject>() {
                @Override
                public void onResult(JSONObject result) {
//                    Log.e(TAG, "OkHttp: "+result.toString());
                    if(Constant.IS_JIAMI){
//                        boolean code = result.getBoolean("code");
//                        if (code) {
                            OnHttpResult.onSuccessResult(EncryPtionUtil.getInstance(mContext).toJieMi(result));
//                        }else{
//                            String message = result.getString("message");
//                            OnHttpResult.onErrorResult(new HttpThrowable(HttpThrowable.UNKNOWN,TextUtils.isEmpty(message)?"接口失败":message,new Throwable()));
//                            if (!TextUtils.isEmpty(message)){
//                                ToastUtil.showToast(mContext,message);
//                            }
//                        }
                    }else{
                        OnHttpResult.onSuccessResult(result);
                    }
                }

                @Override
                public void onError(HttpThrowable httpThrowable) {
                    Log.e(TAG, "onError: "+httpThrowable.toString() );
                    OnHttpResult.onErrorResult(httpThrowable);
                }
            }, (LifecycleTransformer) null);

    }

    public static abstract class OnHttpResult {
        public  void onSuccessResult(JSONObject jsonObject){};

        public  void onErrorResult(HttpThrowable httpThrowable){};
    }

}

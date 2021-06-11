package com.tyxh.framlive.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicVideoObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;
    private boolean is_end =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
        finish();
    }
    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        Log.e(TAG, "goToShowMsg: "+new Gson().toJson(wxMsg));
        if (wxMsg.mediaObject instanceof WXMusicVideoObject) {
            WXMusicVideoObject musicVideoObject = (WXMusicVideoObject) wxMsg.mediaObject;
            String identification = musicVideoObject.identification;	// 分享到微信时的音乐标识符字段
            String messageExt = wxMsg.messageExt;	//分享到微信时传的额外信息字段
            // 应用根据identification与messageExt自行处理

        }
    }
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            if(!is_end) {
                is_end = true;
                SendAuth.Resp authResp = (SendAuth.Resp) resp;
                final String code = authResp.code;
                LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getWxtoken(Constant.APP_ID, Constant.APP_SECRECT, "authorization_code", code), new HttpBackListener() {
                    @Override
                    public void onSuccessListener(Object result) {
                        super.onSuccessListener(result);
                        JSONObject jsonObject = (JSONObject) result;
                        try {
                            getWxUserInfo(jsonObject.getString("openid"), jsonObject.getString("access_token"));
                        } catch (JSONException e) {
                            Log.e(TAG, "onSuccessListener:weChat in access_token perform failed");
                        }
                    }

                    @Override
                    public void onErrorLIstener(String error) {
                        super.onErrorLIstener(error);
                    }
                });
            }
        }
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        Log.i(TAG, "onResp: "+ "openid = " + resp.openId+" result = "+result);
        finish();
    }

    /**
     * 获取用户信息
     * @param openid
     * @param accToken
     */
    private void getWxUserInfo(String openid, String accToken) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getWxUserInfo(accToken, openid, "zh_CN"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                EventBus.getDefault().post(new EventMessage("wx_login",openid,accToken));
//                Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
//                intent.putExtra("wechat", true);
//                intent.putExtra("openId", openid);
//                intent.putExtra("accessToken", accToken);
//                startActivity(intent);
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

}

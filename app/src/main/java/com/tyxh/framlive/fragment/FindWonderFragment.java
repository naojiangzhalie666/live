package com.tyxh.framlive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.ActBackBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.OrderSnBean;
import com.tyxh.framlive.pop_dig.ChoseDialog;
import com.tyxh.framlive.pop_dig.LoadDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.zfb.PayResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 一元活动购买界面
 */
public class FindWonderFragment extends Fragment {
    @BindView(R.id.wonder_img)
    ImageView mWonderImg;
    private Unbinder unbinder;
    private String mToken;
    private static final int SDK_PAY_FLAG = 1;
    private LoadDialog mLoadDialog;
    private ChoseDialog mChoseDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_wonder, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToken = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        mLoadDialog = new LoadDialog(getActivity());
        mChoseDialog = new ChoseDialog(getActivity(),getActivity());
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mChoseDialog.setOnBuyClickListener(new ChoseDialog.OnBuyClickListener() {
            @Override
            public void onWechatListener() {
                mChoseDialog.dismiss();
                getOrderSn("1");
            }

            @Override
            public void onZfbListener() {
                mChoseDialog.dismiss();
                getOrderSn("2");
            }
        });
    }

    @OnClick(R.id.wonder_img)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wonder_img:
                getJoinAct();
                break;

        }
    }

    private void getJoinAct(){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().queryJoAct(mToken, "2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ActBackBean backBean =new Gson().fromJson(result.toString(),ActBackBean.class);
                if(backBean.getRetCode() ==0 ){
                    //一元活动购买情况【0:活动未开始; 1:可购; 2:已购买; 3:活动已过期】
                    String unaryAct = backBean.getRetData().getUnaryAct();
                    switch (unaryAct){
                        case "0":ToastUtil.showToast(getActivity(),"活动未开始，请耐心等待");break;
                        case "1":    mChoseDialog.show();break;
                        case "2": ToastUtil.showToast(getActivity(),"您已购买过，无法重复购买");break;
                        case "3": ToastUtil.showToast(getActivity(),"活动已过期，无法购买");break;
                    }
                }else{
                    ToastUtil.showToast(getActivity(),backBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }
    /*----购买--start---*/
    /*获取订单号*/
    private void getOrderSn(String type) {
        mLoadDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("diamondId", 16);
        map.put("actId", "1");
        map.put("remark", "一元活动购买");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getAddOrderdetail(mToken, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mLoadDialog.dismiss();
                OrderSnBean bean = new Gson().fromJson(result.toString(), OrderSnBean.class);
                if (bean.getRetCode() == 0) {
                    goPay(type, bean.getRetData().getOrderSn());
                } else {
                    Toast.makeText(getActivity(), bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mLoadDialog.dismiss();

            }
        });
    }

    /*支付--type  1微信 2支付宝*/
    private void goPay(String type, String orderSn) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().goPay(mToken, orderSn, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                JSONObject jsonObject = (JSONObject) result;
                Integer retCode = jsonObject.getInteger("retCode");
                if (retCode == 0) {
                    if (type.equals("1")) {
                        payByWechat(jsonObject);
                    } else {
                        if (jsonObject.getJSONObject("retData") != null)
                            payByZfb(jsonObject.getJSONObject("retData").getJSONObject("payData").getString("payUrl"));
                    }
                } else {
                    String retMsg = jsonObject.getString("retMsg");
                    Toast.makeText(getActivity(), retMsg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*微信支付*/
    private void payByWechat(JSONObject jsonObject) {
        JSONObject retData = jsonObject.getJSONObject("retData");
        try {
            JSONObject payData = retData.getJSONObject("payData");
            PayReq req = new PayReq();
            req.appId = payData.getString("appid");
            req.partnerId = payData.getString("partnerid");
            req.prepayId = payData.getString("prepayid");
            req.nonceStr = payData.getString("noncestr");
            req.timeStamp = payData.getString("timestamp");
            req.packageValue = payData.getString("package");
            req.sign = payData.getString("sign");
            req.extData = payData.getString("android_wxpay"); // optional
            LiveApplication.api.sendReq(req);
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
//            Toast.makeText(mContext, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*支付宝支付*/
    private void payByZfb(String info) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(info, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /*支付宝返回--支付结果*/
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {//成功
                        toHandlePay(0);
                    } else if (TextUtils.equals(resultStatus, "6001")) {//取消
                        toHandlePay(2);
                    } else {//失败
                        toHandlePay(1);
                    }
                    break;
                }
            }
        }

        ;
    };

    /*微信返回--支付结果*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getMessage().equals("pay_finish")) {
            switch (message.getCode()) {
                case 0:
                    toHandlePay(0);
                    break;
                case -1:
                    toHandlePay(1);
                    break;
                case -2:
                    toHandlePay(2);
                    break;
            }
        } else if (message.getCode() == 1005) {
            ToastUtil.showToast(getActivity(), "登录过期，请重新登录!");
            getActivity().finish();
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    /**
     * 支付结果统一处理
     *
     * @param type 0：成功  1：失败  2：取消
     */
    private void toHandlePay(int type) {
        switch (type) {
            case 0:
                ToastUtil.showToast(getActivity(), "支付成功");
                break;
            case 1:
                ToastUtil.showToast(getActivity(), "支付失败");
                break;
            case 2:
                ToastUtil.showToast(getActivity(), "取消支付");
                break;
        }
    }


    /*----购买--end---*/



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        DevRing.httpManager().stopRequestByTag(LiveHttp.TAG);
    }
}

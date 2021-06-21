package com.tyxh.framlive.utils;

import android.app.Activity;

public class PayUtils {
    private static volatile PayUtils instance;
    private static Activity mContext;
    private static String token;
    private static final int SDK_PAY_FLAG = 1;
    private PayUtils(){}

    public static PayUtils getInstance( Activity context,String tk) {
        if(instance ==null){
            synchronized (PayUtils.class){
                if(instance ==null)
                instance =new PayUtils();
            }
        }
        mContext= context;
        token =tk;
        return instance;
    }



  /*  *//*获取订单号*//*
    private void getOrderSn(String type,String select_id) {
        if (TextUtils.isEmpty(select_id)) {
            Toast.makeText(mContext, "请先进行选择", Toast.LENGTH_SHORT).show();
            return;
        }
        mLoadDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("diamondId", select_id);
        map.put("remark", "钻石包购买");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getAddOrderdetail(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mLoadDialog.dismiss();
                OrderSnBean bean = new Gson().fromJson(result.toString(), OrderSnBean.class);
                if (bean.getRetCode() == 0) {
                    goPay(type, bean.getRetData().getOrderSn());
                } else {
                    Toast.makeText(mContext, bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mLoadDialog.dismiss();

            }
        });
    }

    *//*支付--type  1微信 2支付宝*//*
    private void goPay(String type, String orderSn) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().goPay(token, orderSn, type), new HttpBackListener() {
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
                    Toast.makeText(mContext, retMsg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    *//*微信支付*//*
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

    *//*支付宝支付*//*
    private void payByZfb(String info) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
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

    *//*支付宝返回--支付结果*//*
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

    *//*微信返回--支付结果*//*
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
            ToastUtil.showToast(mContext, "登录过期，请重新登录!");
            finish();
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    *//**
     * 支付结果统一处理
     *
     * @param type 0：成功  1：失败  2：取消
     *//*
    private void toHandlePay(int type) {
        switch (type) {
            case 0:
                ToastUtil.showToast(mContext, "支付成功");
                EventBus.getDefault().post(new EventMessage(PAY_SUCCESS));
                getMineAsset();
                break;
            case 1:
                ToastUtil.showToast(mContext, "支付失败");
                break;
            case 2:
                ToastUtil.showToast(mContext, "取消支付");
//                EventBus.getDefault().post(new EventMessage(PAY_SUCCESS));
                break;
        }
    }*/



}

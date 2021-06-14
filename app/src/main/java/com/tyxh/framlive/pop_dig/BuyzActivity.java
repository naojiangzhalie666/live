package com.tyxh.framlive.pop_dig;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.DensityUtil;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.BuyAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.DiamondBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.OrderSnBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.ui.WebVActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.zfb.PayResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;

public class BuyzActivity extends AppCompatActivity {
    private static final String TAG = "BuyzActivity";
    @BindView(R.id.dig_buy_lastzuan)
    TextView mDigBuyLastzuan;
    @BindView(R.id.dig_buy_recy)
    RecyclerView mDigBuyRecy;
    @BindView(R.id.dig_buy_wchat)
    TextView mTvPaywechat;
    @BindView(R.id.buz_ll)
    LinearLayout mll;
    @BindView(R.id.login_imgv)
    ImageView mimgv_xy;


    private List<DiamondBean.RetDataBean.ListBean> mMapList;
    private BuyAdapter mBuyAdapter;
    private String select = "";
    private String select_id = "";
    private static final int SDK_PAY_FLAG = 1;
    private String token;
    private UserInfoBean user_Info;
    private LoadDialog mLoadDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buyzuan);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        mLoadDialog = new LoadDialog(this);
        mMapList = new ArrayList<>();
        getMineAsset();
        getData();
    }

    /*获取钻石列表*/
    private void getData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getDiamaond(token, "1", "100", "2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                DiamondBean bean = new Gson().fromJson(result.toString(), DiamondBean.class);
                if (bean.getRetCode() == 0) {
                    setData(bean.getRetData().getList());
                } else {
                    Toast.makeText(BuyzActivity.this, bean.getRetMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(token, user_Info.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    mDigBuyLastzuan.setText(data.getDiamond());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    private void setData(List<DiamondBean.RetDataBean.ListBean> listBeans) {
        mMapList.clear();
        mMapList.addAll(listBeans);
        mBuyAdapter = new BuyAdapter(this, mMapList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mDigBuyRecy.setLayoutManager(gridLayoutManager);
        mDigBuyRecy.setAdapter(mBuyAdapter);

        mBuyAdapter.setOnItemClickListener(new BuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
             /*   for (int i = 0; i < mMapList.size(); i++) {
                    if(i==pos){
                        mMapList.get(i).put("select",true);
                    }else{
                        mMapList.get(i).put("select",false);
                    }
                }
                mBuyAdapter.notifyDataSetChanged();*/
                select = "充值" + pos;
                select_id = mMapList.get(pos).getId() + "";
            }

            @Override
            public void onLookMoreListener() {
                select = "";
                if (mMapList.size() >= 6) {
                    mll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(BuyzActivity.this, 300)));
                    mDigBuyRecy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(BuyzActivity.this, 300)));
                } else {
                    mll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    mDigBuyRecy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                mBuyAdapter.setLook_more(true);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        select = "";
//        mBuyAdapter.setLook_more(false);
    }

    @OnClick({R.id.dig_buy_xieyi, R.id.dig_buy_zfb, R.id.dig_buy_wchat, R.id.dig_buy_cancel, R.id.login_cbxieyi, R.id.login_imgv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_buy_xieyi:
                WebVActivity.startMe(this, "用户充值协议");
                break;
            case R.id.dig_buy_wchat:
                if (mimgv_xy.getVisibility() == View.VISIBLE) {
                    getOrderSn("1");
                } else {
                    ToastUtil.showToast(this, "请阅读并同意用户充值协议");
                }
                break;
            case R.id.dig_buy_zfb:
                if (mimgv_xy.getVisibility() == View.VISIBLE) {
                    getOrderSn("2");
                } else {
                    ToastUtil.showToast(this, "请阅读并同意用户充值协议");
                }
                break;
            case R.id.login_cbxieyi:
            case R.id.login_imgv:
                if (mimgv_xy.getVisibility() == View.VISIBLE) {
                    mimgv_xy.setVisibility(View.GONE);
                } else {
                    mimgv_xy.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.dig_buy_cancel:
                finish();
                break;
        }
    }

    /*获取订单号*/
    private void getOrderSn(String type) {
        if (TextUtils.isEmpty(select_id)) {
            Toast.makeText(this, "请先进行选择", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BuyzActivity.this, bean.getRetMsg(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BuyzActivity.this, retMsg, Toast.LENGTH_SHORT).show();
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
        mTvPaywechat.setEnabled(false);
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
            Toast.makeText(this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mTvPaywechat.setEnabled(true);
    }

    /*支付宝支付*/
    private void payByZfb(String info) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyzActivity.this);
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
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
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
                ToastUtil.showToast(this, "支付成功");
                EventBus.getDefault().post(new EventMessage(PAY_SUCCESS));
                getMineAsset();
                break;
            case 1:
                ToastUtil.showToast(this, "支付失败");
                break;
            case 2:
                ToastUtil.showToast(this, "取消支付");
//                EventBus.getDefault().post(new EventMessage(PAY_SUCCESS));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

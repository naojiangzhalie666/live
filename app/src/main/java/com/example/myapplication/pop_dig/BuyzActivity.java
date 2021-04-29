package com.example.myapplication.pop_dig;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BuyAdapter;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

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

public class BuyzActivity extends AppCompatActivity {
    @BindView(R.id.dig_buy_lastzuan)
    TextView mDigBuyLastzuan;
    @BindView(R.id.dig_buy_recy)
    RecyclerView mDigBuyRecy;
    @BindView(R.id.dig_buy_wchat)
    TextView mTvPaywechat;


    private List<Map<String, Object>> mMapList;
    private BuyAdapter mBuyAdapter;
    private String select = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buyzuan);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);

        mDigBuyLastzuan.setText("1000000");

        mMapList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mMapList.add(map);
        }

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
            }

            @Override
            public void onLookMoreListener() {
                select = "";
                mBuyAdapter.setLook_more(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        select = "";
        mBuyAdapter.setLook_more(false);
    }

    @OnClick({R.id.dig_buy_xieyi, R.id.dig_buy_zfb, R.id.dig_buy_wchat, R.id.dig_buy_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_buy_xieyi:
                ToastUtil.showToast(this, "弹出协议");
                break;
            case R.id.dig_buy_wchat:
                getWxMsg();
                break;
            case R.id.dig_buy_zfb:
                ToastUtil.showToast(this, "支付宝充值" + select);
                break;
            case R.id.dig_buy_cancel:
                finish();
                break;
        }
    }

    private void getWxMsg() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getWxPayinfo(), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                JSONObject jsonObject = (JSONObject) result;
                payByWechat(jsonObject);
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    private void payByWechat(JSONObject jsonObject) {
        mTvPaywechat.setEnabled(false);
        JSONObject retData = jsonObject.getJSONObject("retData");
        try {
            PayReq req = new PayReq();
            req.appId = retData.getString("appid");
            req.partnerId = retData.getString("partnerid");
            req.prepayId = retData.getString("prepayid");
            req.nonceStr = retData.getString("noncestr");
            req.timeStamp = retData.getString("timestamp");
            req.packageValue = retData.getString("package");
            req.sign = retData.getString("sign");
            req.extData = retData.getString("android_wxpay"); // optional
            LiveApplication.api.sendReq(req);
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mTvPaywechat.setEnabled(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getMessage().equals("pay_finish")) {
            switch (message.getCode()) {
                case 0:
                    ToastUtil.showToast(this,"支付成功");
                    break;
                case -1:
                    ToastUtil.showToast(this,"支付失败");
                    break;
                case -2:
                    ToastUtil.showToast(this,"取消支付");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
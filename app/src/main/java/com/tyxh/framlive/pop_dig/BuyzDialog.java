package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.BuyAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.DiamondBean;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyzDialog extends Dialog {
    @BindView(R.id.dig_buy_lastzuan)
    TextView mDigBuyLastzuan;
    @BindView(R.id.dig_buy_recy)
    RecyclerView mDigBuyRecy;
    @BindView(R.id.dig_buy_wchat)
    TextView mTvPaywechat;


    private Context mContext;
    private List<DiamondBean.RetDataBean.ListBean> mMapList;
    private BuyAdapter mBuyAdapter;
    private String select = "";

    public BuyzDialog(@NonNull Context context, List<DiamondBean.RetDataBean.ListBean> mpList) {
        super(context);
        mContext = context;
        mMapList = mpList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buyzuan);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        mDigBuyLastzuan.setText("1000000");

        mBuyAdapter = new BuyAdapter(mContext, mMapList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
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
    public void show() {
        super.show();
        select = "";
        mBuyAdapter.setLook_more(false);
    }

    @OnClick({R.id.dig_buy_xieyi, R.id.dig_buy_zfb, R.id.dig_buy_wchat, R.id.dig_buy_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_buy_xieyi:
                ToastUtil.showToast(mContext, "弹出协议");
                break;
            case R.id.dig_buy_wchat:
                payByWechat();
                break;
            case R.id.dig_buy_zfb:
                ToastUtil.showToast(mContext, "支付宝充值" + select);
                break;
            case R.id.dig_buy_cancel:
                dismiss();
                break;
        }
    }

    private void payByWechat() {
        mTvPaywechat.setEnabled(false);
        Toast.makeText(mContext, "后台获取订单信息...", Toast.LENGTH_SHORT).show();
        try {
            PayReq req = new PayReq();
            req.appId = Constant.APP_ID;
            req.partnerId = "1900000109";
            req.prepayId = "1101000000140415649af9fc314aa427";
            req.nonceStr = "1101000000140429eb40476f8896f4c9";
            req.timeStamp = "1398746574";
            req.packageValue = "Sign=WXPay";
            req.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
            req.extData = "live_wechat_pay"; // optional
            LiveApplication.api.sendReq(req);
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(mContext, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mTvPaywechat.setEnabled(true);


    }

}

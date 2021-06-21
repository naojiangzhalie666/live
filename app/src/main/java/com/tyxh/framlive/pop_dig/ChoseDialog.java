package com.tyxh.framlive.pop_dig;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.ui.WebVActivity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoseDialog extends Dialog {

    @BindView(R.id.buz_ll)
    LinearLayout mll;
    @BindView(R.id.login_imgv)
    ImageView mimgv_xy;
    private Context  mContext;
    private Activity mActivity;
    private OnBuyClickListener mOnBuyClickListener;


    public ChoseDialog(@NonNull Context context,Activity mact) {
        super(context);
        mContext =context;
        mActivity =mact;
    }

    public void setOnBuyClickListener(OnBuyClickListener onBuyClickListener) {
        mOnBuyClickListener = onBuyClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chose);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);


    }

    @OnClick({R.id.dig_buy_xieyi, R.id.dig_buy_zfb, R.id.dig_buy_wchat, R.id.dig_buy_cancel, R.id.login_cbxieyi, R.id.login_imgv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_buy_xieyi:
                WebVActivity.startMe(mActivity, "用户充值协议");
                break;
            case R.id.dig_buy_wchat:
                if (mimgv_xy.getVisibility() == View.VISIBLE) {
                    if(mOnBuyClickListener!=null)
                        mOnBuyClickListener.onWechatListener();
                } else {
                    ToastUtil.showToast(mContext, "请阅读并同意用户充值协议");
                }
                break;
            case R.id.dig_buy_zfb:
                if (mimgv_xy.getVisibility() == View.VISIBLE) {
                    if(mOnBuyClickListener!=null)
                        mOnBuyClickListener.onZfbListener();
                } else {
                    ToastUtil.showToast(mContext, "请阅读并同意用户充值协议");
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
               dismiss();
                break;
        }
    }

    public interface OnBuyClickListener{
        void onWechatListener();
        void onZfbListener();
    }

}

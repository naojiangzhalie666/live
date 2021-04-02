package com.example.myapplication.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ShareUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_phonenum)
    TextView mLoginPhonenum;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis:
                ShareUtil.getInstance(this).put("power",0);//普通
                thisLogin();
                break;
            case R.id.login_loginother:
                ToastShow("其它手机号登录");
                statActivity(BindPhoneActivity.class);
                finish();
                break;
            case R.id.ll_wx:
                ShareUtil.getInstance(this).put("power",1);//咨询师
                thisLogin();
                ToastShow("微信登录");
                break;
            case R.id.login_rela:
                if(mLoginImgv.getVisibility() == View.VISIBLE){
                    mLoginImgv.setVisibility(View.GONE);
                }else{
                    mLoginImgv.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private void thisLogin() {
        if (mLoginImgv.getVisibility() == View.VISIBLE) {
            statActivity(MainActivity.class);
            finish();
        } else {
            ToastShow("请阅读并勾选协议");
        }
    }

}
package com.tyxh.framlive.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.utils.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends LiveBaseActivity {

    @BindView(R.id.about_about)
    TextView mAboutAbout;
    @BindView(R.id.about_code)
    TextView mAboutCode;
    @BindView(R.id.about_jianjie)
    TextView mAboutJianjie;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        try {
            mAboutCode.setText("version" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
            mAboutCode.setText("version1.0.0");
            e.printStackTrace();
        }

    }

    @OnClick({R.id.about_jianjie, R.id.about_newbb, R.id.about_hping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_jianjie:
                if (mAboutAbout.getVisibility() == View.VISIBLE) {
                    mAboutAbout.setVisibility(View.GONE);
                } else {
                    mAboutAbout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.about_newbb:
            case R.id.about_hping:
                goShop();
                break;
        }
    }

    private void goShop() {
//存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            //可以接收
            startActivity(intent);
        } else {
            //没有应用市场，我们通过浏览器跳转到Google Play
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
            if (intent.resolveActivity(getPackageManager()) != null) {
                //有浏览器
                startActivity(intent);
            }
        }


    }
}

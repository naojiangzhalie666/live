package com.example.myapplication.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.xzb.Constantc;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindWchatActivity extends LiveBaseActivity {

    @BindView(R.id.bind_wchat_head)
    ImageView mBindWchatHead;
    @BindView(R.id.bind_wchat_name)
    TextView mBindWchatName;
    private UserInfoBean mUserInfo;
    private String user_ico ="";


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_wchat;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mBindWchatName.setText(Constantc.USER_NAME);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if(mUserInfo!=null){
            user_ico = mUserInfo.getRetData().getIco();
        }
        RoundedCorners roundedCorners = new RoundedCorners(16);
        Glide.with(this).load(user_ico).apply(new RequestOptions().transform(new CenterCrop(),roundedCorners)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mBindWchatHead);


    }

    @OnClick({R.id.imgv_back, R.id.bind_wchat_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.bind_wchat_bind:
                ToastShow("绑定微信");
                break;
        }
    }
}

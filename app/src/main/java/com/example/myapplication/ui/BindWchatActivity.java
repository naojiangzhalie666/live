package com.example.myapplication.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindWchatActivity extends BaseActivity {

    @BindView(R.id.bind_wchat_head)
    ImageView mBindWchatHead;
    @BindView(R.id.bind_wchat_name)
    TextView mBindWchatName;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_wchat;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mBindWchatName.setText("昵称？");
        RoundedCorners roundedCorners = new RoundedCorners(16);
        Glide.with(this).load(R.drawable.pj_bg).apply(new RequestOptions().transform(roundedCorners)).into(mBindWchatHead);


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

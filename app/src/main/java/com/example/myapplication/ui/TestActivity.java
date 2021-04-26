package com.example.myapplication.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.utils.TitleUtils;
import com.superc.yyfflibrary.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {


    @BindView(R.id.imageView29)
    ImageView mImageView29;
    @BindView(R.id.imageView30)
    ImageView mImageView30;
    @BindView(R.id.imageView31)
    ImageView mImageView31;
    @BindView(R.id.imageView28)
    ImageView mImageView28;
    @BindView(R.id.imageView27)
    ImageView mImageView27;
    private RoundedCorners mRoundedCorners;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mRoundedCorners = new RoundedCorners(15);
        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(new CenterCrop(),mRoundedCorners)).into(mImageView27);

        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).fitCenter()).into(mImageView28);

        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).centerInside()).into(mImageView29);

//        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).).into(mImageView30);

//        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners)).into(mImageView31);


    }
}

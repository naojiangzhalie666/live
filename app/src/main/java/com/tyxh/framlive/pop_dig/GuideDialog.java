package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.ljy.devring.util.DensityUtil;

import androidx.annotation.NonNull;

public class GuideDialog extends Dialog {

    private ImageView mImageView;
    private Context mContext;
    private int mType = 1;  //1客服  2微信指导

    public GuideDialog(@NonNull Context context,int type) {
        super(context);
        mContext = context;
        mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guide);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
//        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mImageView =findViewById(R.id.guide_imgv);
        if(mType==1){
            mImageView.setPadding(DensityUtil.dp2px(mContext,44),DensityUtil.dp2px(mContext,44),DensityUtil.dp2px(mContext,44),DensityUtil.dp2px(mContext,44));
            Glide.with(mContext).load(R.drawable.guide_kefu).into(mImageView);
        }else{
            Glide.with(mContext).load(R.drawable.guide_wechat).into(mImageView);
        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

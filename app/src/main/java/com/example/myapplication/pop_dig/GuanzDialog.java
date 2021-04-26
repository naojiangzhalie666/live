package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.xzb.ui.TCChatEntity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuanzDialog extends Dialog {
    @BindView(R.id.dig_gz_name)
    TextView mDigGzName;
    @BindView(R.id.dig_gz_sex)
    ImageView mDigGzSex;
    @BindView(R.id.dig_gz_id)
    TextView mDigGzId;
    @BindView(R.id.dig_gz_lxtime)
    TextView mDigGzLxtime;
    @BindView(R.id.dig_gz_old)
    TextView mDigGzOld;
    @BindView(R.id.dig_gz_zctime)
    TextView mDigGzZctime;
    @BindView(R.id.dig_gz_favour)
    TextView mDigGzFavour;
    @BindView(R.id.dig_gz_imgv)
    ImageView mDigGzImgv;
    private TCChatEntity mEntity;
    private OnDigClickListener mOnDigClickListener;
    private Context mContext;

    public GuanzDialog(@NonNull Context context, TCChatEntity tcChatEntity) {
        super(context);
        mContext = context;
        mEntity = tcChatEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guanzhu);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCanceledOnTouchOutside(true);
        mDigGzName.setText(mEntity.getSenderName());
        mDigGzSex.setImageResource(R.drawable.chat_dig_man);
        mDigGzId.setText("12345");
        mDigGzOld.setText("90后");
        mDigGzLxtime.setText("33小时");
        mDigGzZctime.setText("322天");
        mDigGzFavour.setText("恋爱技巧  情感挽回    恋爱技巧  ");
        Glide.with(mContext).load(R.drawable.bg).circleCrop().into(mDigGzImgv);


    }

    public void setOnDigClickListener(OnDigClickListener onDigClickListener) {
        mOnDigClickListener = onDigClickListener;
    }

    @OnClick({R.id.dig_gz_invite, R.id.dig_gz_jubao, R.id.dig_gz_jy, R.id.dig_gz_rmzl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_gz_invite:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onInviteClickListener();
                break;
            case R.id.dig_gz_jubao:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onJubaoClickListener();
                break;
            case R.id.dig_gz_jy:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onJinyanClickListener();
                break;
            case R.id.dig_gz_rmzl:
                if(mOnDigClickListener!=null)
                    mOnDigClickListener.onRenmingClickListener();
                break;
        }
    }

    public interface OnDigClickListener{
        void onInviteClickListener();
        void onJubaoClickListener();
        void onJinyanClickListener();
        void onRenmingClickListener();
    }
}

package com.example.myapplication.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeadDialog extends Dialog {

    @BindView(R.id.dig_talk_old)
    TextView mDigTalkOld;
    @BindView(R.id.dig_talk_head)
    ImageView mDigTalkHead;
    @BindView(R.id.dig_talk_name)
    TextView mDigTalkName;
    @BindView(R.id.dig_talk_sex)
    ImageView mDigTalkSex;
    @BindView(R.id.dig_talk_bezhu)
    TextView mDigTalkBezhu;
    @BindView(R.id.dig_talk_id)
    TextView mDigTalkId;
    @BindView(R.id.dig_talk_time)
    TextView mDigTalkTime;
    @BindView(R.id.dig_talk_day)
    TextView mDigTalkDay;
    @BindView(R.id.dig_talk_favr)
    TextView mDigTalkFavr;
    @BindView(R.id.con)
    ConstraintLayout mCon;
    private Context mContext;

    public HeadDialog(@NonNull Context context) {
        super(context);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_talk);
        ButterKnife.bind(this);
        getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Glide.with(mContext).load(R.drawable.mine_bg).apply(new RequestOptions().circleCrop()).into(mDigTalkHead);
        mDigTalkName.setText("李大蛋");
        mDigTalkSex.setImageResource(R.drawable.chat_dig_woman);
        mDigTalkBezhu.setText("爱谁谁");
        mDigTalkId.setText("124124");
        mDigTalkOld.setText("353后");
        mDigTalkTime.setText("5小时");
        mDigTalkDay.setText("19999天");
        mDigTalkFavr.setText("情感挽回/恋爱技巧/\n第三者问题/心理健康检测/\n未成年人心理");



    }

    @OnClick({R.id.dig_talk_close, R.id.dig_talk_old})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_talk_close:
                dismiss();
                break;
            case R.id.dig_talk_old:
                break;
        }
    }
}

package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.UserDetailBean;

import java.util.List;

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
    private  UserDetailBean.RetDataBean.UserBean mUserBean;
    private UserDetailBean.RetDataBean user_data ;

    public HeadDialog(@NonNull Context context,UserDetailBean.RetDataBean data) {
        super(context);
        mContext = context;
        user_data =data;
        mUserBean = data.getUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_talk);
        ButterKnife.bind(this);
        getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Glide.with(mContext).load(mUserBean.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(mDigTalkHead);
        mDigTalkName.setText(mUserBean.getNickname());
        mDigTalkSex.setImageResource(mUserBean.getGender()==1?R.drawable.chat_dig_man:R.drawable.chat_dig_woman);
        mDigTalkBezhu.setText("");//备注
        mDigTalkId.setText(String.valueOf(mUserBean.getId()));
        mDigTalkOld.setText(mUserBean.getAges());
        mDigTalkTime.setText(mUserBean.getConnectionIncomeValue());
        mDigTalkDay.setText(mUserBean.getRegDays());
        int type = mUserBean.getType();
        if(type==2){
            List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = user_data.getCounselorBeans();
            if (counselorBeans != null && counselorBeans.size() > 0) {
                UserDetailBean.RetDataBean.CounselorBeansBean bb = counselorBeans.get(0);
                Glide.with(mContext).load(bb.getCouHeadImg()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mDigTalkHead);
                mDigTalkName.setText(bb.getCouName());
            }
        }else if(type>2){
            UserDetailBean.RetDataBean.CouMechanismBean couMechanism = user_data.getCouMechanism();
            if(couMechanism!=null){
                Glide.with(mContext).load(couMechanism.getMeLogo()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mDigTalkHead);
                mDigTalkName.setText(couMechanism.getMeName());
            }
        }

        String interest = mUserBean.getInterests();
        if(interest.endsWith(",")){
            interest =interest.substring(0,interest.length()-1);

        }
        mDigTalkFavr.setText(interest.replaceAll(",","/"));



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

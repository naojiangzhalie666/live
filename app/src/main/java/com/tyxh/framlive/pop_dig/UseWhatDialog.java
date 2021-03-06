package com.tyxh.framlive.pop_dig;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.LiveCotctBean;
import com.tyxh.framlive.utils.LiveDateUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.Group;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UseWhatDialog extends Dialog {
    private static final String TAG = "UseWhatDialog";
    @BindView(R.id.dig_usewhat_diamond)
    TextView mDigUsewhatDiamond;
    @BindView(R.id.dig_usewhat_fuwubao)
    TextView mDigUsewhatFuwubao;
    @BindView(R.id.dig_usewhat_card)
    TextView mDigUsewhatCard;
    @BindView(R.id.dig_usewhat_sure)
    TextView mDigUsewhatSure;
    @BindView(R.id.dig_usewhat_imgvone)
    ImageView mDigUsewhatImgvone;
    @BindView(R.id.dig_usewhat_imgvtwo)
    ImageView mDigUsewhatImgvtwo;
    @BindView(R.id.dig_usewhat_imgvthree)
    ImageView mDigUsewhatImgvthree;
    @BindView(R.id.id_one)
    Group mDigUseGpOne;
    @BindView(R.id.id_two)
    Group mDigUseGpTwo;
    @BindView(R.id.id_three)
    Group mDigUseGpThree;
    private OnSureClickListener mOnSureClickListener;
    private Context mContext;
    private Activity mActivity;
    private List<LiveCotctBean.RetDataBean> mDataBeans;
    private int diamond_num = 0;
    private int select_what = -1;
    private int old_select = -1;
    private LiveCotctBean.RetDataBean bean_zuan = new LiveCotctBean.RetDataBean(),
            bean_card = new LiveCotctBean.RetDataBean(), bean_back = new LiveCotctBean.RetDataBean(), bean_select;

    public UseWhatDialog(@NonNull Context context, Activity activity, List<LiveCotctBean.RetDataBean> beans) {
        super(context);
        mContext = context;
        mActivity = activity;
        mDataBeans = beans;
    }

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        mOnSureClickListener = onSureClickListener;
    }

    public void reset() {
        this.old_select = -1;
        this.select_what = -1;
        mDigUsewhatImgvone.setImageResource(R.drawable.dig_use_unse);
        mDigUsewhatImgvtwo.setImageResource(R.drawable.dig_use_unse);
        mDigUsewhatImgvthree.setImageResource(R.drawable.dig_use_unse);
        mDigUsewhatDiamond.setTextColor(mContext.getResources().getColor(R.color.black));
        mDigUsewhatFuwubao.setTextColor(mContext.getResources().getColor(R.color.black));
        mDigUsewhatCard.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    public void setDataBeans(List<LiveCotctBean.RetDataBean> dataBeans) {
        mDataBeans = dataBeans;
        setData();
        Log.e(TAG, "setDataBeans: ??????????????????");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_usewhat);
        ButterKnife.bind(this);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setDimAmount(0f);
        getWindow().setGravity(Gravity.BOTTOM);
        setData();


    }

    @OnClick({R.id.dig_usewhat_diamond, R.id.dig_usewhat_fuwubao, R.id.dig_usewhat_card, R.id.dig_usewhat_sure, R.id.dig_usewhat_imgvone,
            R.id.dig_usewhat_imgvtwo, R.id.dig_usewhat_imgvthree, R.id.dig_usewhat_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_usewhat_diamond:
            case R.id.dig_usewhat_imgvone:
                mDigUsewhatImgvone.setImageResource(R.drawable.dig_use_se);
                mDigUsewhatImgvtwo.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatImgvthree.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatDiamond.setTextColor(mContext.getResources().getColor(R.color.usewhat_tv));
                mDigUsewhatFuwubao.setTextColor(mContext.getResources().getColor(R.color.black));
                mDigUsewhatCard.setTextColor(mContext.getResources().getColor(R.color.black));
                select_what = 4;
                break;
            case R.id.dig_usewhat_fuwubao:
            case R.id.dig_usewhat_imgvtwo:
                mDigUsewhatImgvone.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatImgvtwo.setImageResource(R.drawable.dig_use_se);
                mDigUsewhatImgvthree.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatDiamond.setTextColor(mContext.getResources().getColor(R.color.black));
                mDigUsewhatFuwubao.setTextColor(mContext.getResources().getColor(R.color.usewhat_tv));
                mDigUsewhatCard.setTextColor(mContext.getResources().getColor(R.color.black));
                select_what = 3;
                break;
            case R.id.dig_usewhat_card:
            case R.id.dig_usewhat_imgvthree:
                mDigUsewhatImgvone.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatImgvtwo.setImageResource(R.drawable.dig_use_unse);
                mDigUsewhatImgvthree.setImageResource(R.drawable.dig_use_se);
                mDigUsewhatDiamond.setTextColor(mContext.getResources().getColor(R.color.black));
                mDigUsewhatFuwubao.setTextColor(mContext.getResources().getColor(R.color.black));
                mDigUsewhatCard.setTextColor(mContext.getResources().getColor(R.color.usewhat_tv));
                select_what = 2;
                break;
            case R.id.dig_usewhat_sure:
                if (select_what == -1) {
                    Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (select_what == old_select) {
                    Toast.makeText(mContext, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (select_what) {
                    case 2: //???
                        bean_select = bean_card;
                        break;
                    case 3: //???
                        bean_select = bean_back;
                        break;
                    case 4: //???
                        if (diamond_num < 30) {
                            Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
                            mActivity.startActivity(new Intent(mActivity, BuyzActivity.class));
                            return;
                        }
                        bean_select = bean_zuan;
                        break;
                }
                // TODO: 2021/6/2 ?????????  ????????????????????????   ???????????????
                if (bean_select.getDuration() <= 0) {
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
             /*  bean_select.setDiaNum(40);
               bean_select.setDuration(65);*/
                old_select = select_what;
                if (mOnSureClickListener != null) {
                    mOnSureClickListener.onSureClickListener(bean_select);
                }
                break;
            case R.id.dig_usewhat_buy:
                mActivity.startActivity(new Intent(mActivity, BuyzActivity.class));
                break;
        }
    }

    private void setData() {
        mDigUseGpOne.setVisibility(View.GONE);
        mDigUseGpTwo.setVisibility(View.GONE);
        mDigUseGpThree.setVisibility(View.GONE);
        if (mDataBeans != null) {
            for (int i = 0; i < mDataBeans.size(); i++) {
                LiveCotctBean.RetDataBean retDataBean = mDataBeans.get(i);
                int proType = retDataBean.getProType();
                switch (proType) {
                    case 2://??????
                        mDigUseGpThree.setVisibility(View.VISIBLE);
                        bean_card = retDataBean;
                        mDigUsewhatCard.setText("???????????????????????????" + LiveDateUtil.formatSeconds(retDataBean.getDuration()) + "???");
                        break;
                    case 3://?????????
                        mDigUseGpTwo.setVisibility(View.VISIBLE);
                        bean_back = retDataBean;
                        mDigUsewhatFuwubao.setText("???????????????????????????" + LiveDateUtil.formatSeconds(retDataBean.getDuration()) + "???");
                        break;
                    case 4://??????
                        mDigUseGpOne.setVisibility(View.VISIBLE);
                        bean_zuan = retDataBean;
                        diamond_num = retDataBean.getDiaNum();
                        mDigUsewhatDiamond.setText("????????????????????????" + diamond_num + ")");
                        break;
                }
            }
        }
        switch (select_what) {
            case 2:
                if(mDigUseGpThree.getVisibility() ==View.GONE){
                    select_what = -1;
                }
                break;
            case 3:
                if(mDigUseGpTwo.getVisibility() ==View.GONE){
                    select_what = -1;
                }
                break;
            case 4:
                if(mDigUseGpOne.getVisibility() ==View.GONE){
                    select_what = -1;
                }
                break;
        }
    }


    public interface OnSureClickListener {
        void onSureClickListener(LiveCotctBean.RetDataBean bean);
    }


}

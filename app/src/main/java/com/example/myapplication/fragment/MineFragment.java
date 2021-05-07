package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.VpAdapter;
import com.example.myapplication.base.Constant;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.pop_dig.CodeDialog;
import com.example.myapplication.pop_dig.LogoutDialog;
import com.example.myapplication.pop_dig.ShareDialog;
import com.example.myapplication.ui.AdviceActivity;
import com.example.myapplication.ui.HelpabackActivity;
import com.example.myapplication.ui.LookPersonActivity;
import com.example.myapplication.ui.MailListActivity;
import com.example.myapplication.ui.MybackpActivity;
import com.example.myapplication.ui.NormalActivity;
import com.example.myapplication.ui.OranizeActivity;
import com.example.myapplication.ui.SetActivity;
import com.example.myapplication.ui.SetInActivity;
import com.example.myapplication.ui.WithdrDetailActivity;
import com.example.myapplication.utils.LiveShareUtil;
import com.ljy.devring.util.DensityUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.mine_zuanshi)
    TextView mMineZuanshi;
    @BindView(R.id.mine_cons_eyezuanshi)
    ImageView mMineConsEyezuanshi;
    @BindView(R.id.mine_cons_zuanshi)
    ConstraintLayout mMineConsZuanshi;
    @BindView(R.id.mine_money_tv)
    TextView mMineMoneyTv;
    @BindView(R.id.mine_cons_eyemoney)
    ImageView mMineConsEyemoney;
    @BindView(R.id.mine_cons_money)
    ConstraintLayout mMineConsMoney;
    @BindView(R.id.mine_title)
    TextView mMineTitle;
    @BindView(R.id.mine_id)
    TextView mMineId;
    @BindView(R.id.mine_level)
    TextView mMineLevel;
    @BindView(R.id.mine_daojutv)
    TextView mMineDaojutv;
    @BindView(R.id.mine_cons_daoju)
    ConstraintLayout mMineConsDaoju;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout mConstraintLayout2;
    @BindView(R.id.mine_head)
    ImageView mMineHead;
    @BindView(R.id.mine_vp)
    ViewPager mMineVp;
    @BindView(R.id.mine_edtline)
    View mMineEdtline;
    @BindView(R.id.mine_edt)
    TextView mMineEdt;
    @BindView(R.id.mine_ruzhu)
    RelativeLayout mMineRuzhu;
    @BindView(R.id.mine_logout)
    Button mMineLogout;
    @BindView(R.id.mine_indicator)
    LinearLayout mMineLinator;
    @BindView(R.id.tvvv)
    TextView mTvvv;
    private Unbinder unbinder;
    private boolean show_zuanshi = true, show_money = true;
    private XfFragment mXfFragment;
    private FwFragment mFwFragment;
    private List<Fragment> mFragments;
    private int mPower;
    private CodeDialog mCodeDialog;
    private LogoutDialog mLogoutDialog;
    private ShareDialog mShareDialog;
    private String zuan ="0";
    private String money = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @OnClick({R.id.mine_set, R.id.mine_cons_eyezuanshi, R.id.mine_cons_eyemoney, R.id.mine_guanzhu, R.id.mine_guanzhugzongh, R.id.mine_help,
            R.id.mine_edt, R.id.mine_ruzhu, R.id.mine_logout, R.id.mine_money_tv, R.id.textView16, R.id.mine_zuanshi, R.id.textView14,
            R.id.mine_daojutv, R.id.textView17})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.mine_daojutv:
            case R.id.textView17:
                startActivity(new Intent(getActivity(), MybackpActivity.class));
                break;
            case R.id.mine_cons_eyezuanshi:
                if (show_zuanshi) {
                    mMineConsEyezuanshi.setImageResource(R.drawable.mine_bi);
                    mMineZuanshi.setText("*****");
                } else {
                    mMineConsEyezuanshi.setImageResource(R.drawable.mine_kai);
                    mMineZuanshi.setText(zuan);
                }
                show_zuanshi = !show_zuanshi;
                break;
            case R.id.mine_cons_eyemoney:
                if (show_money) {
                    mMineConsEyemoney.setImageResource(R.drawable.mine_bi);
                    mMineMoneyTv.setText("*****");
                } else {
                    mMineConsEyemoney.setImageResource(R.drawable.mine_kai);
                    mMineMoneyTv.setText(money);
                }
                show_money = !show_money;
                break;
            case R.id.mine_guanzhu:
//                startActivity(new Intent(getActivity(), FollowActivity.class));
                startActivity(new Intent(getActivity(), MailListActivity.class));
                break;
            case R.id.mine_guanzhugzongh:
                mCodeDialog.show();
                break;
            case R.id.mine_help:
                startActivity(new Intent(getActivity(), HelpabackActivity.class));
                break;
            case R.id.mine_edt:
                Intent intent = null;
                if (mPower == Constant.POWER_ZIXUNSHI) {//咨询师
                    intent = new Intent(getActivity(), LookPersonActivity.class);
                    intent.putExtra("is_user", false);
                    startActivity(intent);
                } else {//咨询机构--子咨询师
                    intent = new Intent(getActivity(), OranizeActivity.class);
                    intent.putExtra("is_user", mPower == Constant.POWER_ZIXUNJIGOU ? false : true);
                    startActivity(intent);
                }
                break;
            case R.id.mine_ruzhu:
                if (mPower == Constant.POWER_NORMAL) {
                    startActivity(new Intent(getActivity(), SetInActivity.class));
                } else {
                    mShareDialog.show();
                }
                break;
            case R.id.mine_logout:
                mLogoutDialog.show();
                break;
            case R.id.mine_zuanshi:
            case R.id.textView14:
                if (mPower == Constant.POWER_NORMAL) {//普通
                    startActivity(new Intent(getActivity(), NormalActivity.class));
                } else {//咨询师/机构
                    startActivity(new Intent(getActivity(), AdviceActivity.class));
                }
                break;
            case R.id.mine_money_tv:
            case R.id.textView16:
           /*     Intent intent_ad = new Intent(getActivity(), AdviceActivity.class);
                intent_ad.putExtra("index", 1);
                startActivity(intent_ad);*/
                startActivity(new Intent(getActivity(), WithdrDetailActivity.class));
                break;
        }
    }

    private void init() {
        UserInfoBean userInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        if(userInfo!=null){
            UserInfoBean.RetDataBean retData = userInfo.getRetData();
            setData(retData);


        }
        mLogoutDialog = new LogoutDialog(getActivity());
        mCodeDialog = new CodeDialog(getActivity());
        mFragments = new ArrayList<>();
        mXfFragment = new XfFragment();
        initShare();
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
        if (mPower == Constant.POWER_NORMAL) {//普通
            mMineEdt.setVisibility(View.GONE);
            mMineEdtline.setVisibility(View.GONE);
            mFragments.add(mXfFragment);
            mMineConsMoney.setVisibility(View.GONE);
        } else {
            mTvvv.setText("分享APP");
            mFwFragment = new FwFragment();
            mMineConsMoney.setVisibility(View.VISIBLE);
            mFragments.add(mXfFragment);
            mFragments.add(mFwFragment);
            mMineLinator.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(getActivity(), 5), DensityUtil.dp2px(getActivity(), 5));
            layoutParams.leftMargin = DensityUtil.dp2px(getActivity(), 8);
            ImageView img_one = new ImageView(getActivity());
            img_one.setLayoutParams(layoutParams);
            img_one.setImageResource(R.drawable.min_im_dit);
            img_one.setSelected(true);
            mMineLinator.addView(img_one);
            ImageView img_two = new ImageView(getActivity());
            img_two.setLayoutParams(layoutParams);
            img_two.setImageResource(R.drawable.min_im_dit);
            mMineLinator.addView(img_two);
        }
        mMineVp.setAdapter(new VpAdapter(getFragmentManager(), 1, mFragments));

        /*mMineSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mMineSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mMineSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mMineSmart.setEnableOverScrollBounce(true);//是否启用越界回弹*/

        mLogoutDialog.setOnLogoutClickListener(new LogoutDialog.OnLogoutClickListener() {
            @Override
            public void onLogoutClickListener() {
                LiveShareUtil.getInstance(getActivity()).clear();
                getActivity().finish();
            }
        });
        mMineVp.addOnPageChangeListener(this);


    }

    private void setData(UserInfoBean.RetDataBean data){
        zuan = String.valueOf(data.getDiamond());
        money =String.valueOf(data.getBalance());
        mMineZuanshi.setText(zuan);
        mMineMoneyTv.setText(money);
        mMineDaojutv.setText("--3--");
        mMineTitle.setText(data.getNickname());
        mMineId.setText("ID "+data.getId());
        mMineLevel.setText("lv "+data.getLevel());
        Glide.with(getActivity()).load(data.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(mMineHead);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        toScroll(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void toScroll(int pos) {
        for (int i = 0; i < mMineLinator.getChildCount(); i++) {
            mMineLinator.getChildAt(i).setSelected(i == pos ? true : false);
        }
    }

    private void initShare() {
        mShareDialog = new ShareDialog(getActivity());
        UMWeb web = new UMWeb("https://lanhuapp.com/web/#/item/project/stage?pid=90197f71-56ef-4ecd-8d1b-2fdf22fc9d4c");
        web.setTitle("边框心理");//标题
        web.setThumb(new UMImage(getActivity(), R.drawable.mine_live));  //缩略图
        web.setDescription("my description");//描述

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(getActivity())
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(getActivity())
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWeiboClickListener() {
//                new ShareAction(LookPersonActivity.this).withMedia(web).
//                setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE/*,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,*/).setCallback(shareListener).share();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG).show();

        }
    };

}

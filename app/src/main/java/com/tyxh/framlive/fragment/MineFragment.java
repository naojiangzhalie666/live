package com.tyxh.framlive.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.DensityUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.VpAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.CodeDialog;
import com.tyxh.framlive.pop_dig.LogoutDialog;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.ui.AdviceActivity;
import com.tyxh.framlive.ui.EdtmsgActivity;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.MailListActivity;
import com.tyxh.framlive.ui.MybackpActivity;
import com.tyxh.framlive.ui.NormalActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.ui.SetActivity;
import com.tyxh.framlive.ui.SetInActivity;
import com.tyxh.framlive.ui.WebVActivity;
import com.tyxh.framlive.utils.ImageUtils;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.xzb.utils.TCConstants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MineFragment";
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
    CircleImageView mMineHead;
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
    @BindView(R.id.smart)
    SmartRefreshLayout mSmart;
    private Unbinder unbinder;
    private boolean show_zuanshi = true, show_money = true;
    private XfFragment mXfFragment;
    private FwFragment mFwFragment;
    private List<Fragment> mFragments;
    private int mPower;
    private CodeDialog mCodeDialog;
    private LogoutDialog mLogoutDialog;
    private ShareDialog mShareDialog;
    private String zuan = "0";
    private String money = "0";
    private UserInfoBean mUserInfo;
    private int mAuditState;   //审核状态(0:未申请1:待审核;2:通过;3:驳回)--咨询师/咨询机构认证

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
            R.id.mine_edt, R.id.mine_ruzhu, R.id.mine_logout, R.id.mine_money_tv, R.id.mine_cons_money, R.id.textView16, R.id.mine_zuanshi, R.id.mine_cons_zuanshi, R.id.textView14,
            R.id.mine_daojutv, R.id.textView17, R.id.mine_cons_daoju, R.id.mine_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_head:
                startActivity(new Intent(getActivity(), EdtmsgActivity.class));
                break;
            case R.id.mine_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.mine_daojutv:
            case R.id.textView17:
            case R.id.mine_cons_daoju:
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
//                startActivity(new Intent(getActivity(), HelpabackActivity.class));
                WebVActivity.startMe(getActivity(), "帮助与反馈C");
                break;
            case R.id.mine_edt:
                Intent intent = null;
                if (mPower == Constant.POWER_ZIXUNSHI) {//咨询师
                    intent = new Intent(getActivity(), LookPersonActivity.class);
                    intent.putExtra("query_id", mUserInfo.getRetData().getId());
                    intent.putExtra("is_user", false);
                    startActivity(intent);
                } else {//咨询机构--子咨询师
                    intent = new Intent(getActivity(), OranizeActivity.class);
                    intent.putExtra("is_user", mPower == Constant.POWER_ZIXUNJIGOU ? false : true);
                    intent.putExtra("query_id", mUserInfo.getRetData().getId());
                    startActivity(intent);
                }
                break;
            case R.id.mine_ruzhu:
                if (mPower == Constant.POWER_NORMAL || Constant.IS_SHENHEING) {
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
            case R.id.mine_cons_zuanshi:
//                if (mPower == Constant.POWER_NORMAL) {//普通
//                    startActivity(new Intent(getActivity(), NormalActivity.class));
//                } else {//咨询师/机构
                switch (mAuditState) {
                    case 0://0:未申请
                    case 3://;3:驳回
                    case 1://1:待审核
                        startActivity(new Intent(getActivity(), NormalActivity.class));
                        break;
                    case 2://2:通过
                        startActivity(new Intent(getActivity(), AdviceActivity.class));
                        break;
                }
//                }
                break;
            case R.id.mine_money_tv:
            case R.id.textView16:
            case R.id.mine_cons_money:
                Intent intent_ad = new Intent(getActivity(), AdviceActivity.class);
                intent_ad.putExtra("index", 1);
                startActivity(intent_ad);
//                startActivity(new Intent(getActivity(), WithdrDetailActivity.class));
                break;
        }
    }

    private void init() {
        mUserInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        mAuditState = mUserInfo.getRetData().getAuditState();
        /*if (mUserInfo != null) {
            UserInfoBean.RetDataBean retData = mUserInfo.getRetData();
            setData(retData);
        }*/
        mLogoutDialog = new LogoutDialog(getActivity());
        mCodeDialog = new CodeDialog(getActivity());
        mFragments = new ArrayList<>();
        mXfFragment = new XfFragment();
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        initShare();
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
//        if (mPower == Constant.POWER_NORMAL) {//普通
//            mMineEdt.setVisibility(View.GONE);
//            mMineEdtline.setVisibility(View.GONE);
//            mFragments.add(mXfFragment);
//            mMineConsMoney.setVisibility(View.GONE);
//        } else {
        switch (mAuditState) {
            case 0://0:未申请
            case 3://;3:驳回
                Constant.IS_SHENHEING = false;
                mMineEdt.setVisibility(View.GONE);
                mMineEdtline.setVisibility(View.GONE);
                mFragments.add(mXfFragment);
                mMineConsMoney.setVisibility(View.GONE);
                break;
            case 1://1:待审核
                Constant.IS_SHENHEING = true;
                mMineEdt.setVisibility(View.GONE);
                mMineEdtline.setVisibility(View.GONE);
                mFragments.add(mXfFragment);
                mMineConsMoney.setVisibility(View.GONE);
                break;
            case 2://2:通过
                Constant.IS_SHENHEING = false;
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
                break;
        }
//        }
        mMineVp.setAdapter(new VpAdapter(getFragmentManager(), 1, mFragments));

        /*mMineSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mMineSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mMineSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mMineSmart.setEnableOverScrollBounce(true);//是否启用越界回弹*/

        mLogoutDialog.setOnLogoutClickListener(new LogoutDialog.OnLogoutClickListener() {
            @Override
            public void onLogoutClickListener() {
                LiveShareUtil.getInstance(getActivity()).clear();
                LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE, true);
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        mMineVp.addOnPageChangeListener(this);
        mCodeDialog.setOnPhotoSaveListener(new CodeDialog.OnPhotoSaveListener() {
            @Override
            public void onSaveListener(Bitmap bitmap) {
                if (checkPublishPermission()) {
                    ImageUtils.saveBmp2Gallery(getActivity(), bitmap, "saomiao");
                }
            }
        });
    }

    private void setData(UserInfoBean.RetDataBean data) {
        mMineTitle.setText(data.getNickname());
        mMineId.setText("ID " + data.getId());
        mMineLevel.setText("lv " + data.getLevel());
        Glide.with(getActivity()).load(data.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).circleCrop().into(mMineHead);
        money = data.getCumulativeIncome();
        if (show_money)
            mMineMoneyTv.setText(money);

    }

    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), mUserInfo.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    String diamond = data.getDiamond();
                    if (!TextUtils.isEmpty(diamond)) {
                        if (diamond.contains(".")) {
                            zuan = diamond.substring(0, diamond.indexOf("."));
                        } else {
                            zuan = diamond;
                        }
                    }
                    if (show_zuanshi)
                        mMineZuanshi.setText(zuan);
                    mMineDaojutv.setText(String.valueOf(data.getPropCount()));
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    /*获取用户信息*/
    private void getUserInfo() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(getActivity()).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    setData(userInfoBean.getRetData());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
       /* LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                Log.e(TAG, "onSuccessListener: " + result.toString());
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(getActivity()).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    setData(userInfoBean.getRetData());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getMineAsset();
        getUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        DevRing.httpManager().stopRequestByTag(TAG);
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
        UMWeb web = new UMWeb(Constant.SHARE_URL);
        web.setTitle(Constant.SHARE_NAME);//标题
        web.setThumb(new UMImage(getActivity(), R.drawable.share_suolue));  //缩略图
        web.setDescription(Constant.SHARE_MS);//描述

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

    public void toUpdateData() {
//        UserInfoBean userInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
//        if (userInfo != null) {
//            UserInfoBean.RetDataBean retData = userInfo.getRetData();
//            setData(retData);
//        }
    }

    /**
     * 动态权限检查相关
     */
    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[0]), TCConstants.WRITE_PERMISSION_REQ_CODE);
                Toast.makeText(getActivity(), "该功能需要存储权限", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

}

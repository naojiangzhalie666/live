package com.tyxh.framlive.ui;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.bean.live_mine.ZxjgBean;
import com.tyxh.framlive.bean.live_mine.ZxsBean;
import com.tyxh.framlive.fragment.setin.JgOneFragment;
import com.tyxh.framlive.fragment.setin.JgTwoFragment;
import com.tyxh.framlive.fragment.setin.ManOneFragment;
import com.tyxh.framlive.fragment.setin.ManTwoFragment;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/********************************************************************
 @version: 1.0.0
 @description: 入驻申请界面
 @author: admin
 @time: 2021/3/26 8:43
 @变更历史:
 ********************************************************************/
public class SetInActivity extends LiveBaseActivity {

    @BindView(R.id.setin_man)
    TextView mSetinMan;
    @BindView(R.id.setin_jigou)
    TextView mSetinJigou;
    @BindView(R.id.setin_fram)
    FrameLayout mSetinFram;
    @BindView(R.id.setin_con_end)
    ConstraintLayout mSetinConend;


    private ManOneFragment mManOneFragment;
    private ManTwoFragment mManTwoFragment;

    private JgOneFragment mJgOneFragment;
    private JgTwoFragment mJgTwoFragment;

    private boolean is_man = true;      //true 选择的为咨询师  false 选择为咨询机构
    public ZxsBean mZxsBean;            //咨询师入驻实体
    public ZxjgBean mZxjgBean;          //咨询机构入驻实体
    private UserInfoBean mUserInfo;     //登录帐号实体数据


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_set_in;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mZxsBean = new ZxsBean();
        mZxjgBean = new ZxjgBean();
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        mZxsBean.userId = mUserInfo.getRetData().getId();
        mZxjgBean.userId = mUserInfo.getRetData().getId();
        if (Constant.IS_SHENHEING) {
            mSetinConend.setVisibility(View.VISIBLE);
            return;
        }
        mJgOneFragment = new JgOneFragment();
        mJgTwoFragment = new JgTwoFragment();
        mManOneFragment = new ManOneFragment();
        mManTwoFragment = new ManTwoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.setin_fram, mManOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mManOneFragment);


    }

    @OnClick({R.id.setin_back, R.id.setin_man, R.id.setin_jigou})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setin_back:
                finish();
                break;
            case R.id.setin_man:
                mSetinMan.setTextColor(getResources().getColor(R.color.setin_se));
                mSetinMan.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
                mSetinJigou.setTextColor(getResources().getColor(R.color.login_txt));
                mSetinJigou.setBackgroundResource(R.drawable.bg_circle_lotxt);
                is_man = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mManOneFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mManOneFragment);

                break;
            case R.id.setin_jigou:
                mSetinMan.setTextColor(getResources().getColor(R.color.login_txt));
                mSetinMan.setBackgroundResource(R.drawable.bg_circle_lotxt);
                mSetinJigou.setTextColor(getResources().getColor(R.color.setin_se));
                mSetinJigou.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
                is_man = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mJgOneFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mJgOneFragment);
                break;
        }
    }

    public void goNext() {
        if (is_man) {
            getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mManTwoFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mManTwoFragment);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.setin_fram, mJgTwoFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mJgTwoFragment);
        }
    }

    public void toSub() {
        if (is_man) {
            toCommitMan();
        } else {
            toCommitZxjg();
        }
    }

    /**
     * 咨询师入驻上传提交
     */
    private void toCommitMan() {
        String result = new Gson().toJson(mZxsBean);
        Log.i(TAG, "咨询师上传参数： " + result);
        showLoad();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toSubmitZxs(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    Constant.IS_SHENHEING = true;//应该是userInfo接口返回一个状态---修改为审核中
                    mSetinConend.setVisibility(View.VISIBLE);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });

    }

    /**
     * 咨询机构上传提交
     */
    private void toCommitZxjg(){
        String result = new Gson().toJson(mZxjgBean);
        Log.i(TAG, "咨询机构上传参数： " + result);
        showLoad();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toSubmitZxjg(token, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    Constant.IS_SHENHEING = true;//应该是userInfo接口返回一个状态---修改为审核中
                    mSetinConend.setVisibility(View.VISIBLE);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });


//        Constant.IS_SHENHEING = true;
//        mSetinConend.setVisibility(View.VISIBLE);
    }

}

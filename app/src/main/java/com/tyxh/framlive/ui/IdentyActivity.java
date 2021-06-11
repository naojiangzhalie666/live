package com.tyxh.framlive.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.fragment.identy.IdentyOneFragment;
import com.tyxh.framlive.fragment.identy.IdentyThreeFragment;
import com.tyxh.framlive.fragment.identy.IdentyTworagment;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;

public class IdentyActivity extends LiveBaseActivity {

    @BindView(R.id.identy_one)
    TextView mIdentyOne;
    @BindView(R.id.identy_two)
    TextView mIdentyTwo;
    @BindView(R.id.identy_three)
    TextView mIdentyThree;
    @BindView(R.id.identy_lineone)
    View mIdentyLineone;
    @BindView(R.id.identy_linetwo)
    View mIdentyLinetwo;
    @BindView(R.id.identy_tvone)
    TextView mIdentyTvone;
    @BindView(R.id.identy_twtwo)
    TextView mIdentyTwtwo;
    @BindView(R.id.identy_twthree)
    TextView mIdentyTwthree;
    private int count = 0;
    private IdentyOneFragment mIdentyOneFragment;
    private IdentyTworagment mIdentyTworagment;
    private IdentyThreeFragment mIdentyThreeFragment;
    public String sfz_front = "";  //身份证--头像
    public String sfz_back = "";   //身份证--国辉


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_identy;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mIdentyOneFragment = new IdentyOneFragment();
        mIdentyTworagment = new IdentyTworagment();
        mIdentyThreeFragment = new IdentyThreeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.identy_fram, mIdentyOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mIdentyOneFragment);


    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void goNext() {
        if (count == 0) {
            if (TextUtils.isEmpty(mIdentyOneFragment.getResult())) {
                ToastShow("请先填写数据");
                return;
            }
            Log.e(TAG, "onClick:" + mIdentyOneFragment.getResult());
            getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyTworagment).commit();
            getSupportFragmentManager().beginTransaction().show(mIdentyTworagment);
            mIdentyTwo.setBackgroundResource(R.drawable.denti_se);
            mIdentyTwtwo.setTextColor(getResources().getColor(R.color.login_txt));
            mIdentyLinetwo.setBackgroundResource(R.drawable.bg_circle_solder_lotxt);
            count += 1;
        } else if (count == 1) {
            if (TextUtils.isEmpty(mIdentyTworagment.getResult())) {
                ToastShow("请填写数据");
                return;
            }
            toCommit();
        } else {//验证成功更新用户信息
            getUserInfo();
        }
    }

    public void toCommit() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().addSfrz(token, sfz_front, sfz_back), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                Bundle bundle =new Bundle();
                if(baseBean.getRetCode() ==0){//验证成功--第三页展示数据--成功
                    bundle.putBoolean("result",true);
                }else{//验证失败--第三页展示数据--错误
                    bundle.putBoolean("result",false);
                }
                mIdentyThreeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyThreeFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mIdentyThreeFragment);
                mIdentyThree.setBackgroundResource(R.drawable.denti_se);
                mIdentyTwthree.setTextColor(getResources().getColor(R.color.login_txt));
                count += 1;

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    public void onReset() {
        count = 0;
        mIdentyOneFragment = new IdentyOneFragment();
        mIdentyTworagment = new IdentyTworagment();
        mIdentyThreeFragment = new IdentyThreeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.identy_fram, mIdentyOneFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mIdentyOneFragment);
        mIdentyTwo.setBackgroundResource(R.drawable.denti_unse);
        mIdentyTwtwo.setTextColor(getResources().getColor(R.color.nineninenine));
        mIdentyLinetwo.setBackgroundResource(R.drawable.home_ft);
        mIdentyThree.setBackgroundResource(R.drawable.denti_unse);
        mIdentyTwthree.setTextColor(getResources().getColor(R.color.nineninenine));
    }

    /*获取用户信息*/
    private void getUserInfo() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(token), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(IdentyActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                } else {
                    ToastShow(userInfoBean.getRetMsg());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                hideLoad();
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
      /*  LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                Log.e(TAG, "onSuccessListener: " + result.toString());
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(IdentyActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                } else {
                    ToastShow(userInfoBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });*/
        finish();
    }


}

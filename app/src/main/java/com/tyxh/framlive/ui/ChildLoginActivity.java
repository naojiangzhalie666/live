package com.tyxh.framlive.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LoginBean;
import com.tyxh.framlive.bean.SignBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.tuikit.AVCallManager;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.utils.login.TCUserMgr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.base.Constant.LIVE_UPDATE_CODE;

public class ChildLoginActivity extends LiveBaseActivity {

    @BindView(R.id.child_login_name)
    EditText mChildLoginName;
    @BindView(R.id.child_login_pwd)
    EditText mChildLoginPwd;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;
    @BindView(R.id.login_tvxieyi)
    TextView mLoginTvxieyi;

    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_child_login;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setTextContent();

    }

    @OnClick({R.id.imgv_back, R.id.child_login_forget, R.id.child_login_login,R.id.login_rela,R.id.login_cbxieyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.child_login_forget:
              statActivity(ChildForgetActivity.class);
                break;
            case R.id.child_login_login:
                if (mLoginImgv.getVisibility() != View.VISIBLE) {
                    ToastShow("请阅读并勾选协议");
                    return;
                }
                String name = mChildLoginName.getText().toString();
                String pwd = mChildLoginPwd.getText().toString();
                if(TextUtils.isEmpty(name)){
                    ToastShow("请输入子账号名称");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    ToastShow("请输入密码");
                    return;
                }
                login(name,pwd,"");
                break;
            case R.id.login_rela://协议
            case R.id.login_cbxieyi:
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    mLoginImgv.setVisibility(View.GONE);
                } else {
                    mLoginImgv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /*登录*/
    private void login(String name, String pwd, String type) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (loginBean.getRetCode() == 0) {
                    Constant.TOKEN = "bearer " + loginBean.getRetData().getToken();
                    LiveShareUtil.getInstance(ChildLoginActivity.this).putToken("bearer " + loginBean.getRetData().getToken());
                    getUserInfo("bearer " + loginBean.getRetData().getToken());
                } else {
                    ToastShow(loginBean.getRetMsg());
                    hideLoad();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                hideLoad();
            }
        });
    }

    /*获取用户信息*/
    private void getUserInfo(String tk) {
        showLoad();

        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(tk), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put(LiveShareUtil.APP_USERID, userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put(LiveShareUtil.APP_USERNAME, userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put(LiveShareUtil.APP_USERHEAD, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put(LiveShareUtil.APP_USERCOVER, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    LiveShareUtil.getInstance(ChildLoginActivity.this).putPower(userInfoBean.getRetData().getType());//用户类型
//                    LiveShareUtil.getInstance(LoginActivity.this).putPower(1);//用户类型
                    Log.e(TAG, "onSuccessListener: " + userInfoBean.getRetData().getId());
                    String interest = userInfoBean.getRetData().getInterest();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                        statActivity(MsgInputActivity.class);
                    } else {
                        isNeedUpmsg = false;
                    }
                    thisLogin(userInfoBean.getRetData().getId(), userInfoBean.getRetData());
                } else {
                    ToastShow(userInfoBean.getRetMsg());
                    hideLoad();
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
    }

    /*获取直播的sign*/
    private void thisLogin(String user_id, UserInfoBean.RetDataBean bean) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(ChildLoginActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
                    gotLogin(user_id, bean, signBean.getRetData());
                } else {
                    hideLoad();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });
    }

    /*登录直播的IM并配置聊天的IM*/
    private void gotLogin(String user_id, UserInfoBean.RetDataBean bean, String sign) {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig, long sdk_id) {
                if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
                    UserModel self = new UserModel();
                    self.userId = userid;
                    self.userSig = usersig;
                    ProfileManager.getInstance().setUserModel(self);
                    AVCallManager.getInstance().init(ChildLoginActivity.this);
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                if (!isNeedUpmsg) {
                    EventBus.getDefault().post(new EventMessage(LIVE_UPDATE_CODE));
                    statActivity(MainActivity.class);
                    finish();
                }
                hideLoad();
            }

            @Override
            public void onLoginFailedListener(int errCode, String errInfo) {
                super.onLoginFailedListener(errCode, errInfo);
                ToastShow("登录失败");
                hideLoad();
            }
        });
        mInstance.loginMLVB(user_id, bean.getNickname(), bean.getIco(), bean.getIco(), bean.getGender(), sign);
    }

    private static void loginTUIKitLive(long sdkAppid, String userId, String userSig) {
        try {
            Class<?> classz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive");
            Class<?> tClazz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive$LoginCallback");

            // 反射修改isAttachedTUIKit的值
            Field field = classz.getDeclaredField("sIsAttachedTUIKit");
            field.setAccessible(true);
            field.set(null, true);

            Method method = classz.getMethod("login", int.class, String.class, String.class, tClazz);
            method.invoke(null, sdkAppid, userId, userSig, null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            TUIKitLog.e("LoginActivity", "loginTUIKitLive error: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (msg.getCode() == LIVE_UPDATE_CODE) {
            finish();
        }
    }
    /*设置提现规则中的两个特殊文字*/
    private String xieyi_tv = "我已阅读并同意《用户协议》和《隐私政策》";
    private void setTextContent() {
        SpannableStringBuilder builder = new SpannableStringBuilder(xieyi_tv);
        ClickableSpan clickableSpan_two = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(ChildLoginActivity.this,"边框心理用户协议");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);  drawState.clearShadowLayer();
            }
        };
        ClickableSpan clickableSpan_three = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(ChildLoginActivity.this,"用户隐私条款");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        builder.setSpan(clickableSpan_two, 7, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(clickableSpan_three, 14, xieyi_tv.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mLoginTvxieyi.setMovementMethod(LinkMovementMethod.getInstance());
        mLoginTvxieyi.setHighlightColor(getResources().getColor(android.R.color.transparent));//不设置会有背景色
        mLoginTvxieyi.setText(builder);

    }


}

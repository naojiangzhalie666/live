package com.tyxh.framlive.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LoginBean;
import com.tyxh.framlive.bean.SignBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.tuikit.AVCallManager;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.views.CodeEditText;
import com.tyxh.xzb.utils.login.TCUserMgr;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.base.Constant.LIVE_UPDATE_CODE;

public class BindPhoneActivity extends LiveBaseActivity {

    @BindView(R.id.title)
    TextView mBindTitle;
    @BindView(R.id.bindphone_note)
    TextView mBindphoneNote;
    @BindView(R.id.bindphone_qu)
    TextView mBindphoneQu;
    @BindView(R.id.bindphone_sanjiao)
    ImageView mBindphoneSanjiao;
    @BindView(R.id.bindphone_phonenum)
    EditText mBindphonePhonenum;
    @BindView(R.id.bindphone_rela)
    RelativeLayout mBindphoneRela;
    @BindView(R.id.bindphone_codeedt)
    CodeEditText mBindphonePhoneCode;
    @BindView(R.id.bindphone_sub)
    Button mBindphoneSub;

    private boolean havget_yzm = false;
    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;
    private boolean mNeedUp;//???????????????????????????
    private int mOther;     //1???????????????  2???????????????

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mNeedUp = intent.getBooleanExtra("needUp", false);
        mOther = intent.getIntExtra("other", 1);
        if (mOther == 2) {
            mBindTitle.setText("?????????????????????");
        }


    }

    @OnClick({R.id.imgv_back, R.id.bindphone_qu, R.id.bindphone_sanjiao, R.id.bindphone_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.bindphone_qu:
                break;
            case R.id.bindphone_sanjiao:
                break;
            case R.id.bindphone_sub:
                if (!havget_yzm) {
                    if (TextUtils.isEmpty(mBindphonePhonenum.getText().toString()) || mBindphonePhonenum.getText().toString().length() != 11) {
                        ToastShow("???????????????????????????");
                        return;
                    }
                    getYzm(mBindphonePhonenum.getText().toString());
                } else {
                    if (TextUtils.isEmpty(mBindphonePhoneCode.getText().toString())) {
                        ToastShow("?????????????????????");
                        return;
                    }
                    if (mOther == 2) {//?????????????????????
                        toLogin(mBindphonePhonenum.getText().toString(), mBindphonePhoneCode.getText().toString(), "sms");
                    } else {//???????????????
                        bindPhone(mBindphonePhonenum.getText().toString(), mBindphonePhoneCode.getText().toString());
                    }
                }
                break;
        }
    }

    /*???????????????*/
    private void getYzm(String phone) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().sendMsg(phone), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mBindphoneSub.setText("??????");
                    mBindphoneNote.setText("??????????????????");
                    mBindphoneRela.setVisibility(View.INVISIBLE);
                    mBindphonePhoneCode.setVisibility(View.VISIBLE);
                    mBindphonePhoneCode.requestFocus();
                    havget_yzm = true;
                    ToastShow(baseBean.getRetData());
                } else {
                    ToastShow(baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*???????????????*/
    private void bindPhone(String phone, String code) {
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().bindPhone(token, phone, code), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getUserInfo(token);
                } else {
                    hideLoad();
                    ToastShow(baseBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });

    }

    /*??????*/
    private void toLogin(String name, String pwd, String type) {
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (loginBean.getRetCode() == 0) {
                    Constant.TOKEN = "bearer " + loginBean.getRetData().getToken();
                    LiveShareUtil.getInstance(BindPhoneActivity.this).putToken("bearer " + loginBean.getRetData().getToken());
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

    /*??????????????????*/
    private void getUserInfo(String tk) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(tk), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERID, userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERNAME, userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERHEAD, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERCOVER, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put("user", new Gson().toJson(userInfoBean));//??????????????????
                    LiveShareUtil.getInstance(BindPhoneActivity.this).putPower(userInfoBean.getRetData().getType());//????????????
                    String interest = userInfoBean.getRetData().getInterest();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                        statActivity(MsgInputActivity.class);
                        hideLoad();
                        finish();
                    } else {
                        thisLogin(userInfoBean.getRetData());
                        isNeedUpmsg = false;
                    }

                } else {
                    ToastShow(userInfoBean.getRetMsg());
                    hideLoad();
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                hideLoad();
                if (throwable.errorType == HTTP_ERROR) {//????????????
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);


       /* LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERID, userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERNAME, userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERHEAD, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERCOVER, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put("user", new Gson().toJson(userInfoBean));//??????????????????
                    LiveShareUtil.getInstance(BindPhoneActivity.this).putPower(userInfoBean.getRetData().getType());//????????????
                    String interest = userInfoBean.getRetData().getInterest();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                        statActivity(MsgInputActivity.class);
                        hideLoad();
                        finish();
                    } else {
                        thisLogin(userInfoBean.getRetData());
                        isNeedUpmsg = false;
                    }

                } else {
                    ToastShow(userInfoBean.getRetMsg());
                    hideLoad();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });*/
    }

    /*???????????????sign*/
    private void thisLogin(UserInfoBean.RetDataBean bean) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
                    gotLogin(bean, signBean.getRetData());
                } else {
                    ToastShow(signBean.getRetMsg());
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

    /*???????????????IM??????????????????IM*/
    private void gotLogin(UserInfoBean.RetDataBean bean, String sign) {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig, long sdk_id) {
                if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
                    UserModel self = new UserModel();
                    self.userId = userid;
                    self.userSig = usersig;
                    ProfileManager.getInstance().setUserModel(self);
                    AVCallManager.getInstance().init(BindPhoneActivity.this);
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
                ToastShow("????????????");
                hideLoad();
            }
        });
        mInstance.loginMLVB(bean.getId(), bean.getNickname(), bean.getIco(), bean.getIco(), bean.getGender(), sign);

    }

    private static void loginTUIKitLive(long sdkAppid, String userId, String userSig) {
        try {
            Class<?> classz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive");
            Class<?> tClazz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive$LoginCallback");

            // ????????????isAttachedTUIKit??????
            Field field = classz.getDeclaredField("sIsAttachedTUIKit");
            field.setAccessible(true);
            field.set(null, true);

            Method method = classz.getMethod("login", int.class, String.class, String.class, tClazz);
            method.invoke(null, sdkAppid, userId, userSig, null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            TUIKitLog.e("LoginActivity", "loginTUIKitLive error: " + e.getMessage());
        }
    }

}

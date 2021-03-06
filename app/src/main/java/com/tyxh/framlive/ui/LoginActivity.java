package com.tyxh.framlive.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.rich.oauth.callback.PreLoginCallback;
import com.rich.oauth.callback.TokenCallback;
import com.rich.oauth.core.RichAuth;
import com.rich.oauth.core.UIConfigBuild;
import com.rich.oauth.util.RichLogUtil;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
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
import com.tyxh.framlive.pop_dig.XieyDialog;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.Constantc;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.login.TCUserMgr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.base.Constant.LIVE_UPDATE_CODE;
import static com.tyxh.framlive.base.LiveApplication.api;

public class LoginActivity extends LiveBaseActivity {


    //    @BindView(R.id.login_phonenum)
//    TextView mLoginPhonenum;
    @BindView(R.id.login_tvxieyi)
    TextView mTvXieyi;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;
    private boolean mPermission = false;               // ??????????????????
    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;
    private boolean isNeedUpMobile = false;
    private String xieyi_tv = "???????????????????????????????????????????????????????????????????????????????????????????????????";
    private XieyDialog mXieyDialog;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setTextContent();
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        boolean agree = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getAgree();
        if(agree) {
            goNext();
        }else{
            mXieyDialog = new XieyDialog(this, this);
            mXieyDialog.setOnBtClickListener(new XieyDialog.OnBtClickListener() {
                @Override
                public void onAgreeClickListener() {
                    LiveApplication.getmInstance().toInitAll();
                    mXieyDialog.dismiss();
                    mLoginImgv.setVisibility(View.VISIBLE);
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE,true);
                    goNext();
                }

                @Override
                public void onNotAgreeClickListener() {
                  LoginActivity.this.finish();

                }
            });
            mXieyDialog.show();
        }

    }

    private void goNext() {
        mPermission = checkPublishPermission();
//        getPhoneNum();
        if (user_Info != null) {
            mLoginImgv.setVisibility(View.VISIBLE);
            getUserInfo(token);
        }

    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela, R.id.ll_zi,R.id.login_cbxieyi,R.id.login_vvvv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis://????????????
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    preLogin();
                } else {
                    ToastShow("????????????????????????");
                }
                break;
            case R.id.login_loginother://?????????????????????
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(this, BindPhoneActivity.class);
                    intent.putExtra("other", 2);
                    startActivity(intent);
                } else {
                    ToastShow("????????????????????????");
                }
                break;
            case R.id.ll_wx://????????????
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    loginWx();
                } else {
                    ToastShow("????????????????????????");
                }
                break;
            case R.id.ll_zi:
//                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                startActivity(new Intent(this, ChildLoginActivity.class));
//                } else {
//                    ToastShow("????????????????????????");
//                }
                break;
            case R.id.login_rela://??????
            case R.id.login_cbxieyi:
            case R.id.login_vvvv:
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    mLoginImgv.setVisibility(View.GONE);
                } else {
                    mLoginImgv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /*??????????????????????????????????????????*/
    private void setTextContent() {
        SpannableStringBuilder builder = new SpannableStringBuilder(xieyi_tv);

        ClickableSpan clickableSpan_one = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(LoginActivity.this, "??????????????????????????????");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        ClickableSpan clickableSpan_two = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(LoginActivity.this, "????????????????????????");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        ClickableSpan clickableSpan_three = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(LoginActivity.this, "??????????????????");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {
                super.updateDrawState(drawState);
                drawState.setColor(Color.parseColor("#0DACF6"));
                drawState.setUnderlineText(false);
                drawState.clearShadowLayer();
            }
        };
        builder.setSpan(clickableSpan_one, 7, 19, 0);
        builder.setSpan(clickableSpan_two, 20, 26, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(clickableSpan_three, 27, xieyi_tv.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvXieyi.setMovementMethod(LinkMovementMethod.getInstance());
        mTvXieyi.setHighlightColor(getResources().getColor(android.R.color.transparent));//????????????????????????
        mTvXieyi.setText(builder);


    }


    /*??????*/
    private void login(String name, String pwd, String type) {
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (loginBean.getRetCode() == 0) {
                    Constant.TOKEN = "bearer " + loginBean.getRetData().getToken();
                    LiveShareUtil.getInstance(LoginActivity.this).putToken("bearer " + loginBean.getRetData().getToken());
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
        showLoad();
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(tk), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERID, userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERNAME, userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERHEAD, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERCOVER, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put("user", new Gson().toJson(userInfoBean));//??????????????????
                    LiveShareUtil.getInstance(LoginActivity.this).putPower(userInfoBean.getRetData().getType());//????????????
//                    LiveShareUtil.getInstance(LoginActivity.this).putPower(1);//????????????
                    Log.e(TAG, "onSuccessListener: " + userInfoBean.getRetData().getId());
                    String interest = userInfoBean.getRetData().getInterest();
                    String mobile = userInfoBean.getRetData().getMobile();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                    } else {
                        isNeedUpmsg = false;
                    }
                    int type = userInfoBean.getRetData().getType();
                    if (TextUtils.isEmpty(mobile)) {
                        if (type != 4) {
                            isNeedUpMobile = true;
                        }
                    } else {
                        isNeedUpMobile = false;
                    }
                    if (isNeedUpMobile) {
                        Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                        intent.putExtra("other", 1);
                        intent.putExtra("needUp", isNeedUpmsg);
                        startActivity(intent);
                    } else if (isNeedUpmsg) {
                        statActivity(MsgInputActivity.class);
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
                if (throwable.errorType == HTTP_ERROR) {//????????????
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }

    /*???????????????sign*/
    private void thisLogin(String user_id, UserInfoBean.RetDataBean bean) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
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

    /*???????????????IM??????????????????IM*/
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
                    AVCallManager.getInstance().init(LoginActivity.this);
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                if (!isNeedUpmsg && !isNeedUpMobile) {
                    statActivity(MainActivity.class);
                    finish();
                }
                hideLoad();
            }

            @Override
            public void onLoginFailedListener(int errCode, String errInfo) {
                super.onLoginFailedListener(errCode, errInfo);
                hideLoad();
                ToastShow("????????????");
            }
        });
        mInstance.loginMLVB(user_id, bean.getNickname(), bean.getIco(), bean.getIco(), bean.getGender(), sign);
    }

    /**
     * ????????????????????????
     */
    private boolean checkPublishPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissions = new ArrayList<>();
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
//                permissions.add(Manifest.permission.READ_PHONE_STATE);
//            }
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
//                permissions.add(Manifest.permission.RECORD_AUDIO);
//            }
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
//                permissions.add(Manifest.permission.CAMERA);
//            }
//            if (permissions.size() != 0) {
//                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), TCConstants.WRITE_PERMISSION_REQ_CODE);
//                return false;
//            }
//        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case TCConstants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mPermission = true;
                break;
            default:
                break;
        }
    }

    /*
     * IM??????????????????
     * */
    private void goLogin() {
    /*    // ??????userSig??????
//        String userSig = GenerateTestUserSig.genTestUserSig(login_name);
        String userSig = Constantc.test_userSig;
        TUIKit.login(login_name, userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("????????????" + ", errCode = " + code + ", errInfo = " + desc);
                    }
                });
                Log.e(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
//                UserInfo.getInstance().setAutoLogin(true);
//                Intent intent = new Intent(LoginForDevActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                Log.e(TAG, "imLogin ????????????");
            }
        });*/
    }


    private String mToken, mCarrier;

    private void preLogin() {
        showLoad();
        RichAuth.getInstance().preLogin(LoginActivity.this, new PreLoginCallback() {
            @Override
            public void onPreLoginSuccess() {
                hideLoad();
                // ???????????????
                RichLogUtil.e("???????????????");
                Toast.makeText(LoginActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                getToken();
            }

            @Override
            public void onPreLoginFailure(String errorMsg) {
                hideLoad();
                // ?????????????????????????????????errorMsg
                // errorMsg????????????????????????????????????????????????????????????????????????
                JSONObject jsonObject = JSONObject.parseObject(errorMsg);
                Integer code = jsonObject.getInteger("code");
                if (code == 81010) {
                    Toast.makeText(LoginActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
                RichLogUtil.e("???????????????:" + errorMsg);
                hideLoad();
            }
        });
    }

    private void getToken() {
        //????????????
        UIConfigBuild.Builder configBuild = new UIConfigBuild.Builder();
        //????????????
        configBuild.setOauthLogo(R.drawable.ct_logo_image);
        //??????????????????
        configBuild.setRootBg(R.drawable.rich_oauth_root_bg);
        //?????????????????????
//                configBuild.setNavBgColor(0xff0085d0);
        configBuild.setNavBgColor(0xffffffff);
        //?????????????????????
        configBuild.setNavText("??????");
        //?????????????????????????????????
        configBuild.setNavBack(R.drawable.umcsdk_return_nowbg);
        //???????????????????????????
        configBuild.setNavTextColor(0xff000000);
        //??????????????????
        configBuild.setLoginBtnBg(R.drawable.selector_button_cucc);
        //??????????????????
        configBuild.setLoginBtnText("????????????????????????");
        //?????????????????????dp????????????
        configBuild.setLoginBtnWidth(400);
        //?????????????????????dp????????????
        configBuild.setLoginBtnHight(30);
        //????????????????????????
        configBuild.setLoginBtnTextColor(0xffffffff);
        //??????????????????????????????
        configBuild.setSwitchText("????????????????????????");
        //????????????????????????????????????
        configBuild.setSwitchTextColor(0xff000000);
        //???????????????????????????????????????
        configBuild.setSwitchIsHide(true);
        //????????????????????????????????????????????????
        configBuild.setProtocol("??????????????????", "https://www.baidu.com/");
        // ??????????????????????????????????????????????????????????????????????????????????????????
        configBuild.setPrivacyColor(0xff0085d0, 0xff666666);

        UIConfigBuild uiConfig = configBuild.build();

        RichAuth.getInstance().login(LoginActivity.this, new TokenCallback() {
            @Override
            public void onTokenSuccessResult(String token, String carrier) {
                //????????????token???????????????????????????token????????????,
                RichLogUtil.e("token:" + token);
//                Toast.makeText(LoginActivity.this, "token????????????:" + token, Toast.LENGTH_SHORT).show();
                mToken = token;  // ??????token??????????????????
                mCarrier = carrier;
                login(carrier, token, "phone");
            }

            @Override
            public void onTokenFailureResult(String error) {
                // ??????????????????
                RichLogUtil.e("onTokenFailureResult" + error);
                Log.e(TAG, "onTokenFailureResult:token????????????: " + error);
                Toast.makeText(LoginActivity.this, "????????????:" + error, Toast.LENGTH_SHORT).show();
                hideLoad();
            }

            @Override
            public void onOtherLoginWayResult() {
                // ???????????????????????????
                RichLogUtil.e("????????????????????????");
                Toast.makeText(LoginActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                hideLoad();
            }
        }, uiConfig);
    }

    public static final String[] MYPERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE,};

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, MYPERMISSIONS, 1010);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getMessage().equals("wx_login")) {
            login(msg.getAcc_token(), msg.getOpenid(), "wx");
        } else if (msg.getCode() == 1005) {
            ToastUtil.showToast(this, "??????????????????????????????!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (msg.getCode() == LIVE_UPDATE_CODE) {
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    private void loginWx() {
        //???????????????????????????????????????????????????
        if (!api.isWXAppInstalled()) {
            Toast.makeText(LoginActivity.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "live_login_request_please";
            api.sendReq(req);
        }

    }

    /*---------------------------??????????????????-------------------------------*/
    private void theOldLoginMlvb() {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig, long sdk_id) {
                if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
                    UserModel self = new UserModel();
                    self.userId = userid;
                    self.userSig = usersig;
                    ProfileManager.getInstance().setUserModel(self);
                    AVCallManager.getInstance().init(LoginActivity.this);
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                statActivity(MainActivity.class);
                finish();
                hideLoad();
            }
        });
        mInstance.loginMLVB(Constantc.test_USERID, Constantc.USER_NAME, Constantc.USER_UserAvatar, Constantc.USER_CoverPic, 1, Constantc.test_userSig);
    }


}
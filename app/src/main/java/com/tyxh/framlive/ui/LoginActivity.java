package com.tyxh.framlive.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.telephony.TelephonyManager;
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
    private boolean mPermission = false;               // 是否已经授权
    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;
    private boolean isNeedUpMobile = false;
    private String xieyi_tv = "我已阅读并同意《中国移动认证服务条款》和《用户协议》、《隐私政策》";
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
            if (Constantc.use_old) {
                showLoad();
                LiveShareUtil.getInstance(LoginActivity.this).putPower(2);//用户类型
                theOldLoginMlvb();
                return;
            }
            mLoginImgv.setVisibility(View.VISIBLE);
            getUserInfo(token);
        }

    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela, R.id.ll_zi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis://一键登录
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    preLogin();
                } else {
                    ToastShow("请阅读并勾选协议");
                }
                break;
            case R.id.login_loginother://其它手机号登录
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(this, BindPhoneActivity.class);
                    intent.putExtra("other", 2);
                    startActivity(intent);
                } else {
                    ToastShow("请阅读并勾选协议");
                }
                break;
            case R.id.ll_wx://微信登录
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    loginWx();
                } else {
                    ToastShow("请阅读并勾选协议");
                }
                break;
            case R.id.ll_zi:
//                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                startActivity(new Intent(this, ChildLoginActivity.class));
//                } else {
//                    ToastShow("请阅读并勾选协议");
//                }
                break;
            case R.id.login_rela://协议
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    mLoginImgv.setVisibility(View.GONE);
                } else {
                    mLoginImgv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /*设置提现规则中的两个特殊文字*/
    private void setTextContent() {
        SpannableStringBuilder builder = new SpannableStringBuilder(xieyi_tv);

        ClickableSpan clickableSpan_one = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebVActivity.startMe(LoginActivity.this, "中国移动认证服务条款");
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
                WebVActivity.startMe(LoginActivity.this, "边框心理用户协议");
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
                WebVActivity.startMe(LoginActivity.this, "用户隐私条款");
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
        mTvXieyi.setHighlightColor(getResources().getColor(android.R.color.transparent));//不设置会有背景色
        mTvXieyi.setText(builder);


    }


    /*登录*/
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

    /*获取用户信息*/
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
                    LiveShareUtil.getInstance(LoginActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    LiveShareUtil.getInstance(LoginActivity.this).putPower(userInfoBean.getRetData().getType());//用户类型
//                    LiveShareUtil.getInstance(LoginActivity.this).putPower(1);//用户类型
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
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);



        /*LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                Log.e(TAG, "onSuccessListener: " + result.toString());
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERID, userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERNAME, userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERHEAD, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERCOVER, userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    LiveShareUtil.getInstance(LoginActivity.this).putPower(userInfoBean.getRetData().getType());//用户类型
//                    LiveShareUtil.getInstance(LoginActivity.this).putPower(1);//用户类型
                    Log.e(TAG, "onSuccessListener: " + userInfoBean.getRetData().getId());
                    thisLogin(userInfoBean.getRetData().getId(), userInfoBean.getRetData());
                    String interest = userInfoBean.getRetData().getInterest();
                    String mobile = userInfoBean.getRetData().getMobile();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                    } else {
                        isNeedUpmsg = false;
                    }
                    if (TextUtils.isEmpty(mobile)) {
                        isNeedUpMobile = true;
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
                } else {
                    ToastShow(userInfoBean.getRetMsg());
                    hideLoad();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });*/
    }

    /*获取直播的sign*/
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
                ToastShow("登录失败");
            }
        });
        mInstance.loginMLVB(user_id, bean.getNickname(), bean.getIco(), bean.getIco(), bean.getGender(), sign);
    }

    /**
     * 动态权限检查相关
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
     * IM那套库的登录
     * */
    private void goLogin() {
    /*    // 获取userSig函数
//        String userSig = GenerateTestUserSig.genTestUserSig(login_name);
        String userSig = Constantc.test_userSig;
        TUIKit.login(login_name, userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败" + ", errCode = " + code + ", errInfo = " + desc);
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
                Log.e(TAG, "imLogin 登录成功");
            }
        });*/
    }

    /*一键登录*/
    private void toYjLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                String telphone = tm.getLine1Number();//获取本机号码
                if (!TextUtils.isEmpty(telphone)) {
//                    mLoginPhonenum.setText(telphone);
                } else {
                    Log.e(TAG, "getPhoneNum:未获取到手机号 ");
                }
                return;
            } else {
                requestPermission();
            }
        }


    }

    private String mToken, mCarrier;

    private void preLogin() {
        showLoad();
        RichAuth.getInstance().preLogin(LoginActivity.this, new PreLoginCallback() {
            @Override
            public void onPreLoginSuccess() {
                hideLoad();
                // 预登录成功
                RichLogUtil.e("预登录成功");
                Toast.makeText(LoginActivity.this, "预登录成功", Toast.LENGTH_SHORT).show();
                getToken();
            }

            @Override
            public void onPreLoginFailure(String errorMsg) {
                hideLoad();
                // 预登录失败，错误信息为errorMsg
                // errorMsg为“用户未使用流量进行登录，不满足一键登录条件”
                JSONObject jsonObject = JSONObject.parseObject(errorMsg);
                Integer code = jsonObject.getInteger("code");
                if (code == 81010) {
                    Toast.makeText(LoginActivity.this, "请开启移动网络", Toast.LENGTH_SHORT).show();
                }
                RichLogUtil.e("预登录失败:" + errorMsg);
                hideLoad();
            }
        });
    }

    private void getToken() {
        //一键登录
        UIConfigBuild.Builder configBuild = new UIConfigBuild.Builder();
        //应用图标
        configBuild.setOauthLogo(R.drawable.ct_logo_image);
        //授权页面背景
        configBuild.setRootBg(R.drawable.rich_oauth_root_bg);
        //顶部导航栏背景
//                configBuild.setNavBgColor(0xff0085d0);
        configBuild.setNavBgColor(0xffffffff);
        //顶部导航栏标题
        configBuild.setNavText("登录");
        //顶部导航栏返回按钮资源
        configBuild.setNavBack(R.drawable.umcsdk_return_nowbg);
        //顶部导航栏标题颜色
        configBuild.setNavTextColor(0xff000000);
        //登陆按钮背景
        configBuild.setLoginBtnBg(R.drawable.selector_button_cucc);
        //登陆按钮文本
        configBuild.setLoginBtnText("本机号码一键登录");
        //登陆按钮宽度（dp为单位）
        configBuild.setLoginBtnWidth(400);
        //登陆按钮高度（dp为单位）
        configBuild.setLoginBtnHight(30);
        //登陆按钮文本颜色
        configBuild.setLoginBtnTextColor(0xffffffff);
        //其他登陆方式按钮文本
        configBuild.setSwitchText("使用其他登录方式");
        //其他登陆方式按钮文本颜色
        configBuild.setSwitchTextColor(0xff000000);
        //其他登陆方式按钮显示及隐藏
        configBuild.setSwitchIsHide(true);
        //新增协议，协议名称及协议点击地址
        configBuild.setProtocol("统一认证协议", "https://www.baidu.com/");
        // 协议字体颜色，第一个参数为协议颜色，第二个为协议其他字体颜色
        configBuild.setPrivacyColor(0xff0085d0, 0xff666666);

        UIConfigBuild uiConfig = configBuild.build();

        RichAuth.getInstance().login(LoginActivity.this, new TokenCallback() {
            @Override
            public void onTokenSuccessResult(String token, String carrier) {
                //成功获取token，运营商，可通过此token获取号码,
                RichLogUtil.e("token:" + token);
//                Toast.makeText(LoginActivity.this, "token获取成功:" + token, Toast.LENGTH_SHORT).show();
                mToken = token;  // 百纳token两分钟有效。
                mCarrier = carrier;
                login(carrier, token, "phone");
            }

            @Override
            public void onTokenFailureResult(String error) {
                // 获取失败信息
                RichLogUtil.e("onTokenFailureResult" + error);
                Log.e(TAG, "onTokenFailureResult:token获取失败: " + error);
                Toast.makeText(LoginActivity.this, "获取失败:" + error, Toast.LENGTH_SHORT).show();
                hideLoad();
            }

            @Override
            public void onOtherLoginWayResult() {
                // 点击了其他方式登录
                RichLogUtil.e("使用其他方式登录");
                Toast.makeText(LoginActivity.this, "其他登录方式", Toast.LENGTH_SHORT).show();
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
            ToastUtil.showToast(this, "登录过期，请重新登录!");
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

    private void loginWx() {
        //先判断用户手机是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(LoginActivity.this, "您的设备未安装微信客户端", Toast.LENGTH_SHORT).show();
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "live_login_request_please";
            api.sendReq(req);
        }

    }

    private void getPhoneNum() {
        //获取手机号码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//                String telphone = tm.getLine1Number();//获取本机号码
//                if (!TextUtils.isEmpty(telphone)) {
//                    mLoginPhonenum.setText(telphone);
//                } else {
//                    Log.e(TAG, "getPhoneNum:未获取到手机号 ");
//                }
                return;
            }
        }

    }

    /*---------------------------原可连麦数据-------------------------------*/
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
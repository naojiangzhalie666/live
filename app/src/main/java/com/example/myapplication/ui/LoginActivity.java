package com.example.myapplication.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.bean.LoginBean;
import com.example.myapplication.bean.SignBean;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.example.xzb.Constantc;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.login.TCUserMgr;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.liteav.AVCallManager;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.myapplication.base.Constant.LIVE_UPDATE_CODE;
import static com.example.myapplication.base.LiveApplication.api;

public class LoginActivity extends LiveBaseActivity {


    @BindView(R.id.login_phonenum)
    TextView mLoginPhonenum;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;
    private boolean mPermission = false;               // 是否已经授权
    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mPermission = checkPublishPermission();
        EventBus.getDefault().register(this);
        getPhoneNum();
    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis://一键登录
                if(Constantc.use_old){
                    theOldLoginMlvb();
                    return;
                }
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    getUserInfo();//暂时使用缓存
                    LiveShareUtil.getInstance(LoginActivity.this).putPower(2);//用户类型
                    ToastShow("一键登录");
                } else {
                    ToastShow("请阅读并勾选协议");
                }
                break;
            case R.id.login_loginother://其它手机号登录
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    statActivity(BindPhoneActivity.class);
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
            case R.id.login_rela://协议
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
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (loginBean.getRetCode() == 0) {
                    Constant.TOKEN = "bearer " + loginBean.getRetData().getToken();
                    LiveShareUtil.getInstance(LoginActivity.this).putToken("bearer " + loginBean.getRetData().getToken());
                    getUserInfo();
                } else {
                    ToastShow(loginBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {

            }
        });
    }

    /*获取用户信息*/
    private void getUserInfo() {
        showLoad();
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERID,userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERNAME,userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERHEAD,userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERCOVER,userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(LoginActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    LiveShareUtil.getInstance(LoginActivity.this).putPower(userInfoBean.getRetData().getType());//用户类型
//                    LiveShareUtil.getInstance(LoginActivity.this).putPower(1);//用户类型
                    thisLogin(userInfoBean.getRetData());
                    String interest = userInfoBean.getRetData().getInterest();
                    if (TextUtils.isEmpty(interest)) {
                        isNeedUpmsg = true;
                        statActivity(MsgInputActivity.class);
                    } else {
                        isNeedUpmsg = false;
                    }

                } else {
                    ToastShow(userInfoBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*获取直播的sign*/
    private void thisLogin(UserInfoBean.RetDataBean bean) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(LoginActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
                    gotLogin(bean,signBean.getRetData());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*登录直播的IM并配置聊天的IM*/
    private void gotLogin(UserInfoBean.RetDataBean bean,String sign) {
        hideLoad();
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
            }
        });
        mInstance.loginMLVB(bean.getId(),bean.getNickname(),bean.getIco(),bean.getIco(),bean.getGender(),sign);
        if (!isNeedUpmsg) {
            statActivity(MainActivity.class);
            finish();
        }
    }

    /**
     * 动态权限检查相关
     */
    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), TCConstants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }

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
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "live_login_request_please";
        api.sendReq(req);
    }

    private void getPhoneNum() {
        //获取手机号码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                String telphone = tm.getLine1Number();//获取本机号码
                if (!TextUtils.isEmpty(telphone)) {
                    mLoginPhonenum.setText(telphone);
                } else {
                    Log.e(TAG, "getPhoneNum:未获取到手机号 ");
                }
                return;
            }
        }

    }
    /*---------------------------原可连麦数据-------------------------------*/
    private void theOldLoginMlvb(){
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
            }
        });
        mInstance.loginMLVB(Constantc.test_USERID,Constantc.USER_NAME, Constantc.USER_UserAvatar,Constantc.USER_CoverPic,1,Constantc.test_userSig);
    }


}
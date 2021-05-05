package com.example.myapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.bean.LoginBean;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.example.xzb.Constantc;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.login.TCUserMgr;
import com.google.gson.Gson;
import com.superc.yyfflibrary.base.BaseActivity;
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

import static com.example.myapplication.base.LiveApplication.api;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_phonenum)
    TextView mLoginPhonenum;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;
    private boolean mPermission = false;               // 是否已经授权
    private String login_name = Constantc.test_USERID;
    private TCUserMgr mInstance;

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
    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis:
                Constant.IS_SHENHEING = false;
                LiveShareUtil.getInstance(LoginActivity.this).putPower("1");
                thisLogin();
                login("liangtiandong", "123456", "");
                break;
            case R.id.login_loginother:
                ToastShow("其它手机号登录");
                statActivity(BindPhoneActivity.class);
                break;
            case R.id.ll_wx:
                loginWx();
                break;
            case R.id.login_rela:
                if (mLoginImgv.getVisibility() == View.VISIBLE) {
                    mLoginImgv.setVisibility(View.GONE);
                } else {
                    mLoginImgv.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private void login(String name, String pwd, String type) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                thisLogin();
            }

            @Override
            public void onErrorLIstener(String error) {

            }
        });
    }

    private void loginWx() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "live_login_request_please";
        api.sendReq(req);

    }

    private String user_openId, accessToken;
    private boolean is_wechat;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
       /* is_wechat = intent.getBooleanExtra("wechat", false);
        user_openId = intent.getStringExtra("openId");
        accessToken = intent.getStringExtra("accessToken");
        if (is_wechat) {
            login(accessToken, user_openId, "wx");
        }*/
    }

    private void thisLogin() {
        if (mLoginImgv.getVisibility() == View.VISIBLE) {
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
            mInstance.loginMLVB();
            statActivity(MainActivity.class);
            finish();
        } else {
            ToastShow("请阅读并勾选协议");
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
    public void onEventMsg(EventMessage msg){
        if(msg.getMessage().equals("wx_login")){
            login(msg.getAcc_token(),msg.getOpenid(), "wx");
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


}
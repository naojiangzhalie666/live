package com.example.myapplication.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.GenerateTestUserSig;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.TCGlobalConfig;
import com.example.xzb.utils.TCHTTPMgr;
import com.example.xzb.utils.TCUtils;
import com.example.xzb.utils.login.TCUserMgr;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ShareUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_phonenum)
    TextView mLoginPhonenum;
    @BindView(R.id.login_imgv)
    ImageView mLoginImgv;
    private boolean mPermission = false;               // 是否已经授权

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mPermission = checkPublishPermission();
    }


    @OnClick({R.id.login_loginthis, R.id.login_loginother, R.id.ll_wx, R.id.login_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginthis:
                ShareUtil.getInstance(this).put("power",0);//普通
                thisLogin();
                break;
            case R.id.login_loginother:
                ToastShow("其它手机号登录");
                statActivity(BindPhoneActivity.class);
                finish();
                break;
            case R.id.ll_wx:
                ShareUtil.getInstance(this).put("power",1);//咨询师
                thisLogin();
                ToastShow("微信登录");
                break;
            case R.id.login_rela:
                if(mLoginImgv.getVisibility() == View.VISIBLE){
                    mLoginImgv.setVisibility(View.GONE);
                }else{
                    mLoginImgv.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private void thisLogin() {
        if (mLoginImgv.getVisibility() == View.VISIBLE) {
            checkLogin();
            statActivity(MainActivity.class);
            finish();
        } else {
            ToastShow("请阅读并勾选协议");
        }
    }
    /*
    * 直播那套lib的登录
    * */
    private void checkLogin() {
        if (TCUtils.isNetworkAvailable(this)) {
            //返回true表示存在本地缓存，进行登录操作，显示loadingFragment
            if (TCUserMgr.getInstance().hasUser()) {
                TCUserMgr.getInstance().autoLogin(new TCHTTPMgr.Callback() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        Log.e(TAG, "onSuccess:自动登录成功 " );
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.e(TAG, "onSuccess:自动登录失败 " );
                    }
                });
            } else if (TextUtils.isEmpty(TCGlobalConfig.APP_SVR_URL)) {

                if (TCUtils.isNetworkAvailable(this)) {
                    login("yangfan", "");
                }
            }
        }
    }
    private void login(String username, String password) {
        final TCUserMgr tcLoginMgr = TCUserMgr.getInstance();
        String sign = GenerateTestUserSig.genTestUserSig("yangfan");
        tcLoginMgr.login(username, sign, new TCHTTPMgr.Callback() {
            @Override
            public void onSuccess(JSONObject data) {
                ToastShow("登录成功");
            }

            @Override
            public void onFailure(int code, final String msg) {
                ToastShow(msg);
            }
        });
    }

    /*
     * IM那套库的登录
     * */
    private void goLogin(){
        // 获取userSig函数
        String userSig = GenerateTestUserSig.genTestUserSig("yangfan");
        TUIKit.login("yangfan", userSig, new IUIKitCallBack() {
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
        });
    }

    /**
     *   动态权限检查相关
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
            } if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
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



}
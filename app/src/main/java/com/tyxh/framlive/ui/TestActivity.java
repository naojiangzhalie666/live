package com.tyxh.framlive.ui;

import android.Manifest;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.rich.oauth.callback.PreLoginCallback;
import com.rich.oauth.callback.TokenCallback;
import com.rich.oauth.core.RichAuth;
import com.rich.oauth.core.UIConfigBuild;
import com.rich.oauth.util.RichLogUtil;
import com.rich.oauth.util.SHA256Util;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.utils.TitleUtils;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TestActivity extends LiveBaseActivity {


    @BindView(R.id.imageView29)
    ImageView mImageView29;
    @BindView(R.id.imageView30)
    ImageView mImageView30;
    @BindView(R.id.imageView31)
    ImageView mImageView31;
    @BindView(R.id.imageView28)
    ImageView mImageView28;
    @BindView(R.id.imageView27)
    ImageView mImageView27;
    @BindView(R.id.test_phonenum)
    TextView mtv_phoneNum;
    @BindView(R.id.test_token)
    EditText mtv_phoneToken;
    private RoundedCorners mRoundedCorners;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        ButterKnife.bind(this);
        mRoundedCorners = new RoundedCorners(15);
        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(new CenterCrop(), mRoundedCorners)).into(mImageView27);
        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).fitCenter()).into(mImageView28);
        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).centerInside()).into(mImageView29);
//        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners).).into(mImageView30);
//        Glide.with(this).load(R.drawable.mine_bg).apply(new RequestOptions().transform(mRoundedCorners)).into(mImageView31);
        mImageView29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preLogin();
            }
        });

        requestPermission();
    }

    public static final String[] MYPERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE,};

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(TestActivity.this, MYPERMISSIONS, 1010);
        }
    }

    private String mToken, mCarrier;

    private void preLogin() {
        RichAuth.getInstance().preLogin(TestActivity.this, new PreLoginCallback() {
            @Override
            public void onPreLoginSuccess() {
                // 预登录成功
                RichLogUtil.e("预登录成功");
                Toast.makeText(TestActivity.this, "预登录成功", Toast.LENGTH_SHORT).show();
                getToken();
            }

            @Override
            public void onPreLoginFailure(String errorMsg) {
                // 预登录失败，错误信息为errorMsg
                // errorMsg为“用户未使用流量进行登录，不满足一键登录条件”
                RichLogUtil.e("预登录失败:" + errorMsg);
                Toast.makeText(TestActivity.this, "预登录失败：" + errorMsg, Toast.LENGTH_SHORT).show();
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

        RichAuth.getInstance().login(TestActivity.this, new TokenCallback() {
            @Override
            public void onTokenSuccessResult(String token, String carrier) {
                //成功获取token，运营商，可通过此token获取号码,
                RichLogUtil.e("token:" + token);
                Toast.makeText(TestActivity.this, "token获取成功:" + token, Toast.LENGTH_SHORT).show();
                mToken = token;  // 百纳token两分钟有效。
                mCarrier = carrier;
                Log.e(TAG, "onTokenSuccessResult:token " + mToken + " mcarrier:" + carrier);
                mtv_phoneNum.setText("token:"+token  +" carrier:"+carrier);
                mtv_phoneToken.setText(token);
//                getPhoneNumberFromTencent(token, carrier);
            }

            @Override
            public void onTokenFailureResult(String error) {
                // 获取失败信息
                RichLogUtil.e("onTokenFailureResult" + error);
                Log.e(TAG, "onTokenFailureResult:token获取失败: " + error);
                Toast.makeText(TestActivity.this, "获取失败:" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOtherLoginWayResult() {
                // 点击了其他方式登录
                RichLogUtil.e("使用其他方式登录");
                Toast.makeText(TestActivity.this, "其他登录方式", Toast.LENGTH_SHORT).show();
            }
        }, uiConfig);
    }

    private void getPhoneNumberFromTencent(String token, String carrier) {
     /*   PhoneNumberNet net = new PhoneNumberNet(this);
        net.setOnPhoneNumberListener(new PhoneNumberNet.OnPhoneNumberListener() {
            @Override
            public void onPhoneNumberSuccess(final String phoneNumber) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestActivity.this, "取号返回:" + phoneNumber, Toast.LENGTH_SHORT).show();
                        mTvToken.setText("取号返回:" + phoneNumber);
                    }
                });
            }

            @Override
            public void onPhoneNumberFailure(String error) {
                mTvToken.setText("获取号码失败:" + error);
                Toast.makeText(TestActivity.this, "获取号码失败:" + error, Toast.LENGTH_SHORT).show();
                RichLogUtil.e("获取号码失败: " + error);
            }
        });*/


        // 现网
        String NET_TENCENT_URL = "https://yun.tim.qq.com/v5/rapidauth/validate";
        // 测试
        //  String NET_TENCENT_URL_TEST = "https://test.tim.qq.com/v5/rapidauth/validate";

        // 腾讯云申请
        String sdkappid = "1400295957";
        //注意： 验证token接口用于第三方业务服务器和腾讯云服务之间的交互，其中的appkey需要业务方放在服务器上高度保密。这里只用于模拟生成sig
        String appKey = "6f2ac4586e315335f602efaeeced74b9";
        // 需要与生成sig的随机数一致
        String randomNumber = String.valueOf(new Random().nextInt(999999999));
        // 需要与生成sig的时间戳一致
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        // sig由开发者服务器生成，demo这里只是模拟sig生成
        String sig = SHA256Util.getSHA256StrJava("appkey=" + appKey + "&random=" + randomNumber + "&time=" + time);

//        net.getPhoneNumber(NET_TENCENT_URL, token, carrier, sdkappid, sig, randomNumber, time);
        Map<String, Object> params = new HashMap<>();
        params.put("sig", sig);
        params.put("carrier", carrier);
        params.put("token", token);
        params.put("time", time);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getvalidata(LiveApplication.APP_ID, randomNumber, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

}

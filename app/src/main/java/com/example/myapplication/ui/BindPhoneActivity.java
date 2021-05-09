package com.example.myapplication.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.base.LiveBaseActivity;
import com.example.myapplication.bean.BaseBean;
import com.example.myapplication.bean.LoginBean;
import com.example.myapplication.bean.SignBean;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.example.xzb.utils.login.TCUserMgr;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.liteav.AVCallManager;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends LiveBaseActivity {

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
    @BindView(R.id.bindphone_yzm)
    EditText mBindphoneYzm;
    @BindView(R.id.bindphone_sub)
    Button mBindphoneSub;

    private boolean havget_yzm = false;
    private TCUserMgr mInstance;
    private boolean isNeedUpmsg = true;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);


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
                    if(TextUtils.isEmpty(mBindphonePhonenum.getText().toString())||mBindphonePhonenum.getText().toString().length()!=11){
                        ToastShow("请输入正确的手机号");
                        return;
                    }
                    getYzm(mBindphonePhonenum.getText().toString());
                }else{
                    if(TextUtils.isEmpty(mBindphoneYzm.getText().toString())){
                        ToastShow("验证码不能为空");
                        return;
                    }
                    toLogin(mBindphonePhonenum.getText().toString(),mBindphoneYzm.getText().toString(),"sms");
                }
                break;
        }
    }
    /*获取验证码*/
    private void getYzm(String phone) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().sendMsg( phone), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0){
                    mBindphoneSub.setText("确认");
                    mBindphoneNote.setText("请输入验证码");
                    mBindphoneRela.setVisibility(View.INVISIBLE);
                    mBindphoneYzm.setVisibility(View.VISIBLE);
                    havget_yzm = true;
                    ToastShow(baseBean.getRetData());
                }else{
                    ToastShow(baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*登录*/
    private void toLogin(String name,String pwd,String type){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().login(name, pwd, type), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                LoginBean loginBean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (loginBean.getRetCode() == 0) {
                    Constant.TOKEN = "bearer " + loginBean.getRetData().getToken();
                    LiveShareUtil.getInstance(BindPhoneActivity.this).putToken("bearer " + loginBean.getRetData().getToken());
                    getUserInfo();
                }else{
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
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERID,userInfoBean.getRetData().getId());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERNAME,userInfoBean.getRetData().getNickname());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERHEAD,userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERCOVER,userInfoBean.getRetData().getIco());
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                    LiveShareUtil.getInstance(BindPhoneActivity.this).putPower(userInfoBean.getRetData().getType());//用户类型
                    thisLogin(userInfoBean.getRetData());
                    String interest = userInfoBean.getRetData().getInterest();
                    if(TextUtils.isEmpty(interest)){
                        isNeedUpmsg = true;
                        statActivity(MsgInputActivity.class);
                    }else{
                        isNeedUpmsg =false;
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
                    LiveShareUtil.getInstance(BindPhoneActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
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
            }
        });
        mInstance.loginMLVB(bean.getId(),bean.getNickname(),bean.getIco(),bean.getIco(),bean.getGender(),sign);
        if (!isNeedUpmsg) {
            statActivity(MainActivity.class);
            finish();
        }
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

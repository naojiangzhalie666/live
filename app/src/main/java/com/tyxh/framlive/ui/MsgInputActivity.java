package com.tyxh.framlive.ui;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
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
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.SignBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.tuikit.AVCallManager;
import com.tyxh.framlive.fragment.NewFirstFragment;
import com.tyxh.framlive.fragment.NewLastFragment;
import com.tyxh.framlive.fragment.NewSecondFragment;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.utils.login.TCUserMgr;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.base.Constant.LIVE_UPDATE_CODE;

public class MsgInputActivity extends LiveBaseActivity {

    @BindView(R.id.msginput_next)
    Button mMsginputNext;
    @BindView(R.id.msginput_num)
    TextView mMsginputNum;
    private NewFirstFragment mNewFirstFragment;
    private NewSecondFragment mNewSecondFragment;
    private NewLastFragment mNewLastFragment;
    private int now_num = 1;
    private int sex;//1 男 2女
    private String old_nian = "";
    private String old_pos = "";
    private String msg_last = "";
    private String msg_lastId = "";
    private TCUserMgr mInstance;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_msg_input;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mNewFirstFragment = new NewFirstFragment();
        mNewSecondFragment = new NewSecondFragment();
        mNewLastFragment = new NewLastFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.msginput_fram, mNewFirstFragment).commit();
        getSupportFragmentManager().beginTransaction().show(mNewFirstFragment);


    }

    @OnClick(R.id.msginput_next)
    public void onClick() {
        if (now_num == 1) {
            if (mNewFirstFragment.sex == 0) {
                ToastShow("请选择性别");
                return;
            }
            mMsginputNum.setText("2/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram, mNewSecondFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewSecondFragment);
            sex = mNewFirstFragment.sex;
            now_num += 1;
        } else if (now_num == 2) {
            if (TextUtils.isEmpty(mNewSecondFragment.old_nian)) {
                ToastShow("请选择年龄");
                return;
            }
            mMsginputNum.setText("3/3");
            getSupportFragmentManager().beginTransaction().replace(R.id.msginput_fram, mNewLastFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mNewLastFragment);
            old_nian = mNewSecondFragment.old_nian;
            old_pos = mNewSecondFragment.click_pos;
            mMsginputNext.setText("开启心灵之旅");
            now_num += 1;
        } else {
            if (TextUtils.isEmpty(mNewLastFragment.msg_last)) {
                ToastShow("请至少选择一项");
                return;
            } else {
                String[] split = mNewLastFragment.msg_last.split(",");
                if (split.length > 5) {
                    ToastShow("爱好至多选择5个");
                    return;
                }
            }
            msg_last = mNewLastFragment.msg_last;
            msg_lastId = mNewLastFragment.msg_lastId;
            Log.i(TAG, "onClick:sex " + sex + " old_nian: " + old_nian + " msg_Last: " + msg_lastId);
            toUpdateMsg();
        }

    }

    /*put请求上传基本信息*/
    private void toUpdateMsg() {
        showLoad();
        Map<String, Object> map = new HashMap<>();
        map.put("age", old_pos);
        map.put("gender", sex + "");
        map.put("ico", "");
        map.put("id", LiveShareUtil.getInstance(this).get(LiveShareUtil.APP_USERID, ""));
        map.put("interest", msg_lastId);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().updateUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getUserInfo();
                } else {
                    ToastShow(baseBean.getRetMsg());
                    hideLoad();
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error); hideLoad();
            }
        });
    }

    /*获取用户信息*/
    private void getUserInfo() {
        EventBus.getDefault().post(new EventMessage(LIVE_UPDATE_CODE));
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new CommonObserver<UserInfoBean>() {
            @Override
            public void onResult(UserInfoBean userInfoBean) {
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(MsgInputActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                }else{
                    hideLoad();
                    ToastShow(userInfoBean.getRetMsg());
                }
                thisLogin(userInfoBean.getRetData());
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



       /* LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserInfo(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                UserInfoBean userInfoBean = new Gson().fromJson(result.toString(), UserInfoBean.class);
                if (userInfoBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(MsgInputActivity.this).put("user", new Gson().toJson(userInfoBean));//保存用户信息
                }else{
                    hideLoad();
                    ToastShow(userInfoBean.getRetMsg());
                }
                thisLogin(userInfoBean.getRetData());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });*/
    }

    /*获取直播的sign*/
    private void thisLogin(UserInfoBean.RetDataBean bean) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(MsgInputActivity.this).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
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

    /*登录直播的IM并配置聊天的IM*/
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
                    AVCallManager.getInstance().init(MsgInputActivity.this);
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                EventBus.getDefault().post(new EventMessage(LIVE_UPDATE_CODE));
                statActivity(MainActivity.class);
                finish();
                hideLoad();
            }

            @Override
            public void onLoginFailedListener(int errCode, String errInfo) {
                super.onLoginFailedListener(errCode, errInfo);
                ToastShow("登录失败");
                hideLoad();
            }
        });
        mInstance.loginMLVB(bean.getId(), bean.getNickname(), bean.getIco(), bean.getIco(), bean.getGender(), sign);

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

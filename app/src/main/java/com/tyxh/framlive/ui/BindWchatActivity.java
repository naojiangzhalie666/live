package com.tyxh.framlive.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tyxh.framlive.base.LiveApplication.api;

public class BindWchatActivity extends LiveBaseActivity {

    @BindView(R.id.bind_wchat_head)
    ImageView mBindWchatHead;
    @BindView(R.id.bind_wchat_name)
    TextView mBindWchatName;
    private UserInfoBean mUserInfo;
    private String user_ico = "";


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_bind_wchat;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(true, this);
        ButterKnife.bind(this);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        if (mUserInfo != null) {
            user_ico = mUserInfo.getRetData().getIco();
            mBindWchatName.setText(mUserInfo.getRetData().getNickname());
        }
        RoundedCorners roundedCorners = new RoundedCorners(16);
        Glide.with(this).load(user_ico).apply(new RequestOptions().transform(new CenterCrop(), roundedCorners)).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mBindWchatHead);


    }

    @OnClick({R.id.imgv_back, R.id.bind_wchat_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.bind_wchat_bind:
                loginWx();
                break;
        }
    }

    private void loginWx() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "live_login_request_please";
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getMessage().equals("wx_login")) {
            toBindWxTo(msg.getOpenid());
//            login(msg.getAcc_token(), msg.getOpenid(), "wx");
        } else if (msg.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


    }

    /*提交服务器openid进行绑定*/
    private void toBindWxTo(String openid) {
        Log.i(TAG, "微信 + Openid= " + openid);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().bindWxchat(token, openid), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() == 0){
                    mUserInfo.getRetData().setUnionID(openid);
                    LiveShareUtil.getInstance(BindWchatActivity.this).put("user", new Gson().toJson(mUserInfo));//保存用户信息
                    setResult(RESULT_OK);
                }
                ToastShow(baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

}

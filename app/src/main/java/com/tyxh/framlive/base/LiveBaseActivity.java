package com.tyxh.framlive.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.LoadDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

public abstract class LiveBaseActivity extends BaseActivity {
    public LoadDialog mLoadDialog;
    public String token = "";
    public UserInfoBean user_Info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLoadDialog = new LoadDialog(this);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info =LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        user_Info =LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public void showLoad(){
        if(mLoadDialog!=null&&!mLoadDialog.isShowing())
            mLoadDialog.show();
    }
     public void hideLoad(){
        if(mLoadDialog!=null)
            mLoadDialog.dismiss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg){
        if(msg.getCode() == 1005){
            ToastUtil.showToast(this,"登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}

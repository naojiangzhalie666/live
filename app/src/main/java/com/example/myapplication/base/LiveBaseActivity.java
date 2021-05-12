package com.example.myapplication.base;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.pop_dig.LoadDialog;
import com.example.myapplication.ui.LoginActivity;
import com.example.myapplication.utils.LiveShareUtil;
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
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
        mLoadDialog = new LoadDialog(this);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info =LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public void showLoad(){
        if(mLoadDialog!=null)
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

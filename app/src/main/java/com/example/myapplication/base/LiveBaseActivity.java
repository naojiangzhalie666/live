package com.example.myapplication.base;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.ui.MailListActivity;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

public abstract class LiveBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg){
        if(msg.getCode() == 1005){
            ToastUtil.showToast(this,"登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, MailListActivity.class));
        }

    }
}

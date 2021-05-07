package com.example.myapplication.pop_dig;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;

import com.example.myapplication.R;
import com.example.myapplication.base.Constant;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.chat.BaseHelfActivity;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.chat.ChathelfFragment;
import com.example.myapplication.ui.LoginActivity;
import com.ljy.devring.util.DensityUtil;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.constraintlayout.widget.Constraints;
import butterknife.ButterKnife;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

public class ChathelfActivity extends BaseHelfActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChathelfFragment mChatFragment;
    private ChatInfo mChatInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chathelf);
        ButterKnife.bind(this);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(this,400));
        getWindow().setGravity(Gravity.BOTTOM);
        chat(getIntent());
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        chat(intent);
    }


    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
            startSplashActivity(null);
            return;
        }

//        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
//        if (bean != null) {
//            mChatInfo = new ChatInfo();
//            mChatInfo.setType(bean.chatType);
//            mChatInfo.setId(bean.sender);
//            bundle.putSerializable(Constant.CHAT_INFO, mChatInfo);
//            Log.i(TAG, "offline mChatInfo: " + mChatInfo);
//        } else {
        mChatInfo = (ChatInfo) bundle.getSerializable(Constant.CHAT_INFO);
        if (mChatInfo == null) {
            startSplashActivity(null);
            return;
        }
//        }

        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            mChatFragment = new ChathelfFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        } else {
            startSplashActivity(bundle);
        }
    }

    private void startSplashActivity(Bundle bundle) {
//        Intent intent = new Intent(ChatActivity.this, SplashActivity.class);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        startActivity(intent);
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventmsg(EventMessage msg){
        if(msg.getMessage().equals("chathelf_finish")){
            finish();
        }else if(msg.getCode() == 1005){
            ToastUtil.showToast(this,"登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

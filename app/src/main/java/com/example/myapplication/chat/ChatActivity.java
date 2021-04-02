package com.example.myapplication.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseChatActivity;
import com.example.myapplication.base.Constant;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

public class ChatActivity extends BaseChatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void init() {
        chat(getIntent());
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
            mChatFragment = new ChatFragment();
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
}

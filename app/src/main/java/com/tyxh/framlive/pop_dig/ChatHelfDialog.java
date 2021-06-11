package com.tyxh.framlive.pop_dig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyxh.framlive.R;
import com.tyxh.framlive.chat.ChathelfFragment;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

public class ChatHelfDialog extends Dialog {


    private Bundle mBundle ;
    private ChathelfFragment mChatFragment;
    private ChatInfo mChatInfo;
    private Context mContext;

    private static final String TAG = "ChatFragment";
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private TextView mtv_title;
    private ImageView mimgv_back;
    private List<String> mStringList;
    private String[] mStrings = new String[]{"情感分析", "情感分析", "第三者问题", "未成年人心理", "未成年人心理"};
    private ServiceDialog mServiceDialog;
    private List<String> mService_strs;
    private SjbgDialog mSjbgDialog;
    private List<String> mSjbg_strs;
    private boolean is_user = false;

    public ChatHelfDialog(@NonNull Context context,Bundle bundle) {
        super(context);
        mContext =context;
        mBundle = bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chathelf);
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
//        init();

    }

    /*private void init(){
        if (mBundle == null) {
            startSplashActivity(null);
            return;
        }
        mChatInfo = (ChatInfo) mBundle.getSerializable(Constant.CHAT_INFO);
        if (mChatInfo == null) {
            startSplashActivity(null);
            return;
        }
        initView();

        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(mContext);
        helper.customizeChatLayout(mChatLayout);
    }

    private void initMine(){
        mStringList = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mStringList.add(mStrings[i]);
        }

        mService_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mService_strs.add("");
        }
        mServiceDialog = new ServiceDialog(mContext,mService_strs);
        mServiceDialog.setOnTalkClickListener(new ServiceDialog.OnTalkClickListener() {
            @Override
            public void onTalkClickListener(String content) {
                MessageInfo info = MessageInfoUtil.buildTextMessage(content);
                mChatLayout.sendMessage(info,false);
            }
        });

        mSjbg_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mSjbg_strs.add("");
        }
        mSjbgDialog = new SjbgDialog(mContext,mSjbg_strs);
        mSjbgDialog.setOnTalkClickListener(new SjbgDialog.OnTalkClickListener() {
            @Override
            public void onTalkClickListener(String content) {
//                MessageInfo info = MessageInfoUtil.buildTextMessage(content);
//                mChatLayout.sendMessage(info,false);
            }
        });


    }


    private void initView() {
        initMine();
        //从布局文件中获取聊天面板组件
        mChatLayout = findViewById(R.id.chat_layout);

        mtv_title = findViewById(R.id.chat_name);

        mimgv_back =findViewById(R.id.chat_back);
        *//*
         *聊天下方是否需要多展示（咨询师与用户展示不同）
         * *//*
        if(is_user){
            mChatLayout.getInputLayout().disableServiceAction(false);//展示服务项目
            mChatLayout.getInputLayout().disableSjbgAction(true);//隐藏咨询报告
        }else{
            mChatLayout.getInputLayout().disableServiceAction(true);
            mChatLayout.getInputLayout().disableSjbgAction(false);
        }


        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();
        *//*
         * 需要聊天的基本信息
         *//*
        mChatLayout.setChatInfo(mChatInfo);
        mtv_title.setText(mChatInfo.getChatName());

        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        *//*隐藏原TitlBar*//*
        mTitleBar.setVisibility(GONE);
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
            }
        });
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
//                    DemoApplication.instance().startActivity(intent);
                }
            });
        }
        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }
//                ChatInfo info = new ChatInfo();
//                info.setId(messageInfo.getFromUser());
//                Intent intent = new Intent(DemoApplication.instance(), FriendProfileActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, info);
//                DemoApplication.instance().startActivity(intent);
            }
        });

        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.onStartActivityListener() {
            @Override
            public void onStartGroupMemberSelectActivity() {
//                Intent intent = new Intent(DemoApplication.instance(), StartGroupMemberSelectActivity.class);
//                GroupInfo groupInfo = new GroupInfo();
//                groupInfo.setId(mChatInfo.getId());
//                groupInfo.setChatName(mChatInfo.getChatName());
//                intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
//                startActivityForResult(intent, 1);
            }

            @Override
            public boolean handleStartGroupLiveActivity() {
                // 打开群直播
//                LiveRoomAnchorActivity.start(DemoApplication.instance(), mChatInfo.getId());
                // demo层对消息进行处理，不走默认的逻辑
                return true;
            }
        });
        mChatLayout.getInputLayout().setOnNewAddClickListener(new InputLayout.onNewAddClickListener() {
            @Override
            public void onDiamondClickListener() {
                mContext.startActivity(new Intent(mContext, BuyzActivity.class));
            }

            @Override
            public void onSerViceCLickListener() {
                mServiceDialog.show();
            }

            @Override
            public void onSjbgClickListener() {
                mSjbgDialog.show();
            }

        });
        mChatLayout.setOnSendClickListener(new AbsChatLayout.OnSendClickListener() {
            @Override
            public void onSendClickListener(MessageInfo msg) {
                ToastUtil.showToast(mContext,"钻石不够,充值后发送");
                mChatLayout.sendMessage(msg, false);
            }
        });
        // TODO: 2021/5/5 如果是用户的话进行提醒
        MessageInfo msg = buildTextMessage("私聊1钻石/每条");
        mChatLayout.getChatManager().getCurrentProvider().getDataSource().add(msg);
        mChatLayout.getChatManager().getCurrentProvider().updateAdapter(DATA_CHANGE_TYPE_REFRESH,0);

        mimgv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(mContext.getResources().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(mContext.getResources().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(mContext.getResources().getString(R.string.ui_at_all_me));
                break;
            default:
                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                break;

        }
    }

    private int getAtInfoType(List<V2TIMGroupAtInfo> atInfoList) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;

    }
*//*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String result_id = data.getStringExtra(TUIKitConstants.Selection.USER_ID_SELECT);
            String result_name = data.getStringExtra(TUIKitConstants.Selection.USER_NAMECARD_SELECT);
            mChatLayout.getInputLayout().updateInputText(result_name, result_id);
        }
    }*//*

   *//* @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }*//*

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }

    *//*@Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }*//*
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }
    *//**
     * 创建一条文本消息
     *
     * @param message 消息内容
     * @return
     *//*
    public static MessageInfo buildTextMessage(String message) {
        MessageInfo info = new MessageInfo();
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage(message);

        info.setExtra(message);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setSelf(true);
        info.setTimMessage(v2TIMMessage);
        info.setFromUser(V2TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_STATUS_ZUANS);
        return info;
    }
    private void startSplashActivity(Bundle bundle) {
//        Intent intent = new Intent(ChatActivity.this, SplashActivity.class);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        startActivity(intent);
        dismiss();
    }*/
}

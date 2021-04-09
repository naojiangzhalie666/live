package com.example.myapplication.chat;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.TalkAdapter;
import com.example.myapplication.base.Constant;
import com.example.myapplication.chat.helper.ChatLayoutHelper;
import com.example.myapplication.pop_dig.BuyzDialog;
import com.example.myapplication.pop_dig.HeadDialog;
import com.example.myapplication.pop_dig.ServiceDialog;
import com.example.myapplication.utils.FlowLayoutManager;
import com.example.myapplication.utils.SpaceItemDecoration;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {
    private static final String TAG = "ChatFragment";
    private View mBaseView;
    private ChatLayout mChatLayout;
    private RecyclerView mRecyclerView;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    private TextView mtv_title;
    private TextView mtv_id;
    private TextView mtv_content;
    private TextView mtv_state;
    private ImageView mimgv_head;
    private ImageView mimgv_back;
    private ConstraintLayout mcon_tp;
    private TalkAdapter mTalkAdapter;
    private List<String> mStringList;
    private String[] mStrings = new String[]{"情感分析", "情感分析", "第三者问题", "未成年人心理", "未成年人心理"};
    private ServiceDialog mServiceDialog;
    private List<String> mService_strs;
    private HeadDialog mHeadDialog;
    private List<Map<String,Object>> mBuy_strs;
    private BuyzDialog mBuyzDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_chat, container, false);
        return mBaseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMine();
    }

    private void initMine(){
        mRecyclerView = mBaseView.findViewById(R.id.chat_recy);
        mcon_tp = mBaseView.findViewById(R.id.chat_tp_con);
        mStringList = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mStringList.add(mStrings[i]);
        }
        mTalkAdapter = new TalkAdapter(getActivity(),mStringList);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(2)));
        mRecyclerView.setLayoutManager(flowLayoutManager);
        mRecyclerView.setAdapter(mTalkAdapter);

        mHeadDialog = new HeadDialog(getActivity());

        mService_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mService_strs.add("");
        }
        mServiceDialog = new ServiceDialog(getActivity(),mService_strs);
        mServiceDialog.setOnTalkClickListener(new ServiceDialog.OnTalkClickListener() {
            @Override
            public void onTalkClickListener(String content) {
                MessageInfo info = MessageInfoUtil.buildTextMessage(content);
                mChatLayout.sendMessage(info,false);
            }
        });

        mcon_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeadDialog.show();
            }
        });

        mBuy_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String,Object> map =new HashMap<>();
            map.put("select",false);
            mBuy_strs.add(map);
        }
        mBuyzDialog = new BuyzDialog(getActivity(),mBuy_strs);
    }


    private void initView() {
        //从布局文件中获取聊天面板组件
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);

        mtv_title = mBaseView.findViewById(R.id.chat_name);

        mimgv_back = mBaseView.findViewById(R.id.chat_back);
        mtv_id = mBaseView.findViewById(R.id.chat_id);
        mtv_state = mBaseView.findViewById(R.id.chat_state);
        mtv_content = mBaseView.findViewById(R.id.chat_tvcontent);
        mimgv_head = mBaseView.findViewById(R.id.chat_head);
        mtv_id.setText("ID:2123");
        mtv_content.setText("关注方向");
        Glide.with(this).load(R.drawable.man_se).into(mimgv_head);
        mtv_state.setVisibility(VISIBLE);
        /*
        *聊天下方是否需要多展示（咨询师与用户展示不同）
        * */
        mChatLayout.getInputLayout().disableVideoRecordAction(false);//是否展示摄像


        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();
        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        mtv_title.setText(mChatInfo.getChatName());

        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        /*隐藏原TitlBar*/
        mTitleBar.setVisibility(GONE);
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
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
                mBuyzDialog.show();
            }

            @Override
            public void onSerViceCLickListener() {
                mServiceDialog.show();
            }
        });

        if (false/*mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP*/) {
            V2TIMManager.getConversationManager().getConversation(mChatInfo.getId(), new V2TIMValueCallback<V2TIMConversation>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e(TAG, "getConversation error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess(V2TIMConversation v2TIMConversation) {
                    if (v2TIMConversation == null) {
                        Log.d(TAG, "getConversation failed");
                        return;
                    }
                    mChatInfo.setAtInfoList(v2TIMConversation.getGroupAtInfoList());

                    final V2TIMMessage lastMessage = v2TIMConversation.getLastMessage();

                    updateAtInfoLayout();
                    mChatLayout.getAtInfoLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final List<V2TIMGroupAtInfo> atInfoList = mChatInfo.getAtInfoList();
                            if (atInfoList == null || atInfoList.isEmpty()) {
                                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                                return;
                            } else {
                                mChatLayout.getChatManager().getAtInfoChatMessages(atInfoList.get(atInfoList.size() - 1).getSeq(), lastMessage, new IUIKitCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        mChatLayout.getMessageLayout().scrollToPosition((int) atInfoList.get(atInfoList.size() - 1).getSeq());
                                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mChatLayout.getMessageLayout().getLayoutManager();
                                        mLayoutManager.scrollToPositionWithOffset((int) atInfoList.get(atInfoList.size() - 1).getSeq(), 0);

                                        atInfoList.remove(atInfoList.size() - 1);
                                        mChatInfo.setAtInfoList(atInfoList);

                                        updateAtInfoLayout();
                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                        Log.d(TAG, "getAtInfoChatMessages failed");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
        mimgv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(getResources().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(getResources().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(getResources().getString(R.string.ui_at_all_me));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String result_id = data.getStringExtra(TUIKitConstants.Selection.USER_ID_SELECT);
            String result_name = data.getStringExtra(TUIKitConstants.Selection.USER_NAMECARD_SELECT);
            mChatLayout.getInputLayout().updateInputText(result_name, result_id);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        mChatInfo = (ChatInfo) bundle.getSerializable(Constant.CHAT_INFO);
        if (mChatInfo == null) {
            return;
        }
        initView();

        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(getActivity());
        helper.customizeChatLayout(mChatLayout);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getActivity().getResources().getDisplayMetrics());
    }

}

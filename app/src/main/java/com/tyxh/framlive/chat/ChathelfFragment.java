package com.tyxh.framlive.chat;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.AbsChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.ContctBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.helper.ChatLayoutHelper;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.ServiceDialog;
import com.tyxh.framlive.pop_dig.SjbgDialog;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout.DATA_CHANGE_TYPE_REFRESH;
import static com.tyxh.framlive.bean.EventMessage.ATTEN_SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChathelfFragment extends BaseFragment {
    private static final String TAG = "ChatFragment";
    private View mBaseView;
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    private TextView mtv_title;
    private ImageView mimgv_back;
    private List<String> mStringList;
    private String[] mStrings = new String[]{"????????????", "????????????", "???????????????", "??????????????????", "??????????????????"};
    private ServiceDialog mServiceDialog;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mService_strs;
    private SjbgDialog mSjbgDialog;
    private List<ContctBean.RetDataBean.ListBean> mSjbg_strs;


    private String mToken;
    private int mPower;
    private boolean is_first = true;
    private UserInfoBean mUserInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_chathelf, container, false);
        return mBaseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMine();
    }

    private void initMine() {
        mUserInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        mToken = LiveShareUtil.getInstance(getActivity()).getToken();
        mStringList = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            mStringList.add(mStrings[i]);
        }

        mService_strs = new ArrayList<>();
        mSjbg_strs = new ArrayList<>();
//        getDetail(mChatInfo.getId());

    }


    private void initView() {
        //??????????????????????????????????????????
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);

        mtv_title = mBaseView.findViewById(R.id.chat_name);

        mimgv_back = mBaseView.findViewById(R.id.chat_back);

        //?????????????????????UI??????????????????
        mChatLayout.initDefault();
        /*
         * ???????????????????????????
         */
        mChatLayout.setChatInfo(mChatInfo);
        mtv_title.setText(mChatInfo.getChatName());

        //??????????????????????????????
        mTitleBar = mChatLayout.getTitleBar();
        /*?????????TitlBar*/
        mTitleBar.setVisibility(GONE);
        //?????????????????????????????????????????????????????????????????????????????????
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        /*
         *?????????????????????????????????????????????????????????????????????
         * */
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
        if (mPower == Constant.POWER_NORMAL) {//??????
            mChatLayout.getInputLayout().disableServiceAction(false);//??????????????????
            mChatLayout.getInputLayout().disableSjbgAction(true);//??????????????????
            // ??????????????????????????????
            MessageInfo msg = buildTextMessage("??????1??????/??????");
            mChatLayout.getChatManager().getCurrentProvider().getDataSource().add(msg);
            mChatLayout.getChatManager().getCurrentProvider().updateAdapter(DATA_CHANGE_TYPE_REFRESH, 0);
        } else {
            mChatLayout.getInputLayout().disableServiceAction(true);
            mChatLayout.getInputLayout().disableSjbgAction(false);
            getContHis(mChatInfo.getId());
        }
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
                //??????adapter??????????????????????????????????????????1
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
                // ???????????????
//                LiveRoomAnchorActivity.start(DemoApplication.instance(), mChatInfo.getId());
                // demo????????????????????????????????????????????????
                return true;
            }
        });
        mChatLayout.getInputLayout().setOnNewAddClickListener(new InputLayout.onNewAddClickListener() {
            @Override
            public void onDiamondClickListener() {
                startActivity(new Intent(getActivity(), BuyzActivity.class));
            }

            @Override
            public void onSerViceCLickListener() {
                mServiceDialog.show();
            }

            @Override
            public void onSjbgClickListener() {
                if (mSjbgDialog == null) {
                    Toast.makeText(getActivity(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSjbgDialog.show();
            }

            @Override
            public void onAudioClickListener() {
                super.onAudioClickListener();
                ToastUtil.showToast(getActivity(),"??????/??????????????????????????????");
            }

            @Override
            public void onVideoClickListener() {
                super.onVideoClickListener();
                ToastUtil.showToast(getActivity(),"??????/??????????????????????????????");
            }
        });
        mChatLayout.setOnSendClickListener(new AbsChatLayout.OnSendClickListener() {
            @Override
            public void onSendClickListener(MessageInfo msg) {
//                mChatLayout.sendMessage(msg, false);
                toSendMsg(msg);
            }
        });

        mimgv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        if (is_first) {
            getDetail(mChatInfo.getId());
            is_first = false;
        }
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

        // TODO ??????api??????ChatLayout?????????????????????
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

    /**
     * ????????????????????????
     *
     * @param message ????????????
     * @return
     */
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

    /*???????????????  ??????????????????????????????*/
    private void toSendMsg(MessageInfo msg) {
        //??????--???????????? ?????????--??????--?????????????????????
        if (mPower != Constant.POWER_NORMAL) {
            mChatLayout.sendMessage(msg, false);
            return;
        }
        String id = mUserInfo.getRetData().getId();
        if (id.contains(".")) {
            id = id.substring(0, mChatInfo.getId().indexOf("."));
        }
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().privateChat(mToken, id), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mChatLayout.sendMessage(msg, false);
                } else {
                    ToastUtil.showToast(getActivity(), baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void getDetail(String to_userid) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, to_userid), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        setUserDatil(retData);
                    }
                } else {
                    ToastUtil.showToast(getActivity(), result.getRetMsg());
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (throwable.errorType == HTTP_ERROR) {//????????????
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);

    }

    private void setUserDatil(UserDetailBean.RetDataBean data) {
        UserDetailBean.RetDataBean.UserBean user = data.getUser();
        if (user != null) {
            int type = user.getType();
            if (type > 1) {
                toAttentionThis(user.getId());
            }
        }
        List<UserDetailBean.RetDataBean.ServicePackagesBean> servicePackages = data.getServicePackages();
        if (servicePackages != null) {
            mService_strs.clear();
            mService_strs.addAll(servicePackages);
            mServiceDialog = new ServiceDialog(getActivity(), mService_strs);
            mServiceDialog.setOnTalkClickListener(new ServiceDialog.OnTalkClickListener() {
                @Override
                public void onTalkClickListener(String content) {
                    MessageInfo info = MessageInfoUtil.buildTextMessage(content);
                    toSendMsg(info);
//                mChatLayout.sendMessage(info,false);
                }
            });
        }
    }
    private void getContHis(String toid) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContxtHis(mToken, 1, 10, toid), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ContctBean bean = new Gson().fromJson(result.toString(), ContctBean.class);
                if (bean.getRetCode() == 0) {
                    List<ContctBean.RetDataBean.ListBean> list = bean.getRetData().getList();
                    if (list != null && list.size() != 0&&getActivity()!=null&&!getActivity().isFinishing()) {
                        setSjbgDig(toid, list);
                    }
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void setSjbgDig(String toUserid, List<ContctBean.RetDataBean.ListBean> listBeans) {
        mSjbg_strs.clear();
        mSjbg_strs.addAll(listBeans);
        mSjbgDialog = new SjbgDialog(getActivity(), mSjbg_strs, mToken, toUserid);
    }


    /*???????????????????????????--?????????????????????*/
    private void toAttentionThis(int mPusherId) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().addAttention(mToken, String.valueOf(mPusherId), "1"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean =new Gson().fromJson(result.toString(),BaseBean.class);
                if(baseBean.getRetCode() ==0 ){
                    EventBus.getDefault().post(new EventMessage(ATTEN_SUCCESS));
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DevRing.httpManager().stopRequestByTag(TAG);
    }
}

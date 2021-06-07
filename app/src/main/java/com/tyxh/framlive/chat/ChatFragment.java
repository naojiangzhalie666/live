package com.tyxh.framlive.chat;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.AbsChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.TalkAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.ContctBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LiveCotctBean;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.helper.ChatLayoutHelper;
import com.tyxh.framlive.chat.tuikit.TRTCAudioCallActivity;
import com.tyxh.framlive.chat.tuikit.TRTCVideoCallActivity;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.HeadDialog;
import com.tyxh.framlive.pop_dig.ServiceDialog;
import com.tyxh.framlive.pop_dig.SjbgDialog;
import com.tyxh.framlive.pop_dig.UseWhatDialog;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.utils.FlowLayoutManager;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.SpaceItemDecoration;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout.DATA_CHANGE_TYPE_REFRESH;

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
    private ServiceDialog mServiceDialog;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mService_strs;
    private SjbgDialog mSjbgDialog;
    private List<ContctBean.RetDataBean.ListBean> mSjbg_strs;

    private HeadDialog mHeadDialog;

    private String mToken;
    private UserInfoBean mUserInfo;
    private int mPower;
    private boolean is_first = true;
    private int mType = 1;//被聊天人的权限 1-普通用户；2-咨询师；3-主机构；4-子机构
    private InputLayout mInputLayout;


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

    private void initMine() {
        mUserInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        mToken = LiveShareUtil.getInstance(getActivity()).getToken();
        mRecyclerView = mBaseView.findViewById(R.id.chat_recy);
        mcon_tp = mBaseView.findViewById(R.id.chat_tp_con);
        mStringList = new ArrayList<>();
        mTalkAdapter = new TalkAdapter(getActivity(), mStringList);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(2)));
        mRecyclerView.setLayoutManager(flowLayoutManager);
        mRecyclerView.setAdapter(mTalkAdapter);
        mService_strs = new ArrayList<>();

        mSjbg_strs = new ArrayList<>();

        mcon_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPower == Constant.POWER_NORMAL) {//本人为普通  跳转咨询师/机构详情
                    switch (mType) {
                        case 1:
                            if (mHeadDialog != null) {
                                mHeadDialog.show();
                            } else {
                                ToastUtil.showToast(getActivity(), "用户信息获取失败，退出重试");
                            }
                            break;
                        case 2:
                            Intent int_person = new Intent(getActivity(), LookPersonActivity.class);
                            int_person.putExtra("is_user", true);//咨询师页面
                            int_person.putExtra("query_id", mChatInfo.getId());
                            startActivity(int_person);
                            break;
                        case 3:
                        case 4:
                            Intent int_orgi = new Intent(getActivity(), OranizeActivity.class);
                            int_orgi.putExtra("is_user", true);//咨询机构页面
                            int_orgi.putExtra("query_id", mChatInfo.getId());
                            startActivity(int_orgi);
                            break;
                    }
                } else {//本人为咨询师/机构--弹出信息
                    if (mHeadDialog != null) {
                        mHeadDialog.show();
                    } else {
                        ToastUtil.showToast(getActivity(), "用户信息获取失败，退出重试");
                    }
                }

            }
        });

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
        mtv_id.setText("ID:" + mChatInfo.getId());

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
        /*
         *聊天下方是否需要多展示（咨询师与用户展示不同）
         * */
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
        if (mPower == Constant.POWER_NORMAL) {//普通
            mChatLayout.getInputLayout().disableServiceAction(false);//展示服务项目
            mChatLayout.getInputLayout().disableSjbgAction(true);//隐藏疏解报告
            // 普通用户的话进行提醒
            MessageInfo msg = buildTextMessage("私聊1钻石/每条");
            mChatLayout.getChatManager().getCurrentProvider().getDataSource().add(msg);
            mChatLayout.getChatManager().getCurrentProvider().updateAdapter(DATA_CHANGE_TYPE_REFRESH, 0);
            mtv_content.setText("擅长方向");
        } else {
            mChatLayout.getInputLayout().disableServiceAction(true);
            mChatLayout.getInputLayout().disableSjbgAction(false);
            mtv_content.setText("关注方向");
            getContHis(mChatInfo.getId());
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
                startActivity(new Intent(getActivity(), BuyzActivity.class));
//                mBuyzDialog.show();
            }

            @Override
            public void onSerViceCLickListener() {
                if (mServiceDialog != null) {
                    mServiceDialog.show();
                } else {
                    Toast.makeText(getActivity(), "数据获取失败,稍后请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSjbgClickListener() {
                if (mSjbgDialog == null) {
                    Toast.makeText(getActivity(), "暂无报告", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSjbgDialog.show();
            }

            @Override
            public void onAudioClickListener() {
                super.onAudioClickListener();
                if(mPower == Constant.POWER_NORMAL) {//我是用户  肯定弹
                    getUseData(true);
                }else if(mType>1){//我是咨询师  对方也是or机构   我也会弹
                    getUseData(true);
                }else {//我是咨询师   对方是用户  那就不弹  对方弹
                    toCallAudio(new LiveCotctBean.RetDataBean(),false);
                }
            }

            @Override
            public void onVideoClickListener() {
                super.onVideoClickListener();
                if(mPower == Constant.POWER_NORMAL) {//我是用户  肯定弹
                    getUseData(false);
                }else if(mType>1){//我是咨询师  对方也是or机构   我也会弹
                    getUseData(false);
                }else {//我是咨询师   对方是用户  那就不弹  对方弹
                    toCallVideo(new LiveCotctBean.RetDataBean(),false);
                }
            }
        });
        mChatLayout.setOnSendClickListener(new AbsChatLayout.OnSendClickListener() {
            @Override
            public void onSendClickListener(MessageInfo msg) {
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
     /*   mInputLayout = mChatLayout.getInputLayout();
        mInputLayout.startAudioCall();//发出语音连线邀请
        //开始计时
        mInputLayout.stopAudioCall(); //停止语音连线
        mInputLayout.startVideoCall();//发出视频连线邀请
        //开始计时
        mInputLayout.stopVideoCall();//停止视频连线*/


    }

    private UseWhatDialog mUseWhatDialog;

    /*获取可用于连麦的列表--卡、包、钻*/
    private void getUseData(boolean is_audio) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContTime(mToken, mChatInfo.getId(), mUserInfo.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LiveCotctBean bean = new Gson().fromJson(result.toString(), LiveCotctBean.class);
                if (bean.getRetCode() == 0) {
                    mUseWhatDialog = new UseWhatDialog(getActivity(), getActivity(), bean.getRetData());
                    mUseWhatDialog.show();
                    mUseWhatDialog.setOnSureClickListener(new UseWhatDialog.OnSureClickListener() {
                        @Override
                        public void onSureClickListener(LiveCotctBean.RetDataBean bean) {
                            mUseWhatDialog.dismiss();
                            if(is_audio){
                                toCallAudio(bean,true);
                            }else{
                                toCallVideo(bean,true);
                            }
                        }
                    });
                }else{
                    ToastUtil.showToast(getActivity(),bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /**
     * 语音连接
     * @param bean
     * @param need_sock     是否需要进行Socket连接  false
     */
    private void toCallAudio(LiveCotctBean.RetDataBean bean,boolean need_sock){
        if(bean ==null){
            ToastUtil.showToast(getActivity(),"请先进行选择");
            return;
        }
        List<UserModel> contactList = new ArrayList<>();
        UserModel model = new UserModel();
        model.userId = mChatLayout.getChatInfo().getId();
        model.userName = mChatLayout.getChatInfo().getChatName();
        model.userSig = TUIKitConfigs.getConfigs().getGeneralConfig().getUserSig();
        contactList.add(model);
        Bundle bundle =new Bundle();
        bundle.putSerializable("data",bean);
        bundle.putBoolean("need_sock",need_sock);
        // TODO: 2021/6/1 先进行消耗品选择  然后进入下方界面  进入倒计时等 操作
        TRTCAudioCallActivity.startCallSomeone(getActivity().getApplicationContext(), contactList);

    }

    /**
     * 视频链接
     * @param bean
     * @param need_sock    是否需要进行Socket连接  false
     */
    private void toCallVideo(LiveCotctBean.RetDataBean bean,boolean need_sock){
        if(bean ==null){
            ToastUtil.showToast(getActivity(),"请先进行选择");
            return;
        }
        List<UserModel> contactList = new ArrayList<>();
        UserModel model = new UserModel();
        model.userId = mChatLayout.getChatInfo().getId();
        model.userName = mChatLayout.getChatInfo().getChatName();
        model.userSig = TUIKitConfigs.getConfigs().getGeneralConfig().getUserSig();
        contactList.add(model);
        Bundle bundle =new Bundle();
        bundle.putSerializable("data",bean);
        bundle.putBoolean("need_sock",need_sock);
        // TODO: 2021/6/1 先进行消耗品选择  然后进入下方界面  进入倒计时等 操作
        TRTCVideoCallActivity.startCallSomeone(getActivity().getApplicationContext(), contactList,bundle);


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

    /**
     * 创建一条文本消息
     *
     * @param message 消息内容
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

    /*发送消息前  验证是否可发送该消息*/
    private void toSendMsg(MessageInfo msg) {
        //普通--需要验证 咨询师--机构--子咨询师不花钱
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

    /*获取目标信息资料*/
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
                if (throwable.errorType == HTTP_ERROR) {//重新登录
                    EventBus.getDefault().post(new EventMessage(1005));
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);

    }

    private void setUserDatil(UserDetailBean.RetDataBean data) {
        UserDetailBean.RetDataBean.UserBean user = data.getUser();
        if (user != null) {
            mType = user.getType();
            Glide.with(getActivity()).load(user.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
            String interest = "";
            if (mPower == Constant.POWER_NORMAL) {//擅长方向---机构没有添加擅长方向的位置
                List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = data.getCounselorBeans();
                if (counselorBeans != null && counselorBeans.size() > 0) {
                    UserDetailBean.RetDataBean.CounselorBeansBean counselorBeansBean = counselorBeans.get(0);
                    interest = counselorBeansBean.getInterests();
                }
            } else {//关注方向
                interest = user.getInterests();
            }
            if (!TextUtils.isEmpty(interest)) {
                if (interest.endsWith(",")) {
                    interest = interest.substring(0, interest.length() - 1);
                }
                String[] split = interest.split(",");
                for (int i = 0; i < split.length; i++) {
                    mStringList.add(split[i]);
                }
                mTalkAdapter.notifyDataSetChanged();
            }


            mHeadDialog = new HeadDialog(getActivity(),data);
//            mtv_state.setVisibility(VISIBLE);//是否在直播中
            if (mType > 1) {
                toAttentionThis(user.getId());
            }
            if(mType ==2){//咨询师
                List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = data.getCounselorBeans();
                if (counselorBeans != null && counselorBeans.size() > 0) {
                    UserDetailBean.RetDataBean.CounselorBeansBean bb = counselorBeans.get(0);
                    Glide.with(getActivity()).load(bb.getCouHeadImg()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
                }
            }else if(mType>2){//咨询机构、子机构
                UserDetailBean.RetDataBean.CouMechanismBean couMechanism = data.getCouMechanism();
                if(couMechanism!=null){
                    Glide.with(getActivity()).load(couMechanism.getMeLogo()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
                }
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

    /*私聊自动关注该主播--如果不是普通人*/
    private void toAttentionThis(int mPusherId) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().addAttention(mToken, String.valueOf(mPusherId), "1"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void getContHis(String toid) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContxtHis(mToken, 1, 10, toid), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ContctBean bean = new Gson().fromJson(result.toString(), ContctBean.class);
                if (bean.getRetCode() == 0) {
                    List<ContctBean.RetDataBean.ListBean> list = bean.getRetData().getList();
                    if (list != null && list.size() != 0) {
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

}

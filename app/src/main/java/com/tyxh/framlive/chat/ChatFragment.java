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
import com.tencent.imsdk.v2.V2TIMCallback;
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
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
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
    private int mType = 1;//????????????????????? 1-???????????????2-????????????3-????????????4-?????????
    private InputLayout mInputLayout;
    private boolean lm_once = true;


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
        TUIKitConfigs.getConfigs().getGeneralConfig().setUserNickname(mUserInfo.getRetData().getNickname());
        TUIKitConfigs.getConfigs().getGeneralConfig().setUserFaceUrl(mUserInfo.getRetData().getIco());
        mcon_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (mPower == Constant.POWER_NORMAL) {//???????????????  ???????????????/????????????*/
                    switch (mType) {
                        case 1:
                            if (mHeadDialog != null) {
                                mHeadDialog.show();
                            } else {
                                ToastUtil.showToast(getActivity(), "???????????????????????????????????????");
                            }
                            break;
                        case 2:
                            Intent int_person = new Intent(getActivity(), LookPersonActivity.class);
                            int_person.putExtra("is_user", true);//???????????????
                            int_person.putExtra("query_id", mChatInfo.getId());
                            startActivity(int_person);
                            break;
                        case 3:
                        case 4:
                            Intent int_orgi = new Intent(getActivity(), OranizeActivity.class);
                            int_orgi.putExtra("is_user", true);//??????????????????
                            int_orgi.putExtra("query_id", mChatInfo.getId());
                            startActivity(int_orgi);
                            break;
                    }
               /* } else {//??????????????????/??????--????????????
                    if (mHeadDialog != null) {
                        mHeadDialog.show();
                    } else {
                        ToastUtil.showToast(getActivity(), "???????????????????????????????????????");
                    }
                }*/

            }
        });

    }


    private void initView() {
        //??????????????????????????????????????????
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);
        mtv_title = mBaseView.findViewById(R.id.chat_name);
        mimgv_back = mBaseView.findViewById(R.id.chat_back);
        mtv_id = mBaseView.findViewById(R.id.chat_id);
        mtv_state = mBaseView.findViewById(R.id.chat_state);
        mtv_content = mBaseView.findViewById(R.id.chat_tvcontent);
        mimgv_head = mBaseView.findViewById(R.id.chat_head);
        mtv_id.setText("ID:" + mChatInfo.getId());

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
//                mBuyzDialog.show();
            }

            @Override
            public void onSerViceCLickListener() {
                if (mServiceDialog != null) {
                    mServiceDialog.show();
                } else {
                    Toast.makeText(getActivity(), "??????????????????,???????????????", Toast.LENGTH_SHORT).show();
                }
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
                if(mPower == Constant.POWER_NORMAL) {//????????????  ?????????
                    getUseData(true);
                }else if(mType>1){//???????????????  ????????????or??????   ????????????
                    getUseData(true);
                }else {//???????????????   ???????????????  ????????????  ?????????
                    toCallAudio(new LiveCotctBean.RetDataBean(),false);
                }
            }

            @Override
            public void onVideoClickListener() {
                super.onVideoClickListener();
                if(mPower == Constant.POWER_NORMAL) {//????????????  ?????????
                    getUseData(false);
                }else if(mType>1){//???????????????  ????????????or??????   ????????????
                    getUseData(false);
                }else {//???????????????   ???????????????  ????????????  ?????????
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
        String h_t = mtv_title.getText().toString();
        if(TextUtils.isEmpty(h_t)){
            getDetail(mChatInfo.getId());
        }
        if (is_first) {
            getDetail(mChatInfo.getId());
            is_first = false;
        }
     /*   mInputLayout = mChatLayout.getInputLayout();
        mInputLayout.startAudioCall();//????????????????????????
        //????????????
        mInputLayout.stopAudioCall(); //??????????????????
        mInputLayout.startVideoCall();//????????????????????????
        //????????????
        mInputLayout.stopVideoCall();//??????????????????*/

//        toSetred();
    }

    private void toSetred(){
        V2TIMManager.getMessageManager().markC2CMessageAsRead(mChatInfo.getId(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "processHistoryMsgs setReadMessage failed, code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {
                TUIKitLog.d(TAG, "processHistoryMsgs setReadMessage success");
            }
        });

    }

    private UseWhatDialog mUseWhatDialog;

    /*??????????????????????????????--???????????????*/
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
     * ????????????
     * @param bean
     * @param need_sock     ??????????????????Socket??????  false
     */
    private void toCallAudio(LiveCotctBean.RetDataBean bean,boolean need_sock){
        if(bean ==null){
            ToastUtil.showToast(getActivity(),"??????????????????");
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
        // TODO: 2021/6/1 ????????????????????????  ????????????????????????  ?????????????????? ??????
        TRTCAudioCallActivity.startCallSomeone(getActivity().getApplicationContext(), contactList,bundle);

    }

    /**
     * ????????????
     * @param bean
     * @param need_sock    ??????????????????Socket??????  false
     */
    private void toCallVideo(LiveCotctBean.RetDataBean bean,boolean need_sock){
        if(bean ==null){
            ToastUtil.showToast(getActivity(),"??????????????????");
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
        // TODO: 2021/6/1 ????????????????????????  ????????????????????????  ?????????????????? ??????
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
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().privateChat(mToken, mChatInfo.getId()), new HttpBackListener() {
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

    /*????????????????????????*/
    private void getDetail(String to_userid) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, to_userid), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null&&!getActivity().isFinishing()) {
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
            mType = user.getType();
            mtv_title.setText(user.getNickname());
            Glide.with(getActivity()).load(user.getIco()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
            String interest = "";
            if(mType==1){//????????????????????????---????????????
                mtv_content.setText("????????????:");
                interest = user.getInterests();
            }else{//????????????---???????????????????????????????????????
                mtv_content.setText("????????????:");
                if(mType ==2){
                    List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = data.getCounselorBeans();
                    if (counselorBeans != null && counselorBeans.size() > 0) {
                        UserDetailBean.RetDataBean.CounselorBeansBean counselorBeansBean = counselorBeans.get(0);
                        interest = counselorBeansBean.getInterests();
                        mtv_title.setText(counselorBeansBean.getCouName());
                    }
                }
            }
            mStringList.clear();
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
//            mtv_state.setVisibility(VISIBLE);//??????????????????
            if (mType > 1) {
                toAttentionThis(user.getId());
            }
            if(mType ==2){//?????????
                List<UserDetailBean.RetDataBean.CounselorBeansBean> counselorBeans = data.getCounselorBeans();
                if (counselorBeans != null && counselorBeans.size() > 0) {
                    UserDetailBean.RetDataBean.CounselorBeansBean bb = counselorBeans.get(0);
                    Glide.with(getActivity()).load(bb.getCouHeadImg()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
                    mtv_title.setText(bb.getCouName());
                }
            }else if(mType>2){//????????????????????????
                UserDetailBean.RetDataBean.CouMechanismBean couMechanism = data.getCouMechanism();
                if(couMechanism!=null){
                    Glide.with(getActivity()).load(couMechanism.getMeLogo()).error(R.drawable.live_defaultimg).placeholder(R.drawable.live_defaultimg).into(mimgv_head);
                    mtv_title.setText(couMechanism.getMeName());
                }
            }
            /*?????????????????????????????????????????????????????????*/
            boolean lm = mChatInfo.isLm();
            if(lm&&lm_once){       //????????????[1:?????????2:??????]
                int lm_type = mChatInfo.getLm_type();
                if(lm_type ==1){
                    if(mPower == Constant.POWER_NORMAL) {//????????????  ?????????
                        getUseData(false);
                    }else if(mType>1){//???????????????  ????????????or??????   ????????????
                        getUseData(false);
                    }else {//???????????????   ???????????????  ????????????  ?????????
                        toCallVideo(new LiveCotctBean.RetDataBean(),false);
                    }
                }else if(lm_type ==2){
                    if(mPower == Constant.POWER_NORMAL) {//????????????  ?????????
                        getUseData(true);
                    }else if(mType>1){//???????????????  ????????????or??????   ????????????
                        getUseData(true);
                    }else {//???????????????   ???????????????  ????????????  ?????????
                        toCallAudio(new LiveCotctBean.RetDataBean(),false);
                    }
                }
                lm_once=false;
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

    /*???????????????????????????--?????????????????????*/
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DevRing.httpManager().stopRequestByTag(TAG);
    }
}

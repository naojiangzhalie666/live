package com.tyxh.framlive.chat.tuikit;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.SelectContactActivity;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcaudiocalldemo.ui.audiolayout.TRTCAudioLayout;
import com.tencent.liteav.trtcaudiocalldemo.ui.audiolayout.TRTCAudioLayoutManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LiveCotctBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.UseWhatDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.utils.TCUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;
import static com.tyxh.framlive.chat.tuikit.TRTCVideoCallActivity.PARAM_DATE;

/**
 * 用于展示语音通话的主界面，通话的接听和拒绝就是在这个界面中完成的。
 *
 * @author guanyifeng
 */
public class TRTCAudioCallActivity extends AppCompatActivity {
    private static final String TAG = TRTCAudioCallActivity.class.getName();

    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_BEING_CALLED_FROM_NOTIFICATION = 3;
    public static final int TYPE_CALL = 2;

    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_USER = "user_model";
    public static final String PARAM_BEINGCALL_USER = "beingcall_user_model";
    public static final String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int MAX_SHOW_INVITING_USER = 2;

    private static final int RADIUS = 30;

    /**
     * 界面元素相关
     */
    private ImageView mMuteImg;
    private LinearLayout mMuteLl;
    private ImageView mHangupImg;
    private LinearLayout mHangupLl;
    private ImageView mHandsfreeImg;
    private LinearLayout mHandsfreeLl;
    private ImageView mDialingImg;
    private LinearLayout mDialingLl;
    private TRTCAudioLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv;
    private Runnable mTimeRunnable;
    private int mTimeCount;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    /**
     * 拨号相关成员变量
     */
    private UserModel mSelfModel;
    private List<UserModel> mCallUserModelList = new ArrayList<>(); // 呼叫方
    private Map<String, UserModel> mCallUserModelMap = new HashMap<>();
    private UserModel mSponsorUserModel; // 被叫方
    private List<UserModel> mOtherInvitingUserModelList;
    private int mCallType;
    private ITRTCAVCall mITRTCAVCall;
    private String mGroupId;
    private boolean isHandsFree = true;
    private boolean isMuteMic = false;
    private Vibrator mVibrator;
    private Ringtone mRingtone;
    private boolean mNeed_sock;
    /**
     * 拨号的回调
     */
    private TRTCAVCallListener mTRTCAudioCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            //发生了错误，报错并退出该页面
            ToastUtil.toastLongMessage(getString(R.string.error) + "[" + code + "]:" + msg);
            finishActivity();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
        }

        @Override
        public void onUserEnter(final String userId) {
            Log.e(TAG, "onUserLeave: 语音对方-接通了");
            if (mNeed_sock) {
                mLeft_second = select_bean.getDuration();
                initSocket();
                if (all_Second == 0) {
                    Toast.makeText(TRTCAudioCallActivity.this, "首次连麦，前30秒免费", Toast.LENGTH_SHORT).show();
                    startTimer();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCallingView();
                    TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                    if (layout != null) {
                        layout.stopLoading();
                    } else {
                        // 没有这个用户，需要重新分配, 先获取用户资料，在进行分配
                        ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                            @Override
                            public void onSuccess(UserModel model) {
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }

                            @Override
                            public void onFailed(int code, String msg) {
                                // 获取用户资料失败了，模拟一个用户
                                ToastUtil.toastLongMessage(getString(R.string.get_user_info_tips_before) + userId + getString(R.string.get_user_info_tips_after));
                                UserModel model = new UserModel();
                                model.userId = userId;
                                model.phone = "";
                                model.userName = userId;
                                model.userAvatar = "";
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            Log.e(TAG, "onUserLeave: 语音对方-挂断了");
            stopTimer();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                    //2. 删除用户model
                    UserModel userModel = mCallUserModelMap.remove(userId);
                    if (userModel != null) {
                        mCallUserModelList.remove(userModel);
                    }
                }
            });
        }

        @Override
        public void onReject(final String userId) {
            Log.e(TAG, "onUserLeave: 语音对方-拒绝了");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // 进入拒绝环节
                        //1. 回收界面元素
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.reject_call));
                        }
                    }
                }
            });
        }

        @Override
        public void onNoResp(final String userId) {
            Log.e(TAG, "onUserLeave: 语音对方-无响应");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // 进入无响应环节
                        //1. 回收界面元素
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.no_response));
                        }
                    }
                }
            });
        }

        @Override
        public void onLineBusy(String userId) {
            if (mCallUserModelMap.containsKey(userId)) {
                // 进入无响应环节
                //1. 回收界面元素
                mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                //2. 删除用户model
                UserModel userModel = mCallUserModelMap.remove(userId);
                if (userModel != null) {
                    mCallUserModelList.remove(userModel);
                    ToastUtil.toastLongMessage(userModel.userName + getString(R.string.line_busy));
                }
            }
        }

        @Override
        public void onCallingCancel() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.cancle_calling));
            }
            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.call_time_out));
            }
            finishActivity();
        }

        @Override
        public void onCallEnd() {
            finishActivity();
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
                TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                if (layout != null) {
                    layout.setAudioVolume(entry.getValue());
                }
            }
        }
    };

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        TUIKitLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models, Bundle bundle) {
        TUIKitLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_DATE, bundle);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /*停止语音聊天*/
    public static void stopAudioCall() {

    }


    /**
     * 主动拨打给某些用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomePeople(Context context, List<UserModel> models, String groupId) {
        TUIKitLog.i(TAG, "startCallSomePeople");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 作为用户被叫
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUIKitLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TUIKitLog.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        TUIKitLog.i(TAG, "mCallType: " + mCallType);
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // 有些手机禁止后台启动Activity，但是有bug，比如一种场景：对方反复拨打并取消，三次以上极容易从后台启动成功通话界面，
            // 此时对方再挂断时，此通话Activity调用finish后，上一个从后台启动的Activity就会弹出。此时这个Activity就不能再启动。
            TUIKitLog.w(TAG, "ignore activity launch");
            finishActivity();
            return;
        }
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        mToken = LiveShareUtil.getInstance(this).getToken();
        mPower = LiveShareUtil.getInstance(this).getPower();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.audiocall_activity_call_main);

        PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mRingtone = RingtoneManager.getRingtone(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        initView();
        initData();
        initListener();
    }

    private void finishActivity() {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        TUIKitLog.i(TAG, "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        TUIKitLog.i(TAG, "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        TUIKitLog.i(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        TUIKitLog.i(TAG, "onStop");
    }

    @Override
    public void onBackPressed() {
        TUIKitLog.i(TAG, "onBackPressed");
        // 退出这个界面的时候，需要挂断
        mITRTCAVCall.hangup();
        stopTimer();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        TUIKitLog.i(TAG, "onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.removeListener(mTRTCAudioCallListener);
        }
        stopTimeCount();
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }
    }

    private void initListener() {
        mMuteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuteMic = !isMuteMic;
                mITRTCAVCall.setMicMute(isMuteMic);
                mMuteImg.setActivated(isMuteMic);
                ToastUtil.toastLongMessage(isMuteMic ? getString(R.string.open_silent) : getString(R.string.close_silent));
            }
        });
        mHandsfreeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandsFree = !isHandsFree;
                mITRTCAVCall.setHandsFree(isHandsFree);
                mHandsfreeImg.setActivated(isHandsFree);
                ToastUtil.toastLongMessage(isHandsFree ? getString(R.string.use_speakers) : getString(R.string.use_handset));
            }
        });
        mMuteImg.setActivated(isMuteMic);
        mHandsfreeImg.setActivated(isHandsFree);
    }

    private void initData() {
        // 初始化成员变量
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAudioCallListener);
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        // 初始化从外界获取的数据
        Intent intent = getIntent();
        //自己的资料
        mSelfModel = ProfileManager.getInstance().getUserModel();
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);
        Bundle bundleExtra = intent.getBundleExtra(PARAM_DATE);
        if (bundleExtra != null) {
            select_bean = (LiveCotctBean.RetDataBean) bundleExtra.getSerializable("data");
            mNeed_sock = bundleExtra.getBoolean("need_sock");
            mLeft_second = select_bean.getDuration();
        }
        if (mCallType == TYPE_BEING_CALLED) {
            // 作为被叫
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            showWaitingResponseView();
            mVibrator.vibrate(new long[]{0, 1000, 1000}, 0);
            mRingtone.play();
        } else {
            // 主叫方
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList = params.mUserModels;
                mSponsorUserModel = mCallUserModelList.get(0);
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                }
                startInviting();
                showInvitingView();
            }
        }
    }

    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_AUDIO_CALL, mGroupId);
    }

    private void initView() {
        mMuteImg = (ImageView) findViewById(R.id.img_mute);
        mMuteLl = (LinearLayout) findViewById(R.id.ll_mute);
        mHangupImg = (ImageView) findViewById(R.id.img_hangup);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup);
        mHandsfreeImg = (ImageView) findViewById(R.id.img_handsfree);
        mHandsfreeLl = (LinearLayout) findViewById(R.id.ll_handsfree);
        mDialingImg = (ImageView) findViewById(R.id.img_dialing);
        mDialingLl = (LinearLayout) findViewById(R.id.ll_dialing);
        mLayoutManagerTrtc = (TRTCAudioLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        view_include = findViewById(R.id.audience_include);
        mDigLmTm = findViewById(R.id.dig_lm_tm);
    }


    /**
     * 等待接听界面
     */
    public void showWaitingResponseView() {
        //1. 展示对方的画面
        TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(mSponsorUserModel.userId);
        layout.setUserId(mSponsorUserModel.userName);
        GlideEngine.loadCornerImage(layout.getImageView(), mSponsorUserModel.userAvatar, null, RADIUS);
        updateUserView(mSponsorUserModel, layout);
        //2. 展示电话对应界面
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //3. 设置对应的listener
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.reject();
                finishActivity();
            }
        });
        mDialingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 聊天--主动接通了");
                if (mPower == Constant.POWER_NORMAL) {//普通
                    getUseData(true);
                    return;
                }
                mVibrator.cancel();
                mRingtone.stop();
                //1.分配自己的画面
                mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
                addUserToManager(mSelfModel);
                //2.接听电话
                mITRTCAVCall.accept();
                showCallingView();
            }
        });
        //4. 展示其他用户界面
        showOtherInvitingUserView();
    }

    /**
     * 展示邀请列表
     */
    public void showInvitingView() {
        //1. 展示自己的界面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        addUserToManager(mSelfModel);
        //2. 展示对方的画面
        for (UserModel userModel : mCallUserModelList) {
            TRTCAudioLayout layout = addUserToManager(userModel);
            layout.startLoading();
        }
        //3. 设置底部栏
        mHangupLl.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
                Log.e(TAG, "onClick: 语音未接通-主动挂断");
            }
        });
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //4. 隐藏中间他们也在界面
        hideOtherInvitingUserView();
    }

    /**
     * 展示通话中的界面
     */
    public void showCallingView() {
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.VISIBLE);
        mMuteLl.setVisibility(View.VISIBLE);

        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
                stopTimer();
                Log.e(TAG, "onClick: 语音接通后-主动挂断");
            }
        });
        showTimeCount();
        hideOtherInvitingUserView();
    }

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    if (mTimeTv != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeTv.setText(getShowTime(mTimeCount));
                            }
                        });
                    }
                    mTimeHandler.postDelayed(mTimeRunnable, 1000);
                }
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
    }

    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.isEmpty()) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.contact_avatar_width);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel userModel = mOtherInvitingUserModelList.get(index);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadCornerImage(imageView, userModel.userAvatar, null, SelectContactActivity.RADIUS);
            updateUserView(userModel, imageView);
            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private TRTCAudioLayout addUserToManager(final UserModel userModel) {
        final TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(userModel.userId);
        layout.setUserId(userModel.userName);
        GlideEngine.loadCornerImage(layout.getImageView(), userModel.userAvatar, null, RADIUS);
        updateUserView(userModel, layout);
        return layout;
    }

    private void updateUserView(final UserModel userModel, final Object layout) {
        if (!TextUtils.isEmpty(userModel.userName) && !TextUtils.isEmpty(userModel.userAvatar)) {
            return;
        }
        ArrayList<String> users = new ArrayList<>();
        users.add(userModel.userId);
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.w(TAG, "getUsersInfo code:" + "|desc:" + desc);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() != 1) {
                    TUIKitLog.w(TAG, "getUsersInfo v2TIMUserFullInfos error");
                    return;
                }
                if (TextUtils.isEmpty(userModel.userName)) {
                    userModel.userName = v2TIMUserFullInfos.get(0).getNickName();
                    if (layout instanceof TRTCAudioLayout) {
                        ((TRTCAudioLayout) layout).setUserId(v2TIMUserFullInfos.get(0).getNickName());
                    }
                }
                userModel.userAvatar = v2TIMUserFullInfos.get(0).getFaceUrl();
                if (layout instanceof TRTCAudioLayout) {
                    GlideEngine.loadCornerImage(((TRTCAudioLayout) layout).getImageView(), userModel.userAvatar, null, RADIUS);
                } else if (layout instanceof ImageView) {
                    GlideEngine.loadCornerImage((ImageView) layout, userModel.userAvatar, null, RADIUS);
                }
            }
        });
    }

    /*------------------连麦逻辑--start----------------------*/
    // TODO: 2021/6/1 充值以及消费后的逻辑还没完善--只需要修改总的钻石可用时间--充值or消费后调用我的资产
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Timer mBroadcastTimer;        // 定时的 Timer
    private UseWhatDialog mUseWhatDialog;
    private WebSocketClient mWebSocketClient;
    private static final long HEART_BEAT_RATE = 20 * 1000;
    private long sendTime = 0L;
    private LiveCotctBean.RetDataBean select_bean;
    private boolean is_lmfirst = true;
    protected long mSecond = 0;            // 连麦的时间，单位为秒
    private long all_Second = 0;             //连麦总时长
    private int proType = 0;                //消耗类型
    private String now_zuanshi = "0";                //我的钻石
    private long zuans_duration = 0;         //钻石可用连麦时长
    protected long mLeft_second = 0;
    private BroadcastTimerTask mBroadcastTimerTask;    // 定时任务
    private UserInfoBean mUserInfo;
    private String mToken;
    private View view_include;               //充值提醒
    TextView mDigLmTm;
    private int mPower;

    /**
     * 获取可用于连麦的列表--卡、包、钻
     *
     * @param is_bed false二次访问  true一次访问（接受邀请时）
     */
    private void getUseData(boolean is_bed/*boolean zhudong, boolean is_twice*/) {
        // TODO: 2021/6/2 需要改回来  目前用于测试
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContTime(mToken, mSponsorUserModel.userId, mUserInfo.getRetData().getId()), new HttpBackListener() {
            //        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContTime(mToken, mSponsorUserModel.userId, "7"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LiveCotctBean bean = new Gson().fromJson(result.toString(), LiveCotctBean.class);
                if (bean.getRetCode() == 0) {
                    if (mUseWhatDialog == null) {
                        mUseWhatDialog = new UseWhatDialog(TRTCAudioCallActivity.this, TRTCAudioCallActivity.this, bean.getRetData());
                    } else {
                        mUseWhatDialog.setDataBeans(bean.getRetData());
                    }
                    mUseWhatDialog.show();
                    mUseWhatDialog.setOnSureClickListener(new UseWhatDialog.OnSureClickListener() {
                        @Override
                        public void onSureClickListener(LiveCotctBean.RetDataBean bean) {
                            mUseWhatDialog.dismiss();
                            select_bean = bean;
                            if (is_bed) {
                                mLeft_second = select_bean.getDuration();
                                mVibrator.cancel();
                                mRingtone.stop();
                                //1.分配自己的画面
                                mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
                                addUserToManager(mSelfModel);
                                //2.接听电话
                                mITRTCAVCall.accept();
                                showCallingView();
                                initSocket();
                                if (all_Second == 0) {
                                    Toast.makeText(TRTCAudioCallActivity.this, "首次连麦，前30秒免费", Toast.LENGTH_SHORT).show();
                                    startTimer();
                                }
                            }
                           /* if (!is_twice) {
                                if (zhudong) {
                                    startLinkMic();
                                } else {
                                    joinPusher();
                                }
                            }*/
                        }
                    });
                } else {
                    ToastUtil.toastShortMessage(bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*我的资产*/
    private void getMineAsset() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getAsset(mToken, mUserInfo.getRetData().getId()), new CommonObserver<AssetBean>() {
            @Override
            public void onResult(AssetBean assetBean) {
                if (assetBean.getRetCode() == 0) {
                    AssetBean.RetDataBean data = assetBean.getRetData();
                    if (!TextUtils.isEmpty(data.getDiamond())) {
                        now_zuanshi = data.getDiamond().substring(0, data.getDiamond().indexOf("."));
                    }
                    setAssestLm();
                } else {

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

    private void setAssestLm() {
        if (all_Second != 0 && proType == 4) {//连麦中--重新计算可连麦时长并与已连麦时长进行对比--可用<已用  挂断 |  可用>已用  更新可用
            zuans_duration = Long.parseLong(now_zuanshi) * 2;
            if (zuans_duration < mSecond) {
                Toast.makeText(TRTCAudioCallActivity.this, "余额不足,已断开连线", Toast.LENGTH_SHORT).show();
                stopVideoLx();
                view_include.setVisibility(View.GONE);
            } else {
                mLeft_second = zuans_duration;
//                mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second) * 1000));
                Log.e(TAG, "mtv_ctcleftTm: " + TCUtils.duration((mLeft_second) * 1000));
            }
        }
    }

    // 初始化socket
    public void initSocket() {
        if (null == mWebSocketClient) {
            String sock_url = Constant.SOCKET_URL + mSelfModel.userId + "/" + mSponsorUserModel.userId;
            try {
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e(TAG, "Socket-onOpen: 打开");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e(TAG, "Socket-onMessage: " + message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, "Socket-onClose: " + code);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "Socket-onError: " + ex.toString());
                    }
                };
                mWebSocketClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        }
    }

    private Handler mHandler_socket = new Handler();
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if (mWebSocketClient != null) {//长连接已断开
                    if (mWebSocketClient.isClosed()) {
                        reconnectWs();
                    } else {
                        Map<String, Object> map = new HashMap<>();
//                        map.put("userId",mUserId);
//                        map.put("roomId",mPusherId);
                        map.put("tipsType", "2");//1开始计时  2心跳开始
//                        map.put("proType",select_bean.getProType());
                        Log.e(TAG, "run: heart= " + new Gson().toJson(map));
                        mWebSocketClient.send(new Gson().toJson(map));
                    }
                } else {//长连接处于连接状态
                    initSocket();
                }
                sendTime = System.currentTimeMillis();
            }
            mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler_socket.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    //如果还在继续进行连麦的话----重连
                    if (false)
                        mWebSocketClient.reconnectBlocking();
                    Log.e(TAG, "run: 重连中。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            ++mSecond;
            ++all_Second;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mtv_ctcTm.setText(TCUtils.formattedTime(mSecond));
//                    mtv_ctcTm.setText(TCUtils.formattedTime(all_Second));
                    Log.i(TAG, "mtv_ctcTm: " + TCUtils.formattedTime(all_Second));
                    if (is_lmfirst) {//首次连麦--前30秒免费
                        if (mSecond < 30) {
                            Log.e(TAG, "run:前" + mSecond);
//                            mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second) * 1000));
                            mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                            Log.e(TAG, "mtv_ctcleftTm: " + TCUtils.duration((mLeft_second) * 1000));
                            return;
                        } else if (mSecond == 30) {
                            mSecond = 0;
                            Map<String, Object> map = new HashMap<>();
                            map.put("userId", mSelfModel.userId);
                            map.put("roomId", mSponsorUserModel.userId);
                            map.put("tipsType", "1");//1开始计时  2心跳开始
                            map.put("conType", "2");//1:视频 2:语音
                            map.put("platform", "1");//1：安卓2：ios
                            map.put("proType", select_bean.getProType());
                            map.put("proId", select_bean.getProId());
                            Log.e(TAG, "run:(消耗品信息) socket " + new Gson().toJson(map));
                            if (mWebSocketClient != null && mWebSocketClient.isOpen())
                                mWebSocketClient.send(new Gson().toJson(map));
                            proType = select_bean.getProType();
                            select_bean = null;
                        }
                        Log.e(TAG, "run:30秒已过" + mSecond);
                        is_lmfirst = false;
                    } else if (select_bean != null && mLeft_second - mSecond <= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("userId", mSelfModel.userId);
                        map.put("roomId", mSponsorUserModel.userId);
                        map.put("tipsType", "1");//1开始计时  2心跳开始
                        map.put("conType", "2");//1:视频 2:语音
                        map.put("platform", "1");//1：安卓2：ios
                        map.put("proType", select_bean.getProType());
                        map.put("proId", select_bean.getProId());
                        Log.e(TAG, "run:(消耗品信息) socket " + new Gson().toJson(map));
                        if (mWebSocketClient != null && mWebSocketClient.isOpen())
                            mWebSocketClient.send(new Gson().toJson(map));
                        proType = select_bean.getProType();
                        /*选择完后--之前计时结束后重新计时*/
                        mLeft_second = select_bean.getDuration();
                        mSecond = 0;
                        select_bean = null;
                    }
//                    mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    Log.e(TAG, "mtv_ctcleftTm: " + TCUtils.duration((mLeft_second - mSecond) * 1000));
                    mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    if (mLeft_second - mSecond <= 0) {
                        stopVideoLx();
                        view_include.setVisibility(View.GONE);
                        Toast.makeText(TRTCAudioCallActivity.this, "已断开连线", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mLeft_second - mSecond == 60) {
                        view_include.setVisibility(View.GONE);
                        getUseData(false/*true,true*/);
                    } else if (mLeft_second - mSecond <= 30 && proType == 4 && select_bean == null) {//30秒且为钻石消耗时进行充值提醒
                        view_include.setVisibility(View.VISIBLE);
                    } else {
                        view_include.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void startTimer() {
        mSecond = 0;
        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        all_Second = 0;
        is_lmfirst = true;
        if (mWebSocketClient != null)
            mWebSocketClient.close();
        mHandler_socket.removeCallbacks(heartBeatRunnable);
        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
        mBroadcastTimer = null;
//        mtv_ctcTm.setText("0");
        mDigLmTm.setText("0");
        Log.i(TAG, "mtv_ctcTm: 0");
    }

    /*断开连线*/
    private void stopVideoLx() {
        stopTimer();
        mITRTCAVCall.hangup();
        finishActivity();
    }


    /*------------------连麦逻辑--end----------------------*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getCode() == PAY_SUCCESS) {
            getMineAsset();
        } else if (message.getCode() == 1005) {
            com.superc.yyfflibrary.utils.ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}

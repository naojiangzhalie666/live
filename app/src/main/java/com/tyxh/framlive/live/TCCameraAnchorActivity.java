package com.tyxh.framlive.live;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.pop_dig.BaseDialog;
import com.tyxh.framlive.pop_dig.MeslistActivity;
import com.tyxh.framlive.pop_dig.OnlineDialog;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.xzb.Constantc;
import com.tyxh.xzb.ui.TCSimpleUserInfo;
import com.tyxh.xzb.ui.TCUserAvatarListAdapter;
import com.tyxh.xzb.ui.TCVideoView;
import com.tyxh.xzb.utils.TCUtils;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
import com.tencent.liteav.audiosettingkit.AudioEffectPanel;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.liteav.demo.beauty.constant.BeautyConstants;
import com.tencent.liteav.demo.beauty.model.BeautyInfo;
import com.tencent.liteav.demo.beauty.model.ItemInfo;
import com.tencent.liteav.demo.beauty.model.TabInfo;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tyxh.xzb.utils.TCConstants.IMCMD_CONTACT;
import static com.tyxh.xzb.utils.TCConstants.IMCMD_DISCONTACT;

public class TCCameraAnchorActivity extends TCBaseAnchorActivity {
    private static final String TAG = TCCameraAnchorActivity.class.getSimpleName();
    private TXCloudVideoView mTXCloudVideoView;      // 主播本地预览的 View
    private Button mFlashView;             // 闪光灯按钮
    // 观众头像列表控件
    private RecyclerView mUserAvatarList;        // 用户头像的列表控件
    private TCUserAvatarListAdapter mAvatarListAdapter;     // 头像列表的 Adapter
    private TXCloudVideoView mtx_contact;//连麦观众窗口
    private Button mBt_contact;
    private FrameLayout mFram_contact;
    private ImageView mImgv_contact;
    private TCVideoView mTCVideoView;
    // 主播信息
    private ImageView mHeadIcon;              // 主播头像
    private ImageView mRecordBall;            // 表明正在录制的红点球
    private TextView mBroadcastTime;         // 已经开播的时间
    private TextView mMemberCount;           // 观众数量
    private AudioEffectPanel mPanelAudioControl;     // 音效面板
    private BeautyPanel mBeautyControl;          // 美颜设置的控制类
    private LinearLayout mLinearToolBar;
    // log相关
    private boolean mShowLog;               // 是否打开 log 面板
    private boolean mFlashOn;               // 是否打开闪光灯
    // 连麦主播
    private boolean mPendingRequest;        // 主播是否正在处理请求
    private List<AnchorInfo> mPusherList;            // 当前在麦上的主播
    private ObjectAnimator mObjAnim;               // 动画
    /*---------布局新增-------*/
    private TextView mtv_ctcTm, mtv_ctcget;
    private LinearLayout ll_conline;//连麦时上面的布局展示--连麦时间、剩余可用时间
    private Timer mBroadcastTimer_con;// 定时的 Timer
    private BroadcastTimerTask mBroadcastTimerTask_con;
    private int mSecond_con = 0;//连麦成功后开始计时
    private Button bt_yuyin;//语音的开启与关闭  对应 muteLocalAudio 方法
    private boolean close_locayy = false;//false 本地语音开启  true关闭
    private OnlineDialog mOnlineDialog;
    private List<TCSimpleUserInfo> mOnlin_entits;
    private int mPower;
    private ShareDialog mShareDialog;
    private BaseDialog mBase_closelm;       //确认断开连麦



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.BeautyTheme);
        super.onCreate(savedInstanceState);
//        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_CAMERA_PUSH, TCUserMgr.getInstance().getUserId(), 0, "摄像头推流", null);
        mPusherList = new ArrayList<>();
        mBeautyControl.setBeautyManager(mLiveRoom.getBeautyManager());
        BeautyInfo beautyInfo = mBeautyControl.getDefaultBeautyInfo();
        beautyInfo.setBeautyBg(BeautyConstants.BEAUTY_BG_GRAY);
        mBeautyControl.setBeautyInfo(beautyInfo);
        startPreview();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_camera_anchor);
        super.initView();
        initShare();
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.anchor_video_view);
        mTXCloudVideoView.setLogMargin(10, 10, 45, 55);
        mtx_contact = findViewById(R.id.video_contact);
        mBt_contact = findViewById(R.id.video_out1);
        mFram_contact = findViewById(R.id.video_background1);
        mImgv_contact = findViewById(R.id.video_imageview1);
        ll_conline = findViewById(R.id.video_ll_contactline);
        mtv_ctcTm = findViewById(R.id.video_ctc_time);
        mtv_ctcget = findViewById(R.id.video_ctc_get);
        bt_yuyin = findViewById(R.id.audience_btn_linkmic);
        mOnlin_entits = new ArrayList<>();
        mOnlineDialog = new OnlineDialog(this, mOnlin_entits, TCUserMgr.getInstance().getUserId());
        mOnlineDialog.setOnItemLxListener(new OnlineDialog.OnItemLxListener() {
            @Override
            public void onItemLxLixtener(String userid,String ico) {
                startLinkMic(userid,ico);
            }
        });

        mTCVideoView = new TCVideoView(mtx_contact, mBt_contact, mFram_contact, mImgv_contact, new TCVideoView.OnRoomViewListener() {
            @Override
            public void onKickUser(String userID) {
                if (userID != null) {
                    mBase_closelm.setOnItemClickListener(new BaseDialog.OnItemClickListener() {
                        @Override
                        public void onSureClickListener() {
                            for (AnchorInfo item : mPusherList) {
                                if (userID.equalsIgnoreCase(item.userID)) {
                                    onAnchorExit(item);
                                    break;
                                }
                            }
                            mLiveRoom.kickoutJoinAnchor(userID);
                        }

                        @Override
                        public void onCancelClickListener() {

                        }
                    });
                    mBase_closelm.show();


                }
            }
        });

        mUserAvatarList = (RecyclerView) findViewById(R.id.anchor_rv_avatar);
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, TCUserMgr.getInstance().getUserId());
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mFlashView = (Button) findViewById(R.id.anchor_btn_flash);

        mBroadcastTime = (TextView) findViewById(R.id.anchor_tv_broadcasting_time);
        mBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        mRecordBall = (ImageView) findViewById(R.id.anchor_iv_record_ball);
        mPower = LiveShareUtil.getInstance(this).getPower();
        mHeadIcon = (ImageView) findViewById(R.id.anchor_iv_head_icon);
        showHeadIcon(mHeadIcon, TCUserMgr.getInstance().getAvatar());
        mMemberCount = (TextView) findViewById(R.id.anchor_tv_member_counts);
        mMemberCount.setText("人气0");

        mLinearToolBar = (LinearLayout) findViewById(R.id.tool_bar);
        //AudioEffectPanel
        mPanelAudioControl = (AudioEffectPanel) findViewById(R.id.anchor_audio_control);
        mPanelAudioControl.setAudioEffectManager(mLiveRoom.getAudioEffectManager());
        mPanelAudioControl.setBackgroundColor(getResources().getColor(R.color.audio_gray_color));
        mPanelAudioControl.setOnAudioEffectPanelHideListener(new AudioEffectPanel.OnAudioEffectPanelHideListener() {
            @Override
            public void onClosePanel() {
                mPanelAudioControl.setVisibility(View.GONE);
                mLinearToolBar.setVisibility(View.VISIBLE);
            }
        });

        mBeautyControl = (BeautyPanel) findViewById(R.id.beauty_panel);
        mBeautyControl.setOnBeautyListener(new BeautyPanel.OnBeautyListener() {
            @Override
            public void onTabChange(TabInfo tabInfo, int position) {

            }

            @Override
            public boolean onClose() {
                mBeautyControl.setVisibility(View.GONE);
                mLinearToolBar.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onClick(TabInfo tabInfo, int tabPosition, ItemInfo itemInfo, int itemPosition) {
                return false;
            }

            @Override
            public boolean onLevelChanged(TabInfo tabInfo, int tabPosition, ItemInfo itemInfo, int itemPosition, int beautyLevel) {
                return false;
            }
        });
        mHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (mPower == Constant.POWER_ZIXUNSHI) {//TODO 需要知道进行直播的是咨询师还是机构
                    intent = new Intent(TCCameraAnchorActivity.this, LookPersonActivity.class);
                    intent.putExtra("is_user", false);
                } else {
                    intent = new Intent(TCCameraAnchorActivity.this, OranizeActivity.class);
                    intent.putExtra("is_user", mPower == Constant.POWER_ZIXUNJIGOU ? false : true);
                }
                intent.putExtra("query_id",mUserId);
                startActivity(intent);
            }
        });
        mBase_closelm = new BaseDialog.BaseBuild().setCancel("取消").setSure("确定").setTitle("确定断开连麦？").build(TCCameraAnchorActivity.this);
    }

    /*邀请观众进行连麦*/
    private void startLinkMic(String userid,String ico) {
        Toast.makeText(this, "连麦请求中...", Toast.LENGTH_LONG).show();
        mLiveRoom.requestJoinUserAnchor("连麦", userid, new RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                Log.i(TAG, "onAccept:观众接受已经接收连麦");
                mLiveRoom.responseJoinAnchor(userid, true, "");
                mPendingRequest = false;
                Constantc.LX_HEAD =ico;
                sendContactMsg(true, userid);
                if(mOnlineDialog!=null&&mOnlineDialog.isShowing())
                    mOnlineDialog.dismiss();
            }

            @Override
            public void onReject(String reason) {
                Toast.makeText(TCCameraAnchorActivity.this, reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimeOut() {
                Toast.makeText(TCCameraAnchorActivity.this, "连麦请求超时，观众没有做出回应", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(TCCameraAnchorActivity.this, "连麦请求发生错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        TCUtils.showPicWithUrl(this, view, avatar, R.drawable.face);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecordAnimation();
        mTCVideoView.userID = null;
        mTCVideoView.setUsed(false);
        ll_conline.setVisibility(View.GONE);
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 开始和停止推流相关
     */
    @Override
    protected void startPublish() {
        // 设置美颜参数
        BeautyParams beautyParams = new BeautyParams();
        mLiveRoom.getBeautyManager().setBeautyStyle(beautyParams.mBeautyStyle);
        mLiveRoom.getBeautyManager().setBeautyLevel(beautyParams.mBeautyLevel);
        mLiveRoom.getBeautyManager().setWhitenessLevel(beautyParams.mWhiteLevel);
        mLiveRoom.getBeautyManager().setRuddyLevel(beautyParams.mRuddyLevel);
        // 设置瘦脸参数
        mLiveRoom.getBeautyManager().setFaceSlimLevel(beautyParams.mFaceSlimLevel);
        // 设置大眼参数
        mLiveRoom.getBeautyManager().setEyeScaleLevel(beautyParams.mBigEyeLevel);
        if (TCUtils.checkRecordPermission(this)) {
            super.startPublish();
        }
    }

    @Override
    protected void stopPublish() {
        super.stopPublish();
        if (mPanelAudioControl != null) {
            mPanelAudioControl.unInit();
            mPanelAudioControl = null;
        }
    }

    @Override
    protected void onCreateRoomSuccess() {
        super.onCreateRoomSuccess();
        startRecordAnimation();
    }

    /**
     * MLVB 组件回调
     */
    @Override
    public void onAnchorEnter(final AnchorInfo pusherInfo) {
        if (pusherInfo == null || pusherInfo.userID == null) {
            return;
        }
        /*连麦成功*/
        mLiveRoom.responseJoinAnchor(pusherInfo.userID, true, "");
        mPendingRequest = false;
        Constantc.LX_HEAD =pusherInfo.userAvatar;
        sendContactMsg(true, pusherInfo.userID);
        if(mOnlineDialog!=null&&mOnlineDialog.isShowing())
            mOnlineDialog.dismiss();
        if (mGuanzDialog != null && mGuanzDialog.isShowing())
            mGuanzDialog.dismiss();



        mTCVideoView.userID = pusherInfo.userID;
        mTCVideoView.setUsed(true);
        if (mTCVideoView == null) {
            return;
        }
        if (mPusherList != null) {
            boolean exist = false;
            for (AnchorInfo item : mPusherList) {
                if (pusherInfo.userID.equalsIgnoreCase(item.userID)) {
                    exist = true;
                    break;
                }
            }
            if (exist == false) {
                mPusherList.add(pusherInfo);
            }
        }
        mTCVideoView.startLoading();
        mLiveRoom.startRemoteView(pusherInfo, mTCVideoView.videoView, new PlayCallback() {
            @Override
            public void onBegin() {
                mTCVideoView.stopLoading(true); //推流成功，stopLoading 大主播显示出踢人的button
                ll_conline.setVisibility(View.VISIBLE);
                startTimer();
            }

            @Override
            public void onError(int errCode, String errInfo) {
                mTCVideoView.stopLoading(false);
                onDoAnchorExit(pusherInfo);
            }

            @Override
            public void onEvent(int event, Bundle param) {

            }
        }); //开启远端视频渲染
    }

    @Override
    public void onAnchorExit(AnchorInfo pusherInfo) {
        onDoAnchorExit(pusherInfo);
    }

    private void onDoAnchorExit(AnchorInfo pusherInfo) {
        if (mPusherList != null) {
            Iterator<AnchorInfo> it = mPusherList.iterator();
            while (it.hasNext()) {
                AnchorInfo item = it.next();
                if (pusherInfo.userID.equalsIgnoreCase(item.userID)) {
                    it.remove();
                    break;
                }
            }
        }

        mLiveRoom.stopRemoteView(pusherInfo);//关闭远端视频渲染
        mTCVideoView.userID = null;
        mTCVideoView.setUsed(false);
        ll_conline.setVisibility(View.GONE);
        Constantc.LX_HEAD = "";
        sendContactMsg(false, "");
        stopTimer();
    }

    @Override
    public void onRequestJoinAnchor(final AnchorInfo pusherInfo, String reason) {
        BaseDialog dig = new BaseDialog.BaseBuild().setSure("接受").setCancel("拒绝").setTitle(pusherInfo.userName + "向您发起连麦请求").build(this);
        dig.setOnItemClickListener(new BaseDialog.OnItemClickListener() {
            @Override
            public void onSureClickListener() {
                mLiveRoom.responseJoinAnchor(pusherInfo.userID, true, "");
                mPendingRequest = false;
                Constantc.LX_HEAD = pusherInfo.userAvatar;
                sendContactMsg(true, pusherInfo.userID);

            }

            @Override
            public void onCancelClickListener() {
                mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "主播拒绝了您的连麦请求");
                mPendingRequest = false;
            }
        });

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPendingRequest == true) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "请稍后，主播正在处理其它人的连麦请求");
                    return;
                }

                if (mPusherList.size() >= 1) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "主播端连麦人数超过最大限制");
                    return;
                }
                dig.show();
                mPendingRequest = true;

                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dig.dismiss();
                        mPendingRequest = false;
                    }
                }, 10000);
            }
        });
    }

    /**
     * 主播连麦成功后发送该自定义消息
     *
     * @param is_lm true = 连麦中   false==中断连麦
     */

    private void sendContactMsg(boolean is_lm, String userid) {
        mLiveRoom.sendRoomCustomMsg(String.valueOf(is_lm ? IMCMD_CONTACT : IMCMD_DISCONTACT), userid, new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                if (errCode != 0) {
                    Log.e(TAG, "onError: " + errCode);
                }
            }

            @Override
            public void onSuccess() {
            }
        });

    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      音乐控制面板相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mPanelAudioControl && mPanelAudioControl.getVisibility() != View.GONE && ev.getRawY() < mPanelAudioControl.getTop()) {
            mPanelAudioControl.setVisibility(View.GONE);
            mPanelAudioControl.hideAudioPanel();
            mLinearToolBar.setVisibility(View.VISIBLE);
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      界面动画与时长统计
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {
        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {
        if (null != mObjAnim)
            mObjAnim.cancel();
    }

    @Override
    protected void onBroadcasterTimeUpdate(long second) {
        super.onBroadcasterTimeUpdate(second);
        if (!mTCSwipeAnimationController.isMoving())
            mBroadcastTime.setText(TCUtils.formattedTime(second));
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      点击事件与调用函数相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.switch_cam) {
            if (mLiveRoom != null) {
                mLiveRoom.switchCamera();
            }
        } else if (id == R.id.anchor_btn_flash) {
            if (mLiveRoom == null || !mLiveRoom.enableTorch(!mFlashOn)) {
                Toast.makeText(getApplicationContext(), "打开闪光灯失败", Toast.LENGTH_SHORT).show();
                return;
            }
            mFlashOn = !mFlashOn;
            mFlashView.setBackgroundDrawable(mFlashOn ?
                    getResources().getDrawable(R.drawable.flash_on) :
                    getResources().getDrawable(R.drawable.flash_off));
        } else if (id == R.id.beauty_btn || id == R.id.beauty_btnface) {
            if (mBeautyControl.isShown()) {
                mBeautyControl.setVisibility(View.GONE);
                mLinearToolBar.setVisibility(View.VISIBLE);
            } else {
                mBeautyControl.setVisibility(View.VISIBLE);
                mLinearToolBar.setVisibility(View.GONE);
            }
        } else if (id == R.id.btn_close) {
            if (mSecond == 0) {
                finish();
           /* }  else if(false){
                mBase_closelm.show();*/
            } else {
                showExitInfoDialog("当前正在直播，是否退出直播？", false);
            }
        } else if (id == R.id.btn_audio_ctrl) {
            if (mPanelAudioControl.isShown()) {
                mPanelAudioControl.setVisibility(View.GONE);
                mPanelAudioControl.hideAudioPanel();
                mLinearToolBar.setVisibility(View.VISIBLE);
            } else {
                mPanelAudioControl.setVisibility(View.VISIBLE);
                mPanelAudioControl.showAudioPanel();
                mLinearToolBar.setVisibility(View.GONE);
            }
        } else if (id == R.id.btn_log) {
            showLog();
        } else if (id == R.id.audience_btn_linkmic) {
            if (close_locayy) {
                bt_yuyin.setBackgroundResource(R.drawable.linkmic_on);
                mLiveRoom.muteLocalAudio(false);
            } else {
                bt_yuyin.setBackgroundResource(R.drawable.linkmic_off);
                mLiveRoom.muteLocalAudio(true);
            }
            close_locayy = !close_locayy;
        } else if (id == R.id.btn_online) {
            mOnlineDialog.show();
        }else if (id == R.id.camera_siliao) {
            startActivity(new Intent(this, MeslistActivity.class));
        } else if (id == R.id.btn_share) {
            mShareDialog.show();
        } else {
            super.onClick(v);
        }
    }


    @Override
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        stopRecordAnimation();
        super.showErrorAndQuit(errorCode, errorMsg);
    }


    private void showLog() {
        mShowLog = !mShowLog;
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.showLog(mShowLog);
        }
        ImageView liveLog = (ImageView) findViewById(R.id.btn_log);
        if (mShowLog) {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_on);

        } else {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_off);
        }
        mTCVideoView.videoView.showLog(mShowLog);
    }

    private void initShare(){
        mShareDialog = new ShareDialog(this);
        UMWeb web = new UMWeb(Constant.SHARE_URL);
        web.setTitle(Constant.SHARE_NAME);//标题
        web.setThumb(new UMImage(this, R.drawable.share_suolue));  //缩略图
        web.setDescription(Constant.SHARE_MS);//描述

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(TCCameraAnchorActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(TCCameraAnchorActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }

            @Override
            public void onWeiboClickListener() {
//                new ShareAction(LookPersonActivity.this).withMedia(web).
//                setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE/*,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,*/).setCallback(shareListener).share();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(TCCameraAnchorActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TCCameraAnchorActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TCCameraAnchorActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      成员进退房事件信息处理
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    protected void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (mAvatarListAdapter.addItem(userInfo))
            super.handleMemberJoinMsg(userInfo);
        mMemberCount.setText("人气" + String.format(Locale.CHINA, "%d", mCurrentMemberCount));
        mOnlineDialog.addItem(userInfo);
    }

    @Override
    protected void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        mAvatarListAdapter.removeItem(userInfo.userid);
        super.handleMemberQuitMsg(userInfo);
        mMemberCount.setText("人气" + String.format(Locale.CHINA, "%d", mCurrentMemberCount));
        mOnlineDialog.removeItem(userInfo.userid);
    }


    /**
     * 权限相关
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        showErrorAndQuit(-1314, "获取权限失败");
                        return;
                    }
                }
                this.startPublish();
                break;
            default:
                break;
        }
    }

    protected void startPreview() {
        // 打开本地预览，传入预览的 View
        mTXCloudVideoView.setVisibility(View.VISIBLE);
        mLiveRoom.startLocalPreview(true, mTXCloudVideoView);
    }

    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond_con;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mtv_ctcTm.setText(TCUtils.formattedTime(mSecond_con));
                    if(mSecond_con>=30) {
                        mtv_ctcget.setText((30 * (mSecond_con-30) / 60) + "");
                    }
                }
            });
        }
    }

    private void startTimer() {
        mSecond_con = 0;
        if (mBroadcastTimer_con == null) {
            mBroadcastTimer_con = new Timer(true);
            mBroadcastTimerTask_con = new BroadcastTimerTask();
            mBroadcastTimer_con.schedule(mBroadcastTimerTask_con, 1000, 1000);
        }
    }

    private void stopTimer() {
        if (null != mBroadcastTimer_con) {
            mBroadcastTimerTask_con.cancel();
        }
        mBroadcastTimer_con = null;
        mtv_ctcTm.setText("0");
        mtv_ctcget.setText("0");
    }


}

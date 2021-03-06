package com.tyxh.framlive.live;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.liteav.audiosettingkit.AudioEffectPanel;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.liteav.demo.beauty.constant.BeautyConstants;
import com.tencent.liteav.demo.beauty.model.BeautyInfo;
import com.tencent.liteav.demo.beauty.model.ItemInfo;
import com.tencent.liteav.demo.beauty.model.TabInfo;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.pop_dig.BaseDialog;
import com.tyxh.framlive.pop_dig.JxqDialog;
import com.tyxh.framlive.pop_dig.MeslistActivity;
import com.tyxh.framlive.pop_dig.OnlineDialog;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.xzb.Constantc;
import com.tyxh.xzb.important.MLVBCommonDef;
import com.tyxh.xzb.ui.TCSimpleUserInfo;
import com.tyxh.xzb.ui.TCUserAvatarListAdapter;
import com.tyxh.xzb.ui.TCVideoView;
import com.tyxh.xzb.utils.TCUtils;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
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
    private TXCloudVideoView mTXCloudVideoView;      // ????????????????????? View
    private Button mFlashView;             // ???????????????
    // ????????????????????????
    private RecyclerView mUserAvatarList;        // ???????????????????????????
    private TCUserAvatarListAdapter mAvatarListAdapter;     // ??????????????? Adapter
    private TXCloudVideoView mtx_contact;//??????????????????
    private Button mBt_contact;
    private FrameLayout mFram_contact;
    private ImageView mImgv_contact;
    private TCVideoView mTCVideoView;
    // ????????????
    private ImageView mHeadIcon;              // ????????????
    private ImageView mRecordBall;            // ??????????????????????????????
    private TextView mBroadcastTime;         // ?????????????????????
    private TextView mMemberCount;           // ????????????
    private AudioEffectPanel mPanelAudioControl;     // ????????????
    private BeautyPanel mBeautyControl;          // ????????????????????????
    private LinearLayout mLinearToolBar;
    // log??????
    private boolean mShowLog;               // ???????????? log ??????
    private boolean mFlashOn;               // ?????????????????????
    // ????????????
    private boolean mPendingRequest;        // ??????????????????????????????
    private List<AnchorInfo> mPusherList;            // ????????????????????????
    private ObjectAnimator mObjAnim;               // ??????
    /*---------????????????-------*/
    private TextView mtv_ctcTm, mtv_ctcget;
    private LinearLayout ll_conline;//??????????????????????????????--?????????????????????????????????
    private Timer mBroadcastTimer_con;// ????????? Timer
    private BroadcastTimerTask mBroadcastTimerTask_con;
    private int mSecond_con = 0;//???????????????????????????
    private Button bt_yuyin;//????????????????????????  ?????? muteLocalAudio ??????
    private boolean close_locayy = false;//false ??????????????????  true??????
    private OnlineDialog mOnlineDialog;
    private List<TCSimpleUserInfo> mOnlin_entits;
    private int mPower;
    private ShareDialog mShareDialog;
    private BaseDialog mBase_closelm;       //??????????????????


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.BeautyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_CAMERA_PUSH, TCUserMgr.getInstance().getUserId(), 0, "???????????????", null);
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
            public void onItemLxLixtener(String userid, String ico) {
                startLinkMic(userid, ico);
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
        showHeadIcon(mHeadIcon, mAvatarPicUrl);
        mMemberCount = (TextView) findViewById(R.id.anchor_tv_member_counts);
        mMemberCount.setText("??????0");

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
                if (mPower == Constant.POWER_ZIXUNSHI) {//TODO ???????????????????????????????????????????????????
                    intent = new Intent(TCCameraAnchorActivity.this, LookPersonActivity.class);
                    intent.putExtra("is_user", false);
                } else {
                    intent = new Intent(TCCameraAnchorActivity.this, OranizeActivity.class);
                    intent.putExtra("is_user",false);
                }
                intent.putExtra("query_id", mUserId);
                startActivity(intent);
            }
        });
        mBase_closelm = new BaseDialog.BaseBuild().setCancel("??????").setSure("??????").setTitle("?????????????????????").build(TCCameraAnchorActivity.this);
    }

    /*????????????????????????*/
    private void startLinkMic(String userid, String ico) {
        if(Constant.USER_STATE.equals("3")){
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "???????????????...", Toast.LENGTH_LONG).show();
        mLiveRoom.requestJoinUserAnchor("??????", userid, new RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                Log.i(TAG, "onAccept:??????????????????????????????");
                mLiveRoom.responseJoinAnchor(userid, true, "");
                mPendingRequest = false;
                Constantc.LX_HEAD = ico;
                sendContactMsg(true, userid);
                if (mOnlineDialog != null && mOnlineDialog.isShowing())
                    mOnlineDialog.dismiss();
            }

            @Override
            public void onReject(String reason) {
                Toast.makeText(TCCameraAnchorActivity.this, reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimeOut() {
                Toast.makeText(TCCameraAnchorActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(TCCameraAnchorActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param view   view
     * @param avatar ????????????
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
     * ???????????????????????????
     */
    @Override
    protected void startPublish() {
        // ??????????????????
        BeautyParams beautyParams = new BeautyParams();
        mLiveRoom.getBeautyManager().setBeautyStyle(beautyParams.mBeautyStyle);
        mLiveRoom.getBeautyManager().setBeautyLevel(beautyParams.mBeautyLevel);
        mLiveRoom.getBeautyManager().setWhitenessLevel(beautyParams.mWhiteLevel);
        mLiveRoom.getBeautyManager().setRuddyLevel(beautyParams.mRuddyLevel);
        // ??????????????????
        mLiveRoom.getBeautyManager().setFaceSlimLevel(beautyParams.mFaceSlimLevel);
        // ??????????????????
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

    JxqDialog mJxqDialog;

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) {
            stopPublish();
            mJxqDialog = new JxqDialog(this);
            mJxqDialog.setOnOutClickListener(new JxqDialog.OnOutClickListener() {
                @Override
                public void onOutListener() {
                    LiveShareUtil.getInstance(TCCameraAnchorActivity.this).clear();
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE, true);
                    startActivity(new Intent(TCCameraAnchorActivity.this, LoginActivity.class));
                    finish();
                }
            });
            mJxqDialog.show();
        } else {
            showErrorAndQuit(errorCode, errorMessage);
        }
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
        Log.e(TAG, "onWarning:???????????????????????????????????????????????????????????????????????? " );
        Toast.makeText(this, "???????????????????????????????????????????????????????????????????????? ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDebugLog(String log) {
        Log.d(TAG, log);
    }


    /**
     * MLVB ????????????
     */
    @Override
    public void onAnchorEnter(final AnchorInfo pusherInfo) {
        if (pusherInfo == null || pusherInfo.userID == null) {
            return;
        }
        /*????????????*/
        mLiveRoom.responseJoinAnchor(pusherInfo.userID, true, "");
        mPendingRequest = false;
        Constantc.LX_HEAD = pusherInfo.userAvatar;
        sendContactMsg(true, pusherInfo.userID);
        if (mOnlineDialog != null && mOnlineDialog.isShowing())
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
                mTCVideoView.stopLoading(true); //???????????????stopLoading ???????????????????????????button
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
        }); //????????????????????????
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

        mLiveRoom.stopRemoteView(pusherInfo);//????????????????????????
        mTCVideoView.userID = null;
        mTCVideoView.setUsed(false);
        ll_conline.setVisibility(View.GONE);
        Constantc.LX_HEAD = "";
        sendContactMsg(false, "");
        stopTimer();
    }
    private boolean mIsLm =false;
    @Override
    public void onRequestJoinAnchor(final AnchorInfo pusherInfo, String reason) {
        if (mIsLm) {
            mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "???????????????????????????????????????");
            return;
        }
        BaseDialog dig = new BaseDialog.BaseBuild().setSure("??????").setCancel("??????").setTitle(pusherInfo.userName + "????????????????????????").build(this);
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
                mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "?????????????????????????????????");
                mPendingRequest = false;
            }
        });

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPendingRequest == true) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "??????????????????????????????????????????????????????");
                    return;
                }

                if (mPusherList.size() >= 1) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "???????????????????????????????????????");
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
     * ?????????????????????????????????????????????
     *
     * @param is_lm true = ?????????   false==????????????
     */

    private void sendContactMsg(boolean is_lm, String userid) {
        mIsLm =is_lm;
        Constant.USER_STATE = is_lm ? "3" : "2";
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
     * //                      ????????????????????????
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
     *     //                      ???????????????????????????
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * ???????????????????????????
     */
    private void startRecordAnimation() {
        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    /**
     * ???????????????????????????
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
     * //                      ?????????????????????????????????
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
                Toast.makeText(getApplicationContext(), "?????????????????????", Toast.LENGTH_SHORT).show();
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
                showExitInfoDialog("??????????????????????????????????????????", false);
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
        } else if (id == R.id.camera_siliao) {
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

    private void initShare() {
        mShareDialog = new ShareDialog(this);
        UMWeb web = new UMWeb(Constant.SHARE_URL);
        web.setTitle(Constant.SHARE_NAME);//??????
        web.setThumb(new UMImage(this, R.drawable.share_suolue));  //?????????
        web.setDescription(Constant.SHARE_MS);//??????

        mShareDialog.setOnItemClickListener(new ShareDialog.OnItemClickListener() {
            @Override
            public void onWeQuanClickListener() {
                new ShareAction(TCCameraAnchorActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
                        .share();
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(TCCameraAnchorActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
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
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(TCCameraAnchorActivity.this, "????????????", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         * @param t ????????????
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TCCameraAnchorActivity.this, "????????????" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TCCameraAnchorActivity.this, "????????????", Toast.LENGTH_LONG).show();

        }
    };

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ?????????????????????????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    protected void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        //?????????????????? ??????false???????????????????????????????????????????????????
        if (mAvatarListAdapter.addItem(userInfo))
            super.handleMemberJoinMsg(userInfo);
        mMemberCount.setText("??????" + String.format(Locale.CHINA, "%d", mCurrentMemberCount));
        mOnlineDialog.addItem(userInfo);
    }

    @Override
    protected void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        mAvatarListAdapter.removeItem(userInfo.userid);
        super.handleMemberQuitMsg(userInfo);
        mMemberCount.setText("??????" + String.format(Locale.CHINA, "%d", mCurrentMemberCount));
        mOnlineDialog.removeItem(userInfo.userid);
    }


    /**
     * ????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        showErrorAndQuit(-1314, "??????????????????");
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
        // ???????????????????????????????????? View
        mTXCloudVideoView.setVisibility(View.VISIBLE);
        mLiveRoom.startLocalPreview(true, mTXCloudVideoView);
    }

    /**
     * ?????????
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond_con;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mtv_ctcTm.setText(TCUtils.formattedTime(mSecond_con));
                    if (mSecond_con >= 30) {
                        mtv_ctcget.setText((30 * (mSecond_con - 30) / 60) + "");
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

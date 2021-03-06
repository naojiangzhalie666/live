package com.tyxh.xzb.utils.audience;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyxh.xzb.R;
import com.tyxh.xzb.important.IMLVBLiveRoomListener;
import com.tyxh.xzb.important.MLVBCommonDef;
import com.tyxh.xzb.important.MLVBLiveRoom;
import com.tyxh.xzb.ui.ErrorDialogFragment;
import com.tyxh.xzb.ui.LiveDialogFragment;
import com.tyxh.xzb.ui.TCChatEntity;
import com.tyxh.xzb.ui.TCChatMsgListAdapter;
import com.tyxh.xzb.ui.TCSimpleUserInfo;
import com.tyxh.xzb.ui.TCUserAvatarListAdapter;
import com.tyxh.xzb.ui.TCVideoView;
import com.tyxh.xzb.ui.TCVideoViewMgr;
import com.tyxh.xzb.ui.dialog.TCInputTextMsgDialog;
import com.tyxh.xzb.ui.views.TCHeartLayout;
import com.tyxh.xzb.ui.views.TCSwipeAnimationController;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.TCDanmuMgr;
import com.tyxh.xzb.utils.TCGlobalConfig;
import com.tyxh.xzb.utils.TCUtils;
import com.tyxh.xzb.utils.login.TCELKReportMgr;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
import com.tyxh.xzb.utils.roomutil.AudienceInfo;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import master.flame.danmaku.controller.IDanmakuView;

/**
 * Module:   TCAudienceActivity
 * <p>
 * Function: ??????????????????
 * <p>
 * <p>
 * 1. MLVB ????????????????????????????????????{@link TCAudienceActivity#startPlay()} ??? {@link TCAudienceActivity#stopPlay()}
 * <p>
 * 2. MLVB ????????????????????????????????????{@link TCAudienceActivity#startLinkMic()} ??? {@link TCAudienceActivity#stopLinkMic()}
 * <p>
 * 3. ????????????????????????????????????
 **/
public class TCAudienceActivity extends Activity implements IMLVBLiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener {
    private static final String TAG = TCAudienceActivity.class.getSimpleName();
    //??????????????????
    private static final long LINK_MIC_INTERVAL = 3 * 1000;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TXCloudVideoView mTXCloudVideoView;      // ?????????????????? View
    private MLVBLiveRoom mLiveRoom;              // MLVB ??????
    // ????????????
    private TCInputTextMsgDialog mInputTextMsgDialog;    // ???????????????
    private ListView mListViewMsg;           // ??????????????????
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>(); // ??????????????????
    private TCChatMsgListAdapter mChatMsgListAdapter;    // ???????????????Adapter
    private ImageView mBtnLinkMic;            // ????????????
    private Button mBtnSwitchCamera;       // ?????????????????????
    private ImageView mIvAvatar;              // ??????????????????
    private TextView mTvPusherName;          // ??????????????????
    private TextView mMemberCount;           // ????????????????????????
    private String mPusherAvatar;          // ????????????????????????
    private long mCurrentAudienceCount;  // ??????????????????
    private long mHeartCount;            // ????????????

    private boolean mPlaying = false;       // ??????????????????
    private String mPusherNickname;        // ????????????
    private String mPusherId;              // ??????id
    private String mGroupId = "";          // ??????id
    private String mUserId = "";           // ??????id
    private String mNickname = "";         // ????????????
    private String mAvatar = "";           // ????????????
    private String mFileId = "";
    private String mTimeStamp = "";
    //??????????????????
    private RecyclerView mUserAvatarList;
    private TCUserAvatarListAdapter mAvatarListAdapter;
    //????????????
    private TCHeartLayout mHeartLayout;
    //??????????????????
    private TCFrequeControl mLikeFrequeControl;
    //??????
    private TCDanmuMgr mDanmuMgr;
    private IDanmakuView mDanmuView;
    //????????????
    private RelativeLayout mControlLayer;
    private TCSwipeAnimationController mTCSwipeAnimationController;
    private ImageView mBgImageView;
    //????????????
    private String mCoverUrl = "";
    private String mTitle = ""; //??????
    //log??????
    private boolean mShowLog;
    private boolean mIsBeingLinkMic;                    // ????????????????????????
    // ??????????????????
    private List<AnchorInfo> mPusherList = new ArrayList<>();    // ??????????????????
    private TCVideoViewMgr mVideoViewMgr;                      // ?????????????????????View?????????
    //??????
    private BeautyPanel mBeautyControl;
    private ErrorDialogFragment mErrDlgFragment = new ErrorDialogFragment();
    private LiveDialogFragment mLiveDialogFragment = new LiveDialogFragment();
    private long mStartPlayPts;

    private long mLastLinkMicTime;   // ????????????????????????????????????????????????
    /*---------------??????????????????---------------------------*/
    private BroadcastTimerTask mBroadcastTimerTask;    // ????????????
    protected long mSecond = 0;            // ??????????????????????????????
    protected long mLeft_second = 3600;
    private Timer mBroadcastTimer;        // ????????? Timer
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date, mtv_jg, mtv_title, mtv_ctcTm, mtv_ctcleftTm;
    private LinearLayout ll_conline;//??????????????????????????????--?????????????????????????????????
    private TXCloudVideoView mtx_contact;
    private FrameLayout mfram_contact;
    private ImageView mimg_contact;
    private Button mBt_contact;
    private TCVideoView mTCVideoView;//???????????????View


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartPlayPts = System.currentTimeMillis();
        setTheme(R.style.BeautyTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_xzb_audience);

        Intent intent = getIntent();
        mPusherId = intent.getStringExtra(TCConstants.PUSHER_ID);
        mGroupId = intent.getStringExtra(TCConstants.GROUP_ID);
        mPusherNickname = intent.getStringExtra(TCConstants.PUSHER_NAME);
        mPusherAvatar = intent.getStringExtra(TCConstants.PUSHER_AVATAR);
        mHeartCount = Long.decode(intent.getStringExtra(TCConstants.HEART_COUNT));
        mCurrentAudienceCount = Long.decode(intent.getStringExtra(TCConstants.MEMBER_COUNT));
        mFileId = intent.getStringExtra(TCConstants.FILE_ID);
        mTimeStamp = intent.getStringExtra(TCConstants.TIMESTAMP);
        mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
        mUserId = TCUserMgr.getInstance().getUserId();
        mNickname = TCUserMgr.getInstance().getNickname();
        mAvatar = TCUserMgr.getInstance().getAvatar();
        mCoverUrl = getIntent().getStringExtra(TCConstants.COVER_PIC);
        mVideoViewMgr = new TCVideoViewMgr(this, null);
        if (TextUtils.isEmpty(mNickname)) {
            mNickname = mUserId;
        }
        // ????????? MLVB ??????
        mLiveRoom = MLVBLiveRoom.sharedInstance(this);
        initView();
//        initGift();
        mBeautyControl.setBeautyManager(mLiveRoom.getBeautyManager());
        startPlay();
        //?????????????????????????????????????????????????????????sdk??????????????????????????????????????????????????????
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*-------???????????????????????????--------*/
    private void initMineViews() {
        mtv_name = findViewById(R.id.anchor_tv_broadcasting_name);
        mtv_gg = findViewById(R.id.anchor_tv_member_gz);
        mtv_id = findViewById(R.id.cam_id);
        mtv_date = findViewById(R.id.cam_date);
        mtv_jg = findViewById(R.id.cam_jgname);
        mtv_title = findViewById(R.id.cam_title);
        mtv_ctcTm = findViewById(R.id.video_ctc_time);
        mtv_ctcleftTm = findViewById(R.id.video_ctc_lefttm);
        ll_conline = findViewById(R.id.video_ll_contactline);
        mtx_contact = findViewById(R.id.video_player1);
        mfram_contact = findViewById(R.id.loading_background1);
        mimg_contact = findViewById(R.id.loading_imageview1);
        mBt_contact = findViewById(R.id.btn_kick_out1);
        mTCVideoView = new TCVideoView(mtx_contact, mBt_contact, mfram_contact, mimg_contact, null);


        mtv_jg.setText("??????????????????????????????");
        mtv_title.setText(mTitle);
        mtv_name.setText(mNickname);
        mtv_id.setText("??????ID???" + "124124");
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        mtv_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TCAudienceActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initView() {
        initMineViews();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.audience_play_root);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });

        mControlLayer = (RelativeLayout) findViewById(R.id.anchor_rl_controllLayer);
        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControlLayer);

        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.anchor_video_view);
        mTXCloudVideoView.setLogMargin(10, 10, 45, 55);
        mListViewMsg = (ListView) findViewById(R.id.im_msg_listview);
        mListViewMsg.setVisibility(View.VISIBLE);
        mHeartLayout = (TCHeartLayout) findViewById(R.id.heart_layout);
        mTvPusherName = (TextView) findViewById(R.id.anchor_tv_broadcasting_time);
        mTvPusherName.setText(TCUtils.getLimitString(mPusherNickname, 10));

        findViewById(R.id.anchor_iv_record_ball).setVisibility(View.GONE);

        mUserAvatarList = (RecyclerView) findViewById(R.id.anchor_rv_avatar);
        mUserAvatarList.setVisibility(View.VISIBLE);
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, mPusherId);
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mIvAvatar = (ImageView) findViewById(R.id.anchor_iv_head_icon);
        TCUtils.showPicWithUrl(this, mIvAvatar, mPusherAvatar, R.drawable.face);
        mMemberCount = (TextView) findViewById(R.id.anchor_tv_member_counts);

        mCurrentAudienceCount++;
        mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentAudienceCount));
        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mDanmuView = (IDanmakuView) findViewById(R.id.anchor_danmaku_view);
        mDanmuView.setVisibility(View.VISIBLE);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(mDanmuView);

        mBgImageView = (ImageView) findViewById(R.id.audience_background);
        mBgImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mBtnLinkMic = findViewById(R.id.anchor_liax);
        if (TCGlobalConfig.ENABLE_LINKMIC) {
            mBtnLinkMic.setVisibility(View.VISIBLE);
            mBtnLinkMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toLianx();
                }
            });
        }

        mBtnSwitchCamera = (Button) findViewById(R.id.audience_btn_switch_cam);
        mBtnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBeingLinkMic) {
                    mLiveRoom.switchCamera();
                }
            }
        });

        //????????????
        mBeautyControl = (BeautyPanel) findViewById(R.id.beauty_panel);

        TCUtils.blurBgPic(this, mBgImageView, mCoverUrl, R.drawable.bg);
    }


    /*-----????????????     ???-----*/
 /*   private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private GiftAnimatorLayout mGiftAnimatorLayout;

    private void initGift() {
        mGiftAnimatorLayout = findViewById(R.id.lottie_animator_layout);
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);

    }


    private void showGiftPanel() {
      *//*  IGiftPanelView giftPanelView = new GiftPanelViewImp(this);
        giftPanelView.init(mGiftInfoDataHandler);
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                sendGift(giftInfo);
            }

            @Override
            public void onChargeClick() {

            }

            @Override
            public void onSendClickListener(GiftInfo giftInfo) {

            }
        });
        giftPanelView.show();*//*
    }

    //?????????????????????????????????????????????????????????
    private void sendGift(final GiftInfo giftInfo) {
        GiftInfo giftInfoCopy = giftInfo.copy();
        giftInfoCopy.sendUser = getString(R.string.live_message_me);
        giftInfoCopy.sendUserHeadIcon = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621069154&t=f6b05ac96cc89d9a7db321b657bf8dbc";
        mGiftAnimatorLayout.show(giftInfoCopy);
//        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_GIFT), giftInfoCopy.giftId, new TRTCLiveRoomCallback.ActionCallback() {
//            @Override
//            public void onCallback(int code, String msg) {
//                if (code != 0) {
//                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_GIFT), giftInfoCopy.giftId, new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                if (errCode != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess() {
                TCChatEntity entity = new TCChatEntity();
                entity.setSenderName("???:");
                entity.setContent("????????????"+giftInfo.title);
                entity.setType(TCConstants.IMCMD_GIFT);
                entity.setIs_gift(true);
                notifyMsg(entity);
            }
        });
    }
*/
    /*-----????????????  ???-----*/

    private void toLianx() {
        if (mIsBeingLinkMic == false) {
            long curTime = System.currentTimeMillis();
            if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                Toast.makeText(getApplicationContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                mLastLinkMicTime = curTime;
                startLinkMic();
            }
        } else {
            stopLinkMic();
            startPlay();
        }
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ??????????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }

        stopPlay();
        mTCVideoView.userID = null;
        mTCVideoView.setUsed(false);
//        mVideoViewMgr.recycleVideoView();
//        mVideoViewMgr = null;
        stopLinkMic();

        hideNoticeToast();


        long endPushPts = System.currentTimeMillis();
        long diff = (endPushPts - mStartPlayPts) / 1000;
        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY_DURATION, TCUserMgr.getInstance().getUserId(), diff, "??????????????????", null);
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ???????????????????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    private void startPlay() {
        if (mPlaying) return;
        mLiveRoom.setSelfProfile(mNickname, mAvatar);
        mLiveRoom.setListener(this);
        mLiveRoom.enterRoom(mGroupId, mTXCloudVideoView, new IMLVBLiveRoomListener.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showErrorAndQuit("?????????????????????Error:" + errCode);
//                TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY, TCUserMgr.getInstance().getUserId(), -10001, "??????LiveRoom??????", null);
            }

            @Override
            public void onSuccess() {
                mBgImageView.setVisibility(View.GONE);
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_ENTER_LIVE), "", null);
//                TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY, TCUserMgr.getInstance().getUserId(), 10000, "??????LiveRoom??????", null);
            }
        });
        mPlaying = true;
    }

    private void stopPlay() {
        if (mPlaying && mLiveRoom != null) {
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_EXIT_LIVE), "", null);
            mLiveRoom.exitRoom(new IMLVBLiveRoomListener.ExitRoomCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    TXLog.w(TAG, "exit room error : " + errInfo);
                }

                @Override
                public void onSuccess() {
                    TXLog.d(TAG, "exit room success ");
                }
            });
            mPlaying = false;
            mLiveRoom.setListener(null);
        }
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ?????????????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */


    private void startLinkMic() {
        if (mIsBeingLinkMic) {
            return;
        }
        if (!TCUtils.checkRecordPermission(TCAudienceActivity.this)) {
            showNoticeToast("???????????????????????????????????????");
            return;
        }

        mBtnLinkMic.setEnabled(false);
        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_off);

        showNoticeToast("??????????????????......");


        mLiveRoom.requestJoinAnchor("??????", new IMLVBLiveRoomListener.RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                joinPusher();
            }

            @Override
            public void onReject(String reason) {
                mBtnLinkMic.setEnabled(true);
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, reason, Toast.LENGTH_SHORT).show();
                mIsBeingLinkMic = false;
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
            }

            @Override
            public void onTimeOut() {
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String errInfo) {
                Toast.makeText(TCAudienceActivity.this, "???????????????????????????" + errInfo, Toast.LENGTH_SHORT).show();
                hideNoticeToast();
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
            }
        });
    }

    private void joinPusher() {
//        TCVideoView videoView = mVideoViewMgr.getFirstRoomView();
        mTCVideoView.setUsed(true);
        mTCVideoView.userID = mUserId;

        mLiveRoom.startLocalPreview(true, mTCVideoView.videoView);
        mLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));

        BeautyParams beautyParams = new BeautyParams();
        mLiveRoom.getBeautyManager().setBeautyStyle(beautyParams.mBeautyStyle);
        mLiveRoom.getBeautyManager().setBeautyLevel(beautyParams.mBeautyLevel);
        mLiveRoom.getBeautyManager().setWhitenessLevel(beautyParams.mWhiteLevel);
        mLiveRoom.getBeautyManager().setRuddyLevel(beautyParams.mRuddyLevel);

        mLiveRoom.joinAnchor(new IMLVBLiveRoomListener.JoinAnchorCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                stopLinkMic();
                mBtnLinkMic.setEnabled(true);
                mIsBeingLinkMic = false;
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                Toast.makeText(TCAudienceActivity.this, "???????????????" + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                mBtnLinkMic.setEnabled(true);
                mIsBeingLinkMic = true;
                startTimer();
                ll_conline.setVisibility(View.VISIBLE);
                if (mBtnSwitchCamera != null) {
                    mBtnSwitchCamera.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void stopLinkMic() {
        if (!mIsBeingLinkMic) return;
        ll_conline.setVisibility(View.GONE);
        stopTimer();
        mIsBeingLinkMic = false;

        //????????????Button
        if (mBtnLinkMic != null) {
            mBtnLinkMic.setEnabled(true);
            mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
        }

        //?????????????????????Button
        if (mBtnSwitchCamera != null) {
            mBtnSwitchCamera.setVisibility(View.INVISIBLE);
        }
        mLiveRoom.stopLocalPreview();
        mLiveRoom.quitJoinAnchor(new IMLVBLiveRoomListener.QuitAnchorCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess() {

            }
        });

       /* if (mVideoViewMgr != null) {
            mVideoViewMgr.recycleVideoView(mUserId);
            mPusherList.clear();
        }*/
        if (mTCVideoView != null) {
            mTCVideoView.userID = null;
            mTCVideoView.setUsed(false);
            mPusherList.clear();
        }

    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      MLVB ??????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public void onAnchorEnter(final AnchorInfo pusherInfo) {
        if (pusherInfo == null || pusherInfo.userID == null) {
            return;
        }

        final TCVideoView videoView = mVideoViewMgr.applyVideoView(pusherInfo.userID);
        if (videoView == null) {
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

        videoView.startLoading();
        mLiveRoom.startRemoteView(pusherInfo, videoView.videoView, new IMLVBLiveRoomListener.PlayCallback() {
            @Override
            public void onBegin() {
                videoView.stopLoading(false); //???????????????stopLoading ????????????????????????button
            }

            @Override
            public void onError(int errCode, String errInfo) {
                videoView.stopLoading(false);
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
        mVideoViewMgr.recycleVideoView(pusherInfo.userID);
    }

    /**
     * ????????????????????????
     *
     * @param audienceInfo ??????????????????
     */
    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {

    }

    /**
     * ????????????????????????
     *
     * @param audienceInfo ??????????????????
     */
    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {

    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {
        Toast.makeText(getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_LONG).show();
        stopLinkMic();
    }

    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        handleTextMsg(userInfo, message);
    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        int type = Integer.valueOf(cmd);
        switch (type) {
            case TCConstants.IMCMD_ENTER_LIVE:
                handleAudienceJoinMsg(userInfo);
                break;
            case TCConstants.IMCMD_EXIT_LIVE:
                handleAudienceQuitMsg(userInfo);
                break;
            case TCConstants.IMCMD_PRAISE:
                handlePraiseMsg(userInfo);
                break;
            case TCConstants.IMCMD_PAILN_TEXT:
                handleTextMsg(userInfo, message);
                break;
            case TCConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, message);
                break;
            case TCConstants.IMCMD_GIFT:
                handleGiftMsg(userInfo, message);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRoomDestroy(String roomID) {
        stopLinkMic();
        showErrorAndQuit("10010");//10010?????????????????????--???????????????
    }

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM ??????????????????
            TCUtils.showKickOut(TCAudienceActivity.this);
        } else {
            showErrorAndQuit("????????????????????????Error:");
        }
    }

    /**
     * ????????????
     *
     * @param warningCode ????????? TRTCWarningCode
     * @param warningMsg  ????????????
     * @param extraInfo   ???????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {
        Log.d(TAG, log);
    }


    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      ?????????????????????????????????
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    public void handleAudienceJoinMsg(TCSimpleUserInfo userInfo) {
        //?????????????????? ??????false???????????????????????????????????????????????????
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

        mCurrentAudienceCount++;
        mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        //?????????????????????????????????
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    public void handleAudienceQuitMsg(TCSimpleUserInfo userInfo) {
        if (mCurrentAudienceCount > 0)
            mCurrentAudienceCount--;
        else
            Log.d(TAG, "????????????????????????????????????????????????");

        mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        mAvatarListAdapter.removeItem(userInfo.userid);

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();

//        entity.setSenderName("??????");
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("????????????");
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        mHeartCount++;

        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     * @param text
     */
    public void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {
        handleTextMsg(userInfo, text);
        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.avatar, userInfo.nickname, text);
        }
    }

    /**
     * ????????????????????????
     */
    private void handleGiftMsg(TCSimpleUserInfo userInfo, String giftId) {
       /* if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            *//*?????????????????????*//*
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(userInfo.nickname);
            entity.setContent("???????????? "+giftInfo.title);
            entity.setType(TCConstants.TEXT_TYPE);
            entity.setIs_gift(true);
            notifyMsg(entity);
            if (giftInfo != null) {
                if (userInfo != null) {
                    giftInfo.sendUserHeadIcon = userInfo.avatar;
                    if (!TextUtils.isEmpty(userInfo.nickname)) {
                        giftInfo.sendUser = userInfo.nickname;
                    } else {
                        giftInfo.sendUser = userInfo.userid;
                    }
                }
                mGiftAnimatorLayout.show(giftInfo);
            }
        }*/
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     * @param text
     */
    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContent(text);
        entity.setType(TCConstants.TEXT_TYPE);

        notifyMsg(entity);
    }


    /**
     * ????????????????????????
     *
     * @param entity
     */
    private void notifyMsg(final TCChatEntity entity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mArrayListChatEntity.size() > 1000) {
                    while (mArrayListChatEntity.size() > 900) {
                        mArrayListChatEntity.remove(0);
                    }
                }

                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param errorMsg
     */
    private void showErrorAndQuit(String errorMsg) {
        stopPlay();
// TODO: 2021/4/9 ??????????????????????????????????????????????????????????????????????????????
      /*  Intent rstData = new Intent();
        rstData.putExtra(TCConstants.ACTIVITY_RESULT,errorMsg);
        setResult(TCVideoListFragment.START_LIVE_PLAY,rstData);
*/
        if (errorMsg.contains("10010") && !mLiveDialogFragment.isAdded() && !this.isFinishing()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mLiveDialogFragment, "loading");
            transaction.commitAllowingStateLoss();
            return;
        }
        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);

            //??????????????????.show(...)???????????????dialogfragment?????????IllegalStateException
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                       ????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Intent rstData = new Intent();
            long memberCount = mCurrentAudienceCount - 1;
            rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
            rstData.putExtra(TCConstants.HEART_COUNT, mHeartCount);
            rstData.putExtra(TCConstants.PUSHER_ID, mPusherId);
            setResult(0, rstData);
            stopPlay();
            finish();
        } else if (id == R.id.btn_like) {
            if (mHeartLayout != null) {
                mHeartLayout.addFavor();
            }

            //????????????????????????
            if (mLikeFrequeControl == null) {
                mLikeFrequeControl = new TCFrequeControl();
                mLikeFrequeControl.init(2, 1);
            }
            if (mLikeFrequeControl.canTrigger()) {
                mHeartCount++;
                mLiveRoom.setCustomInfo(MLVBCommonDef.CustomFieldOp.INC, "praise", 1, null);
                //???ChatRoom??????????????????
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_PRAISE), "", null);
            }
        } else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        } else if (id == R.id.btn_share) {
        } else if (id == R.id.btn_log) {
            showLog();
        } else if (id == R.id.record) {
        } else if (id == R.id.retry_record) {
        } else if (id == R.id.close_record) {
        }else if (id == R.id.audience_gift) {
//            showGiftPanel();
        }
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

        mVideoViewMgr.showLog(mShowLog);
    }


    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                       ???????????????????????????
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * ??????????????????
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (display.getWidth()); //????????????
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * TextInputDialog????????????
     *
     * @param msg       ????????????
     * @param danmuOpen ??????????????????
     */
    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        //????????????
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("???:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(mAvatar, mNickname, msg);
            }
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_DANMU), msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.w(TAG, "sendRoomDanmuMsg error: " + errInfo);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomDanmuMsg success");
                }
            });
        } else {
            mLiveRoom.sendRoomTextMsg(msg, new IMLVBLiveRoomListener.SendRoomTextMsgCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.d(TAG, "sendRoomTextMsg error:");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomTextMsg success:");
                }
            });
        }
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    private Toast mNoticeToast;
    private Timer mNoticeTimer;

    private void showNoticeToast(String text) {
        if (mNoticeToast == null) {
            mNoticeToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        }

        if (mNoticeTimer == null) {
            mNoticeTimer = new Timer();
        }

        mNoticeToast.setText(text);
        mNoticeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mNoticeToast.show();
            }
        }, 0, 3000);

    }

    private void hideNoticeToast() {
        if (mNoticeToast != null) {
            mNoticeToast.cancel();
            mNoticeToast = null;
        }
        if (mNoticeTimer != null) {
            mNoticeTimer.cancel();
            mNoticeTimer = null;
        }
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                startLinkMic();
                break;
            default:
                break;
        }
    }

    /**
     * ?????????
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mtv_ctcTm.setText(TCUtils.formattedTime(mSecond));
                    mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                }
            });
        }
    }

    private void startTimer() {
        mSecond = 0;
        //????????????
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        //????????????
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }


}

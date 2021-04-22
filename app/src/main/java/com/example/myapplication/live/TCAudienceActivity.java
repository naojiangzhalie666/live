package com.example.myapplication.live;

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

import com.example.myapplication.pop_dig.BuyzDialog;
import com.example.myapplication.pop_dig.CarDialog;
import com.example.xzb.R;
import com.example.xzb.important.IMLVBLiveRoomListener;
import com.example.xzb.important.MLVBCommonDef;
import com.example.xzb.important.MLVBLiveRoom;
import com.example.xzb.ui.ErrorDialogFragment;
import com.example.xzb.ui.LiveDialogFragment;
import com.example.xzb.ui.TCChatEntity;
import com.example.xzb.ui.TCChatMsgListAdapter;
import com.example.xzb.ui.TCSimpleUserInfo;
import com.example.xzb.ui.TCUserAvatarListAdapter;
import com.example.xzb.ui.TCVideoView;
import com.example.xzb.ui.TCVideoViewMgr;
import com.example.xzb.ui.dialog.TCInputTextMsgDialog;
import com.example.xzb.ui.views.TCHeartLayout;
import com.example.xzb.ui.views.TCSwipeAnimationController;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.TCDanmuMgr;
import com.example.xzb.utils.TCGlobalConfig;
import com.example.xzb.utils.TCUtils;
import com.example.xzb.utils.audience.TCFrequeControl;
import com.example.xzb.utils.login.TCELKReportMgr;
import com.example.xzb.utils.login.TCUserMgr;
import com.example.xzb.utils.roomutil.AnchorInfo;
import com.example.xzb.utils.roomutil.AudienceInfo;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yf.xzbgift.base.Constants;
import com.yf.xzbgift.imple.DefaultGiftAdapterImp;
import com.yf.xzbgift.imple.GiftAdapter;
import com.yf.xzbgift.imple.GiftInfoDataHandler;
import com.yf.xzbgift.imple.GiftPanelDelegate;
import com.yf.xzbgift.imple.GiftPanelViewImp;
import com.yf.xzbgift.imple.IGiftPanelView;
import com.yf.xzbgift.important.GiftAnimatorLayout;
import com.yf.xzbgift.important.GiftInfo;
import com.yf.xzbgift.important.TUIKitLive;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import master.flame.danmaku.controller.IDanmakuView;

import static com.example.xzb.utils.TCConstants.IMCMD_CONTACT;
import static com.example.xzb.utils.TCConstants.IMCMD_DISCONTACT;

/**
 * Module:   TCAudienceActivity
 * <p>
 * Function: 观众观看界面
 * <p>
 * <p>
 * 1. MLVB 观众开始和停止观看主播：{@link TCAudienceActivity#startPlay()} 和 {@link TCAudienceActivity#stopPlay()}
 * <p>
 * 2. MLVB 观众开始连麦和停止连麦：{@link TCAudienceActivity#startLinkMic()} 和 {@link TCAudienceActivity#stopLinkMic()}
 * <p>
 * 3. 房间消息、弹幕、点赞处理
 **/
public class TCAudienceActivity extends Activity implements IMLVBLiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener {
    private static final String TAG = TCAudienceActivity.class.getSimpleName();
    //连麦间隔控制
    private static final long LINK_MIC_INTERVAL = 3 * 1000;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TXCloudVideoView mTXCloudVideoView;      // 观看大主播的 View
    private MLVBLiveRoom mLiveRoom;              // MLVB 组件
    // 消息相关
    private TCInputTextMsgDialog mInputTextMsgDialog;    // 消息输入框
    private ListView mListViewMsg;           // 消息列表控件
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>(); // 消息列表集合
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter
    private ImageView mBtnLinkMic;            // 连麦按钮
    private Button mBtnSwitchCamera;       // 切换摄像头按钮
    private ImageView mIvAvatar;              // 主播头像控件
    private TextView mTvPusherName;          // 主播昵称控件
    private TextView mMemberCount;           // 当前观众数量控件
    private String mPusherAvatar;          // 主播头像连接地址
    private long mCurrentAudienceCount;  // 当前观众数量
    private long mHeartCount;            // 点赞数量

    private boolean mPlaying = false;       // 是否正在播放
    private String mPusherNickname;        // 主播昵称
    private String mPusherId;              // 主播id
    private String mGroupId = "";          // 房间id
    private String mUserId = "";           // 我的id
    private String mNickname = "";         // 我的昵称
    private String mAvatar = "";           // 我的头像
    private String mFileId = "";
    private String mTimeStamp = "";
    //头像列表控件
    private RecyclerView mUserAvatarList;
    private TCUserAvatarListAdapter mAvatarListAdapter;
    //点赞动画
    private TCHeartLayout mHeartLayout;
    //点赞频率控制
    private TCFrequeControl mLikeFrequeControl;
    //弹幕
    private TCDanmuMgr mDanmuMgr;
    private IDanmakuView mDanmuView;
    //手势动画
    private RelativeLayout mControlLayer;
    private TCSwipeAnimationController mTCSwipeAnimationController;
    private ImageView mBgImageView;
    //分享相关
    private String mCoverUrl = "";
    private String mTitle = ""; //标题
    //log相关
    private boolean mShowLog;
    private boolean mIsBeingLinkMic;                    // 当前是否正在连麦
    // 麦上主播相关
    private List<AnchorInfo> mPusherList = new ArrayList<>();    // 麦上主播列表
    private TCVideoViewMgr mVideoViewMgr;                      // 主播对应的视频View管理类
    //美颜
    private BeautyPanel mBeautyControl;
    private ErrorDialogFragment mErrDlgFragment = new ErrorDialogFragment();
    private LiveDialogFragment mLiveDialogFragment = new LiveDialogFragment();
    private long mStartPlayPts;

    private long mLastLinkMicTime;   // 上次发起连麦的时间，用于频率控制
    /*---------------布局新增数据---------------------------*/
    private BroadcastTimerTask mBroadcastTimerTask;    // 定时任务
    protected long mSecond = 0;            // 连麦的时间，单位为秒
    protected long mLeft_second = 3600;
    private Timer mBroadcastTimer;        // 定时的 Timer
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date, mtv_jg, mtv_title, mtv_ctcTm, mtv_ctcleftTm;
    private LinearLayout ll_conline;//连麦时上面的布局展示--连麦时间、剩余可用时间
    private TXCloudVideoView mtx_contact;
    private FrameLayout mfram_contact;
    private ImageView mimg_contact;
    private Button mBt_contact;
    private TCVideoView mTCVideoView;//连麦时展示View
    private List<Map<String, Object>> mBuy_strs;
    private BuyzDialog mBuyzDialog;//钻石购买弹窗
    private List<Map<String, Object>> mCar_strs;
    private CarDialog mCarDialog;//购物车弹窗
    private RelativeLayout mRelativeLayout;
    private FrameLayout mfram_layout;
    private IGiftPanelView mGiftPanelView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartPlayPts = System.currentTimeMillis();
        setTheme(R.style.BeautyTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_audience);

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
        // 初始化 MLVB 组件
        mLiveRoom = MLVBLiveRoom.sharedInstance(this);
        initView();
        initGift();
        mBeautyControl.setBeautyManager(mLiveRoom.getBeautyManager());
        startPlay();
        //在这里停留，让列表界面卡住几百毫秒，给sdk一点预加载的时间，形成秒开的视觉效果
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*-------新增布局的一些设置--------*/
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
        mfram_layout = findViewById(R.id.audience_fram_contac);
        mTCVideoView = new TCVideoView(mtx_contact, mBt_contact, mfram_contact, mimg_contact, null);


        mtv_jg.setText("天宇新航心理咨询机构");
        mtv_title.setText(mTitle);
        mtv_name.setText(mNickname);
        mtv_id.setText("边框ID：" + "124124");
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        mtv_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TCAudienceActivity.this, "关注该主播", Toast.LENGTH_SHORT).show();
            }
        });
        mBuy_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mBuy_strs.add(map);
        }
        mBuyzDialog = new BuyzDialog(this, mBuy_strs);
        mCar_strs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> map = new HashMap<>();
            mCar_strs.add(map);
        }
        mCarDialog = new CarDialog(this, mCar_strs);

    }


    private void initView() {
        initMineViews();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.audience_play_root);
        mRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
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

        //美颜功能
        mBeautyControl = (BeautyPanel) findViewById(R.id.beauty_panel);

        TCUtils.blurBgPic(this, mBgImageView, mCoverUrl, R.drawable.bg);
    }


    /*-----礼物逻辑     始-----*/
    private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private GiftAnimatorLayout mGiftAnimatorLayout;

    private void initGift() {
        mGiftAnimatorLayout = findViewById(R.id.lottie_animator_layout);
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);
        mGiftPanelView = new GiftPanelViewImp(this);
        mGiftPanelView.init(mGiftInfoDataHandler);
        mGiftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                sendGift(giftInfo);
            }

            @Override
            public void onChargeClick() {
                mBuyzDialog.show();
            }
        });
    }


    private void showGiftPanel() {
        mGiftPanelView.show();
    }

    //发送礼物消息出去同时展示礼物动画和弹幕
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
                entity.setSenderName("我:");
                entity.setContent("送了一个" + giftInfo.title);
                entity.setType(TCConstants.IMCMD_GIFT);
                entity.setIs_gift(true);
                notifyMsg(entity);
            }
        });
    }

    /*-----礼物逻辑  终-----*/

    private void toLianx() {
        if (mIsBeingLinkMic == false) {
            long curTime = System.currentTimeMillis();
            if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                Toast.makeText(getApplicationContext(), "太频繁啦，休息一下！", Toast.LENGTH_SHORT).show();
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
     * //                      生命周期相关
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
        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY_DURATION, TCUserMgr.getInstance().getUserId(), diff, "直播播放时长", null);
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      开始和停止播放相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    private void startPlay() {
        if (mPlaying) return;
        mLiveRoom.setSelfProfile(mNickname, mAvatar);
        mLiveRoom.setListener(this);
        mLiveRoom.enterRoom(mGroupId, mTXCloudVideoView, new EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showErrorAndQuit("加入房间失败，Error:" + errCode);
//                TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY, TCUserMgr.getInstance().getUserId(), -10001, "进入LiveRoom失败", null);
            }

            @Override
            public void onSuccess() {
                mBgImageView.setVisibility(View.GONE);
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_ENTER_LIVE), "", null);
//                TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY, TCUserMgr.getInstance().getUserId(), 10000, "进入LiveRoom成功", null);
            }
        });
        mPlaying = true;
    }

    private void stopPlay() {
        if (mPlaying && mLiveRoom != null) {
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_EXIT_LIVE), "", null);
            mLiveRoom.exitRoom(new ExitRoomCallback() {
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
     * //                      发起和结束连麦
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */


    private void startLinkMic() {
        if (mIsBeingLinkMic) {
            return;
        }
        if (!TCUtils.checkRecordPermission(TCAudienceActivity.this)) {
            showNoticeToast("请先打开摄像头与麦克风权限");
            return;
        }

        mBtnLinkMic.setEnabled(false);
        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_off);

        showNoticeToast("等待主播接受......");


        mLiveRoom.requestJoinAnchor("连麦", new RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, "主播接受了您的连麦请求，开始连麦", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TCAudienceActivity.this, "连麦请求超时，主播没有做出回应", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String errInfo) {
                Toast.makeText(TCAudienceActivity.this, "连麦请求发生错误，" + errInfo, Toast.LENGTH_SHORT).show();
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

        mLiveRoom.joinAnchor(new JoinAnchorCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                stopLinkMic();
                mBtnLinkMic.setEnabled(true);
                mIsBeingLinkMic = false;
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                Toast.makeText(TCAudienceActivity.this, "连麦失败：" + errInfo, Toast.LENGTH_SHORT).show();
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

        //启用连麦Button
        if (mBtnLinkMic != null) {
            mBtnLinkMic.setEnabled(true);
            mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
        }

        //隐藏切换摄像头Button
        if (mBtnSwitchCamera != null) {
            mBtnSwitchCamera.setVisibility(View.INVISIBLE);
        }
        mLiveRoom.stopLocalPreview();
        mLiveRoom.quitJoinAnchor(new QuitAnchorCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess() {
                sendContactMsg(false, "");
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
     * //                      MLVB 回调
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
        mLiveRoom.startRemoteView(pusherInfo, videoView.videoView, new PlayCallback() {
            @Override
            public void onBegin() {
                videoView.stopLoading(false); //推流成功，stopLoading 小主播隐藏踢人的button
            }

            @Override
            public void onError(int errCode, String errInfo) {
                videoView.stopLoading(false);
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
        mVideoViewMgr.recycleVideoView(pusherInfo.userID);
    }

    /**
     * 收到观众进房通知
     *
     * @param audienceInfo 进房观众信息
     */
    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {

    }

    /**
     * 收到观众退房通知
     *
     * @param audienceInfo 退房观众信息
     */
    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {

    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {
        Toast.makeText(getApplicationContext(), "不好意思，您被主播踢开", Toast.LENGTH_LONG).show();
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
            case IMCMD_CONTACT:
                if (!message.equals(mUserId)) {
                    mLiveRoom.getTXLivePlayer().pause();
                    mTXCloudVideoView.setVisibility(View.GONE);
                    mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.live_contact_bg));
                    mfram_layout.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "onRecvRoomCustomMsg:开始连麦userID =  " + message);
                break;
            case IMCMD_DISCONTACT:
                if (!message.equals(mUserId)) {
                    mLiveRoom.getTXLivePlayer().resume();
                    mTXCloudVideoView.setVisibility(View.VISIBLE);
                    mRelativeLayout.setBackground(null);
                    mfram_layout.setVisibility(View.GONE);
                }
                Log.e(TAG, "onRecvRoomCustomMsg:中断连麦userID =  " + message);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRoomDestroy(String roomID) {
        stopLinkMic();
        showErrorAndQuit("10010");//10010代表房间已解散--直播已结束
    }

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM 被强制下线。
            TCUtils.showKickOut(TCAudienceActivity.this);
        } else {
            showErrorAndQuit("视频流播放失败，Error:");
        }
    }

    /**
     * 警告回调
     *
     * @param warningCode 错误码 TRTCWarningCode
     * @param warningMsg  警告信息
     * @param extraInfo   额外信息，如警告发生的用户，一般不需要关注，默认是本地错误
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
     *     //                      接收到各类的消息的处理
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 观众进房消息
     *
     * @param userInfo
     */
    public void handleAudienceJoinMsg(TCSimpleUserInfo userInfo) {
        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

        mCurrentAudienceCount++;
        mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        //左下角显示用户加入消息
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("进场了");
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * 观众退房消息
     *
     * @param userInfo
     */
    public void handleAudienceQuitMsg(TCSimpleUserInfo userInfo) {
        if (mCurrentAudienceCount > 0)
            mCurrentAudienceCount--;
        else
            Log.d(TAG, "接受多次退出请求，目前人数为负数");

        mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        mAvatarListAdapter.removeItem(userInfo.userid);

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("离开了");
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    /**
     * 收到点赞消息
     *
     * @param userInfo
     */
    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();

//        entity.setSenderName("通知");
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("点了个赞");
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        mHeartCount++;

        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * 说到弹幕消息
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
     * 处理礼物弹幕消息
     */
    private void handleGiftMsg(TCSimpleUserInfo userInfo, String giftId) {
        if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            /*发送消息到列表*/
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(userInfo.nickname);
            entity.setContent("送了一个 " + giftInfo.title);
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
        }
    }

    /**
     * 收到文本消息
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
     * 更新消息列表控件
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
     * 显示错误以及退出的弹窗
     *
     * @param errorMsg
     */
    private void showErrorAndQuit(String errorMsg) {
        stopPlay();
// TODO: 2021/4/9 需要最后进行接入时放开，播放错误时的跳转以及标识设置
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

            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                       点击事件
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

            //点赞发送请求限制
            if (mLikeFrequeControl == null) {
                mLikeFrequeControl = new TCFrequeControl();
                mLikeFrequeControl.init(2, 1);
            }
            if (mLikeFrequeControl.canTrigger()) {
                mHeartCount++;
                mLiveRoom.setCustomInfo(MLVBCommonDef.CustomFieldOp.INC, "praise", 1, null);
                //向ChatRoom发送点赞消息
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_PRAISE), "", null);
            }
        } else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        } else if (id == R.id.btn_share) {
            ToastUtil.showToast(this, "分享直播间");
        } else if (id == R.id.btn_log) {
            showLog();
        } else if (id == R.id.record) {
        } else if (id == R.id.retry_record) {
        } else if (id == R.id.close_record) {
        } else if (id == R.id.audience_gift) {
            showGiftPanel();
        } else if (id == R.id.audience_siliao) {
            ToastUtil.showToast(this, "进行私聊");
        } else if (id == R.id.audience_car) {
            mCarDialog.show();
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
     *     //                       消息输入与发送相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * TextInputDialog发送回调
     *
     * @param msg       文本信息
     * @param danmuOpen 是否打开弹幕
     */
    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        //消息回显
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(mAvatar, mNickname, msg);
            }
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_DANMU), msg, new SendRoomCustomMsgCallback() {
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
            mLiveRoom.sendRoomTextMsg(msg, new SendRoomTextMsgCallback() {
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
     * //                      弹窗消息
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
     * //                      权限管理
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
     * 记时器
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
        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }


}

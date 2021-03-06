package com.tyxh.framlive.live;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.AssetBean;
import com.tyxh.framlive.bean.BaseBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LiveCotctBean;
import com.tyxh.framlive.bean.LiveOneBean;
import com.tyxh.framlive.bean.NextLevel;
import com.tyxh.framlive.bean.RoomBean;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.BaseDialog;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.CarDialog;
import com.tyxh.framlive.pop_dig.ChathelfActivity;
import com.tyxh.framlive.pop_dig.JxqDialog;
import com.tyxh.framlive.pop_dig.ShareDialog;
import com.tyxh.framlive.pop_dig.UseWhatDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.ui.ShowGoodsActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.xzbgift.base.Constants;
import com.tyxh.framlive.xzbgift.imple.DefaultGiftAdapterImp;
import com.tyxh.framlive.xzbgift.imple.GiftAdapter;
import com.tyxh.framlive.xzbgift.imple.GiftInfoDataHandler;
import com.tyxh.framlive.xzbgift.imple.GiftPanelDelegate;
import com.tyxh.framlive.xzbgift.imple.GiftPanelViewImp;
import com.tyxh.framlive.xzbgift.imple.IGiftPanelView;
import com.tyxh.framlive.xzbgift.important.GiftAnimatorLayout;
import com.tyxh.framlive.xzbgift.important.GiftInfo;
import com.tyxh.framlive.xzbgift.important.TUIKitLive;
import com.tyxh.xzb.Constantc;
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
import com.tyxh.xzb.ui.dialog.RemindDialog;
import com.tyxh.xzb.ui.dialog.TCInputTextMsgDialog;
import com.tyxh.xzb.ui.views.TCHeartLayout;
import com.tyxh.xzb.ui.views.TCSwipeAnimationController;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.TCDanmuMgr;
import com.tyxh.xzb.utils.TCGlobalConfig;
import com.tyxh.xzb.utils.TCHTTPMgr;
import com.tyxh.xzb.utils.TCUtils;
import com.tyxh.xzb.utils.audience.TCFrequeControl;
import com.tyxh.xzb.utils.login.TCELKReportMgr;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
import com.tyxh.xzb.utils.roomutil.AudienceInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import master.flame.danmaku.controller.IDanmakuView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.bean.EventMessage.ATTEN_SUCCESS;
import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;
import static com.tyxh.xzb.utils.TCConstants.IMCMD_CONTACT;
import static com.tyxh.xzb.utils.TCConstants.IMCMD_DISCONTACT;

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
    private ImageView mBtnLinkMic, mimgv_camera;            // ????????????
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
    private String user_jigou = "";        // ????????????
    private int user_type;                 //???????????????1-???????????????2-????????????3-????????????4-?????????
    private boolean is_guanzhu;            //????????????????????????
    private boolean is_lianxianz;          //?????????????????? ---true ?????????  false????????????
    private String mPuserLxAvatar = "";     //??????????????????????????????
    private String mPuserLxUserid = "";     //????????????????????????id
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
    protected long mLeft_second = 70;
    private Timer mBroadcastTimer;        // ????????? Timer
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date, mtv_jg, mtv_title, mtv_ctcTm, mtv_ctcleftTm, mDigLmTm, mDigCz;
    private LinearLayout ll_conline;//??????????????????????????????--?????????????????????????????????
    private TXCloudVideoView mtx_contact;
    private FrameLayout mfram_contact;
    private ImageView mimg_contact;
    private ImageView mimg_defa;                //??????????????????
    private Bitmap mbit_def;
    private Button mBt_contact;
    private TCVideoView mTCVideoView;           //???????????????View
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mCar_strs;
    private CarDialog mCarDialog;                //???????????????
    private RelativeLayout mRelativeLayout;
    private FrameLayout mfram_layout;
    private ImageView mimgv_headzb, mimgv_lx;
    private IGiftPanelView mGiftPanelView;
    private String now_zuanshi = "0";                //????????????
    private TXLivePusher mTxLivePusher;
    private boolean is_open = true;
    private BaseDialog mBaseDialog;         //????????????
    private boolean mPendingRequest;        // ????????????????????????
    private ShareDialog mShareDialog;       //????????????
    private View view_include;               //????????????
    private long zuans_duration = 0;         //????????????????????????
    private BaseDialog mBase_closelm;       //??????????????????
    protected Handler mMainHandler = new Handler(Looper.getMainLooper());
    private UserInfoBean mUserInfo;
    private String mToken;
    private int dm_count = 0;
    private BaseDialog mDig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        mToken = LiveShareUtil.getInstance(this).getToken();
        getMineAsset();
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
        user_type = intent.getIntExtra(Constant.LIVE_ISJG, 0);
        is_lianxianz = intent.getBooleanExtra(Constant.LVIE_ISLIANX, false);
        if (user_type > 2) {
            user_jigou = intent.getStringExtra(Constant.LIVE_JGNAME);
        }
        mUserId = TCUserMgr.getInstance().getUserId();
        mNickname = mUserInfo.getRetData().getNickname();
//        mAvatar = TCUserMgr.getInstance().getAvatar();
        mAvatar = mUserInfo.getRetData().getIco();
        mCoverUrl = getIntent().getStringExtra(TCConstants.COVER_PIC);
        mVideoViewMgr = new TCVideoViewMgr(this, null);
        if (TextUtils.isEmpty(mNickname)) {
            mNickname = mUserId;
        }
        // ????????? MLVB ??????
        mLiveRoom = MLVBLiveRoom.sharedInstance(this);
        initView();
        initGift();
        mBeautyControl.setBeautyManager(mLiveRoom.getBeautyManager());
        startPlay();
        //?????????????????????????????????????????????????????????sdk??????????????????????????????????????????????????????
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTxLivePusher = mLiveRoom.getTxLivePusher();
        getDetail();
        mtv_gg.setVisibility(View.GONE);
        isAttention();
        toCommitWatch();
    }

    /*-------???????????????????????????--------*/
    private void initMineViews() {
        initShare();
        mtv_name = findViewById(R.id.anchor_tv_broadcasting_name);
        mtv_gg = findViewById(R.id.anchor_tv_member_gz);
        mtv_id = findViewById(R.id.cam_id);
        mtv_date = findViewById(R.id.cam_date);
        mtv_jg = findViewById(R.id.cam_jgname);
        mtv_title = findViewById(R.id.cam_title);
        mtv_ctcTm = findViewById(R.id.video_ctc_time);
        mDigLmTm = findViewById(R.id.dig_lm_tm);
        mDigCz = findViewById(R.id.textView143);
        mtv_ctcleftTm = findViewById(R.id.video_ctc_lefttm);
        ll_conline = findViewById(R.id.video_ll_contactline);
        mtx_contact = findViewById(R.id.video_player1);
        mfram_contact = findViewById(R.id.loading_background1);
        mimg_contact = findViewById(R.id.loading_imageview1);
        mimg_defa = findViewById(R.id.video_imgv);
        mBt_contact = findViewById(R.id.btn_kick_out1);
        mfram_layout = findViewById(R.id.audience_fram_contac);
        mimgv_headzb = findViewById(R.id.audience_headzb);
        mimgv_lx = findViewById(R.id.audience_headlx);
        view_include = findViewById(R.id.audience_include);
        mTCVideoView = new TCVideoView(mtx_contact, mBt_contact, mfram_contact, mimg_contact, null);

        Glide.with(this).load(mAvatar).placeholder(R.drawable.bg).error(R.drawable.bg).centerCrop().into(mimg_defa);
        Glide.with(this).load(mAvatar).centerCrop().into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                BitmapDrawable bd = (BitmapDrawable) resource;
                mbit_def = bd.getBitmap();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        if (user_type > 2) {
            mtv_jg.setVisibility(View.VISIBLE);
            mtv_jg.setText(user_jigou);
        }
        mtv_title.setText(mTitle);
        mtv_name.setText(mPusherNickname);
        mtv_id.setText("??????ID???" + mPusherId);
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        mtv_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChangeGz();
            }
        });
        mCar_strs = new ArrayList<>();
        mBase_closelm = new BaseDialog.BaseBuild().setCancel("??????").setSure("??????").setTitle("?????????????????????").build(TCAudienceActivity.this);
        mBase_closelm.setOnItemClickListener(new BaseDialog.OnItemClickListener() {
            @Override
            public void onSureClickListener() {
                stopLinkMic();
                startPlay();

            }

            @Override
            public void onCancelClickListener() {

            }
        });
        mDigCz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUseWhatDialog!=null){
                    getUseData(true, true, null);
                }
//                startActivity(new Intent(TCAudienceActivity.this, BuyzActivity.class));
            }
        });
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
        mTXCloudVideoView.setVisibility(View.GONE);
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
        Glide.with(this).load(mPusherAvatar).placeholder(R.drawable.bg).error(R.drawable.bg).centerCrop().into(mimgv_headzb);//????????????????????????
        mMemberCount = (TextView) findViewById(R.id.anchor_tv_member_counts);

        mCurrentAudienceCount++;
        mMemberCount.setText("??????" + String.format(Locale.CHINA, "%d", mCurrentAudienceCount));
        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mDanmuView = (IDanmakuView) findViewById(R.id.anchor_danmaku_view);
        mDanmuView.setVisibility(View.VISIBLE);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(mDanmuView);

        mBgImageView = (ImageView) findViewById(R.id.audience_background);
        mBgImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mBtnLinkMic = findViewById(R.id.anchor_liax);
        mimgv_camera = findViewById(R.id.anchor_camera);
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
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (user_type == 2) {//?????????
                    intent = new Intent(TCAudienceActivity.this, LookPersonActivity.class);
                } else {//????????????--?????????
                    intent = new Intent(TCAudienceActivity.this, OranizeActivity.class);
                }
                intent.putExtra("query_id", mPusherId);
                intent.putExtra("is_user", true);
                startActivity(intent);
            }
        });

        //????????????
        mBeautyControl = (BeautyPanel) findViewById(R.id.beauty_panel);

        TCUtils.blurBgPic(this, mBgImageView, mCoverUrl, R.drawable.bg);
    }

    /*-----????????????     ???-----*/
    private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private GiftAnimatorLayout mGiftAnimatorLayout;

    private void initGift() {
        mGiftAnimatorLayout = findViewById(R.id.lottie_animator_layout);
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);
    }

    private void showGiftPanel() {
        mGiftPanelView = new GiftPanelViewImp(this, mAvatar);
        mGiftPanelView.setMoney_zuan(now_zuanshi);
        getNextLevel();
        mGiftPanelView.init(mGiftInfoDataHandler);
        mGiftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                sendGift(giftInfo, true);
            }

            @Override
            public void onChargeClick() {
                startActivity(new Intent(TCAudienceActivity.this, BuyzActivity.class));
            }

            @Override
            public void onSendClickListener(GiftInfo giftInfo) {
                sendGift(giftInfo, false);
            }
        });
        mGiftPanelView.show();
    }

    //?????????????????????????????????????????????????????????
    private void sendGift(final GiftInfo giftInfo, boolean is_lw) {
        GiftInfo giftInfoCopy = giftInfo.copy();
        giftInfoCopy.sendUser = "???";
        giftInfoCopy.sendUserHeadIcon = mAvatar;
        if (is_lw) {/*????????????  ????????????????????????   ?????????????????????*/
            buyGifts(giftInfo.giftId, giftInfo.title, giftInfoCopy);

        } else {//??????????????????????????????
            sendByBack(giftInfo.giftId, giftInfo.title, giftInfoCopy);
        }
    }

    /*-----????????????  ???-----*/

    private void toLianx() {
        if (mIsBeingLinkMic == false) {
            long curTime = System.currentTimeMillis();
            if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                Toast.makeText(getApplicationContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                mLastLinkMicTime = curTime;
                getUseData(true, false, null);
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
    public void onBackPressed() {
        if (mIsBeingLinkMic) {
            mBase_closelm.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ??????????????????");
        EventBus.getDefault().unregister(this);
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }

        stopPlay();
        mVideoViewMgr.recycleVideoView();
        mVideoViewMgr = null;
        mTCVideoView.userID = null;
        mTCVideoView.setUsed(false);

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
        startTaskTimer();
        mLiveRoom.setSelfProfile(mNickname, mAvatar);
        mLiveRoom.setListener(this);
        mLiveRoom.enterRoom(mGroupId, mTXCloudVideoView, new EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showErrorAndQuit("??????????????????");
//                TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_LIVE_PLAY, TCUserMgr.getInstance().getUserId(), -10001, "??????LiveRoom??????", null);
            }

            @Override
            public void onSuccess() {
                mTXCloudVideoView.setVisibility(View.VISIBLE);
                /* //??????2??????????????????
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLiveRoom.getTXLivePlayer().resume();
                                    mTXCloudVideoView.setVisibility(View.VISIBLE);
                                    mRelativeLayout.setBackground(null);
                                    mfram_layout.setVisibility(View.GONE);
                                }
                            });
                        }
                    }, 2000 , 1000);*/

                mBgImageView.setVisibility(View.GONE);
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_ENTER_LIVE), "", null);
                getLiveDate();
            }
        });
        mPlaying = true;
    }

    /*??????????????????*/
    private void getLiveDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TCHTTPMgr.getInstance().getToken());
        map.put("userID", mUserId);
        map.put("roomID", mPusherId);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getRoomData(LiveShareUtil.getInstance(this).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LiveOneBean bean = new Gson().fromJson(result.toString(), LiveOneBean.class);
                if (bean.getRetCode() == 0) {
                    toChulLiveResult(bean.getRetData());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void toChulLiveResult(RoomBean.RetDataBean bean) {
        List<RoomBean.RetDataBean.PushersBean> pushers = bean.pushers;
        if (pushers != null && !pushers.isEmpty()) {
            if (pushers.size() > 1) {//??????????????????????????????--//????????????????????????????????????
                is_lianxianz = true;
                mPuserLxAvatar = pushers.get(1).userAvatar;
                mPuserLxUserid = pushers.get(1).userID;
                onRecvRoomCustomMsg("", "", "", mPuserLxAvatar, String.valueOf(IMCMD_CONTACT), mPuserLxUserid);
                mBtnLinkMic.setVisibility(View.GONE);
            } else {
                mBtnLinkMic.setVisibility(View.VISIBLE);
            }
        }
    }


    private void stopPlay() {
        stopTaskTimer();
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
            mLiveRoom.getTXLivePlayer().stopPlay(true);
            if (mTXCloudVideoView != null)
                mTXCloudVideoView.onDestroy();
            mTXCloudVideoView = null;
            mLiveRoom.setListener(null);
        }
    }
    /**
     * ????????????????????????  zhudong--true ????????????  false????????????
     */
    private boolean checkPublishPermission(boolean zhudong) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), zhudong?100:101);
//                Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /*????????????*/
    private void startLinkMic() {
        if (mIsBeingLinkMic) {
            return;
        }
        if (!TCUtils.checkRecordPermission(TCAudienceActivity.this)) {
            showNoticeToast("???????????????????????????????????????");
            return;
        }

        mBtnLinkMic.setEnabled(false);
//        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_off);
        mBtnLinkMic.setVisibility(View.GONE);

        showNoticeToast("??????????????????......");


        mLiveRoom.requestJoinAnchor("??????", new RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                joinPusher();
            }

            @Override
            public void onReject(String reason) {
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setVisibility(View.VISIBLE);
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, reason, Toast.LENGTH_SHORT).show();
                mIsBeingLinkMic = false;
//                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
            }

            @Override
            public void onTimeOut() {
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setVisibility(View.VISIBLE);
//                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                hideNoticeToast();
                Toast.makeText(TCAudienceActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String errInfo) {
                Toast.makeText(TCAudienceActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                hideNoticeToast();
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setVisibility(View.VISIBLE);
//                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
            }
        });
    }

    /*???????????????--????????????*/
    private void joinPusher() {
//        TCVideoView videoView = mVideoViewMgr.getFirstRoomView();
        mTCVideoView.setUsed(true);
        mTCVideoView.userID = mUserId;
        mLiveRoom.startLocalPreview(true, mTCVideoView.videoView);
        mLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
        /*--------??????????????????????????????--start-----------*/
        mTxLivePusher = mLiveRoom.getTxLivePusher();
        TXLivePushConfig config = mTxLivePusher.getConfig();
        //?????????????????????????????????--??????
        config.setPauseImg(mbit_def == null ? BitmapFactory.decodeResource(getResources(), R.drawable.bg) : mbit_def);
        config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO);
        mTxLivePusher.setConfig(config);
        mimgv_camera.setImageResource(R.drawable.audience_closecamear);
        mimg_defa.setVisibility(View.VISIBLE);
        is_open = false;
        /*--------??????????????????????????????--end-----------*/

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
                mBtnLinkMic.setVisibility(View.VISIBLE);
                mIsBeingLinkMic = false;
//                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                Toast.makeText(TCAudienceActivity.this, "???????????????" + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setVisibility(View.GONE);
                mimgv_camera.setVisibility(View.VISIBLE);
                mIsBeingLinkMic = true;
                mTxLivePusher.pausePusher();
                mLeft_second = select_bean.getDuration();
                initSocket();
                if (all_Second == 0) {
                    Toast.makeText(TCAudienceActivity.this, "??????????????????30?????????", Toast.LENGTH_SHORT).show();
                    startTimer();
                }
                ll_conline.setVisibility(View.VISIBLE);
                if (mBtnSwitchCamera != null) {
                    mBtnSwitchCamera.setVisibility(View.GONE);
                }

            }
        });
    }

    /*????????????*/
    private void stopLinkMic() {
        if (!mIsBeingLinkMic) return;
        ll_conline.setVisibility(View.GONE);
        mHandler_socket.removeCallbacks(heartBeatRunnable);
        stopTimer();
        mIsBeingLinkMic = false;
        is_open = true;
        mimgv_camera.setImageResource(R.drawable.audience_opencamear);
        mimg_defa.setVisibility(View.GONE);

        //????????????Button
        if (mBtnLinkMic != null) {
            mBtnLinkMic.setEnabled(true);
            mBtnLinkMic.setVisibility(View.VISIBLE);
//            mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
            mimgv_camera.setVisibility(View.GONE);
            mimg_defa.setVisibility(View.GONE);
        }

        //?????????????????????Button
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
     * ?????????????????????????????????????????????
     *
     * @param is_lm true = ?????????   false==????????????
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
        mLiveRoom.startRemoteView(pusherInfo, videoView.videoView, new PlayCallback() {
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

    /*????????????????????????????????????--????????????*/
    @Override
    public void onRequestJoinAnchor(AnchorInfo pusherInfo, String reason) {
        mDig = new BaseDialog.BaseBuild().setSure("??????").setCancel("??????").setTitle(pusherInfo.userName + "????????????????????????").build(this);
        mDig.setOnItemClickListener(new BaseDialog.OnItemClickListener() {
            @Override
            public void onSureClickListener() {

                getUseData(false, false, pusherInfo);
            }

            @Override
            public void onCancelClickListener() {
                mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "???????????????????????????");
                mPendingRequest = false;
            }
        });

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPendingRequest == true) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "????????????????????????????????????????????????");
                    return;
                }

                if (mPusherList.size() >= 1) {
                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, false, "??????????????????????????????");
                    return;
                }

                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isDestroyed())
                        mDig.dismiss();
                        mPendingRequest = false;
                    }
                }, 10000);
                if(!checkPublishPermission(false)){
                    return;
                }
                mDig.show();
                mPendingRequest = true;
            }
        });
    }

    /*?????????????????????*/
    @Override
    public void onKickoutJoinAnchor() {
        Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_LONG).show();
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

    /*???????????????????????????*/
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
                    // TODO: 2021/4/30 ??????????????????????????????--or???????????????????????????????????????????????????????????????????????????
                    getDetail(message);
                    mLiveRoom.getTXLivePlayer().pause();
                    mTXCloudVideoView.setVisibility(View.GONE);
                    mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.live_contact_bg));
                    mfram_layout.setVisibility(View.VISIBLE);
                    mBtnLinkMic.setVisibility(View.GONE);
                    if(mUseWhatDialog!=null&&mUseWhatDialog.isShowing()){
//                        mUseWhatDialog.dismiss();
                    }

                }
                Log.i(TAG, "onRecvRoomCustomMsg:????????????userID =  " + message);
                break;
            case IMCMD_DISCONTACT:
                if (!message.equals(mUserId)) {
                    mLiveRoom.getTXLivePlayer().resume();
                    mTXCloudVideoView.setVisibility(View.VISIBLE);
                    mRelativeLayout.setBackground(null);
                    mfram_layout.setVisibility(View.GONE);
                    mBtnLinkMic.setVisibility(View.VISIBLE);
                   /* //??????2??????????????????
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLiveRoom.getTXLivePlayer().resume();
                                    mTXCloudVideoView.setVisibility(View.VISIBLE);
                                    mRelativeLayout.setBackground(null);
                                    mfram_layout.setVisibility(View.GONE);
                                }
                            });
                        }
                    }, 2000 , 1000);*/
                }
                Log.i(TAG, "onRecvRoomCustomMsg:????????????userID =  " + message);
                break;
            default:
                break;
        }
    }

    /*??????????????????????????????*/
    private void getDetail(String id) {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, id), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        UserDetailBean.RetDataBean.UserBean user = retData.getUser();
                        if (user != null)
                            Glide.with(TCAudienceActivity.this).load(user.getIco()).placeholder(R.drawable.bg).error(R.drawable.bg).centerCrop().into(mimgv_lx);
                    }
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);

    }

    @Override
    public void onRoomDestroy(String roomID) {
        stopLinkMic();
        showErrorAndQuit("10010");//10010?????????????????????--???????????????
    }

    /*????????????Error?????????--????????????or????????????????????????*/
    JxqDialog mJxqDialog;

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM ??????????????????
            mJxqDialog = new JxqDialog(this);
            mJxqDialog.setOnOutClickListener(new JxqDialog.OnOutClickListener() {
                @Override
                public void onOutListener() {
                    LiveShareUtil.getInstance(TCAudienceActivity.this).clear();
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE, true);
                    startActivity(new Intent(TCAudienceActivity.this, LoginActivity.class));
                    finish();
                }
            });
            mJxqDialog.show();
        } else {
            showErrorAndQuit("??????????????????????????????");
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
        mMemberCount.setText("??????" + String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        //?????????????????????????????????
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_ENTER);
        entity.setHead(userInfo.avatar);
        notifyMsg(entity);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    public void handleAudienceQuitMsg(TCSimpleUserInfo userInfo) {
        //????????????????????????
//        if (mCurrentAudienceCount > 0)
//            mCurrentAudienceCount--;
//        else
//            Log.d(TAG, "????????????????????????????????????????????????");

        mMemberCount.setText("??????" + String.format(Locale.CHINA, "%d", mCurrentAudienceCount));

        mAvatarListAdapter.removeItem(userInfo.userid);

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_EXIT);
        entity.setHead(userInfo.avatar);
        /*??????????????????*/
//        notifyMsg(entity);
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
        entity.setHead(userInfo.avatar);
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
        if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            /*?????????????????????*/
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(userInfo.nickname);
            entity.setContent("???????????? " + giftInfo.title);
            entity.setType(TCConstants.TEXT_TYPE);
            entity.setIs_gift(true);
            entity.setHead(userInfo.avatar);
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
        EventBus.getDefault().post(new EventMessage("chathelf_finish"));
        stopLinkMic();
        stopPlay();
// TODO: 2021/4/9 ??????????????????????????????????????????????????????????????????????????????
      /*  Intent rstData = new Intent();
        rstData.putExtra(TCConstants.ACTIVITY_RESULT,errorMsg);
        setResult(TCVideoListFragment.START_LIVE_PLAY,rstData);
*/
        if (errorMsg.contains("10010") && !this.isFinishing()) {
            RemindDialog mRemindDialog = new RemindDialog(this);
            mRemindDialog.show();
            mRemindDialog.setOnllClickListenenr(new RemindDialog.OnllClickListenenr() {
                @Override
                public void onllClickListener() {
                    mRemindDialog.dismiss();
                    TCAudienceActivity.this.finish();
                }
            });
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if (mRemindDialog != null&&!isDestroyed())
                        mRemindDialog.dismiss();
                    TCAudienceActivity.this.finish();
                }
            }, 3000, 1000);
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.add(mLiveDialogFragment, "loading");
//            transaction.commitAllowingStateLoss();
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

    //    ????????????
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            if (!mIsBeingLinkMic) {
                Intent rstData = new Intent();
                long memberCount = mCurrentAudienceCount - 1;
                rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                rstData.putExtra(TCConstants.HEART_COUNT, mHeartCount);
                rstData.putExtra(TCConstants.PUSHER_ID, mPusherId);
                setResult(0, rstData);
                stopPlay();
                finish();
            } else {
                mBase_closelm.show();
            }
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
            mShareDialog.show();
        } else if (id == R.id.btn_log) {
            showLog();
        } else if (id == R.id.record) {
        } else if (id == R.id.retry_record) {
        } else if (id == R.id.close_record) {
        } else if (id == R.id.audience_gift) {
            showGiftPanel();
        } else if (id == R.id.audience_siliao) {
            startChatActivity(mPusherId, mPusherNickname);
        } else if (id == R.id.audience_car) {
            if (mCarDialog != null) {
                mCarDialog.show();
            } else {
                Toast.makeText(this, "??????????????????,???????????????", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.anchor_camera) {
            mTxLivePusher = mLiveRoom.getTxLivePusher();
            if (is_open) {
                TXLivePushConfig config = mTxLivePusher.getConfig();
                //?????????????????????????????????--??????
                config.setPauseImg(mbit_def == null ? BitmapFactory.decodeResource(getResources(), R.drawable.bg) : mbit_def);
                config.setPauseImg(86400,5);
                config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO);
                mTxLivePusher.setConfig(config);
                mimgv_camera.setImageResource(R.drawable.audience_closecamear);
                mimg_defa.setVisibility(View.VISIBLE);
                mTxLivePusher.pausePusher();
            } else {
                mTxLivePusher.resumePusher();
                mimgv_camera.setImageResource(R.drawable.audience_opencamear);
                mimg_defa.setVisibility(View.GONE);
            }
            is_open = !is_open;
        }
    }

    private void startChatActivity(String pusherId, String pusherNickname) {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(pusherId);
        chatInfo.setChatName(pusherNickname);
        Intent intent = new Intent(this, ChathelfActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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
        entity.setHead(mAvatar);
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
                    //????????????????????????--??????3??????????????????
                    dm_count += 1;
                    if (dm_count == 3)
                        toGoTask(5, "3");
                }
            });
        }
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
                new ShareAction(TCAudienceActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
                        .share();
                toGoTask(7, "1");
            }

            @Override
            public void onWechatClickListener() {
                new ShareAction(TCAudienceActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//????????????
                        .withMedia(web)
                        .setCallback(shareListener)//???????????????
                        .share();
                toGoTask(7, "1");
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
            Toast.makeText(TCAudienceActivity.this, "????????????", Toast.LENGTH_LONG).show();
            /*???????????????????????????*/
            toGoTask(7, "1");
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         * @param t ????????????
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TCAudienceActivity.this, "????????????" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption ?????????????????????
         * @param platform ????????????
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TCAudienceActivity.this, "????????????", Toast.LENGTH_LONG).show();

        }
    };


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
                if(!checkPublishPermission(true)){
                    return;
                }
                startLinkMic();
                break;
            case 101:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                if(mDig!=null) {
                    mDig.show();
                    mPendingRequest = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMineAsset();
        if (mCarDialog != null)
            mCarDialog.getNextLevel();
        Log.e(TAG, "onRestart: ");
    }

    /*-----------????????????---------------*/
    private void getDetail() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, mPusherId), new CommonObserver<UserDetailBean>() {
            @Override
            public void onResult(UserDetailBean result) {
                Log.d(TAG, new Gson().toJson(result));
                if (result.getRetCode() == 0) {
                    UserDetailBean.RetDataBean retData = result.getRetData();
                    if (retData != null) {
                        setUserDatil(retData);
                    }
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
        List<UserDetailBean.RetDataBean.ServicePackagesBean> servicePackages = data.getServicePackages();
        if (servicePackages != null) {
            mCar_strs.clear();
            mCar_strs.addAll(servicePackages);
            mCarDialog = new CarDialog(this, mCar_strs);
            mCarDialog.setOnAdaClickListener(new CarDialog.OnAdaClickListener() {
                @Override
                public void onItemClickListener(int pos) {
                    UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mCar_strs.get(pos);
                    Intent intt = new Intent(TCAudienceActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "????????????-" : "?????????-") + mPusherNickname);
                    intt.putExtra("query_id", mPusherId);
                    intt.putExtra("is_user", true);
                    startActivity(intt);//??????????????????
                }

                @Override
                public void onTiyanClickListener(int pos) {

                    UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mCar_strs.get(pos);
                    Intent intt = new Intent(TCAudienceActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "????????????-" : "?????????-") + mPusherNickname);
                    intt.putExtra("query_id", mPusherId);
                    intt.putExtra("is_user", true);
                    startActivity(intt);//??????????????????
//                    Toast.makeText(TCAudienceActivity.this, "??????"+servicePackagesBean.getId(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMoreClickListener() {
                    if (user_type > 2) {
                        Intent int_orgi = new Intent(TCAudienceActivity.this, OranizeActivity.class);
                        int_orgi.putExtra("is_user", true);
                        int_orgi.putExtra("query_id", mPusherId);
                        startActivity(int_orgi);//??????????????????
                    } else {
                        Intent int_person = new Intent(TCAudienceActivity.this, LookPersonActivity.class);
                        int_person.putExtra("is_user", true);
                        int_person.putExtra("query_id", mPusherId);
                        startActivity(int_person);//???????????????
                    }
                }
            });

        }
    }

    /*????????????????????????*/
    private void isAttention() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().isAttention(mToken, mPusherId), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    is_guanzhu = (boolean) baseBean.getRetData();
                    mtv_gg.setVisibility(View.VISIBLE);
                    mtv_gg.setText(is_guanzhu ? "?????????" : "??????");
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    /*????????????????????????????????????*/
    private void toChangeGz() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().addLiveAtten(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), mPusherId), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    is_guanzhu = !is_guanzhu;
                    mtv_gg.setText(is_guanzhu ? "?????????" : "??????");
                }
                ToastUtil.showToast(TCAudienceActivity.this, baseBean.getRetMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*?????????????????????*/
    private void buyGifts(String giftId, String title, GiftInfo giftInfoCopy) {
        Map<String, Object> map = new HashMap<>();
        map.put("proId", Double.parseDouble(giftId));    //??????id
        map.put("proNum", "1");      //????????????
        map.put("roomId", Long.parseLong(mPusherId));//??????id
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().buyGifts(mToken, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    getMineAsset();
                    toUpdate(giftId, title, giftInfoCopy);
                 /*   //????????????????????????--??????3??????????????????
                    dm_count+=1;
                    if(dm_count==3)
                        toGoTask(5,"3");*/
                } else {
                    ToastUtil.showToast(TCAudienceActivity.this, baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*??????????????????*/
    private void sendByBack(String giftId, String title, GiftInfo giftInfoCopy) {
        Map<String, Object> map = new HashMap<>();
        map.put("proId", Double.parseDouble(giftId));    //??????id
        map.put("roomId", Long.parseLong(mPusherId));//??????id
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().useGifts(mToken, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean baseBean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (baseBean.getRetCode() == 0) {
                    mGiftPanelView.notiBbGift();
                    toUpdate(giftId, title, giftInfoCopy);
                } else {
                    ToastUtil.showToast(TCAudienceActivity.this, baseBean.getRetMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /*????????????  ???????????????*/
    private void toUpdate(String id, String title, GiftInfo giftInfoCopy) {
        mGiftAnimatorLayout.show(giftInfoCopy);
        getNextLevel();
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_GIFT), id, new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                if (errCode != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), "??????????????????[%1$d]:%2$s", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess() {
                TCChatEntity entity = new TCChatEntity();
                entity.setSenderName("???:");
                entity.setContent("????????????" + title);
                entity.setType(TCConstants.IMCMD_GIFT);
                entity.setIs_gift(true);
                entity.setHead(mAvatar);
                notifyMsg(entity);
            }
        });
    }

    private void getNextLevel() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getMyNextLevel(mToken), new CommonObserver<NextLevel>() {
            @Override
            public void onResult(NextLevel bean) {
                if (bean.getRetCode() == 0 && bean != null) {
                    BigDecimal now_exp = new BigDecimal(bean.getRetData().getCurExp());
                    BigDecimal tall_exp = new BigDecimal(bean.getRetData().getExp());
                    BigDecimal last_exp = tall_exp.subtract(now_exp);
                    mGiftPanelView.setJingYAndNeedZunas(now_exp.doubleValue(), last_exp.doubleValue());
                } else {
                    ToastUtil.showToast(TCAudienceActivity.this, bean.getRetMsg());
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

    /*??????????????????--??????????????????*/
    private void toCommitWatch() {
        Map<String, Object> map = new HashMap<>();
        map.put("roomId", mPusherId);
        map.put("userId", mUserId);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().watchHis(mToken, requestBody), new HttpBackListener() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getCode() == PAY_SUCCESS) {
            if (mCarDialog != null) {
                mCarDialog.getMineAsset();
            }
            getMineAsset();
        } else if (message.getCode() == ATTEN_SUCCESS) {//????????????????????????
            isAttention();
        } else if (message.getCode() == 1005) {
            ToastUtil.showToast(this, "??????????????????????????????!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /*------------------????????????--start----------------------*/
    // TODO: 2021/6/1 ??????????????????????????????????????????--???????????????????????????????????????--??????or???????????????????????????
    private UseWhatDialog mUseWhatDialog;
    private WebSocketClient mWebSocketClient;
    private static final long HEART_BEAT_RATE = 20 * 1000;
    private long sendTime = 0L;
    private LiveCotctBean.RetDataBean select_bean;
    private boolean is_lmfirst = true;
    protected long mSecond = 0;            // ??????????????????????????????
    private long all_Second = 0;             //???????????????
    private int proType = 0;                //????????????

    /*??????????????????????????????--???????????????*/
    private void getUseData(boolean zhudong, boolean is_twice, AnchorInfo pusherInfo) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContTime(mToken, mPusherId, mUserInfo.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LiveCotctBean bean = new Gson().fromJson(result.toString(), LiveCotctBean.class);
                if (bean.getRetCode() == 0) {
                    if (mUseWhatDialog == null) {
                        mUseWhatDialog = new UseWhatDialog(TCAudienceActivity.this, TCAudienceActivity.this, bean.getRetData());
                    } else {
                        if(!mIsBeingLinkMic){
                            mUseWhatDialog.reset();
                        }
                        mUseWhatDialog.setDataBeans(bean.getRetData());
                    }
                    mUseWhatDialog.show();
                    mUseWhatDialog.setOnSureClickListener(new UseWhatDialog.OnSureClickListener() {
                        @Override
                        public void onSureClickListener(LiveCotctBean.RetDataBean bean) {
                            mUseWhatDialog.dismiss();
                            select_bean = bean;
                            if (!is_twice) {
                                if (zhudong) {
                                    startLinkMic();
                                } else {
                                    mLiveRoom.responseJoinAnchor(pusherInfo.userID, true, "");
                                    mPendingRequest = false;
                                    Constantc.LX_HEAD = pusherInfo.userAvatar;
                                    sendContactMsg(true, pusherInfo.userID);
                                    joinPusher();
                                }
                            }
                        }
                    });
                    mUseWhatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if(is_twice&& select_bean == null&&mIsBeingLinkMic){
                                view_include.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    ToastUtil.showToast(TCAudienceActivity.this, bean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*????????????*/
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
                    if (mGiftPanelView != null)
                        mGiftPanelView.setMoney_zuan(now_zuanshi);
                   /* money =String.valueOf(data.getBalance());
                    mMineZuanshi.setText(zuan);
                    mMineMoneyTv.setText(money);
                    mMineDaojutv.setText(String.valueOf(data.getPropCount()));*/
                } else {

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

    private void setAssestLm() {
        if (all_Second != 0 && proType == 4) {//?????????--????????????????????????????????????????????????????????????--??????<??????  ?????? |  ??????>??????  ????????????
            zuans_duration = Long.parseLong(now_zuanshi) * 2;
            if (zuans_duration < mSecond) {
                Toast.makeText(TCAudienceActivity.this, "????????????,???????????????", Toast.LENGTH_SHORT).show();
                stopLinkMic();
                view_include.setVisibility(View.GONE);
            } else {
                mLeft_second = zuans_duration;
                mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second) * 1000));
            }
        }
    }

    // ?????????socket
    public void initSocket() {
        if (null == mWebSocketClient) {
            String sock_url = Constant.SOCKET_URL + mUserId + "/" + mPusherId;
            try {
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e(TAG, "Socket-onOpen: ??????");
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
            mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//??????????????????
        }
    }

    private Handler mHandler_socket = new Handler();
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if (mWebSocketClient != null) {//??????????????????
                    if (mWebSocketClient.isClosed()) {
//                        reconnectWs();
                        if(!isDestroyed()&&all_Second!=0)
                        ToastUtil.showToast(TCAudienceActivity.this,"????????????????????????,???????????????");
                        stopLinkMic();
                    } else {
                        Map<String, Object> map = new HashMap<>();
//                        map.put("userId",mUserId);
//                        map.put("roomId",mPusherId);
                        map.put("tipsType", "2");//1????????????  2????????????
//                        map.put("proType",select_bean.getProType());
                        mWebSocketClient.send(new Gson().toJson(map));
                        Log.i(TAG, new Gson().toJson(map));
                    }
                } else {//???????????????????????????
                    initSocket();
                }
                sendTime = System.currentTimeMillis();
            }
            mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    };

    /**
     * ????????????
     */
    private void reconnectWs() {
        mHandler_socket.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    //????????????????????????????????????----??????
                    if (all_Second != 0)
                        mWebSocketClient.reconnectBlocking();
                    Log.e(TAG, "run: ??????????????????");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * ?????????
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            ++mSecond;
            ++all_Second;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mtv_ctcTm.setText(TCUtils.formattedTime(mSecond));
                    mtv_ctcTm.setText(TCUtils.formattedTime(all_Second));
                    if (is_lmfirst) {//????????????--???30?????????
                        if (mSecond < 30) {
//                            Log.e(TAG, "run:???"+mSecond );
                            mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second) * 1000));
                            mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                            return;
                        } else if (mSecond == 30) {
                            mSecond = 0;
                            Map<String, Object> map = new HashMap<>();
                            map.put("userId", mUserId);
                            map.put("roomId", mPusherId);
                            map.put("tipsType", "1");//1????????????  2????????????
                            map.put("conType", "1");//1:?????? 2:??????
                            map.put("platform", "1");//1?????????2???ios
                            map.put("proType", select_bean.getProType());
                            map.put("proId", select_bean.getProId());
                            try {
                                mWebSocketClient.send(new Gson().toJson(map));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            proType = select_bean.getProType();
                            select_bean = null;
                        }
//                        Log.e(TAG, "run:???"+mSecond);
                        is_lmfirst = false;
                    } else if (select_bean != null && mLeft_second - mSecond <= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("userId", mUserId);
                        map.put("roomId", mPusherId);
                        map.put("tipsType", "1");//1????????????  2????????????
                        map.put("conType", "1");//1:?????? 2:??????
                        map.put("platform", "1");//1?????????2???ios
                        map.put("proType", select_bean.getProType());
                        map.put("proId", select_bean.getProId());
                        mWebSocketClient.send(new Gson().toJson(map));
                        proType = select_bean.getProType();
                        /*????????????--?????????????????????????????????*/
                        mLeft_second = select_bean.getDuration();
                        mSecond = 0;
                        select_bean = null;
                    }
                    mtv_ctcleftTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    if (mLeft_second - mSecond <= 0) {
                        stopLinkMic();
                        view_include.setVisibility(View.GONE);
                        Toast.makeText(TCAudienceActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                   if (mLeft_second - mSecond == 60) {//60?????????????????????
                        getUseData(true, true, null);
                    }else if(mLeft_second - mSecond <= 60 && select_bean == null) {
                       if(mUseWhatDialog!=null&&!mUseWhatDialog.isShowing())
                       view_include.setVisibility(View.VISIBLE);
                   } else {
                        view_include.setVisibility(View.GONE);
                    }
                  /*  //30??????????????????????????????????????????
                  if (mLeft_second - mSecond == 60) {
                        view_include.setVisibility(View.GONE);
                        getUseData(true, true, null);
                    } else if (mLeft_second - mSecond <= 30 && proType == 4 && select_bean == null) {//30??????????????????????????????????????????
                        view_include.setVisibility(View.VISIBLE);
                    } else {
                        view_include.setVisibility(View.GONE);
                    }*/
                }
            });
        }
    }

    private void startTimer() {
        mSecond = 0;
        Constant.USER_STATE = "3";
        //????????????
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        all_Second = 0;
        is_lmfirst = true;
        Constant.USER_STATE = "1";
        if (mWebSocketClient != null) {
            mWebSocketClient.close();
        }
        view_include.setVisibility(View.GONE);
        if(mUseWhatDialog!=null&&mUseWhatDialog.isShowing()&&!isDestroyed()){
            mUseWhatDialog.dismiss();
        }
        mHandler_socket.removeCallbacks(heartBeatRunnable);
        //????????????
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
        mBroadcastTimer = null;
        mtv_ctcTm.setText("0");
        mDigLmTm.setText("0");
    }

    private int task_second = 0;
    private BroadcastTimerTaskTask mTask_broas;
    private Timer mBroad_task;// ????????? Timer

    /**
     * ?????????
     */
    private class BroadcastTimerTaskTask extends TimerTask {
        public void run() {
            ++task_second;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (task_second >= 300) {
                        toGoTask(2, "300");
                        stopTaskTimer();
                    }
                }
            });
        }
    }

    /*?????????????????????--???5???????????????*/
    private void startTaskTimer() {
        task_second = 0;
        if (mBroad_task == null) {
            mBroad_task = new Timer(true);
            mTask_broas = new BroadcastTimerTaskTask();
            mBroad_task.schedule(mTask_broas, 1000, 1000);
        }
    }

    private void stopTaskTimer() {
        if (null != mBroad_task) {
            mBroad_task.cancel();
        }
        mBroad_task = null;
        mtv_ctcTm.setText("0");
    }

    /*????????????
     * ???????????????2:????????????;5:?????????;7:????????????;8:???????????????
     * */
    private void toGoTask(int type, String duration) {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().toWhildTask(mToken, type, duration), new HttpBackListener() {
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


    /*------------------????????????--end----------------------*/
    /* mBaseDialog = new BaseDialog.BaseBuild().setCancel("??????").setSure("?????????").setTitle("????????????????????????,???????????????").build(TCAudienceActivity.this);
                        mBaseDialog.setOnItemClickListener(new BaseDialog.OnItemClickListener() {
                            @Override
                            public void onSureClickListener() {
                                startActivity(new Intent(TCAudienceActivity.this, BuyzActivity.class));
                            }

                            @Override
                            public void onCancelClickListener() {

                            }
                        });
                        mBaseDialog.show();*/


}

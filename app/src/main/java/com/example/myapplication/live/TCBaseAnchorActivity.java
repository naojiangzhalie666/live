package com.example.myapplication.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.pop_dig.GuanzDialog;
import com.example.myapplication.pop_dig.ReportActivity;
import com.example.xzb.R;
import com.example.xzb.adapter.QianAdapter;
import com.example.xzb.important.IMLVBLiveRoomListener;
import com.example.xzb.important.MLVBCommonDef;
import com.example.xzb.important.MLVBLiveRoom;
import com.example.xzb.important.MLVBLiveRoomImpl;
import com.example.xzb.ui.ErrorDialogFragment;
import com.example.xzb.ui.FinishDetailDialogFragment;
import com.example.xzb.ui.TCChatEntity;
import com.example.xzb.ui.TCChatMsgListAdapter;
import com.example.xzb.ui.TCSimpleUserInfo;
import com.example.xzb.ui.dialog.LiveCloseDialog;
import com.example.xzb.ui.dialog.TCInputTextMsgDialog;
import com.example.xzb.ui.views.TCHeartLayout;
import com.example.xzb.ui.views.TCSwipeAnimationController;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.TCDanmuMgr;
import com.example.xzb.utils.TCGlobalConfig;
import com.example.xzb.utils.TCHTTPMgr;
import com.example.xzb.utils.TCUtils;
import com.example.xzb.utils.TitleUtils;
import com.example.xzb.utils.countdown.CountDownTimerView;
import com.example.xzb.utils.countdown.ICountDownTimerView;
import com.example.xzb.utils.login.TCELKReportMgr;
import com.example.xzb.utils.login.TCUserMgr;
import com.example.xzb.utils.roomutil.AnchorInfo;
import com.example.xzb.utils.roomutil.AudienceInfo;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.rtmp.TXLog;
import com.yf.xzbgift.imple.DefaultGiftAdapterImp;
import com.yf.xzbgift.imple.GiftAdapter;
import com.yf.xzbgift.imple.GiftInfoDataHandler;
import com.yf.xzbgift.important.GiftAnimatorLayout;
import com.yf.xzbgift.important.GiftInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import master.flame.danmaku.controller.IDanmakuView;

/**
 * Module:   TCBaseAnchorActivity
 * <p>
 * Function: 主播推流的页面
 * <p>
 * 1. MLVB 组件的使用，创建或者销毁房间：{@link TCBaseAnchorActivity#startPublish()}; 以及相关事件回调监听
 * <p>
 * 2. 处理消息接收到的文本信息：{@link TCBaseAnchorActivity#onRecvRoomTextMsg(String, String, String, String, String)}
 */
public class TCBaseAnchorActivity extends Activity implements IMLVBLiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener, MLVBLiveRoomImpl.StandardCallback {
    private static final String TAG = TCBaseAnchorActivity.class.getSimpleName();

    // 消息列表相关
    private ListView mLvMessage;             // 消息控件
    private TCInputTextMsgDialog mInputTextMsgDialog;    // 消息输入框
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter
    private ArrayList<TCChatEntity> mArrayListChatEntity;   // 消息内容

    private ErrorDialogFragment mErrDlgFragment;        // 错误提示弹窗
    private TCHeartLayout mHeartLayout;           // 点赞动画的布局

    protected TCSwipeAnimationController mTCSwipeAnimationController;  // 动画控制类

    private String mTitle;                 // 直播标题
    private String mCoverPicUrl;           // 直播封面图
    private String mAvatarPicUrl;          // 个人头像地址
    private String mNickName;              // 个人昵称
    private String mUserId;                // 个人用户id
    private String mLocation;              // 个人定位地址
    protected long mTotalMemberCount = 0;  // 总进房观众数量
    protected long mCurrentMemberCount = 0;// 当前观众数量
    protected long mHeartCount = 0;        // 点赞数量

    private TCDanmuMgr mDanmuMgr;              // 弹幕管理类

    protected MLVBLiveRoom mLiveRoom;              // MLVB 组件类

    protected Handler mMainHandler = new Handler(Looper.getMainLooper());


    private Button mButtonStartRoom;//开始直播按钮
    // 定时的 Timer 去更新开播时间
    private Timer mBroadcastTimer;        // 定时的 Timer
    private BroadcastTimerTask mBroadcastTimerTask;    // 定时任务
    protected long mSecond = 0;            // 开播的时间，单位为秒
    private long mStartPushPts;          // 开始直播的时间，用于 ELK 上报统计。 您可以不关注
    private RelativeLayout mRela_befor;
    private RelativeLayout mControllLayer;

    /*---------------布局新增数据---------------------------*/
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date,mtv_jg,mtv_title;
    private List<Map<String, Object>> mqianStrs;
    private QianAdapter mQianAdapter;
    private TextView mtv_one;
    private ImageView mtv_back;
    private RecyclerView mRec_qian;
    private EditText medt_title;
    private LiveCloseDialog mLiveCloseDialog;
    private CountDownTimerView mCountDownTimerView;  //倒计时view
    private GiftAnimatorLayout mGiftAnimatorLayout;   //礼物动画和礼物弹幕的显示
    private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private GuanzDialog mGuanzDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TitleUtils.getStatusBarHeight(this);
        super.onCreate(savedInstanceState);
        TitleUtils.setStatusBar(this, false, true);
        mLiveCloseDialog = new LiveCloseDialog(this);

        mStartPushPts = System.currentTimeMillis();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(TCConstants.USER_ID);
        mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
        mCoverPicUrl = intent.getStringExtra(TCConstants.COVER_PIC);
        mAvatarPicUrl = intent.getStringExtra(TCConstants.USER_HEADPIC);
        mNickName = intent.getStringExtra(TCConstants.USER_NICK);
        mLocation = intent.getStringExtra(TCConstants.USER_LOC);


        mArrayListChatEntity = new ArrayList<>();
        mErrDlgFragment = new ErrorDialogFragment();
        mLiveRoom = MLVBLiveRoom.sharedInstance(this);

        if (TextUtils.isEmpty(mNickName)) {
            mNickName = mUserId;
        }
        initView();
        mLiveRoom.setSelfProfile(mNickName, mAvatarPicUrl);
//        startPublish();
    }

    /**
     * 特别注意，以下几个 findViewById 由于是依赖于子类
     * {@link TCCameraAnchorActivity}
     * 的布局，所以id要保持一致。 若id发生改变，此处id也要同时修改
     */
    protected void initView() {
        initMineViews();
        mRela_befor = findViewById(R.id.rela_before);
        mtv_one = findViewById(R.id.tvone);
        mtv_back = findViewById(R.id.reback);
        mRec_qian = findViewById(R.id.mine_recy);
        medt_title = findViewById(R.id.input_title);
        mCountDownTimerView = findViewById(R.id.countdown_timer_view);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRec_qian.setLayoutManager(manager);
        mqianStrs = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("select", false);
            mqianStrs.add(map);
        }
        mQianAdapter = new QianAdapter(this, mqianStrs);
        mRec_qian.setAdapter(mQianAdapter);
        mQianAdapter.setOnItemClickListener(new QianAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mqianStrs.size(); i++) {
                    Map<String, Object> map = mqianStrs.get(i);
                    if (i == pos) {
                        boolean select = (boolean) map.get("select");
                        map.put("select", !select);
                    }
                }
                mQianAdapter.notifyItemChanged(pos);
            }
        });
        mtv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        mtv_one.requestFocus();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_root);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });
        mButtonStartRoom = (Button) findViewById(R.id.btn_start_room);
        mControllLayer = (RelativeLayout) findViewById(R.id.anchor_rl_controllLayer);
        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControllLayer);

        mLvMessage = (ListView) findViewById(R.id.im_msg_listview);
        mHeartLayout = (TCHeartLayout) findViewById(R.id.heart_layout);

        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mLvMessage, mArrayListChatEntity);
        mLvMessage.setAdapter(mChatMsgListAdapter);
        mLvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                TCChatEntity tcChatEntity = mArrayListChatEntity.get(postion);
                mGuanzDialog = new GuanzDialog(TCBaseAnchorActivity.this,tcChatEntity);
                mGuanzDialog.setOnDigClickListener(new GuanzDialog.OnDigClickListener() {
                    @Override
                    public void onInviteClickListener() {
                        ToastUtil.showToast(TCBaseAnchorActivity.this,"邀请"+tcChatEntity.getSenderName()+"进行连线");
                    }

                    @Override
                    public void onJubaoClickListener() {
                        startActivity(new Intent(TCBaseAnchorActivity.this, ReportActivity.class));
                    }

                    @Override
                    public void onJinyanClickListener() {
                        ToastUtil.showToast(TCBaseAnchorActivity.this,"禁言"+tcChatEntity.getSenderName());
                    }

                    @Override
                    public void onRenmingClickListener() {
                        ToastUtil.showToast(TCBaseAnchorActivity.this,"任命"+tcChatEntity.getSenderName()+"为助理");
                    }
                });
                mGuanzDialog.show();
            }
        });

        IDanmakuView danmakuView = (IDanmakuView) findViewById(R.id.anchor_danmaku_view);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(danmakuView);
        mButtonStartRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = medt_title.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    title = medt_title.getHint().toString();
                }
                mtv_title.setText(title.trim());
                mTitle =title.trim();
                mRela_befor.setVisibility(View.INVISIBLE);
                mControllLayer.setVisibility(View.VISIBLE);
                mCountDownTimerView.countDownAnimation(CountDownTimerView.DEFAULT_COUNTDOWN_NUMBER);
                mCountDownTimerView.setOnCountDownListener(new ICountDownTimerView.ICountDownListener() {
                    @Override
                    public void onCountDownComplete() {
                        startPublish();
                    }
                });

            }
        });
    }
    /*-------新增布局的一些设置--------*/
    private void initMineViews() {
        mGiftAnimatorLayout = findViewById(R.id.lottie_animator_layout);
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);
        mtv_name = findViewById(R.id.anchor_tv_broadcasting_name);
        mtv_gg = findViewById(R.id.anchor_tv_member_gz);
        mtv_id = findViewById(R.id.cam_id);
        mtv_date = findViewById(R.id.cam_date);
        mtv_jg = findViewById(R.id.cam_jgname);
        mtv_title = findViewById(R.id.cam_title);
        mtv_gg.setVisibility(View.GONE);
        mtv_jg.setText("天宇新航心理咨询机构");
        mtv_name.setText(mNickName);
        mtv_id.setText("边框ID：" + "124124");
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));



    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            showExitInfoDialog("当前正在直播，是否退出直播？", false);
        }else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        }
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      Activity声明周期相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public void onBackPressed() {
        if (mSecond == 0) {
            finish();
        } else {
            showExitInfoDialog("当前正在直播，是否退出直播？", false);
        }
    }

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
        stopTimer();
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        stopPublish();
        long endPushPts = System.currentTimeMillis();
        long diff = (endPushPts - mStartPushPts) / 1000;
        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_CAMERA_PUSH_DURATION, TCUserMgr.getInstance().getUserId(), diff, "摄像头推流时长", null);
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      开始和停止推流相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    protected void startPublish() {
        mLiveRoom.setListener(this);
        mLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
        String roomInfo = mTitle;
        try {
            roomInfo = new JSONObject()
                    .put("title", mTitle)
                    .put("frontcover", mCoverPicUrl)
                    .put("location", mLocation)
                    .toString();
        } catch (JSONException e) {
            roomInfo = mTitle;
        }

        mLiveRoom.createRoom("", roomInfo, new CreateRoomCallback() {
            @Override
            public void onSuccess(String roomId) {
                Log.w(TAG, String.format("创建直播间%s成功", roomId));
                onCreateRoomSuccess();
            }

            @Override
            public void onError(int errCode, String e) {
                Log.w(TAG, String.format("创建直播间错误, code=%s,error=%s", errCode, e));
                showErrorAndQuit(errCode, "创建直播房间失败,Error:" + e);
            }
        });
    }

    /**
     * 创建直播间成功
     */
    protected void onCreateRoomSuccess() {
        startTimer();
        // 填写了后台服务器地址
        if (!TextUtils.isEmpty(TCGlobalConfig.APP_SVR_URL)) {
            try {
                JSONObject body = new JSONObject().put("userId", mUserId)
                        .put("title", mTitle)
                        .put("frontCover", mCoverPicUrl)
                        .put("location", mLocation);
                TCHTTPMgr.getInstance().requestWithSign(TCGlobalConfig.APP_SVR_URL + "/upload_room", body, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void stopPublish() {
        mLiveRoom.exitRoom(new ExitRoomCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "exitRoom Success");
            }

            @Override
            public void onError(int errCode, String e) {
                Log.e(TAG, "exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
            }
        });

        mLiveRoom.setListener(null);
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      MLVB 组件回调
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    public void onAnchorEnter(AnchorInfo pusherInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo pusherInfo) {

    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {

    }

    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {

    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo pusherInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {

    }

    @Override
    public void onRequestRoomPK(AnchorInfo pusherInfo) {

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
                handleMemberJoinMsg(userInfo);
                break;
            case TCConstants.IMCMD_EXIT_LIVE:
                handleMemberQuitMsg(userInfo);
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
        TXLog.w(TAG, "room closed");
        showErrorAndQuit(0, "房间已解散");
    }

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) {
            TCUtils.showKickOut(TCBaseAnchorActivity.this);
        } else {
            showErrorAndQuit(errorCode, errorMessage);
        }
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {
        Log.d(TAG, log);
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      处理接收到的各种信息
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    protected void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContent(text);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);
    }

    /**
     * 处理观众加入信息
     *
     * @param userInfo
     */
    protected void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        mTotalMemberCount++;
        mCurrentMemberCount++;
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("进场了");
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * 处理观众退出信息
     *
     * @param userInfo
     */
    protected void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        if (mCurrentMemberCount > 0)
            mCurrentMemberCount--;
        else
            Log.d(TAG, "接受多次退出请求，目前人数为负数");

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("离开了");
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    /**
     * 处理点赞信息
     *
     * @param userInfo
     */
    protected void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("通知");
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"通知"
        entity.setContent("点了个赞");
        mHeartLayout.addFavor();
        mHeartCount++;

        //todo：修改显示类型
        entity.setType(TCConstants.PRAISE);
        notifyMsg(entity);
    }

    /**
     * 处理弹幕信息
     *
     * @param userInfo
     * @param text
     */
    protected void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContent(text);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.avatar, userInfo.nickname, text);
        }
    }
    /**
    /**
     * 处理礼物弹幕消息
     */
    private void handleGiftMsg(TCSimpleUserInfo userInfo, String giftId) {
        if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            /*发送消息到列表*/
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(userInfo.nickname);
            entity.setContent("送了一个 "+giftInfo.title);
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
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      发送文本信息
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
        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }


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

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        // 发送弹幕或发送房间信息
        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(TCUserMgr.getInstance().getAvatar(), TCUserMgr.getInstance().getNickname(), msg);
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


    private void notifyMsg(final TCChatEntity entity) {
        runOnUiThread(new Runnable() {
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
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      弹窗相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 显示直播结果的弹窗
     * <p>
     * 如：观看数量、点赞数量、直播时长数
     */
    protected void showPublishFinishDetailsDialog() {
        //确认则显示观看detail
        FinishDetailDialogFragment dialogFragment = new FinishDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("time", TCUtils.formattedTime(mSecond));
        args.putString("heartCount", String.format(Locale.CHINA, "%d", mHeartCount));
        args.putString("totalMemberCount", String.format(Locale.CHINA, "%d", mTotalMemberCount));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded())
            dialogFragment.dismiss();
        else
            dialogFragment.show(getFragmentManager(), "");
    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showExitInfoDialog(String msg, Boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            mLiveCloseDialog.show();
            mLiveCloseDialog.setOnLogoutClickListener(new LiveCloseDialog.OnLogoutClickListener() {
                @Override
                public void onLogoutClickListener() {
                    mLiveCloseDialog.dismiss();
                    stopPublish();
                    showPublishFinishDetailsDialog();
                }
            });
            return;
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showPublishFinishDetailsDialog();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示错误并且退出直播的弹窗
     *
     * @param errorCode
     * @param errorMsg
     */
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        stopTimer();
        stopPublish();
        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putInt("errorCode", errorCode);
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      开播时长相关
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    protected void onBroadcasterTimeUpdate(long second) {

    }

    @Override
    public void onError(int errCode, String errInfo) {
        Log.e("StandardCallback", "onError: errCode= "+errCode  +" errInfo= "+errInfo );
    }

    @Override
    public void onSuccess() {
        Log.e("StandardCallback", "onSuccess: ");

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
                    onBroadcasterTimeUpdate(mSecond);
                }
            });
        }
    }

    private void startTimer() {
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

package com.tyxh.framlive.live;

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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.rtmp.TXLog;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.QianAdapter;
import com.tyxh.framlive.base.ApiService;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.InterestBean;
import com.tyxh.framlive.bean.TitleLabelBean;
import com.tyxh.framlive.bean.UserDetailBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.CarDialog;
import com.tyxh.framlive.pop_dig.GuanzDialog;
import com.tyxh.framlive.pop_dig.ReportActivity;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.ui.LookPersonActivity;
import com.tyxh.framlive.ui.OranizeActivity;
import com.tyxh.framlive.ui.ShowGoodsActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.framlive.xzbgift.imple.DefaultGiftAdapterImp;
import com.tyxh.framlive.xzbgift.imple.GiftAdapter;
import com.tyxh.framlive.xzbgift.imple.GiftInfoDataHandler;
import com.tyxh.framlive.xzbgift.important.GiftAnimatorLayout;
import com.tyxh.framlive.xzbgift.important.GiftInfo;
import com.tyxh.xzb.Constantc;
import com.tyxh.xzb.important.IMLVBLiveRoomListener;
import com.tyxh.xzb.important.MLVBLiveRoom;
import com.tyxh.xzb.important.MLVBLiveRoomImpl;
import com.tyxh.xzb.ui.ErrorDialogFragment;
import com.tyxh.xzb.ui.FinishDetailDialogFragment;
import com.tyxh.xzb.ui.TCChatEntity;
import com.tyxh.xzb.ui.TCChatMsgListAdapter;
import com.tyxh.xzb.ui.TCSimpleUserInfo;
import com.tyxh.xzb.ui.dialog.LiveCloseDialog;
import com.tyxh.xzb.ui.dialog.TCInputTextMsgDialog;
import com.tyxh.xzb.ui.views.TCHeartLayout;
import com.tyxh.xzb.ui.views.TCSwipeAnimationController;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.TCDanmuMgr;
import com.tyxh.xzb.utils.TCGlobalConfig;
import com.tyxh.xzb.utils.TCHTTPMgr;
import com.tyxh.xzb.utils.TCUtils;
import com.tyxh.xzb.utils.TitleUtils;
import com.tyxh.xzb.utils.countdown.CountDownTimerView;
import com.tyxh.xzb.utils.countdown.ICountDownTimerView;
import com.tyxh.xzb.utils.login.TCELKReportMgr;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
import com.tyxh.xzb.utils.roomutil.AudienceInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ljy.devring.http.support.throwable.HttpThrowable.HTTP_ERROR;
import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;
import static com.tyxh.xzb.utils.TCConstants.IMCMD_CONTACT;
import static com.tyxh.xzb.utils.TCConstants.IMCMD_DISCONTACT;

/**
 * Module:   TCBaseAnchorActivity
 * <p>
 * Function: 主播推流的页面
 * <p>
 * 1. MLVB 组件的使用，创建或者销毁房间：{@link TCBaseAnchorActivity#startPublish()}; 以及相关事件回调监听
 * <p>
 * 2. 处理消息接收到的文本信息：{@link TCBaseAnchorActivity#onRecvRoomTextMsg(String, String, String, String, String)}
 */
public abstract class TCBaseAnchorActivity extends Activity implements IMLVBLiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener, MLVBLiveRoomImpl.StandardCallback {
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
    protected String mAvatarPicUrl;          // 个人头像地址
    private String mNickName;              // 个人昵称
    public String mUserId;                // 个人用户id
    private String mLocation;              // 个人定位地址
    private String mJigouName;              //机构名称
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
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date, mtv_jg, mtv_title, mtv_zuan;
    private List<InterestBean.RetDataBean> mqianStrs;
    private QianAdapter mQianAdapter;
    private TextView mtv_one;
    private ImageView mtv_back, imgv_refresh;
    private RecyclerView mRec_qian;
    private EditText medt_title;
    private LiveCloseDialog mLiveCloseDialog;
    private CountDownTimerView mCountDownTimerView;  //倒计时view
    private GiftAnimatorLayout mGiftAnimatorLayout;   //礼物动画和礼物弹幕的显示
    private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    protected GuanzDialog mGuanzDialog;
    public UserInfoBean mUserInfo;
    public String mToken;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mCar_strs;
    private CarDialog mCarDialog;//购物车弹窗
    private int user_type;             //用户类型：1-普通用户；2-咨询师；3-主机构；4-子机构
    private String groupId = "";
    private boolean is_jiyanNow =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TitleUtils.getStatusBarHeight(this);
        super.onCreate(savedInstanceState);
        TitleUtils.setStatusBar(this, false, true);
        EventBus.getDefault().register(this);
        mUserInfo = LiveShareUtil.getInstance(this).getUserInfo();
        user_type = mUserInfo.getRetData().getType();
        mToken = LiveShareUtil.getInstance(this).getToken();
        mLiveCloseDialog = new LiveCloseDialog(this);

        mStartPushPts = System.currentTimeMillis();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(TCConstants.USER_ID);
        mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
        mCoverPicUrl = intent.getStringExtra(TCConstants.COVER_PIC);
        mAvatarPicUrl = intent.getStringExtra(TCConstants.USER_HEADPIC);
        mNickName = intent.getStringExtra(TCConstants.USER_NICK);
        mLocation = intent.getStringExtra(TCConstants.USER_LOC);
        mJigouName = intent.getStringExtra(TCConstants.JIGOU_NAME);


        mArrayListChatEntity = new ArrayList<>();
        mErrDlgFragment = new ErrorDialogFragment();
        mLiveRoom = MLVBLiveRoom.sharedInstance(this);

        mCar_strs = new ArrayList<>();
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
        imgv_refresh = findViewById(R.id.refresh_title);
        mRec_qian = findViewById(R.id.mine_recy);
        medt_title = findViewById(R.id.input_title);
        mCountDownTimerView = findViewById(R.id.countdown_timer_view);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRec_qian.setLayoutManager(manager);
        mqianStrs = new ArrayList<>();
        mQianAdapter = new QianAdapter(this, mqianStrs);
        mRec_qian.setAdapter(mQianAdapter);
        mQianAdapter.setOnItemClickListener(new QianAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                for (int i = 0; i < mqianStrs.size(); i++) {
                    InterestBean.RetDataBean map = mqianStrs.get(i);
                    if (i == pos) {
                        map.setSelect(true);
                        select_id = map.getId();
                        live_qian = map.getLabelName();
                        toChangeTitle(select_id);
                    } else {
                        map.setSelect(false);
                    }
                }
                mQianAdapter.notifyDataSetChanged();
            }
        });
        getInterest();
        getDetail();
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
                List<String> list_userids = new ArrayList<>();
                list_userids.add(tcChatEntity.userid);
                V2TIMManager.getGroupManager().getGroupMembersInfo(groupId, list_userids, new V2TIMSendCallback<List<V2TIMGroupMemberFullInfo>>() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<V2TIMGroupMemberFullInfo> v2TIMGroupMemberFullInfos) {
                        Log.e(TAG, "onSuccess: " + new Gson().toJson(v2TIMGroupMemberFullInfos));
                        if (v2TIMGroupMemberFullInfos != null && v2TIMGroupMemberFullInfos.size() > 0) {
                            long muteUntil = v2TIMGroupMemberFullInfos.get(0).getMuteUntil();
                            long long_now = System.currentTimeMillis();
                            if(muteUntil>long_now){
                                is_jiyanNow =true;
                            }else{
                                is_jiyanNow =false;
                            }
                        }

                    }
                });
                Log.e(TAG, "onItemClick: 是否已经禁言:="+(is_jiyanNow?"禁言":"没有禁言") );
                mGuanzDialog = new GuanzDialog(TCBaseAnchorActivity.this, tcChatEntity, mToken,is_jiyanNow);
                mGuanzDialog.setOnDigClickListener(new GuanzDialog.OnDigClickListener() {
                    @Override
                    public void onInviteClickListener() {
                        startLinkMic(tcChatEntity.getUserid(), tcChatEntity.getHead());
                    }

                    @Override
                    public void onJubaoClickListener() {
                        Intent int_report = new Intent(TCBaseAnchorActivity.this, ReportActivity.class);
                        int_report.putExtra("per_id", tcChatEntity.getUserid());
                        int_report.putExtra("per_type", tcChatEntity.getSenderName());
                        startActivity(int_report);
                    }

                    @Override
                    public void onJinyanClickListener() {


                        V2TIMManager.getGroupManager().muteGroupMember(groupId, tcChatEntity.userid, is_jiyanNow?0:60 * 60, new V2TIMCallback() {
                            @Override
                            public void onError(int code, String desc) {
                                Toast.makeText(TCBaseAnchorActivity.this, "禁言失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(TCBaseAnchorActivity.this, is_jiyanNow?"已取消禁言":"禁言成功", Toast.LENGTH_SHORT).show();
                                is_jiyanNow=!is_jiyanNow;
                                mGuanzDialog.setIs_jy(is_jiyanNow);
                            }
                        });
                    }

                    @Override
                    public void onRenmingClickListener() {
                        ToastUtil.showToast(TCBaseAnchorActivity.this, "任命" + tcChatEntity.getSenderName() + "为助理");
//                        rmZhuli(tcChatEntity.getUserid());
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
                if (TextUtils.isEmpty(title)) {
                    title = medt_title.getHint().toString();
                }
                mtv_title.setText(title.trim());
                mTitle = title.trim();
                mRela_befor.setVisibility(View.INVISIBLE);
                mControllLayer.setVisibility(View.VISIBLE);
                mCountDownTimerView.countDownAnimation(CountDownTimerView.DEFAULT_COUNTDOWN_NUMBER);
                mCountDownTimerView.setOnCountDownListener(new ICountDownTimerView.ICountDownListener() {
                    @Override
                    public void onCountDownComplete() {
                        startPublish();
                    }
                });
                toPushTitle();
            }
        });
    }

    /*任命助理*/
    private void rmZhuli(String userid){
        HashMap<String,String> map =new HashMap<>();
        map.put("zl",userid);
        V2TIMManager.getGroupManager().setGroupAttributes(groupId, map, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "aaa_onError: 任命助理失败 code: "+code+"  desc: "+desc );
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "aaa_onSuccess: 已任命"+userid+"为助理");
                getGroupMsg();
            }
        });
    }
    /**
    *   获取群属性
    * */
    private void getGroupMsg(){
        V2TIMManager.getGroupManager().getGroupAttributes(groupId, null, new V2TIMSendCallback<Map<String, String>>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "onError: code: "+code+"  desc: "+desc);
            }

            @Override
            public void onSuccess(Map<String, String> stringStringMap) {
                Log.e(TAG, "onSuccess: "+new Gson().toJson(stringStringMap) );
            }
        });
    }

    /*邀请观众进行连麦*/
    private void startLinkMic(String userid, String ico) {
        if(Constant.USER_STATE.equals("3")){
            Toast.makeText(this, "连麦中无法继续邀请", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "连麦请求中...", Toast.LENGTH_LONG).show();
        mLiveRoom.requestJoinUserAnchor("连麦", userid, new RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                Log.i(TAG, "onAccept:观众接受已经接收连麦");
              /*  mLiveRoom.responseJoinAnchor(userid, true, "");
                Constantc.LX_HEAD =ico;
                sendContactMsg(true, userid);
                if (mGuanzDialog != null && mGuanzDialog.isShowing())
                    mGuanzDialog.dismiss();*/
            }

            @Override
            public void onReject(String reason) {
                Toast.makeText(TCBaseAnchorActivity.this, reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimeOut() {
                Toast.makeText(TCBaseAnchorActivity.this, "连麦请求超时，观众没有做出回应", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(TCBaseAnchorActivity.this, "连麦请求发生错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 主播连麦成功后发送该自定义消息
     *
     * @param is_lm true = 连麦中   false==中断连麦
     */

    private void sendContactMsg(boolean is_lm, String userid) {
        Constant.USER_STATE = is_lm?"3":"2";
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
        mtv_zuan = findViewById(R.id.camera_sszuan);
        mtv_gg.setVisibility(View.GONE);
        mtv_jg.setVisibility(TextUtils.isEmpty(mJigouName) ? View.GONE : View.VISIBLE);
        mtv_jg.setText(mJigouName);
        mtv_name.setText(mNickName);
        mtv_id.setText("边框ID：" + mUserId);
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            showExitInfoDialog("当前正在直播，是否退出直播？", false);
        } else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        } else if (id == R.id.camera_car) {
            if (mCarDialog == null) {
                ToastUtil.showToast(this, "未获取到服务信息");
                return;
            }
            mCarDialog.show();
//            startActivity(new Intent(this, LookPersonActivity.class));//咨询师页面
        } else if (id == R.id.refresh_title) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.live_refresh);
            imgv_refresh.startAnimation(animation);
            toChangeTitle(select_id);
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
        EventBus.getDefault().unregister(this);
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
                    .put("label", live_qian)
                    .toString();
        } catch (JSONException e) {
            roomInfo = mTitle;
        }

        mLiveRoom.createRoom("", roomInfo,mTitle, new CreateRoomCallback() {
            @Override
            public void onSuccess(String roomId) {
                Log.w(TAG, String.format("创建直播间%s成功", roomId));
                Constant.USER_STATE = "2";
                groupId = roomId;
                onCreateRoomSuccess();
            }

            @Override
            public void onError(int errCode, String e) {
                Log.w(TAG, String.format("创建直播间错误, code=%s,error=%s", errCode, e));
                showErrorAndQuit(errCode, "创建直播房间失败," + e);
            }
        });
    }

    /**
     * 创建直播间成功
     */
    protected void onCreateRoomSuccess() {
        toNotice();
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
                Constant.USER_STATE = "1";
                Log.i(TAG, "exitRoom Success");
            }

            @Override
            public void onError(int errCode, String e) {
                Constant.USER_STATE = "1";
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
        Log.i(TAG, "onAccept:观众接收连麦");
        /*连麦成功*/
        mLiveRoom.responseJoinAnchor(pusherInfo.userID, true, "");
        Constantc.LX_HEAD = pusherInfo.userAvatar;
        sendContactMsg(true, pusherInfo.userID);
        if (mGuanzDialog != null && mGuanzDialog.isShowing())
            mGuanzDialog.dismiss();
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
    /*JxqDialog mJxqDialog;
    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        Log.e(TAG, "onError: 错误" );
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) {
            Log.e(TAG, "onError:登录失效");
            mJxqDialog =new JxqDialog(this);
            mJxqDialog.setOnOutClickListener(new JxqDialog.OnOutClickListener() {
                @Override
                public void onOutListener() {
                    LiveShareUtil.getInstance(TCBaseAnchorActivity.this).clear();
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE, true);
                    startActivity(new Intent(TCBaseAnchorActivity.this, LoginActivity.class));
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

    }

    @Override
    public void onDebugLog(String log) {
        Log.d(TAG, log);
    }*/


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
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);
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
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);

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
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);
        /*去掉界面提醒*/
//        notifyMsg(entity);
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
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);

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
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);

        notifyMsg(entity);

        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.avatar, userInfo.nickname, text);
        }
    }

    /**
     * /**
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
            entity.setHead(userInfo.avatar);
            entity.setUserid(userInfo.userid);
            BigDecimal bigDecimal = new BigDecimal(mtv_zuan.getText().toString());
            BigDecimal bigDec_add = new BigDecimal(giftInfo.price);
            mtv_zuan.setText(bigDecimal.add(bigDec_add).toString());
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
        entity.setHead(mAvatarPicUrl);
        entity.setUserid(mUserId);

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
        Log.e("StandardCallback", "onError: errCode= " + errCode + " errInfo= " + errInfo);
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
        toGoTask(8, mSecond + "");
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }

    /*任务完成
     * 触发类型【2:观看直播;5:发弹幕;7:分享直播;8:直播时长】
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

    /*-----------接口调用---------------*/
    private void getDetail() {
        DevRing.httpManager().commonRequest(DevRing.httpManager().getService(ApiService.class).getDetails(mToken, mUserId), new CommonObserver<UserDetailBean>() {
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
                if (throwable.errorType == HTTP_ERROR) {//重新登录
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
                    Intent intt = new Intent(TCBaseAnchorActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "咨询机构-" : "咨询师-") + mNickName);
                    intt.putExtra("query_id", mUserId);
                    intt.putExtra("is_user", false);
                    startActivity(intt);//商品套餐页面
                }

                @Override
                public void onTiyanClickListener(int pos) {

                    UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mCar_strs.get(pos);
                    Intent intt = new Intent(TCBaseAnchorActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "咨询机构-" : "咨询师-") + mNickName);
                    intt.putExtra("query_id", mUserId);
                    intt.putExtra("is_user", false);
                    startActivity(intt);//商品套餐页面
//                    Toast.makeText(TCAudienceActivity.this, "购买"+servicePackagesBean.getId(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMoreClickListener() {
                    if (user_type > 2) {
                        Intent int_orgi = new Intent(TCBaseAnchorActivity.this, OranizeActivity.class);
                        int_orgi.putExtra("is_user", true);
                        int_orgi.putExtra("query_id", mUserId);
                        startActivity(int_orgi);//咨询机构页面
                    } else {
                        Intent int_person = new Intent(TCBaseAnchorActivity.this, LookPersonActivity.class);
                        int_person.putExtra("is_user", true);
                        int_person.putExtra("query_id", mUserId);
                        startActivity(int_person);//咨询师页面
                    }
                }
            });

        }
    }


    private int select_id;

    /*获取兴趣爱好列表展示*/
    private void getInterest() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getCouLabel(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                InterestBean interestBean = new Gson().fromJson(result.toString(), InterestBean.class);
                if (interestBean.getRetCode() == 0) {
                    List<InterestBean.RetDataBean> retData = interestBean.getRetData();
                    if (retData != null && retData.size() > 0) {
                        retData.get(0).setSelect(true);
                        select_id = retData.get(0).getId();
                        live_qian = retData.get(0).getLabelName();
                        toChangeTitle(select_id);
                    }
                    mqianStrs.addAll(interestBean.getRetData());
                    mQianAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(TCBaseAnchorActivity.this, interestBean.getRetMsg());
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private String live_qian = "";

    private void toChangeTitle(int id) {
        Integer[] strings = new Integer[]{id};
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(strings));
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getLiveTitle(mToken, requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                TitleLabelBean bean = new Gson().fromJson(result.toString(), TitleLabelBean.class);
                if (bean.getRetCode() == 0 && bean.getRetData() != null) {
                    medt_title.setText(bean.getRetData().getTitle());
                    medt_title.setSelection(bean.getRetData().getTitle().length());
                }
                Log.e(TAG, "onSuccessListener: " + result.toString());

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*通知关注直播间的人开始直播了*/
    private void toNotice() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().noticeUsershow(mToken), new HttpBackListener() {
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
    /*开播时传标题给后台*/
    private void toPushTitle(){
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().pushTitle(mToken, mTitle), new HttpBackListener() {
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
            if (mCarDialog != null)
                mCarDialog.getMineAsset();
        } else if (message.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}

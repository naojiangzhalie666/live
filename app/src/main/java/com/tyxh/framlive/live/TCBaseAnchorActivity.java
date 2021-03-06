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
 * Function: ?????????????????????
 * <p>
 * 1. MLVB ?????????????????????????????????????????????{@link TCBaseAnchorActivity#startPublish()}; ??????????????????????????????
 * <p>
 * 2. ???????????????????????????????????????{@link TCBaseAnchorActivity#onRecvRoomTextMsg(String, String, String, String, String)}
 */
public abstract class TCBaseAnchorActivity extends Activity implements IMLVBLiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener, MLVBLiveRoomImpl.StandardCallback {
    private static final String TAG = TCBaseAnchorActivity.class.getSimpleName();

    // ??????????????????
    private ListView mLvMessage;             // ????????????
    private TCInputTextMsgDialog mInputTextMsgDialog;    // ???????????????
    private TCChatMsgListAdapter mChatMsgListAdapter;    // ???????????????Adapter
    private ArrayList<TCChatEntity> mArrayListChatEntity;   // ????????????

    private ErrorDialogFragment mErrDlgFragment;        // ??????????????????
    private TCHeartLayout mHeartLayout;           // ?????????????????????

    protected TCSwipeAnimationController mTCSwipeAnimationController;  // ???????????????

    private String mTitle;                 // ????????????
    private String mCoverPicUrl;           // ???????????????
    protected String mAvatarPicUrl;          // ??????????????????
    private String mNickName;              // ????????????
    public String mUserId;                // ????????????id
    private String mLocation;              // ??????????????????
    private String mJigouName;              //????????????
    protected long mTotalMemberCount = 0;  // ?????????????????????
    protected long mCurrentMemberCount = 0;// ??????????????????
    protected long mHeartCount = 0;        // ????????????

    private TCDanmuMgr mDanmuMgr;              // ???????????????

    protected MLVBLiveRoom mLiveRoom;              // MLVB ?????????

    protected Handler mMainHandler = new Handler(Looper.getMainLooper());


    private Button mButtonStartRoom;//??????????????????
    // ????????? Timer ?????????????????????
    private Timer mBroadcastTimer;        // ????????? Timer
    private BroadcastTimerTask mBroadcastTimerTask;    // ????????????
    protected long mSecond = 0;            // ??????????????????????????????
    private long mStartPushPts;          // ?????????????????????????????? ELK ??????????????? ??????????????????
    private RelativeLayout mRela_befor;
    private RelativeLayout mControllLayer;

    /*---------------??????????????????---------------------------*/
    private TextView mtv_name, mtv_gg, mtv_id, mtv_date, mtv_jg, mtv_title, mtv_zuan;
    private List<InterestBean.RetDataBean> mqianStrs;
    private QianAdapter mQianAdapter;
    private TextView mtv_one;
    private ImageView mtv_back, imgv_refresh;
    private RecyclerView mRec_qian;
    private EditText medt_title;
    private LiveCloseDialog mLiveCloseDialog;
    private CountDownTimerView mCountDownTimerView;  //?????????view
    private GiftAnimatorLayout mGiftAnimatorLayout;   //????????????????????????????????????
    private GiftAdapter mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    protected GuanzDialog mGuanzDialog;
    public UserInfoBean mUserInfo;
    public String mToken;
    private List<UserDetailBean.RetDataBean.ServicePackagesBean> mCar_strs;
    private CarDialog mCarDialog;//???????????????
    private int user_type;             //???????????????1-???????????????2-????????????3-????????????4-?????????
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
     * ??????????????????????????? findViewById ????????????????????????
     * {@link TCCameraAnchorActivity}
     * ??????????????????id?????????????????? ???id?????????????????????id??????????????????
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
                Log.e(TAG, "onItemClick: ??????????????????:="+(is_jiyanNow?"??????":"????????????") );
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
                                Toast.makeText(TCBaseAnchorActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(TCBaseAnchorActivity.this, is_jiyanNow?"???????????????":"????????????", Toast.LENGTH_SHORT).show();
                                is_jiyanNow=!is_jiyanNow;
                                mGuanzDialog.setIs_jy(is_jiyanNow);
                            }
                        });
                    }

                    @Override
                    public void onRenmingClickListener() {
                        ToastUtil.showToast(TCBaseAnchorActivity.this, "??????" + tcChatEntity.getSenderName() + "?????????");
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

    /*????????????*/
    private void rmZhuli(String userid){
        HashMap<String,String> map =new HashMap<>();
        map.put("zl",userid);
        V2TIMManager.getGroupManager().setGroupAttributes(groupId, map, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "aaa_onError: ?????????????????? code: "+code+"  desc: "+desc );
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "aaa_onSuccess: ?????????"+userid+"?????????");
                getGroupMsg();
            }
        });
    }
    /**
    *   ???????????????
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
                Toast.makeText(TCBaseAnchorActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(TCBaseAnchorActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param is_lm true = ?????????   false==????????????
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

    /*-------???????????????????????????--------*/
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
        mtv_id.setText("??????ID???" + mUserId);
        mtv_date.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            showExitInfoDialog("??????????????????????????????????????????", false);
        } else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        } else if (id == R.id.camera_car) {
            if (mCarDialog == null) {
                ToastUtil.showToast(this, "????????????????????????");
                return;
            }
            mCarDialog.show();
//            startActivity(new Intent(this, LookPersonActivity.class));//???????????????
        } else if (id == R.id.refresh_title) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.live_refresh);
            imgv_refresh.startAnimation(animation);
            toChangeTitle(select_id);
        }
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      Activity??????????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public void onBackPressed() {
        if (mSecond == 0) {
            finish();
        } else {
            showExitInfoDialog("??????????????????????????????????????????", false);
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
        TCELKReportMgr.getInstance().reportELK(TCConstants.ELK_ACTION_CAMERA_PUSH_DURATION, TCUserMgr.getInstance().getUserId(), diff, "?????????????????????", null);
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      ???????????????????????????
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
                Log.w(TAG, String.format("???????????????%s??????", roomId));
                Constant.USER_STATE = "2";
                groupId = roomId;
                onCreateRoomSuccess();
            }

            @Override
            public void onError(int errCode, String e) {
                Log.w(TAG, String.format("?????????????????????, code=%s,error=%s", errCode, e));
                showErrorAndQuit(errCode, "????????????????????????," + e);
            }
        });
    }

    /**
     * ?????????????????????
     */
    protected void onCreateRoomSuccess() {
        toNotice();
        startTimer();
        // ??????????????????????????????
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
     * //                      MLVB ????????????
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    @Override
    public void onAnchorEnter(AnchorInfo pusherInfo) {
        Log.i(TAG, "onAccept:??????????????????");
        /*????????????*/
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
        showErrorAndQuit(0, "???????????????");
    }
    /*JxqDialog mJxqDialog;
    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        Log.e(TAG, "onError: ??????" );
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) {
            Log.e(TAG, "onError:????????????");
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
     * //                      ??????????????????????????????
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
     * ????????????????????????
     *
     * @param userInfo
     */
    protected void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        mTotalMemberCount++;
        mCurrentMemberCount++;
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_ENTER);
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);

        notifyMsg(entity);
    }

    /**
     * ????????????????????????
     *
     * @param userInfo
     */
    protected void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        if (mCurrentMemberCount > 0)
            mCurrentMemberCount--;
        else
            Log.d(TAG, "????????????????????????????????????????????????");

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("?????????");
        entity.setType(TCConstants.MEMBER_EXIT);
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);
        /*??????????????????*/
//        notifyMsg(entity);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    protected void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("??????");
        entity.setSenderName(TextUtils.isEmpty(userInfo.nickname) ? userInfo.userid : userInfo.nickname);//"??????"
        entity.setContent("????????????");
        mHeartLayout.addFavor();
        mHeartCount++;

        //todo?????????????????????
        entity.setType(TCConstants.PRAISE);
        entity.setHead(userInfo.avatar);
        entity.setUserid(userInfo.userid);

        notifyMsg(entity);
    }

    /**
     * ??????????????????
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
     *     //                      ??????????????????
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
        lp.width = (int) (display.getWidth()); //????????????
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
                Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("???:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        entity.setHead(mAvatarPicUrl);
        entity.setUserid(mUserId);

        notifyMsg(entity);

        // ?????????????????????????????????
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
     *     //                      ????????????
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * ???????????????????????????
     * <p>
     * ???????????????????????????????????????????????????
     */
    protected void showPublishFinishDetailsDialog() {
        //?????????????????????detail
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
     * ??????????????????
     *
     * @param msg     ????????????
     * @param isError true?????????????????????????????? false???????????????????????????????????????
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
            //????????????????????????????????????????????????
            stopPublish();
            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
     * ???????????????????????????????????????
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
     * //                      ??????????????????
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
     * ?????????
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
        //????????????
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        //????????????
        toGoTask(8, mSecond + "");
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
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

    /*-----------????????????---------------*/
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
                    Intent intt = new Intent(TCBaseAnchorActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "????????????-" : "?????????-") + mNickName);
                    intt.putExtra("query_id", mUserId);
                    intt.putExtra("is_user", false);
                    startActivity(intt);//??????????????????
                }

                @Override
                public void onTiyanClickListener(int pos) {

                    UserDetailBean.RetDataBean.ServicePackagesBean servicePackagesBean = mCar_strs.get(pos);
                    Intent intt = new Intent(TCBaseAnchorActivity.this, ShowGoodsActivity.class);
                    intt.putExtra("data", servicePackagesBean);
                    intt.putExtra("name", (user_type > 2 ? "????????????-" : "?????????-") + mNickName);
                    intt.putExtra("query_id", mUserId);
                    intt.putExtra("is_user", false);
                    startActivity(intt);//??????????????????
//                    Toast.makeText(TCAudienceActivity.this, "??????"+servicePackagesBean.getId(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMoreClickListener() {
                    if (user_type > 2) {
                        Intent int_orgi = new Intent(TCBaseAnchorActivity.this, OranizeActivity.class);
                        int_orgi.putExtra("is_user", false);
                        int_orgi.putExtra("query_id", mUserId);
                        startActivity(int_orgi);//??????????????????
                    } else {
                        Intent int_person = new Intent(TCBaseAnchorActivity.this, LookPersonActivity.class);
                        int_person.putExtra("is_user", false);
                        int_person.putExtra("query_id", mUserId);
                        startActivity(int_person);//???????????????
                    }
                }
            });

        }
    }


    private int select_id;

    /*??????????????????????????????*/
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

    /*??????????????????????????????????????????*/
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
    /*???????????????????????????*/
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
            ToastUtil.showToast(this, "??????????????????????????????!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}

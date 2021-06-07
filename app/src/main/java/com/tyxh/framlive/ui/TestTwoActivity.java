package com.tyxh.framlive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.LiveCotctBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.BaseChatActivity;
import com.tyxh.framlive.pop_dig.BuyzActivity;
import com.tyxh.framlive.pop_dig.UseWhatDialog;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.TitleUtils;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tyxh.framlive.bean.EventMessage.PAY_SUCCESS;
/********************************************************************
  @version: 1.0.0
  @description: 1分钟时后台Socket提示过来进行选择，如果使用的是钻石  1分钟时未选择且到达30秒时进行充值弹窗提示
  @author: admin
  @time: 2021/5/31 18:53
  @变更历史:
********************************************************************/
public class TestTwoActivity extends BaseChatActivity {
    private static final String TAG = "TestTwoActivity";
    @BindView(R.id.textView58)
    TextView mTextView58;
    @BindView(R.id.textView129)
    TextView mTextView129;
    @BindView(R.id.dig_lm_tm)
    TextView mDigLmTm;
    @BindView(R.id.dig_tmone)
    TextView mDigTmone;
    @BindView(R.id.dig_tmtwo)
    TextView mDigTmtwo;

    private View view_include;


    private WebSocketClient mWebSocketClient;
    private UseWhatDialog mUseWhatDialog;

    private static final long HEART_BEAT_RATE = 20 * 1000;
    private long sendTime = 0L;
    private String token;
    private UserInfoBean user_Info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        TitleUtils.setStatusTextColor(true, this);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        view_include = findViewById(R.id.testtwo_include);

    }


    @OnClick({R.id.textView58, R.id.textView129, R.id.dig_lm_imgv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView58:
                initSocket();
                break;
            case R.id.textView129:
                getUseData();
                break;
            case R.id.dig_lm_imgv:
                startActivity(new Intent(this, BuyzActivity.class));
                break;
        }
    }

    private boolean is_lmfirst = true;
    protected long mSecond = 0;            // 连麦的时间，单位为秒
    private long all_Second = 0;             //连麦总时长
    protected long mLeft_second = 40;
    private Timer mBroadcastTimer;        // 定时的 Timer
    private BroadcastTimerTask mBroadcastTimerTask;    // 定时任务
    private LiveCotctBean.RetDataBean select_bean;
    private boolean is_start = false;
    private int proType = 0;                //消耗类型

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
               /*     mDigTmone.setText(TCUtils.formattedTime(mSecond));
                    mDigTmtwo.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    if (mLeft_second - mSecond <= 0) {
                        stopTimer();
                        view_include.setVisibility(View.GONE);
                        Toast.makeText(TestTwoActivity.this, "断开连线", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mLeft_second - mSecond <= 30) {
                        view_include.setVisibility(View.VISIBLE);
                    } else {
                        view_include.setVisibility(View.GONE);
                    }*/


//                    mDigTmone.setText(TCUtils.formattedTime(mSecond));
                    mDigTmone.setText(TCUtils.formattedTime(all_Second));
                    if (is_lmfirst) {//首次连麦--前30秒免费
                        if (mSecond < 30) {
                            Log.e(TAG, "run:前"+mSecond );
                            mDigTmtwo.setText(TCUtils.duration((mLeft_second) * 1000));
                            mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                            return;
                        } else if (mSecond == 30) {
                            mSecond = 0;
                            Map<String,Object> map =new HashMap<>();
                            map.put("userId","7");
                            map.put("roomId","8");
                            map.put("tipsType","1");//1开始计时  2心跳开始
                            map.put("proType",select_bean.getProType());
                            //测试--注释掉
                            mWebSocketClient.send(new Gson().toJson(map));
                            Log.e(TAG, "run: "+new Gson().toJson(map) );
                            proType =select_bean.getProType();
                            select_bean =null;
                        }
                        Log.e(TAG, "run:过"+mSecond);
                        is_lmfirst=false;
                    }else if(select_bean !=null&&mLeft_second - mSecond <= 0){
                        Map<String,Object> map =new HashMap<>();
                        map.put("userId","7");
                        map.put("roomId","8");
                        map.put("tipsType","1");//1开始计时  2心跳开始
                        map.put("proType",select_bean.getProType());
                        //测试--注释掉
                        mWebSocketClient.send(new Gson().toJson(map));
                        proType =select_bean.getProType();
                        /*选择完后--之前计时结束后重新计时*/
                        mLeft_second =select_bean.getDuration();
                        mSecond =0;
                        select_bean =null;
                        Log.e(TAG, "run: "+new Gson().toJson(map) );
                    }
                    mDigTmtwo.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    mDigLmTm.setText(TCUtils.duration((mLeft_second - mSecond) * 1000));
                    if (mLeft_second - mSecond <= 0) {
                        stopTimer();
                        view_include.setVisibility(View.GONE);
                        Toast.makeText(TestTwoActivity.this, "断开连线", Toast.LENGTH_SHORT).show();
                        mWebSocketClient.close();
                        mHandler.removeCallbacks(heartBeatRunnable);
                        return;
                    }
                    if (mLeft_second - mSecond == 60) {
                        view_include.setVisibility(View.GONE);
                        getUseData();
                    } else if (mLeft_second - mSecond <= 30&&proType ==4) {//30秒且为钻石消耗时进行充值提醒？
                        view_include.setVisibility(View.VISIBLE);
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
        is_lmfirst =false;
        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
        mBroadcastTimer = null;
        mDigLmTm.setText("0");
    }

    /*获取可用于连麦的列表--卡、包、钻*/
    private void getUseData() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getContTime(token, "8", user_Info.getRetData().getId()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LiveCotctBean bean = new Gson().fromJson(result.toString(), LiveCotctBean.class);
                if (bean.getRetCode() == 0) {
                    if(mUseWhatDialog ==null) {
                        mUseWhatDialog = new UseWhatDialog(TestTwoActivity.this, TestTwoActivity.this, bean.getRetData());
                    }else{
                        mUseWhatDialog.setDataBeans(bean.getRetData());
                    }
                    mUseWhatDialog.show();
                    mUseWhatDialog.setOnSureClickListener(new UseWhatDialog.OnSureClickListener() {
                        @Override
                        public void onSureClickListener(LiveCotctBean.RetDataBean bean) {
                            mUseWhatDialog.dismiss();
                            mLeft_second = bean.getDuration();
                            select_bean =bean;
                            Log.e(TAG, "onSureClickListener: "+new Gson().toJson(bean) );
                            if(!is_start)
                            initSocket();
                            if(all_Second==0)
                            startTimer();
                        }
                    });
                }

            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsg(EventMessage message) {
        if (message.getCode()==PAY_SUCCESS) {
            getUseData();
        } else if (message.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 初始化socket
    public void initSocket() {
        is_start=true;
        //测试--注释掉
        if (null == mWebSocketClient) {
        String sock_url = Constant.SOCKET_URL+"7/8";
            try {
                //前面是userid  后面是roomid
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e(TAG, "onOpen: 打开");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e(TAG, "onMessage: " + message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, "onClose: " + code);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "onError: " + ex.toString());
                    }
                };
                mWebSocketClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        }
//        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    private Handler mHandler = new Handler();
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                //测试--注释掉
                if (mWebSocketClient != null) {//长连接已断开
                    if (mWebSocketClient.isClosed()) {
                        reconnectWs();
                    } else {
                        Map<String,Object> map =new HashMap<>();
                        map.put("tipsType","2");//1开始计时  2心跳开始
                        mWebSocketClient.send(new Gson().toJson(map));
                    }
                } else {//长连接处于连接状态
                    initSocket();
                }
                Map<String,Object> map =new HashMap<>();
//                        map.put("userId",mUserId);
//                        map.put("roomId",mPusherId);
                map.put("tipsType","2");//1开始计时  2心跳开始
//                        map.put("proType",select_bean.getProType());
                Log.e(TAG, "run: "+new Gson().toJson(map));
                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
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

}

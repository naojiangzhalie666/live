package com.tyxh.framlive.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.utils.LiveShareUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.tyxh.framlive.base.Constant.USER_STATE;

public class StateService extends Service {
    private static final String TAG = "StateService";
    private String mUserId;
    private String state_soc = "wss://appshop.bkxinli.com/portal/websocket/";

    public StateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUserId = (String) LiveShareUtil.getInstance(LiveApplication.getmInstance()).get(LiveShareUtil.APP_USERID, "");

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    private WebSocketClient mWebSocketClient;
    private static final long HEART_BEAT_RATE = 10 * 1000;//心跳间隔
    private long sendTime = 0L;

    // 初始化socket
    public void initSocket() {
        if (null == mWebSocketClient) {
            String sock_url = state_soc + mUserId;
            try {
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e(TAG, "State_Socket：连接成功");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e(TAG, "State_Socket：返回数据" + message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, "State_Socket：已关闭");
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "State_Socket：连接错误" + ex.toString());
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
                        map.put("state", USER_STATE);//1开始计时  2心跳开始
                        try {
                            mWebSocketClient.send(new Gson().toJson(map));
                        } catch (Exception e) {
                            Log.e(TAG, "run: "+e.toString() );
                        }
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
                    mWebSocketClient.reconnectBlocking();
                    Log.e(TAG, "State_Socket：重新连接中...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopConnect();
    }

    public void stopConnect() {
        if (mWebSocketClient != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("state", "4");//离线
            mWebSocketClient.send(new Gson().toJson(map));
            mWebSocketClient.close();
        }
        mHandler_socket.removeCallbacks(heartBeatRunnable);
    }


}

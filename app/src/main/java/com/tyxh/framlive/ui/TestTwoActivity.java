package com.tyxh.framlive.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.tyxh.framlive.R;
import com.tyxh.framlive.chat.BaseChatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import androidx.annotation.Nullable;

public class TestTwoActivity extends BaseChatActivity {
    private static final String TAG = "TestTwoActivity";
    private WebSocketClient mWebSocketClient;

    private static final long HEART_BEAT_RATE = 20 * 1000;
    private long sendTime = 0L;
    private Handler mHandler = new Handler();
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if (mWebSocketClient!=null) {//长连接已断开
                    if(mWebSocketClient.isClosed()){
                        reconnectWs();
                    }else{
                        mWebSocketClient.send("321");
                    }
                } else {//长连接处于连接状态
                   initSocket();
                }
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
                    if(false)
                    mWebSocketClient.reconnectBlocking();
                    Log.e(TAG, "run: 重连中。。。" );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.textView58).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSocket();
            }
        });

    }

    // 初始化socket
    public void initSocket() {
        if (null == mWebSocketClient) {
            try {
                mWebSocketClient = new WebSocketClient(new URI("http://172.16.3.235:8081/socket/2/3")) {
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
    }
}

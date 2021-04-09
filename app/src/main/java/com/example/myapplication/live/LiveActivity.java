package com.example.myapplication.live;

import android.util.Log;

import com.example.myapplication.R;
import com.superc.yyfflibrary.base.BaseActivity;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveActivity extends BaseActivity {


    @BindView(R.id.pusher_tx_cloud_view)
    TXCloudVideoView mPusherTxCloudView;
    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_live;
    }


    @Override
    public void init() {
        ButterKnife.bind(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher = new TXLivePusher(this);
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.startCameraPreview(mPusherTxCloudView);

        String rtmpURL = "rtmp://134887.livepush.myqcloud.com/live/mine?txSecret=fbaa0deaf99b742c6a3a95a4696e015b&txTime=606E5D51"; //此处填写您的 rtmp 推流地址
        int ret = mLivePusher.startPusher(rtmpURL.trim());
        if (ret == -5) {
            Log.i(TAG, "startRTMPPush: license 校验失败");
        }

    }
}

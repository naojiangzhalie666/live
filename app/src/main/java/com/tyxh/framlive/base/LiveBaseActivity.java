package com.tyxh.framlive.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.tyxh.framlive.R;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.pop_dig.GoleftPopWindow;
import com.tyxh.framlive.pop_dig.JxqDialog;
import com.tyxh.framlive.pop_dig.LoadDialog;
import com.tyxh.framlive.ui.LoginActivity;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.xzb.important.IMLVBLiveRoomListener;
import com.tyxh.xzb.important.MLVBCommonDef;
import com.tyxh.xzb.important.MLVBLiveRoom;
import com.tyxh.xzb.utils.roomutil.AnchorInfo;
import com.tyxh.xzb.utils.roomutil.AudienceInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

public abstract class LiveBaseActivity extends BaseActivity implements IMLVBLiveRoomListener {
    public static final String TAG = "LiveHttp";
    public LoadDialog mLoadDialog;
    public String token = "";
    public UserInfoBean user_Info;
    private JxqDialog mJxqDialog;
    private GoleftPopWindow mGoleftPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLoadDialog = new LoadDialog(this);
        token = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken();
        user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
        super.onCreate(savedInstanceState);
        MLVBLiveRoom.sharedInstance(this).setListener(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    public void showPop() {
        mGoleftPopWindow = GoleftPopWindow.getInstance(this, LayoutInflater.from(this).inflate(R.layout.dialog_goleft, null));
        mGoleftPopWindow.show("刚刚有个人连麦成功了哦");
    }

    @Override
    public int getContentLayoutId() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        user_Info = LiveShareUtil.getInstance(LiveApplication.getmInstance()).getUserInfo();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //销毁时--关闭正在进行的网络访问
        DevRing.httpManager().stopRequestByTag(TAG);
    }

    public void showLoad() {
        if (mLoadDialog != null && !mLoadDialog.isShowing())
            mLoadDialog.show();
    }

    public void hideLoad() {
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getCode() == 1005) {
            ToastUtil.showToast(this, "登录过期，请重新登录!");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM 被强制下线。
            mJxqDialog =new JxqDialog(this);
            mJxqDialog.setOnOutClickListener(new JxqDialog.OnOutClickListener() {
                @Override
                public void onOutListener() {
                    LiveShareUtil.getInstance(LiveBaseActivity.this).clear();
                    LiveShareUtil.getInstance(LiveApplication.getmInstance()).put(LiveShareUtil.APP_AGREE, true);
                    startActivity(new Intent(LiveBaseActivity.this, LoginActivity.class));
                    finish();
                }
            });
            mJxqDialog.show();
        }
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onRoomDestroy(String roomID) {

    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {

    }

    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {

    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {

    }

    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {

    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {

    }
}

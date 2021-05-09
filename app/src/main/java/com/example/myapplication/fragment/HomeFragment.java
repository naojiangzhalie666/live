package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeAdapter;
import com.example.myapplication.base.Constant;
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.bean.EventMessage;
import com.example.myapplication.bean.MineTCVideoInfo;
import com.example.myapplication.bean.RoomBean;
import com.example.myapplication.bean.SignBean;
import com.example.myapplication.bean.UserInfoBean;
import com.example.myapplication.live.TCAudienceActivity;
import com.example.myapplication.live.TCCameraAnchorActivity;
import com.example.myapplication.ui.FindActivity;
import com.example.myapplication.ui.LookPersonActivity;
import com.example.myapplication.ui.OranizeActivity;
import com.example.myapplication.ui.ShowGoodsActivity;
import com.example.myapplication.utils.LiveShareUtil;
import com.example.myapplication.utils.httputil.HttpBackListener;
import com.example.myapplication.utils.httputil.LiveHttp;
import com.example.xzb.Constantc;
import com.example.xzb.important.MLVBLiveRoom;
import com.example.xzb.important.MLVBLiveRoomImpl;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.TCHTTPMgr;
import com.example.xzb.utils.login.TCUserMgr;
import com.example.xzb.utils.onlinelive.TCVideoInfo;
import com.example.xzb.utils.onlinelive.TCVideoListMgr;
import com.example.xzb.utils.roomutil.RoomInfo;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.AVCallManager;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static final int START_LIVE_PLAY = 100;
    private long mLastClickTime = 0;//避免连击
    private int mRecordType = TCConstants.RECORD_TYPE_CAMERA;   // 默认摄像头推流

    @BindView(R.id.home_recy)
    RecyclerView mHomeRecy;
    @BindView(R.id.home_smart)
    SmartRefreshLayout mHomeSmart;
    @BindView(R.id.home_camera)
    ImageView mImgv_camera;

    private List<MineTCVideoInfo> mLists;
    private HomeAdapter mHomeAdapter;

    private Unbinder unbinder;
    private int mPower;
    private TCUserMgr mInstance;
    private UserInfoBean mUserInfo;
    private String mUserId;
    private int page = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    @OnClick({R.id.home_camera, R.id.home_chanpin, R.id.home_tiyan, R.id.home_newman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_camera:
                startPublish();
                break;
            case R.id.home_chanpin:
                startActivity(new Intent(getActivity(), LookPersonActivity.class));//咨询师页面
                break;
            case R.id.home_tiyan:
                startActivity(new Intent(getActivity(), OranizeActivity.class));//咨询机构页面
                break;
            case R.id.home_newman:
                startActivity(new Intent(getActivity(), ShowGoodsActivity.class));//商品套餐页面
                Toast toast = Toast.makeText(getActivity(), "跳转商品页面", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
    }


    private void init() {
        mUserInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        mUserId = mUserInfo.getRetData().getId();
        mPower = LiveShareUtil.getInstance(getActivity()).getPower();
        if (mPower == Constant.POWER_NORMAL) {
            mImgv_camera.setVisibility(View.GONE);
        } else {
            mImgv_camera.setVisibility(View.VISIBLE);
        }
        mHomeSmart.setEnableLoadMore(false);
        mHomeSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                mHomeSmart.setEnableLoadMore(true);
                getLiveData();
            }
        });
        mHomeSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getLiveData();
            }
        });


        mLists = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(getActivity(), mLists);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mHomeRecy.setLayoutManager(gridLayoutManager);
        mHomeRecy.setAdapter(mHomeAdapter);
        mHomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                try {
                    if (pos >= mLists.size()) {
                        return;
                    }
                    if (0 == mLastClickTime || System.currentTimeMillis() - mLastClickTime > 1000) {
                        MineTCVideoInfo item = mLists.get(pos);
                        if (item == null) {
                            Log.e("Home", "live list item is null at position:" + pos);
                            return;
                        }
                        startLivePlay(item);
                    }
                    mLastClickTime = System.currentTimeMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mHomeAdapter.setOnLastClickListener(new HomeAdapter.OnLastClickListener() {
            @Override
            public void onLastClickListener() {
                startActivity(new Intent(getActivity(), FindActivity.class));
            }
        });

        if (Constantc.mlvb_login) {
            getLiveData();
        } else {
            if (Constantc.use_old) {
                theOldLoginMlvb();
                return;
            }
            loginMLVB();
        }
    }

    private void getLiveData() {
        if (Constantc.use_old) {
            /*原生直播列表获取--要不要调用？*/
            TCVideoListMgr.getInstance().fetchLiveList(getActivity(), new TCVideoListMgr.Listener() {
                @Override
                public void onVideoList(int retCode, ArrayList<TCVideoInfo> result, boolean refresh) {
                    mHomeSmart.finishRefresh();
                    mHomeSmart.finishLoadMore();
                    onRefreshVideoList(retCode, result, refresh);
                }
            });
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("index", 0);
        map.put("cnt", 10);
        map.put("userID", mUserId);
        map.put("token", TCHTTPMgr.getInstance().getToken());
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getRoomList(LiveShareUtil.getInstance(getActivity()).getToken(), requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mHomeSmart.finishRefresh();
                mHomeSmart.finishLoadMore();
                RoomBean bean = new Gson().fromJson(result.toString(), RoomBean.class);
                if (bean.getRetCode() == 0) {
                    if (page == 0) {
                        mLists.clear();
                    }
                    toZhData(bean.getRetData());
                } else {
                    Toast.makeText(getActivity(), bean.getRetMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    /*获取直播的sign*/
    private void loginMLVB() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getUserSig(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken()), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                SignBean signBean = new Gson().fromJson(result.toString(), SignBean.class);
                if (signBean.getRetCode() == 0) {
                    LiveShareUtil.getInstance(getActivity()).put(LiveShareUtil.APP_USERSIGN, signBean.getRetData());
                    loginMLVBEnd(signBean.getRetData());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /**
     * MLVB的登录
     */
    private void loginMLVBEnd(String sign) {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig, long sdk_id) {
                if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
                    UserModel self = new UserModel();
                    self.userId = userid;
                    self.userSig = usersig;
                    ProfileManager.getInstance().setUserModel(self);
                    AVCallManager.getInstance().init(LiveApplication.getmInstance());
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                getLiveData();
                Log.i("HomeFragment", "onSuccess: MLVB登录成功");
            }
        });
        UserInfoBean.RetDataBean retData = mUserInfo.getRetData();
        mInstance.loginMLVB(retData.getId(), retData.getNickname(), retData.getIco(), retData.getIco(), retData.getGender(), sign);
    }

    /*整合直播数据*/
    private void toZhData(List<RoomBean.RetDataBean> data) {
        if (data == null || data.size() < 10) {
            mHomeSmart.setEnableLoadMore(false);
        }
        ArrayList<MineTCVideoInfo> infos = new ArrayList();
        for (RoomBean.RetDataBean value : data) {
            List<RoomBean.RetDataBean.PushersBean> pushers = value.pushers;

            MineTCVideoInfo info = new MineTCVideoInfo();
            info.playUrl = value.mixedPlayURL;
            info.title = value.roomName;
            info.userId = value.roomCreator;
            info.groupId = value.roomID;
            info.roomInfo = value.roomInfo;
            info.viewerCount = value.audienceCount;
            info.livePlay = true;
            if (pushers != null && !pushers.isEmpty()) {
                RoomBean.RetDataBean.PushersBean pusher = pushers.get(0);
                info.nickname = pusher.userName;
                info.avatar = pusher.userAvatar;
                info.push_size = pushers.size();
            }
            try {
                JSONObject jsonRoomInfo = new JSONObject(value.roomInfo);
                info.title = jsonRoomInfo.optString("title");
                info.frontCover = jsonRoomInfo.optString("frontcover");
                info.location = jsonRoomInfo.optString("location");
                info.lable = jsonRoomInfo.optString("label");
            } catch (Exception e) {
                e.printStackTrace();
                if (!TextUtils.isEmpty(value.roomInfo)) {
                    info.title = value.roomInfo;
                }
            }
            try {
                JSONObject jsonCunstomInfo = new JSONObject(value.custom);
                info.likeCount = jsonCunstomInfo.optInt("praise");
            } catch (Exception e) {
                e.printStackTrace();
            }
            infos.add(info);
        }
        mLists.addAll(infos);
        mHomeAdapter.notifyDataSetChanged();
        toSetOldData();

    }

    /*填充之前获取直播列表时的数据*/
    private void toSetOldData() {
        ArrayList<RoomInfo> room_infos = new ArrayList<>();
        for (int i = 0; i < mLists.size(); i++) {
            MineTCVideoInfo mineTCVideoInfo = mLists.get(i);
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.audienceCount = mineTCVideoInfo.viewerCount;
            roomInfo.roomName = mineTCVideoInfo.title;
            roomInfo.roomInfo = mineTCVideoInfo.roomInfo;
            roomInfo.roomID = mineTCVideoInfo.groupId;
            roomInfo.roomCreator = mineTCVideoInfo.userId;
            roomInfo.mixedPlayURL = mineTCVideoInfo.playUrl;
            room_infos.add(roomInfo);
        }

        MLVBLiveRoomImpl liveRoom = (MLVBLiveRoomImpl) MLVBLiveRoom.sharedInstance(getActivity());
        liveRoom.setRoomList(room_infos);
    }

    /**
     * 开始播放视频
     *
     * @param item 视频数据
     */
    private void startLivePlay(final MineTCVideoInfo item) {
        Intent intent = null;
        if (item.livePlay) {
            intent = new Intent(getActivity(), TCAudienceActivity.class);
            intent.putExtra(TCConstants.PLAY_URL, item.playUrl);
        } else {
//            intent = new Intent(getActivity(), TCPlaybackActivity.class);回放
//            intent.putExtra(TCConstants.PLAY_URL, TextUtils.isEmpty(item.hlsPlayUrl) ? item.playUrl : item.hlsPlayUrl);
        }

        intent.putExtra(TCConstants.PUSHER_ID, item.userId != null ? item.userId : "");
        intent.putExtra(TCConstants.PUSHER_NAME, TextUtils.isEmpty(item.nickname) ? item.userId : item.nickname);
        intent.putExtra(TCConstants.PUSHER_AVATAR, item.avatar);
        intent.putExtra(TCConstants.HEART_COUNT, "" + item.likeCount);
        intent.putExtra(TCConstants.MEMBER_COUNT, "" + item.viewerCount);
        intent.putExtra(TCConstants.GROUP_ID, item.groupId);
        intent.putExtra(TCConstants.PLAY_TYPE, item.livePlay);
        intent.putExtra(TCConstants.FILE_ID, item.fileId != null ? item.fileId : "");
        intent.putExtra(TCConstants.COVER_PIC, item.frontCover);
        intent.putExtra(TCConstants.TIMESTAMP, item.createTime);
        intent.putExtra(TCConstants.ROOM_TITLE, item.title);
        startActivityForResult(intent, START_LIVE_PLAY);
    }

    /**
     * 准备发起直播界面
     */
    private void startPublish() {
        Intent intent = null;
        if (mRecordType == TCConstants.RECORD_TYPE_SCREEN) {
//            intent = new Intent(this, TCScreenAnchorActivity.class);//录屏
        } else {
            intent = new Intent(getActivity(), TCCameraAnchorActivity.class);
        }

        if (intent != null) {
            intent.putExtra(TCConstants.ROOM_TITLE, TCUserMgr.getInstance().getNickname());
            intent.putExtra(TCConstants.USER_ID, TCUserMgr.getInstance().getUserId());
            intent.putExtra(TCConstants.USER_NICK, TCUserMgr.getInstance().getNickname());
            intent.putExtra(TCConstants.USER_HEADPIC, TCUserMgr.getInstance().getAvatar());
            intent.putExtra(TCConstants.COVER_PIC, TCUserMgr.getInstance().getCoverPic());
            intent.putExtra(TCConstants.USER_LOC, "天津");
            startActivity(intent);
        }
    }

    private static void loginTUIKitLive(long sdkAppid, String userId, String userSig) {
        try {
            Class<?> classz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive");
            Class<?> tClazz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive$LoginCallback");
            // 反射修改isAttachedTUIKit的值
            Field field = classz.getDeclaredField("sIsAttachedTUIKit");
            field.setAccessible(true);
            field.set(null, true);

            Method method = classz.getMethod("login", int.class, String.class, String.class, tClazz);
            method.invoke(null, sdkAppid, userId, userSig, null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            TUIKitLog.e("LoginActivity", "loginTUIKitLive error: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constantc.mlvb_login) {
            page = 0;
            getLiveData();
        } else {
            if (Constantc.use_old) {
                theOldLoginMlvb();
                return;
            }
            loginMLVB();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getMessage().equals("fresh_home")) {
            if (Constantc.mlvb_login) {
                getLiveData();
            } else {
                loginMLVB();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
/*---------------------------原可连麦数据-------------------------------*/
    private void theOldLoginMlvb() {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig, long sdk_id) {
                if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
                    UserModel self = new UserModel();
                    self.userId = userid;
                    self.userSig = usersig;
                    ProfileManager.getInstance().setUserModel(self);
                    AVCallManager.getInstance().init(getActivity());
                }
                loginTUIKitLive(sdk_id, userid, usersig);
                getLiveData();
            }
        });
        mInstance.loginMLVB(Constantc.test_USERID, Constantc.USER_NAME, Constantc.USER_UserAvatar, Constantc.USER_CoverPic, 1, Constantc.test_userSig);
    }

    private void onRefreshVideoList(final int retCode, final ArrayList<TCVideoInfo> result, final boolean refresh) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (retCode == 0 ) {
                        mLists.clear();
                        if (result != null) {
                            mLists.addAll(toCLone(result));
                        }
                        if (refresh) {
                            mHomeAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "刷新列表失败", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private List<MineTCVideoInfo> toCLone( ArrayList<TCVideoInfo> result){
        List<MineTCVideoInfo> list =new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            TCVideoInfo tcVideoInfo = result.get(i);
            MineTCVideoInfo mineTCVideoInfo =new MineTCVideoInfo();
            mineTCVideoInfo.push_size =1;
            mineTCVideoInfo.lable = "人气主播";
            mineTCVideoInfo.avatar =tcVideoInfo.avatar;
            mineTCVideoInfo.createTime =tcVideoInfo.createTime;
            mineTCVideoInfo.fileId = tcVideoInfo.fileId;
            mineTCVideoInfo.frontCover = tcVideoInfo.frontCover;
            mineTCVideoInfo.groupId = tcVideoInfo.groupId;
            mineTCVideoInfo.hlsPlayUrl = tcVideoInfo.hlsPlayUrl;
            mineTCVideoInfo.likeCount = tcVideoInfo.likeCount;
            mineTCVideoInfo.livePlay = tcVideoInfo.livePlay;
            mineTCVideoInfo.location = tcVideoInfo.location;
            mineTCVideoInfo.nickname =tcVideoInfo.nickname;
            mineTCVideoInfo.playUrl =tcVideoInfo.playUrl;
            mineTCVideoInfo.title = tcVideoInfo.title;
            mineTCVideoInfo.userId = tcVideoInfo.userId;
            mineTCVideoInfo.viewerCount =tcVideoInfo.viewerCount;
            list.add(mineTCVideoInfo);
        }
        return list;
    }


}

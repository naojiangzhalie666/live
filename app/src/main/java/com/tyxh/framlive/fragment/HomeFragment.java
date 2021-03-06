package com.tyxh.framlive.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljy.devring.DevRing;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tyxh.framlive.R;
import com.tyxh.framlive.adapter.HomeAdapter;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.bean.ActBackBean;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.bean.MineTCVideoInfo;
import com.tyxh.framlive.bean.RoomBean;
import com.tyxh.framlive.bean.SignBean;
import com.tyxh.framlive.bean.UserInfoBean;
import com.tyxh.framlive.chat.ChatActivity;
import com.tyxh.framlive.chat.tuikit.AVCallManager;
import com.tyxh.framlive.live.TCAudienceFaultActivity;
import com.tyxh.framlive.live.TCCameraAnchorActivity;
import com.tyxh.framlive.ui.FindActivity;
import com.tyxh.framlive.ui.MainActivity;
import com.tyxh.framlive.ui.WebGoodActivity;
import com.tyxh.framlive.ui.WebNewActivity;
import com.tyxh.framlive.ui.WebTiyanActivity;
import com.tyxh.framlive.utils.LiveDateZh;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.tyxh.xzb.Constantc;
import com.tyxh.xzb.important.MLVBLiveRoom;
import com.tyxh.xzb.important.MLVBLiveRoomImpl;
import com.tyxh.xzb.utils.TCConstants;
import com.tyxh.xzb.utils.TCHTTPMgr;
import com.tyxh.xzb.utils.login.TCUserMgr;
import com.tyxh.xzb.utils.roomutil.RoomInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
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
    private long mLastClickTime = 0;//????????????
    private int mRecordType = TCConstants.RECORD_TYPE_CAMERA;   // ?????????????????????

    @BindView(R.id.home_scroll)
    NestedScrollView mHomeScroll;
    @BindView(R.id.home_recy)
    RecyclerView mHomeRecy;
    @BindView(R.id.home_smart)
    SmartRefreshLayout mHomeSmart;
    @BindView(R.id.home_camera)
    ImageView mImgv_camera;
    @BindView(R.id.home_guajian)
    ImageView mImgv_guajian;

    private List<MineTCVideoInfo> mLists;
    private HomeAdapter mHomeAdapter;

    private Unbinder unbinder;
    private int mPower;
    private TCUserMgr mInstance;
    private UserInfoBean mUserInfo;
    private String mUserId;
    private int page = 0;
    private MainActivity mMainActivity;
    private boolean is_cliclpush =false;
    private String[][] mDatas =new String[][]{
            {"?????????","????????????????????????????????????????????????"},
            {"??????","???????????????????????????,????????????????????????"},
            {"?????????-??????","????????????????????????????????????????????????????????????"},
            {"?????????","????????????????????????????????????????????????"},
            {"?????????-?????????","?????????????????????????????????????????????"},
            {"?????????-??????","????????????????????????Ta??????????????????"},
            {"?????????","???????????????????????????3????????????Ta?????????"}};


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
        mMainActivity = (MainActivity) getActivity();
        init();
    }

    @OnClick({R.id.home_camera, R.id.home_chanpin, R.id.home_tiyan, R.id.home_newman, R.id.home_guajian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_camera:
                if (checkPublishPermission()) {
                    if(!is_cliclpush)
                    startPublish();
                    is_cliclpush =true;
                }
                break;
            case R.id.home_chanpin:
                WebGoodActivity.startMe(getActivity(), "????????????");
                break;
            case R.id.home_tiyan:
                WebTiyanActivity.startMe(getActivity(), "????????????");
                break;
            case R.id.home_newman:
                WebNewActivity.startMe(getActivity(), "????????????");
                break;
            case R.id.home_guajian:
                mMainActivity.toGoWhat(1);
                break;
        }
    }

    /**
     * ????????????????????????
     */
    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() != 0) {
               requestPermissions(permissions.toArray(new String[0]), TCConstants.WRITE_PERMISSION_REQ_CODE);
                Toast.makeText(mMainActivity, "???????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case TCConstants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                if(!is_cliclpush)
                    startPublish();
                is_cliclpush =true;
                break;
            default:
                break;
        }
    }


    private void startChatActivity(String title, String userid) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
//        chatInfo.setChatName(title);
        chatInfo.setId(userid);
        chatInfo.setChatName(title);
//        Intent intent = new Intent(getActivity(), ChathelfActivity.class);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constant.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                page += 10;
                getLiveData();
            }
        });


        mLists = new ArrayList<>();
        mLists_fault = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(getActivity(), mLists_fault);
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
                    if (pos >= mLists_fault.size()) {
                        return;
                    }
                    if(mLists_fault.get(pos).fault){
                        Intent intent =new Intent(getActivity(), TCAudienceFaultActivity.class);
                        intent.putExtra("index",mLists_fault.get(pos).index);
                        startActivity(intent);
                        return;
                    }
                    if (0 == mLastClickTime || System.currentTimeMillis() - mLastClickTime > 1000) {
                        MineTCVideoInfo item = mLists_fault.get(pos);
                        if (item == null) {
                            Log.e("Home", "live list item is null at position:" + pos);
                            return;
                        }
                        /* ??????????????????  item  ????????????*/
                        startActivityForResult(LiveDateZh.startPlay(item,getActivity()), START_LIVE_PLAY);
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
            loginMLVB();
        }
    }

    /*????????????????????????*/
    public void toRefresh() {
        if (mHomeSmart != null&&mHomeScroll!=null) {
            mHomeScroll.scrollTo(0, 0);
            mHomeSmart.autoRefresh();
        }
    }

    /*?????????????????????????????????*/
    private void getZig() {
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().queryJoAct(LiveShareUtil.getInstance(getActivity()).getToken(), "2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                ActBackBean backBean = new Gson().fromJson(result.toString(), ActBackBean.class);
                if (backBean.getRetCode() == 0) {
                    //???????????????????????????0:?????????1:?????????
                    if (backBean.getRetData().getUnaryAct().equals("1")) {
                        if (mImgv_guajian != null)
                            mImgv_guajian.setVisibility(View.VISIBLE);
                    } else {
                        if (mImgv_guajian != null)
                            mImgv_guajian.setVisibility(View.GONE);
                    }
                } else {
                    if (mImgv_guajian != null)
                        mImgv_guajian.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*??????????????????*/
    private void getLiveData() {
        Map<String, Object> map = new HashMap<>();
        map.put("index", page);
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
                    count=0;
                    toSeiCount(bean);
                } else {
                    Toast.makeText(getActivity(), bean.getRetMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mHomeSmart.finishRefresh();
                mHomeSmart.finishLoadMore();
            }
        });

    }

    private int count = 0;

    private void toSeiCount(RoomBean bean) {
        toZhData(bean.getRetData());
    }

    /*???????????????sign*/
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
     * MLVB?????????
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
                page = 0;
                getLiveData();
                Log.i("HomeFragment", "onSuccess: MLVB????????????");
            }
        });
        UserInfoBean.RetDataBean retData = mUserInfo.getRetData();
        mInstance.loginMLVB(retData.getId(), retData.getNickname(), retData.getIco(), retData.getIco(), retData.getGender(), sign);
    }
    private List<MineTCVideoInfo> mLists_fault;//TODO ???????????????????????????--?????????  mLists
    /*??????????????????*/
    private void toZhData(List<RoomBean.RetDataBean> data) {
        if (page == 0) {
            mLists.clear();
        }
        if (data == null || data.size() < 10) {
            mHomeSmart.setEnableLoadMore(false);
        }
        ArrayList<MineTCVideoInfo> infos = new ArrayList();
        for (RoomBean.RetDataBean value : data) {
            infos.add(LiveDateZh.getMineVideo(value));
        }
        mLists.addAll(infos);
        mLists_fault.clear();
        mLists_fault.addAll(mLists);
        for (int i = 0; i < 7; i++) {
            MineTCVideoInfo mineTCVideoInfo=new MineTCVideoInfo();
            mineTCVideoInfo.push_size=2;
            mineTCVideoInfo.fault=true;
            mineTCVideoInfo.index=i;
            mineTCVideoInfo.lable ="????????????";
            mineTCVideoInfo.viewerCount =2*(i+1);
            mineTCVideoInfo.nickname = mDatas[i][0];
            mineTCVideoInfo.title = mDatas[i][1];
            mineTCVideoInfo.type=1;// type>2????????????or?????????
            mLists_fault.add(mineTCVideoInfo);
        }
        mHomeAdapter.notifyDataSetChanged();
        toSetOldData();
    }

    /*??????????????????????????????????????????*/
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
     * ????????????????????????
     */
    private void startPublish() {
        mUserInfo = LiveShareUtil.getInstance(getActivity()).getUserInfo();
        Intent intent = null;
        if (mRecordType == TCConstants.RECORD_TYPE_SCREEN) {
//            intent = new Intent(this, TCScreenAnchorActivity.class);//??????
        } else {
            intent = new Intent(getActivity(), TCCameraAnchorActivity.class);
        }
        if (intent != null) {
            intent.putExtra(TCConstants.ROOM_TITLE, mUserInfo.getRetData().getNickname());
            intent.putExtra(TCConstants.USER_ID, TCUserMgr.getInstance().getUserId());
            intent.putExtra(TCConstants.USER_NICK, mUserInfo.getRetData().getNickname());
            intent.putExtra(TCConstants.USER_HEADPIC, mUserInfo.getRetData().getIco());
            intent.putExtra(TCConstants.COVER_PIC, mUserInfo.getRetData().getIco());
            intent.putExtra(TCConstants.JIGOU_NAME, "");
            intent.putExtra(TCConstants.USER_LOC, "??????");
            startActivity(intent);
        }
        is_cliclpush =false;
    }

    private static void loginTUIKitLive(long sdkAppid, String userId, String userSig) {
        try {
            Class<?> classz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive");
            Class<?> tClazz = Class.forName("com.tencent.qcloud.tim.tuikit.live.TUIKitLive$LoginCallback");
            // ????????????isAttachedTUIKit??????
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
        is_cliclpush =false;
        if (Constantc.mlvb_login) {
            page = 0;
            getLiveData();
            getZig();
        } else {
            loginMLVB();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventMessage msg) {
        if (msg.getMessage().equals("fresh_home")) {
            if (Constantc.mlvb_login) {
                page = 0;
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
        DevRing.httpManager().stopRequestByTag(LiveHttp.TAG);
    }
}

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
import com.example.myapplication.base.LiveApplication;
import com.example.myapplication.live.TCAudienceActivity;
import com.example.myapplication.live.TCCameraAnchorActivity;
import com.example.myapplication.ui.FindActivity;
import com.example.myapplication.ui.LookPersonActivity;
import com.example.myapplication.ui.OranizeActivity;
import com.example.myapplication.ui.ShowGoodsActivity;
import com.example.xzb.Constantc;
import com.example.xzb.utils.TCConstants;
import com.example.xzb.utils.login.TCUserMgr;
import com.example.xzb.utils.onlinelive.TCVideoInfo;
import com.example.xzb.utils.onlinelive.TCVideoListMgr;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.ShareUtil;
import com.tencent.liteav.AVCallManager;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    private List<TCVideoInfo> mLists;
    private HomeAdapter mHomeAdapter;

    private Unbinder unbinder;
    private int mPower;
    private TCUserMgr mInstance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                Toast toast =Toast.makeText(getActivity(),"跳转商品页面",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                break;
        }
    }


    private void init() {
        mPower = (int) ShareUtil.getInstance(getActivity()).get("power", 0);
        if (mPower == 0) {//普通
            mImgv_camera.setVisibility(View.GONE);
        } else {//咨询师
            mImgv_camera.setVisibility(View.VISIBLE);
        }
        mHomeSmart.setEnableLoadMore(false);
        mHomeSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getLiveData();
            }
        });
        mHomeSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mHomeSmart.finishLoadMore();
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
                        TCVideoInfo item = mLists.get(pos);
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
            loginMLVB();
        }
    }

    // TODO: 2021/4/9   获取直播数据
    /*
     * 现在报错--
     * get_live_list error, code:202001, errInfo:[LiveRoom] getRoomList 失败[verify failed,please check your token![err=202001]]
     * */
    private void getLiveData() {
        mHomeSmart.finishRefresh();
        TCVideoListMgr.getInstance().fetchLiveList(getActivity(), new TCVideoListMgr.Listener() {
            @Override
            public void onVideoList(int retCode, ArrayList<TCVideoInfo> result, boolean refresh) {
                onRefreshVideoList(retCode, result, refresh);
            }
        });
    }

    private void onRefreshVideoList(final int retCode, final ArrayList<TCVideoInfo> result, final boolean refresh) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (retCode == 0) {
                        mLists.clear();
                        if (result != null) {
                            mLists.addAll((ArrayList<TCVideoInfo>) result.clone());
                        }
                        if (refresh) {
                            mHomeAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "刷新列表失败", Toast.LENGTH_LONG).show();
                    }
                    /*mEmptyView.setVisibility(adapter.getCount() == 0? View.VISIBLE: View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);*/
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * MLVB的登录
     */
    private void loginMLVB() {
        mInstance = TCUserMgr.getInstance();
        mInstance.setOnLoginBackListener(new TCUserMgr.OnLoginBackListener() {
            @Override
            public void onLoginBackListener(String userid, String usersig,long sdk_id) {
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
        mInstance.loginMLVB();
    }

    /**
     * 开始播放视频
     *
     * @param item 视频数据
     */
    private void startLivePlay(final TCVideoInfo item) {
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
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            TUIKitLog.e("LoginActivity", "loginTUIKitLive error: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constantc.mlvb_login) {
            getLiveData();
        } else {
            loginMLVB();
        }
    }
}

package com.tyxh.framlive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.heytap.msp.push.HeytapPushManager;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.superc.yyfflibrary.views.lowhurdles.TabFragmentAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tyxh.framlive.R;
import com.tyxh.framlive.base.Constant;
import com.tyxh.framlive.base.LiveBaseActivity;
import com.tyxh.framlive.bean.EventMessage;
import com.tyxh.framlive.fragment.FindFragment;
import com.tyxh.framlive.fragment.HomeFragment;
import com.tyxh.framlive.fragment.MessageFragment;
import com.tyxh.framlive.fragment.MineFragment;
import com.tyxh.framlive.thirdpush.BrandUtil;
import com.tyxh.framlive.thirdpush.OPPOPushImpl;
import com.tyxh.framlive.thirdpush.PrivateConstants;
import com.tyxh.framlive.thirdpush.ThirdPushTokenMgr;
import com.tyxh.framlive.utils.LiveLog;
import com.tyxh.framlive.utils.NavigaUtils;
import com.tyxh.framlive.views.MainViewpager;
import com.tyxh.framlive.views.TabContainerView;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends LiveBaseActivity implements ViewPager.OnPageChangeListener, ConversationManagerKit.MessageUnreadWatcher {
    /*tab图标集合  未选中  选中*/
    private final int ICONS_RES[][] = {{R.drawable.home_unse, R.drawable.home_se}, {R.drawable.find_unse, R.drawable.find_se}, {R.drawable.msg_unse, R.drawable.msg_se}, {R.drawable.mine_unse, R.drawable.mine_se}};
    /* tab 颜色值*/
    private final int[] TAB_COLORS = new int[]{R.color.gray, R.color.black};
    private String[] titles = new String[]{"首页", "发现", "消息", "我的"};
    private Fragment[] fragments = null;
    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;
    private MainViewpager mPager;
    private TabContainerView mTabLayout;
    private TextView muser_red;

    @Override
    public int getContentLayoutId() {
        setSlideable(false);
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this, StateService.class);
        Constant.USER_STATE ="1";
        startService(intent);
        prepareThirdPushToken();
        mHomeFragment = new HomeFragment();
        mFindFragment = new FindFragment();
        mMessageFragment = new MessageFragment();
        mMineFragment = new MineFragment();
        fragments = new Fragment[]{mHomeFragment, mFindFragment, mMessageFragment, mMineFragment};
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments);
        mPager = findViewById(R.id.tab_pager);
        mPager.setNoScroll(true);
//        mPager.setScrollble(false);
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);
        mTabLayout = (TabContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);
        mTabLayout.initContainer(titles, ICONS_RES, TAB_COLORS, false);
        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);
        mTabLayout.setViewPager(mPager);
        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));
        View[] tabView = mTabLayout.getTabView();
        if (tabView.length >= 2) {
            muser_red = tabView[2].findViewById(R.id.iv_tab_red);
        }

        ConversationManagerKit.getInstance().addUnreadWatcher(this);

    }

    /*各大平台推送*/
    private void prepareThirdPushToken() {
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
        if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                        LiveLog.i(TAG, "huawei get token:" + token);
                        if (!TextUtils.isEmpty(token)) {
                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                        }
                    } catch (ApiException e) {
                        LiveLog.e(TAG, "huawei get token failed, " + e);
                    }
                }
            }.start();
        } else if (BrandUtil.isBrandVivo()) {
            // vivo离线推送
            LiveLog.i(TAG, "vivo support push: " + PushClient.getInstance(getApplicationContext()).isSupport());
            PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state == 0) {
                        String regId = PushClient.getInstance(getApplicationContext()).getRegId();
                        LiveLog.i(TAG, "vivopush open vivo push success regId = " + regId);
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(regId);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    } else {
                        // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                        LiveLog.i(TAG, "vivopush open vivo push fail state = " + state);
                    }
                }
            });
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo离线推送
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(this);
            HeytapPushManager.register(this, PrivateConstants.OPPO_PUSH_APPKEY, PrivateConstants.OPPO_PUSH_APPSECRET, oppo);
        }
        /*else if (BrandUtil.isGoogleServiceSupport()) {
            // 谷歌推送
        }*/
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index = 0, len = fragments.length; index < len; index++) {
            fragments[index].onHiddenChanged(index != position);
        }
        switch (position) {
            case 0:
                if (mHomeFragment != null) {
                    mHomeFragment.toRefresh();
                }
                TitleUtils.setStatusTextColor(false, this);
                break;
            case 1:
                if (mFindFragment != null && mFindFragment.isVisible())
                    mFindFragment.onResume();
                TitleUtils.setStatusTextColor(true, this);
                break;
            case 2:
                if(mMessageFragment!=null) {
                    mMessageFragment.initView();
                }
                TitleUtils.setStatusTextColor(false, this);
                break;
            case 3:
                if (mMineFragment != null && mMineFragment.isVisible()) {
                    mMineFragment.onResume();
                }
                TitleUtils.setStatusTextColor(false, this);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 主页进行指定跳转
     *
     * @param type 1跳转发现--精彩活动 2跳转发现-新手任务
     */
    public void toGoWhat(int type) {
        switch (type) {
            case 1:
                Bundle bundle = new Bundle();
                bundle.putString("msg", "act");
                if (mFindFragment != null)
                    mFindFragment.setArguments(bundle);
                mPager.setCurrentItem(1);
                break;
            case 2:
                Bundle bundleever = new Bundle();
                bundleever.putString("msg", "ever");
                if (mFindFragment != null)
                    mFindFragment.setArguments(bundleever);
                mPager.setCurrentItem(1);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
        if (msg.getMessage().equals("ever") || (msg.getMessage().equals("pipei"))) {
            mPager.setCurrentItem(1);
        } else if (msg.getMessage().equals("main")) {
            mPager.setCurrentItem(0);
        }
    }

    long stT = 0;
    long endT = 0;

    @Override
    public void onBackPressed() {
        stT = System.currentTimeMillis();
        if (stT - endT >= 2000) {
            ToastShow("双击退出");
            endT = stT;
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Intent intentFour = new Intent(this, StateService.class);
        stopService(intentFour);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String what = intent.getStringExtra("what");
        if (!TextUtils.isEmpty(what)) {
            switch (what) {
                case "web":
                    toGoWhat(2);
                    break;

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMineFragment != null && mMineFragment.isVisible()) {
//            mMineFragment.toUpdateData();
        }
    }

    /*判断应用是否展示虚拟键*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Constant.has_navi = NavigaUtils.isNavigationBarExist(this);
        Log.i("MainActivity_foucus", "has navigation: " + Constant.has_navi);
    }

    @Override
    public void updateUnread(int count) {
        muser_red.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        muser_red.setText(count>99?"99.":count + "");
    }
}
package com.example.myapplication.ui;

import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.base.GenerateTestUserSig;
import com.example.myapplication.fragment.FindFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MessageFragment;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.views.MainViewpager;
import com.example.myapplication.views.TabContainerView;
import com.superc.yyfflibrary.base.BaseActivity;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.superc.yyfflibrary.views.lowhurdles.TabFragmentAdapter;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
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


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        TitleUtils.setStatusTextColor(false, this);
        goLogin();
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
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mAdapter);
        mTabLayout = (TabContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);
        mTabLayout.initContainer(titles, ICONS_RES, TAB_COLORS, false);
        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);
        mTabLayout.setViewPager(mPager);
        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));


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
                TitleUtils.setStatusTextColor(false, this);
                break;
            case 1:
                TitleUtils.setStatusTextColor(true, this);
                break;
            case 2:
                TitleUtils.setStatusTextColor(false, this);
                break;
            case 3:
                TitleUtils.setStatusTextColor(false, this);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void goLogin(){
        // 获取userSig函数
        String userSig = GenerateTestUserSig.genTestUserSig("yangfan");
        TUIKit.login("yangfan", userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败" + ", errCode = " + code + ", errInfo = " + desc);
                    }
                });
                Log.e(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
//                UserInfo.getInstance().setAutoLogin(true);
//                Intent intent = new Intent(LoginForDevActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                Log.e(TAG, "imLogin 登录成功");
            }
        });
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

}
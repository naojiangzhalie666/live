package com.example.myapplication.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.myapplication.chat.BaseActivity;
import com.example.myapplication.chat.ClickUtils;
import com.example.myapplication.chat.UserInfo;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;

import androidx.appcompat.app.AppCompatActivity;


/********************************************************************
 @version: 1.0.0
 @description: TitleUtils为修改标题栏颜色的公共类
 说明： 1、 TitleUtils.setStatusTextColor(false, this);中一参false为浅色 true为深色，方法在init中调用
 2、setSlideable(false);表示不使用滑动退出  true表示使用
 @author: EDZ
 @time: 2019/11/20 14:41
 @变更历史:
 ********************************************************************/

public abstract class BaseChatActivity extends AppCompatActivity {
    public String TAG = "BaseActivity";

    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastLongMessage("您的帐号已在其它终端登录");
            logout(LiveApplication.getmInstance());
        }

    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        TitleUtils.getStatusBarHeight(this);
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(this).inflate(getContentLayoutId(), null));
        TitleUtils.setStatusTextColor(false,this);
        TAG = getLocalClassName();
        init();


      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.home_txt));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }*/

        TUIKit.addIMEventListener(mIMEventListener);


    }

    public static void logout(Context context) {
      /*  Log.i(TAG, "logout");
        UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.LOGOUT, true);
        context.startActivity(intent);*/
        //Todo 退出直播
//        TUIKitLive.logout();
    }

    /**
     * @param cls 跳转到的Activity名称class
     */
    public void statActivity(Class cls) {
        statActivity(cls, null);
    }

    /**
     * @param bundle 跳转时携带的数据
     * @param cls    跳转到的Activity名称class
     */
    public void statActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * @param msg 弹出提示类型--只支持String和Int索引
     */
    public void ToastShow(Object msg) {
        if (msg instanceof String) {
            ToastUtil.showToast(this, (String) msg);
        } else {
            ToastUtil.showToast(this, (int) msg);
        }
    }

    public void toChangeTitleCor(boolean is_deep){
        TitleUtils.setStatusTextColor(is_deep, this);
    }


    public abstract int getContentLayoutId();

    public abstract void init();
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        boolean login = UserInfo.getInstance().isAutoLogin();
        if (!login) {
            BaseActivity.logout(LiveApplication.getmInstance());
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        ClickUtils.clear();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }
}

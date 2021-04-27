package com.example.myapplication.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.base.LiveApplication;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录状态的Activity都要集成该类，来完成被踢下线等监听处理。
 */
public class BaseHelfActivity extends AppCompatActivity {

    private static final String TAG = BaseHelfActivity.class.getSimpleName();

    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            ToastUtil.toastLongMessage("您的帐号已在其它终端登录");
            logout(LiveApplication.getmInstance());
        }

    };

    public static void logout(Context context) {
        Log.e(TAG, "logout");
       /* UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);

        Intent intent = new Intent(context, LoginForDevActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.LOGOUT, true);
        context.startActivity(intent);

        TUIKitLive.logout();*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }

        TUIKit.addIMEventListener(mIMEventListener);
    }

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

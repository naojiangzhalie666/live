package com.tyxh.framlive.utils;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogUtils {
    /**
     * 隐藏虚拟按键
     */
    public static void hideBottomNav(Activity dialog) {
        Window window = dialog.getWindow();
        if (window == null || window.getDecorView() == null) {
            return;
        }
        //不加FLAG_NOT_FOCUSABLE，dialog显示时就会显示虚拟按键
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        dialog.setOnShowListener(d -> {
//            //dialog显示之后，要清除FLAG_NOT_FOCUSABLE，否则不会弹出软键盘
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        });
        //隐藏虚拟按键
        hideBottomNavInner(dialog);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            //从后台重新进入时，要再次隐藏虚拟按键
            hideBottomNavInner(dialog);
        });
    }

    private static void hideBottomNavInner(Activity dialog) {
        View decorView = dialog.getWindow().getDecorView();
        int vis = decorView.getSystemUiVisibility();
        vis |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(vis);
    }
}

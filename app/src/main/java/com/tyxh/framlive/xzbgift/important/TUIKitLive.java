package com.tyxh.framlive.xzbgift.important;

import android.content.Context;

public class TUIKitLive {

    private static final String TAG = "TUIKit";

    private static Context sAppContext;
    private static long                 sSdkAppId;
    public static void init(Context context) {
        sAppContext = context;
    }

    public static void unInit() {
    }
    public static Context getAppContext() {
        return sAppContext;
    }

}

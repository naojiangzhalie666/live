package com.yf.xzbgift.imple;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.yf.xzbgift.base.HttpGetRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultGiftAdapterImp extends GiftAdapter {
    private static final String TAG = "DefaultGiftAdapterImp";

    private static final int CORE_POOL_SIZE = 5;
    private static final String GIFT_DATA_URL = "https://liteav-test-1252463788.cos.ap-guangzhou.myqcloud.com/gift_data.json";
    private static final String GIFT_DATA_URL_BACK = "https://liteav-test-1252463788.cos.ap-guangzhou.myqcloud.com/gift_data.json";

    private GiftBeanThreadPool mGiftBeanThreadPool;
    private OnGiftListQueryCallback mOnGiftListQueryCallback;
    private OnGiftListQueryCallback mOnGiftListMineQueryCallback;

    @Override
    public void queryGiftInfoList(final OnGiftListQueryCallback callback) {
        mOnGiftListQueryCallback = callback;
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL, new HttpGetRequest.HttpListener() {
            @Override
            public void success(String response) {
                handleResponseMessage(response);
            }

            @Override
            public void onFailed(String message) {
                if (mOnGiftListQueryCallback != null) {
                    mOnGiftListQueryCallback.onGiftListQueryFailed(message);
                }
            }
        });
        threadPoolExecutor.execute(request);
    }

    @Override
    public void queryGiftMineList(OnGiftListQueryCallback callback) {
        mOnGiftListMineQueryCallback = callback;
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL, new HttpGetRequest.HttpListener() {
            @Override
            public void success(String response) {
                handleMineResponseMessage(response);
            }

            @Override
            public void onFailed(String message) {
                if (mOnGiftListMineQueryCallback != null) {
                    mOnGiftListMineQueryCallback.onGiftListQueryFailed(message);
                }
            }
        });
        threadPoolExecutor.execute(request);
    }

    private synchronized ThreadPoolExecutor getThreadExecutor() {
        if (mGiftBeanThreadPool == null || mGiftBeanThreadPool.isShutdown()) {
            mGiftBeanThreadPool = new GiftBeanThreadPool(CORE_POOL_SIZE);
        }
        return mGiftBeanThreadPool;
    }


    private void handleResponseMessage(String response) {
        if (response == null) {
            return;
        }
        Gson gson = new Gson();
        GiftBean giftBean = gson.fromJson(response, GiftBean.class);
        final List<GiftData> giftDataList = transformGiftInfoList(giftBean);
        if (giftDataList != null) {
            if (mOnGiftListQueryCallback != null) {
                mOnGiftListQueryCallback.onGiftListQuerySuccess(giftDataList);
            }
        }
    }
    /*我的背包请求*/
    private void handleMineResponseMessage(String response) {
        if (response == null) {
            return;
        }
        Gson gson = new Gson();
        BeanMyBack giftBean = gson.fromJson(response, BeanMyBack.class);
        final List<GiftData> giftDataList = transformMineGiftInfoList(giftBean);
        if (giftDataList != null) {
            if (mOnGiftListMineQueryCallback != null) {
                mOnGiftListMineQueryCallback.onGiftListQuerySuccess(giftDataList);
            }
        }
    }

    private List<GiftData> transformGiftInfoList(GiftBean giftBean) {
        if (giftBean == null) {
            return null;
        }
        List<GiftBean.GiftListBean> giftBeanList = giftBean.getGiftList();
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (GiftBean.GiftListBean bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getGiftId();
            giftData.title = bean.getTitle();
            giftData.type = bean.getType();
            giftData.price = bean.getPrice();
            giftData.giftPicUrl = bean.getGiftImageUrl();
            giftData.lottieUrl = bean.getLottieUrl();
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }
    private List<GiftData> transformMineGiftInfoList(BeanMyBack giftBean) {
        if (giftBean == null) {
            return null;
        }
        List<BeanMyBack.RetDataBean> giftBeanList = giftBean.getRetData();
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (BeanMyBack.RetDataBean bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getId();
            giftData.title = bean.getProName();
            giftData.type = bean.getGiftType();
            giftData.price = bean.getGiftPrice();
            giftData.giftPicUrl = bean.getImgUrl();
            giftData.lottieUrl = bean.getLottieUrl();
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }

    public static class GiftBeanThreadPool extends ThreadPoolExecutor {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public GiftBeanThreadPool(int poolSize) {
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),
                    Executors.defaultThreadFactory(), new AbortPolicy());
        }
    }
}

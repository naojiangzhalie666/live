package com.tyxh.framlive.xzbgift.imple;

import android.annotation.TargetApi;
import android.os.Build;

import com.tyxh.framlive.base.LiveApplication;
import com.tyxh.framlive.utils.LiveShareUtil;
import com.tyxh.framlive.utils.httputil.HttpBackListener;
import com.tyxh.framlive.utils.httputil.LiveHttp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.tyxh.framlive.utils.LiveShareUtil.APP_USERID;

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
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().findAllGifts(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), "1","100","2"), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                handleResponseMessage(result.toString());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                if (mOnGiftListMineQueryCallback != null) {
                    mOnGiftListMineQueryCallback.onGiftListQueryFailed(error.toString());
                }
            }
        });



/*
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
        threadPoolExecutor.execute(request);*/
    }

    @Override
    public void queryGiftMineList(OnGiftListQueryCallback callback) {
        mOnGiftListMineQueryCallback = callback;
        LiveHttp.getInstance().toGetData(LiveHttp.getInstance().getApiService().getGift(LiveShareUtil.getInstance(LiveApplication.getmInstance()).getToken(), (String) LiveShareUtil.getInstance(LiveApplication.getmInstance()).get(APP_USERID,"")), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                handleMineResponseMessage(result.toString());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                if (mOnGiftListMineQueryCallback != null) {
                    mOnGiftListMineQueryCallback.onGiftListQueryFailed(error.toString());
                }
            }
        });

     /*   ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL_BACK, new HttpGetRequest.HttpListener() {
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
        threadPoolExecutor.execute(request);*/
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
        AllGiftBean giftBean = gson.fromJson(response, AllGiftBean.class);
        final List<GiftData> giftDataList = transformGiftInfoList(giftBean);
        if (giftDataList != null) {
            if (mOnGiftListQueryCallback != null) {
                mOnGiftListQueryCallback.onGiftListQuerySuccess(giftDataList);
            }
        }
    }
   /* 我的背包请求*/
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

    private List<GiftData> transformGiftInfoList(AllGiftBean giftBean) {
        if (giftBean == null||giftBean.getRetData()==null) {
            return null;
        }
        List<AllGiftBean.RetDataBean.ListBean> giftBeanList = giftBean.getRetData().getList();
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (AllGiftBean.RetDataBean.ListBean bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getId()+"";
            giftData.title = bean.getProName();
            giftData.type = bean.getGiftType();
            giftData.price = bean.getGiftPrice();
            giftData.giftPicUrl = bean.getImgUrl();
            giftData.lottieUrl = bean.getLottieUrl();
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }
    private List<GiftData> transformMineGiftInfoList(BeanMyBack giftBean) {
        if (giftBean == null||giftBean.getRetData()==null) {
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
            giftData.count = bean.getCount();
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

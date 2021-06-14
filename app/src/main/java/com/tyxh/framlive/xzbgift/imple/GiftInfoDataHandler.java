package com.tyxh.framlive.xzbgift.imple;

import com.tyxh.framlive.xzbgift.important.GiftInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftInfoDataHandler {
    private static final String TAG = "GiftInfoManager";

    private GiftAdapter mGiftAdapter;
    private Map<String, GiftInfo> mGiftInfoMap = new HashMap<>();

    public void setGiftAdapter(GiftAdapter adapter) {
        mGiftAdapter = adapter;
        queryGiftInfoList(null);
    }

    public void queryGiftInfoList(final GiftQueryCallback callback) {
        if (mGiftAdapter != null) {
            mGiftAdapter.queryGiftInfoList(new OnGiftListQueryCallback() {
                @Override
                public void onGiftListQuerySuccess(List<GiftData> giftDataList) {
                    List<GiftInfo> giftInfoList = transformGiftInfoList(giftDataList);
                    if (callback != null) {
                        callback.onQuerySuccess(giftInfoList);
                    }
                }

                @Override
                public void onGiftListQueryFailed(String errorMessage) {
                    if (callback != null) {
                        callback.onQueryFailed(errorMessage);
                    }
                }
            });
        }
    }
    public void queryGiftMineList(final GiftQueryCallback callback){
        if (mGiftAdapter != null) {
            mGiftAdapter.queryGiftMineList(new OnGiftListQueryCallback() {
                @Override
                public void onGiftListQuerySuccess(List<GiftData> giftDataList) {
                    List<GiftInfo> giftInfoList = transformGiftMineList(giftDataList);
                    if (callback != null) {
                        callback.onQuerySuccess(giftInfoList);
                    }
                }

                @Override
                public void onGiftListQueryFailed(String errorMessage) {
                    if (callback != null) {
                        callback.onQueryFailed(errorMessage);
                    }
                }
            });
        }
    }




    private List<GiftInfo> transformGiftInfoList(List<GiftData> giftDataList) {
        List<GiftInfo> giftInfoList = new ArrayList<>();
        mGiftInfoMap.clear();
        if (giftDataList != null) {
            for (GiftData giftData : giftDataList) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.giftId = giftData.giftId;
                giftInfo.title = giftData.title;
                giftInfo.type = giftData.type;
                giftInfo.price = giftData.price;
                giftInfo.giftPicUrl = giftData.giftPicUrl;
                giftInfo.lottieUrl = giftData.lottieUrl;
                giftInfoList.add(giftInfo.copy());
                mGiftInfoMap.put(giftInfo.giftId, giftInfo);
            }
        }
        return giftInfoList;
    }

    private List<GiftInfo> transformGiftMineList(List<GiftData> giftDataList) {
        List<GiftInfo> giftInfoList = new ArrayList<>();
        if (giftDataList != null) {
            for (GiftData giftData : giftDataList) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.giftId = giftData.giftId;
                giftInfo.title = giftData.title;
                giftInfo.type = giftData.type;
                giftInfo.price = giftData.price;
                giftInfo.giftPicUrl = giftData.giftPicUrl;
                giftInfo.lottieUrl = giftData.lottieUrl;
                giftInfo.gift_count =String.valueOf(giftData.count);
                giftInfoList.add(giftInfo.copy());
            }
        }
        return giftInfoList;
    }


    public GiftInfo getGiftInfo(String giftId) {
        return mGiftInfoMap.get(giftId);
    }

    public interface GiftQueryCallback {
        void onQuerySuccess(List<GiftInfo> giftInfoList);

        void onQueryFailed(String errorMsg);
    }

}
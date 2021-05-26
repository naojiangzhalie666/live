package com.tyxh.framlive.xzbgift.imple;

public abstract class GiftAdapter {
    /**
     * 查询礼物信息
     * @param callback
     */
    public abstract void queryGiftInfoList(OnGiftListQueryCallback callback);

    public abstract void queryGiftMineList(OnGiftListQueryCallback callback);

}

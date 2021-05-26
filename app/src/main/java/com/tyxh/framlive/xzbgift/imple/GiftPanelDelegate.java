package com.tyxh.framlive.xzbgift.imple;


import com.tyxh.framlive.xzbgift.important.GiftInfo;

public interface GiftPanelDelegate {
    /**
     * 礼物点击事件
     */
    void onGiftItemClick(GiftInfo giftInfo);

    /**
     * 充值点击事件
     */
    void onChargeClick();

    void onSendClickListener(GiftInfo giftInfo);
}

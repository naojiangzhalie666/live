package com.yf.xzbgift.imple;

public interface IGiftPanelView {
    /**
     * 面板通用接口
     */
    void init(GiftInfoDataHandler giftInfoDataHandler);

    /**
     * 打开礼物面板
     *
     */
    void show();

    /**
     * 关闭礼物面板
     */
    void hide();

    //订阅礼物面板事件
    void setGiftPanelDelegate(GiftPanelDelegate delegate);

}

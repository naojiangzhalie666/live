package com.tyxh.framlive.xzbgift.imple;

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

    //设置钻石数量
    void setMoney_zuan(String money_zuan);

    /**
     *
     * @param eve  当前经验值
     * @param zuan  需要再送多少钻石可升级
     */
    void setJingYAndNeedZunas(int eve, String zuan);

    //更新背包数量
    void notiBbGift();

}

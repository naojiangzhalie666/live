package com.tyxh.framlive.bean;

import java.util.List;

public class DiamondNewBean {
    /**
     * retCode : 0000
     * retMsg : 操作成功!
     * retData : [{"id":78,"proName":"钻石包6","platform":"2","proNum":500,"originalPrice":50,"discountType":0,"giveNum":0,"discount":0,"specialOffer":0.01,"isLimitTimes":1,"limitTimes":10,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":0,"personalLimitAmount":0,"enterpriseLimitAmount":0,"limitAmountType":0,"isShelves":1,"diamondCount":-1,"createDate":"2021-06-21 11:58:41","modifyDate":"2021-07-08 09:00:41","flag":2,"firstCharge":0}]
     */

    private int retCode;
    private String retMsg;
    private List<DiamondBean.RetDataBean.ListBean> retData;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public List<DiamondBean.RetDataBean.ListBean> getRetData() {
        return retData;
    }

    public void setRetData(List<DiamondBean.RetDataBean.ListBean> retData) {
        this.retData = retData;
    }

}

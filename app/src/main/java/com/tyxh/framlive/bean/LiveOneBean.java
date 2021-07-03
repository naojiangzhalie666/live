package com.tyxh.framlive.bean;

public class LiveOneBean {
    private int retCode;
    private String retMsg;
    private RoomBean.RetDataBean retData;

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

    public RoomBean.RetDataBean getRetData() {
        return retData;
    }

    public void setRetData(RoomBean.RetDataBean retData) {
        this.retData = retData;
    }
}

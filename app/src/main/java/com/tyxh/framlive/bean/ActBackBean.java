package com.tyxh.framlive.bean;

public class ActBackBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"unaryAct":"1","firstCharge":"1"}
     */

    private int retCode;
    private String retMsg;
    private RetDataBean retData;

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

    public RetDataBean getRetData() {
        return retData;
    }

    public void setRetData(RetDataBean retData) {
        this.retData = retData;
    }

    public static class RetDataBean {
        /**
         * unaryAct : 1
         * firstCharge : 1
         */

        private String unaryAct;
        private String firstCharge;

        public String getUnaryAct() {
            return unaryAct;
        }

        public void setUnaryAct(String unaryAct) {
            this.unaryAct = unaryAct;
        }

        public String getFirstCharge() {
            return firstCharge;
        }

        public void setFirstCharge(String firstCharge) {
            this.firstCharge = firstCharge;
        }
    }
}

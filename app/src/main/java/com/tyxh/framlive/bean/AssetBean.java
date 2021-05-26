package com.tyxh.framlive.bean;

public class AssetBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"diamond":0,"balance":0,"propCount":0}
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
         * diamond : 0
         * balance : 0
         * propCount : 0
         */

        private String diamond;
        private String balance;
        private String propCount;

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getPropCount() {
            return propCount;
        }

        public void setPropCount(String propCount) {
            this.propCount = propCount;
        }
    }
}

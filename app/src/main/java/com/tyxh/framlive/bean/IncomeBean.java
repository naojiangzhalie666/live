package com.tyxh.framlive.bean;

public class IncomeBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"cur_amount":0,"yes_amount":0,"mon_amount":0}
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
         * cur_amount : 0
         * yes_amount : 0
         * mon_amount : 0
         */

        private int cur_amount;
        private int yes_amount;
        private int mon_amount;

        public int getCur_amount() {
            return cur_amount;
        }

        public void setCur_amount(int cur_amount) {
            this.cur_amount = cur_amount;
        }

        public int getYes_amount() {
            return yes_amount;
        }

        public void setYes_amount(int yes_amount) {
            this.yes_amount = yes_amount;
        }

        public int getMon_amount() {
            return mon_amount;
        }

        public void setMon_amount(int mon_amount) {
            this.mon_amount = mon_amount;
        }
    }
}

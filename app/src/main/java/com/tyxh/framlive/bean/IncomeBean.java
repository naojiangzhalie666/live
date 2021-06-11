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

        private double curAmount;
        private double yesAmount;
        private double monAmount;

        public double getCur_amount() {
            return curAmount;
        }

        public void setCur_amount(double cur_amount) {
            this.curAmount = cur_amount;
        }

        public double getYes_amount() {
            return yesAmount;
        }

        public void setYes_amount(double yes_amount) {
            this.yesAmount = yes_amount;
        }

        public double getMon_amount() {
            return monAmount;
        }

        public void setMon_amount(double mon_amount) {
            this.monAmount = mon_amount;
        }
    }
}

package com.tyxh.framlive.bean;

import java.util.List;

public class MxdetailBean {
    private int retCode;
    private String retMsg;
    private List<ListBean> retData;

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

    public List<ListBean> getRetData() {
        return retData;
    }

    public void setRetData(List<ListBean> retData) {
        this.retData = retData;
    }

    public static class ListBean{

        /**
         * amount : 0
         * createDate :
         * id : 0
         * moneyType : 0
         * orderNum :
         * proId : 0
         * proType : 0
         * remark :
         * userId : 0
         */

        private int amount;
        private String createDate;
        private int id;
        private int moneyType;
        private String orderNum;
        private int proId;
        private int proType;
        private String remark;
        private int userId;
        private boolean show;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(int moneyType) {
            this.moneyType = moneyType;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public int getProId() {
            return proId;
        }

        public void setProId(int proId) {
            this.proId = proId;
        }

        public int getProType() {
            return proType;
        }

        public void setProType(int proType) {
            this.proType = proType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

}

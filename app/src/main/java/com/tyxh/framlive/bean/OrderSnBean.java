package com.tyxh.framlive.bean;

public class OrderSnBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"id":42,"orderSn":"1621828730628","userId":9,"diamondId":6,"amount":315,"orderType":1,"orderStatus":1,"tradeNo":null,"giveNum":0,"sourceNum":50,"remark":"","createDate":"2021-05-24 11:58:50","modifyDate":"2021-05-24 11:58:50","flag":2}
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
         * id : 42
         * orderSn : 1621828730628
         * userId : 9
         * diamondId : 6
         * amount : 315
         * orderType : 1
         * orderStatus : 1
         * tradeNo : null
         * giveNum : 0
         * sourceNum : 50
         * remark :
         * createDate : 2021-05-24 11:58:50
         * modifyDate : 2021-05-24 11:58:50
         * flag : 2
         */

        private int id;
        private String orderSn;
        private int userId;
        private int diamondId;
        private int amount;
        private int orderType;
        private int orderStatus;
        private Object tradeNo;
        private int giveNum;
        private int sourceNum;
        private String remark;
        private String createDate;
        private String modifyDate;
        private int flag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getDiamondId() {
            return diamondId;
        }

        public void setDiamondId(int diamondId) {
            this.diamondId = diamondId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public Object getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(Object tradeNo) {
            this.tradeNo = tradeNo;
        }

        public int getGiveNum() {
            return giveNum;
        }

        public void setGiveNum(int giveNum) {
            this.giveNum = giveNum;
        }

        public int getSourceNum() {
            return sourceNum;
        }

        public void setSourceNum(int sourceNum) {
            this.sourceNum = sourceNum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
}

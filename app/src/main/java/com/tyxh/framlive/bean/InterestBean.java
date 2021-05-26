package com.tyxh.framlive.bean;

import java.util.List;

public class InterestBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"id":"1","labelName":"情感修复","userNum":0,"operator":"","createDate":null,"modifyDate":null,"flag":2},{"id":"2","labelName":"未成年人心理","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"3","labelName":"第三者问题","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"4","labelName":"婚姻家庭","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"5","labelName":"恋爱关系","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"6","labelName":"亲子关系","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"7","labelName":"职场问题","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"8","labelName":"个人成长","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"9","labelName":"人际关系","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2},{"id":"10","labelName":"心理健康检测","userNum":0,"operator":null,"createDate":null,"modifyDate":null,"flag":2}]
     */

    private int retCode;
    private String retMsg;
    private List<RetDataBean> retData;

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

    public List<RetDataBean> getRetData() {
        return retData;
    }

    public void setRetData(List<RetDataBean> retData) {
        this.retData = retData;
    }

    public static class RetDataBean {
        /**
         * id : 1
         * labelName : 情感修复
         * userNum : 0
         * operator :
         * createDate : null
         * modifyDate : null
         * flag : 2
         */

        private int id;
        private String labelName;
        private int userNum;
        private String operator;
        private Object createDate;
        private Object modifyDate;
        private int flag;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }

        public Object getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(Object modifyDate) {
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

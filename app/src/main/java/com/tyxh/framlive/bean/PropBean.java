package com.tyxh.framlive.bean;

import java.util.List;

public class PropBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"propId":1,"propPicUrl":null,"count":5,"title":"角色补给卡","value":null}]
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
         * propId : 1
         * propPicUrl : null
         * count : 5
         * title : 角色补给卡
         * value : null
         */

        private int propId;
        private Object propPicUrl;
        private int count;
        private String title;
        private Object value;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getPropId() {
            return propId;
        }

        public void setPropId(int propId) {
            this.propId = propId;
        }

        public Object getPropPicUrl() {
            return propPicUrl;
        }

        public void setPropPicUrl(Object propPicUrl) {
            this.propPicUrl = propPicUrl;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
